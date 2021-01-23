package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Alimento;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Esercizio;

public class InserimentoAllenamento extends AppCompatActivity {

    Cliente cliente;
    Esercizio esercizio;
    private static final String CLIENTE = "CLIENTE";
    public static final String ESERCIZIO = "ESERCIZIO";

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

    private Button btnAggiungiEsercizio, btnModificaEsercizio, btnEliminaEsercizio;
    private EditText etNomeEsercizio, etRipetizioniEsercizio;
    private Spinner spinneGiorno;
    private String giornoEsercizio;
    private String nomeEsercizio;
    private int ripetizioniEsercizio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_allenamento);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAggiungiEsercizio = findViewById(R.id.btnAggiungiEsercizio);
        btnModificaEsercizio = findViewById(R.id.btnModificaEsercizio);
        btnEliminaEsercizio = findViewById(R.id.btnEliminaEsercizio);

        etNomeEsercizio = findViewById(R.id.etNomeEsercizio);
        etRipetizioniEsercizio = findViewById(R.id.etRipetizioniEsercizio);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            if(bundle.containsKey(ESERCIZIO)){
                esercizio = (Esercizio) bundle.get(ESERCIZIO);
                btnModificaEsercizio.setVisibility(View.VISIBLE);
                btnEliminaEsercizio.setVisibility(View.VISIBLE);
                btnAggiungiEsercizio.setVisibility(View.GONE);
            }
            cliente = (Cliente) bundle.get(CLIENTE);
        }

        spinneGiorno = findViewById(R.id.spinnerGiorno);
        spinneGiorno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                giornoEsercizio = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                giornoEsercizio = (String) parent.getItemAtPosition(0);
            }
        });
        setEsercizio();
    }

    public void aggiungiEsercizio(View view) {
        if(etNomeEsercizio.getText().toString().isEmpty()){
            etNomeEsercizio.setError("Inserire un esercizio");
            return;
        }

        if(etRipetizioniEsercizio.getText().toString().isEmpty()){
            etRipetizioniEsercizio.setError("Inserire una ripetizione");
            return;
        }

        nomeEsercizio = etNomeEsercizio.getText().toString();
        ripetizioniEsercizio = Integer.parseInt(etRipetizioniEsercizio.getText().toString());
        Esercizio esercizioDaInserire = new Esercizio(nomeEsercizio, ripetizioniEsercizio, giornoEsercizio, "");
        boolean risultato = databaseService.aggiungiEsercizioASchedaAllenamento(cliente, esercizioDaInserire);
        Toast.makeText(this, ""+risultato, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void setEsercizio(){
        if(esercizio!=null){
            etNomeEsercizio.setText(esercizio.getNome());
            etRipetizioniEsercizio.setText(String.valueOf(esercizio.getRipetizioni()));
            spinneGiorno.setSelection(getIndex(spinneGiorno, esercizio.getGiorno()));
            //spinneGiorno.setSelection(Integer.parseInt(alimento.getGiorno()));
        }
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(databaseService!=null){
            unbindService(serviceConnection);
        }

    }

    public void modificaEsercizio(View view) {
        nomeEsercizio = etNomeEsercizio.getText().toString();
        ripetizioniEsercizio = Integer.parseInt(etRipetizioniEsercizio.getText().toString());
        boolean risultato = databaseService.modificaEsercizioScheda(esercizio, nomeEsercizio, ripetizioniEsercizio, giornoEsercizio);
        Toast.makeText(this, ""+risultato, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void eliminaEsercizio(View view) {
        databaseService.eliminaEsercizio(esercizio);
        finish();
    }
}