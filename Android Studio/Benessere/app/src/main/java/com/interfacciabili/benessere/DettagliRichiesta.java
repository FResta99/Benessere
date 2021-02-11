package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.RichiestaDieta;

public class DettagliRichiesta extends AppCompatActivity {
    TextView tvAlimentoModify;
    EditText etAlimentoModifier, etQuantitaAlimento;
    Spinner sPorzione;
    String tipoPorzione;
    RichiestaDieta richiesta;
    private static final String RICHIESTA = "RICHIESTA";




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
        setContentView(R.layout.activity_dettagli_richiesta);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sPorzione = findViewById(R.id.porzioneSpinner);
        sPorzione.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoPorzione = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tipoPorzione = (String) parent.getItemAtPosition(0);
            }
        });

        tvAlimentoModify = findViewById(R.id.tvAlimentoModifica);
        etAlimentoModifier = findViewById(R.id.etAlimentoModifier);
        etQuantitaAlimento = findViewById(R.id.etQuantitaAlimento);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            richiesta = (RichiestaDieta) bundle.get(RICHIESTA);
            tvAlimentoModify.setText(richiesta.getAlimentoDaModificare());
            etAlimentoModifier.setText(richiesta.getAlimentoRichiesto());
            etQuantitaAlimento.setText(richiesta.getAlimentoRichiestoPorzione());
            sPorzione.setSelection(getIndex(sPorzione, richiesta.getAlimentoRichiestoTipoPorzione()));
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


    public void approvaRichiesta(View view) {
        String alimentoModifier = etAlimentoModifier.getText().toString();
            if(alimentoModifier.isEmpty()){
                etAlimentoModifier.setError(getString(R.string.inserireAlimento));
                return;
            }
            if(etQuantitaAlimento.getText().toString().isEmpty()){
                etQuantitaAlimento.setError(getString(R.string.inserirePorzione));
                return;
            }
        int porzioneModifier = Integer.parseInt(etQuantitaAlimento.getText().toString());
        databaseService.approvaDieta(richiesta, alimentoModifier, porzioneModifier, tipoPorzione);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(DettagliRichiesta.this, DatabaseService.class);
        startService(intentDatabaseService);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }


    public void disapprovaRichiesta(View view) {
        databaseService.disapprovaDieta(richiesta);
        finish();
    }
}