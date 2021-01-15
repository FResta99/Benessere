package com.interfacciabili.benessere;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Alimento;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dieta;

import java.util.List;

public class InserimentoDieta extends AppCompatActivity {
    Cliente cliente;
    Alimento alimentoCliccato;
    ListView lvDietaDietologo;
    FloatingActionButton facAggiungiAlimento;
    ArrayAdapter dietAdapter;
    private static final String CLIENTE = "CLIENTE";
    public static final String ALIMENTO = "ALIMENTO";
    //TODO Inserire la modifica di un alimento

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();

            recuperaDietaCliente();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_dieta);

        lvDietaDietologo = findViewById(R.id.lvDietaDietologo);
        lvDietaDietologo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alimentoCliccato = (Alimento) parent.getItemAtPosition(position);
                modificaAlimento();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            cliente =(Cliente) bundle.get(CLIENTE);
        }
    }

    public void inserisciAlimento(View view) {
        Intent intent = new Intent(InserimentoDieta.this, InserimentoAlimento.class);
        intent.putExtra(CLIENTE, cliente);
        startActivity(intent);
    }

    public void modificaAlimento() {
        Intent intent = new Intent(InserimentoDieta.this, InserimentoAlimento.class);
        intent.putExtra(CLIENTE, cliente);
        intent.putExtra(ALIMENTO, alimentoCliccato);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(databaseService!=null){
            recuperaDietaCliente();
        }
    }

    private void recuperaDietaCliente() {
        dietAdapter = new ArrayAdapter<Alimento>(this, android.R.layout.simple_list_item_1, databaseService.recuperaDieta(cliente.getUsername()));
        lvDietaDietologo.setAdapter(dietAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}