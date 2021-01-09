package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Cliente;

public class RicercaCliente extends AppCompatActivity {
    private static final String EXPERT = "EXPERT";
    private static final String TAG_LOG = "RicercaCliente";

    TextView etName;
    Button btnAggiungi;
    ListView lvRicercaClienti;
    ArrayAdapter clientAdapter;

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private String usernameExpert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_cliente);

        Intent intentFrom = getIntent();
        if (intentFrom != null && intentFrom.hasExtra(EXPERT)) {
            usernameExpert = intentFrom.getStringExtra(EXPERT);

            btnAggiungi = findViewById(R.id.btnCerca);
            etName = findViewById(R.id.etName);

            lvRicercaClienti = findViewById(R.id.lvRicercaClienti);
            lvRicercaClienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cliente clienteCliccato = (Cliente) parent.getItemAtPosition(position);
                    InserisciClienteDialog icd = new InserisciClienteDialog();
                    icd.setUsernameExpert(usernameExpert);
                    icd.setUsernameCliente(clienteCliccato.getUsername());
                    icd.show(getSupportFragmentManager(), "Inserisci cliente");
                }
            });
        } else {
            Log.d(TAG_LOG, "The bundle doesn't contain an expert.");
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        startService(intentDatabaseService);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseService != null) {
            unbindService(serviceConnection);
        }
    }

    public void cercaCliente(View v){
        if(!etName.getText().toString().isEmpty()){
            clientAdapter = new ArrayAdapter<Cliente>(RicercaCliente.this,
                    android.R.layout.simple_list_item_1,
                    databaseService.recuperaClientiSenzaDietologo(etName.getText().toString()));
            lvRicercaClienti.setAdapter(clientAdapter);
        }
    }
}