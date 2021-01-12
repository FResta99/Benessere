package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Dieta;

import java.util.List;

public class InserimentoDieta extends AppCompatActivity {
    String username = "Silvio";
    EditText etColazione1, etColazione2, etPranzo1, etPranzo2, etCena1, etCena2;
    String colazione1, colazione2, pranzo1, pranzo2, cena1, cena2;

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

        etColazione1 = findViewById(R.id.etColazione1);
        etColazione2 = findViewById(R.id.etColazione2);
        etPranzo1 = findViewById(R.id.etPranzo1);
        etPranzo2 = findViewById(R.id.etPranzo2);
        etCena1 = findViewById(R.id.etCena1);
        etCena2 = findViewById(R.id.etCena2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            username =(String) bundle.get("USERNAME");
        }
    }

    public void inserisciDieta(View view) {

        try {
            colazione1 = etColazione1.getText().toString();
            colazione2 = etColazione2.getText().toString();
            pranzo1 = etPranzo1.getText().toString();
            pranzo2 = etPranzo2.getText().toString();
            cena1 = etCena1.getText().toString();
            cena2 = etCena2.getText().toString();
            Toast.makeText(this, "Dieta : " + username, Toast.LENGTH_SHORT).show();
            Dieta dietaDaAggiungere = new Dieta(username, colazione1, colazione2, pranzo1, pranzo2, cena1, cena2);
            boolean successo = databaseService.aggiungiDieta(dietaDaAggiungere);
            Toast.makeText(this, "Dieta aggiunta: " + successo, Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void modificaDieta(View view) {
        try {
            colazione1 = etColazione1.getText().toString();
            colazione2 = etColazione2.getText().toString();
            pranzo1 = etPranzo1.getText().toString();
            pranzo2 = etPranzo2.getText().toString();
            cena1 = etCena1.getText().toString();
            cena2 = etCena2.getText().toString();
            Toast.makeText(this, "Dieta : " + username, Toast.LENGTH_SHORT).show();
            Dieta dietaDaModificare = new Dieta(username, colazione1, colazione2, pranzo1, pranzo2, cena1, cena2);
            boolean successo = databaseService.modificaDieta(dietaDaModificare);
            Toast.makeText(this, "Dieta modificata: " + successo, Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        startService(intentDatabaseService);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    private void recuperaDietaCliente() {
        List<Dieta> dietaRecuperata = databaseService.recuperaDieta(username);
        if(dietaRecuperata.size()>0){
            Dieta dieta = dietaRecuperata.get(0);
            etColazione1.setText(dieta.getColazione1());
            etColazione2.setText(dieta.getColazione2());
            etPranzo1.setText(dieta.getPranzo1());
            etPranzo2.setText(dieta.getPranzo2());
            etCena1.setText(dieta.getCena1());
            etCena2.setText(dieta.getCena2());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}