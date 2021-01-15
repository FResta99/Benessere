package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

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

public class InserimentoAlimento extends AppCompatActivity {

    EditText etNomeAliemento, etPorzioneAlimento;
    Spinner spinnerPorzione, spinnerPasto;
    Button btnAggiungiAlimento, btnModificaAlimento, btnEliminaAlimento;
    String nomeAlimento, porzioneAlimento, porzioneAlimentoSpinner, tipoPasto;
    Cliente cliente;
    Alimento alimento;
    private static final String CLIENTE = "CLIENTE";
    public static final String ALIMENTO = "ALIMENTO";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_alimento);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        btnAggiungiAlimento = findViewById(R.id.btnAggiungiAlimento);
        btnModificaAlimento = findViewById(R.id.btnModificaAlimento);
        btnEliminaAlimento = findViewById(R.id.btnEliminaAlimento);

        if(bundle!=null)
        {
            if(bundle.containsKey(ALIMENTO)){
                alimento = (Alimento) bundle.get(ALIMENTO);
                btnModificaAlimento.setVisibility(View.VISIBLE);
                btnEliminaAlimento.setVisibility(View.VISIBLE);
                btnAggiungiAlimento.setVisibility(View.GONE);
            }
            cliente = (Cliente) bundle.get(CLIENTE);
        }

        etNomeAliemento = findViewById(R.id.etNomeAlimento);
        etPorzioneAlimento = findViewById(R.id.etPorzioneAlimento);

        spinnerPasto = findViewById(R.id.spinnerPasto);
        spinnerPasto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoPasto = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tipoPasto = (String) parent.getItemAtPosition(0);
            }
        });

        spinnerPorzione = findViewById(R.id.spinnerPorzione);
        spinnerPorzione.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                porzioneAlimentoSpinner = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                porzioneAlimentoSpinner = (String) parent.getItemAtPosition(0);
            }
        });
        setAlimento();
    }

    public void aggiungiAlimento(View view) {
        nomeAlimento = etNomeAliemento.getText().toString();
        porzioneAlimento = etPorzioneAlimento.getText().toString();
        Alimento alimentoDaInserire = new Alimento(nomeAlimento, porzioneAlimento, porzioneAlimentoSpinner, tipoPasto, null);
        boolean risultato = databaseService.aggiungiAlimentoADieta(cliente, alimentoDaInserire);
        Toast.makeText(this, ""+risultato, Toast.LENGTH_SHORT).show();
        finish();
    }

    //TODO Aggiustare la selezione della porzione
    public void setAlimento(){
        if(alimento!=null){
            etNomeAliemento.setText(alimento.getNome());
            etPorzioneAlimento.setText(alimento.getPorzione());
            spinnerPorzione.setSelection(getIndex(spinnerPorzione, alimento.getTipoPorzione()));
            spinnerPasto.setSelection(getIndex(spinnerPasto, alimento.getTipoPasto()));
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

    public void modificaAlimento(View view) {
        nomeAlimento = etNomeAliemento.getText().toString();
        porzioneAlimento = etPorzioneAlimento.getText().toString();
        boolean risultato = databaseService.modificaAlimentoDieta(alimento, nomeAlimento, Integer.parseInt(porzioneAlimento), porzioneAlimentoSpinner, tipoPasto);
        Toast.makeText(this, ""+risultato, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void eliminaAlimento(View view) {
        databaseService.eliminaAlimento(alimento);
        finish();
    }
}