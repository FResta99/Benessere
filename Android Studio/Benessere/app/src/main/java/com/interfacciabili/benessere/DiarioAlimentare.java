//TODO: INSERIRE DIALOG

package com.interfacciabili.benessere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Cibo;
import com.interfacciabili.benessere.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class DiarioAlimentare extends AppCompatActivity implements View.OnClickListener {
    private static final String CLIENTE = "CLIENTE";
    private ListView lvDiario;

    private Button btnColazione;
    private Button btnPranzo;
    private Button btnCena;

    private TextView tvNumeroCalorie;

    private Cliente cliente;

    private ArrayAdapter<Cibo> adapterListView;

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();

            updateInformations(databaseService.getCibo(cliente.getUsername(), "COLAZIONE"), "COLAZIONE");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intentFrom =  getIntent();

        if (intentFrom != null && intentFrom.hasExtra(CLIENTE)) {
            setContentView(R.layout.activity_diario_alimentare);

            Toolbar toolbar = findViewById(R.id.toolbar_main);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            tvNumeroCalorie = findViewById(R.id.tvNumeroCalorie);

            btnColazione = findViewById(R.id.btnColazione);
            btnPranzo = findViewById(R.id.btnPranzo);
            btnCena = findViewById(R.id.btnCena);

            btnColazione.setOnClickListener(this);
            btnPranzo.setOnClickListener(this);
            btnCena.setOnClickListener(this);

            lvDiario = findViewById(R.id.lvDiario);

            cliente = intentFrom.getParcelableExtra(CLIENTE);
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        List<Cibo> listFoods = new ArrayList<Cibo>();
        String tipo = "";
        switch (v.getId()) {
            case R.id.btnColazione:
                tipo = "COLAZIONE";
                break;
            case R.id.btnPranzo:
                tipo = "PRANZO";
                break;
            case R.id.btnCena:
                tipo = "CENA";
                break;
        }

        listFoods = databaseService.getCibo(cliente.getUsername(), tipo);

        if (listFoods != null && listFoods.size() > 0) {
            updateInformations(listFoods, tipo);
        } else {
            lvDiario.setAdapter(null);
            Toast.makeText(DiarioAlimentare.this, "Nessun cibo per questa fascia", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_diario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        if (item.getItemId() == R.id.destroyAll){

            new AlertDialog.Builder(DiarioAlimentare.this)
                    .setTitle("Svuota diario")
                    .setMessage("Vuoi davvero svuotare il tuo diario alimentare?")

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            databaseService.eliminaCibo(cliente.getUsername());

                            tvNumeroCalorie.setText(String.valueOf(0));

                            lvDiario.setAdapter(null);
                        }
                    })

                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateInformations(List<Cibo> listFoods, String tipo) {
        tvNumeroCalorie.setText(String.valueOf(contaCalorie(listFoods)));
        adapterListView = new ArrayAdapter<Cibo>(getApplicationContext(), android.R.layout.simple_list_item_1, databaseService.getCibo(cliente.getUsername(), tipo));
        lvDiario.setAdapter(adapterListView);
    }

    private int contaCalorie(List<Cibo> listFoods) {
        int somma = 0;
        for (int c = 0; c < listFoods.size(); c++) {
            somma += listFoods.get(c).getCalorie();
        }

        return somma;
    }

    public void aggiungiProdotto(View view) {
        InserisciCiboDialog ipd = new InserisciCiboDialog();
        ipd.setCliente(cliente.getUsername());
        ipd.show(getSupportFragmentManager(), "Inserire cibo");
    }
}