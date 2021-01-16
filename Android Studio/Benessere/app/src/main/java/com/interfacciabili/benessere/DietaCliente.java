package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Alimento;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dieta;

import java.util.ArrayList;
import java.util.List;

public class DietaCliente extends AppCompatActivity {

    public Cliente cliente = new Cliente("Silvio", "password");
    ListView lvDietaColazione, lvDietaPranzo, lvDietaCena;
    TextView tvDietaColazione, tvDietaPranzo, tvDietaCena, tvAlimentiDieta;
    ArrayAdapter dietAdapterColazione, dietAdapterPranzo, dietAdapterCena;

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();

            mostraDietaCliente();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieta_cliente);

        tvDietaColazione = findViewById(R.id.tvColazione);
        tvDietaPranzo = findViewById(R.id.tvPranzo);
        tvDietaCena = findViewById(R.id.tvCena);
        tvAlimentiDieta = findViewById(R.id.tvAlimentiDieta);

        lvDietaColazione = findViewById(R.id.lvDietaColazione);
        lvDietaColazione.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alimento alimentoCliccato = (Alimento) parent.getItemAtPosition(position);
                ModificaDietaDialog mdd = new ModificaDietaDialog();
                mdd.setAlimento(alimentoCliccato);
                mdd.setUtente(cliente.getUsername());
                mdd.setDietologo("Dietologo1");
                mdd.show(getSupportFragmentManager(), "Modifica");
            }
        });

        lvDietaPranzo = findViewById(R.id.lvDietaPranzo);
        lvDietaPranzo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alimento alimentoCliccato = (Alimento) parent.getItemAtPosition(position);
                ModificaDietaDialog mdd = new ModificaDietaDialog();
                mdd.setAlimento(alimentoCliccato);
                mdd.setUtente(cliente.getUsername());
                mdd.setDietologo("Dietologo1");
                mdd.show(getSupportFragmentManager(), "Modifica");
            }
        });

        lvDietaCena = findViewById(R.id.lvDietaCena);
        lvDietaCena.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alimento alimentoCliccato = (Alimento) parent.getItemAtPosition(position);
                ModificaDietaDialog mdd = new ModificaDietaDialog();
                mdd.setAlimento(alimentoCliccato);
                mdd.setUtente(cliente.getUsername());
                mdd.setDietologo("Dietologo1");
                mdd.show(getSupportFragmentManager(), "Modifica");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        startService(intentDatabaseService);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);

    }

    private void mostraDietaCliente() {
        List<Alimento> dietaRecuperata = databaseService.recuperaDieta(cliente.getUsername());
        String alimenti = getResources().getQuantityString(R.plurals.alimenti_dieta, dietaRecuperata.size(), dietaRecuperata.size());
        tvAlimentiDieta.setText(alimenti);
        List<Alimento> listColazione = new ArrayList<>(), listPranzo = new ArrayList<>(), listCena = new ArrayList<>();
        for (Alimento a: dietaRecuperata) {
            switch (a.getTipoPasto()){
                case "Colazione":
                    listColazione.add(a);
                    break;
                case "Pranzo":
                    listPranzo.add(a);
                    break;
                case "Cena":
                    listCena.add(a);
                    break;
            }

        }
        if(listColazione.isEmpty()){
            tvDietaColazione.setText("Digiuno mattutino");
            lvDietaColazione.setVisibility(View.GONE);
        } else {
            dietAdapterColazione = new ArrayAdapter<Alimento>(this, android.R.layout.simple_list_item_1, listColazione);
            lvDietaColazione.setAdapter(dietAdapterColazione);
        }

        if(listPranzo.isEmpty()){
            tvDietaPranzo.setText("Digiuno a pranzo");
            lvDietaPranzo.setVisibility(View.GONE);
        } else {
            dietAdapterPranzo = new ArrayAdapter<Alimento>(this, android.R.layout.simple_list_item_1, listPranzo);
            lvDietaPranzo.setAdapter(dietAdapterPranzo);
        }

        if (listCena.isEmpty()){
            tvDietaCena.setText("Digiuno serale");
            lvDietaCena.setVisibility(View.GONE);
        } else {
            dietAdapterCena = new ArrayAdapter<Alimento>(this, android.R.layout.simple_list_item_1, listCena);
            lvDietaCena.setAdapter(dietAdapterCena);
        }


    }


}