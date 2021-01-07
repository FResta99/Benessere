package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.control.DietDBHelper;

public class DettagliRichiesta extends AppCompatActivity {
    TextView tvAlimentoModify;
    EditText etAlimentoModifier;

    String alimentoModify, alimentoModifier;
    int id;

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();

            etAlimentoModifier.addTextChangedListener(new TextWatcher() {


                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if(s.length()>0 ){
                        databaseService.approvaDieta(id, s.toString());
                    }
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_richiesta);
        tvAlimentoModify = findViewById(R.id.tvAlimentoModifica);
        etAlimentoModifier = findViewById(R.id.etAlimentoModifier);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            id = (int) bundle.getInt("ID");
            alimentoModify =(String) bundle.get("ALIMENTO_MODIFY");
            tvAlimentoModify.setText(alimentoModify);
            alimentoModifier =(String) bundle.get("ALIMENTO_MODIFIER");
            etAlimentoModifier.setText(alimentoModifier);
        }
    }


    public void approvaRichiesta(View view) {
        databaseService.approvaDieta(id);
        //TODO Automodifica della dieta
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
}