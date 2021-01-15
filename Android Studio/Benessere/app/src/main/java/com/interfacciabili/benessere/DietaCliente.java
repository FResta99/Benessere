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
    ListView lvDieta;
    ArrayAdapter dietAdapter;

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

        lvDieta = findViewById(R.id.lvDieta);
        lvDieta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        dietAdapter = new ArrayAdapter<Alimento>(this, android.R.layout.simple_list_item_1, dietaRecuperata);
        lvDieta.setAdapter(dietAdapter);

    }


}