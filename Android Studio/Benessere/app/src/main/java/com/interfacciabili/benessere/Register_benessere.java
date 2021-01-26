package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Coach;

public class Register_benessere extends AppCompatActivity {
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
        setContentView(R.layout.register_benessere);
        Switch sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    sw.setText("Maschio");
                } else {
                    // The toggle is disabled
                    sw.setText("Femmina");
                }
            }
        });

        Button benessere_Register_Button = (Button) findViewById(R.id.Register_Button);
        benessere_Register_Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean errato = false;

                EditText etUsername = (EditText) findViewById(R.id.editTextTextPersonName6);
                String etUsernameTesto = (String) etUsername.getText().toString();
                if (TextUtils.isEmpty(etUsernameTesto)){
                    etUsername.setError("Inserire Username");
                    errato = true;
                }

                EditText etPassword = (EditText) findViewById(R.id.editTextTextPassword2);
                String etPasswordTesto = (String) etPassword.getText().toString();
                if (TextUtils.isEmpty(etPasswordTesto)){
                    etPassword.setError("Inserire Password");
                    errato = true;
                }

                EditText etMail = (EditText) findViewById(R.id.editTextTextPersonName9);
                String etMailTesto = (String) etMail.getText().toString();
                if (TextUtils.isEmpty(etMailTesto)){
                    etMail.setError("Inserire Mail");
                    errato = true;
                }

                EditText etNome = (EditText) findViewById(R.id.editTextTextPersonName10);
                String etNomeTesto = (String) etNome.getText().toString();
                if (TextUtils.isEmpty(etNomeTesto)){
                    etNome.setError("Inserire Nome");
                    errato = true;
                }

                EditText etCognome = (EditText) findViewById(R.id.editTextTextPersonName11);
                String etCognomeTesto = (String) etCognome.getText().toString();
                if (TextUtils.isEmpty(etCognomeTesto)){
                    etCognome.setError("Inserire Cognome");
                    errato = true;
                }

                EditText etEta = (EditText) findViewById(R.id.editTextNumber);
                String etEtaTesto = (String) etEta.getText().toString();
                int etEtaNum;
                if (TextUtils.isEmpty(etEtaTesto)){
                    etEta.setError("Inserire Età");
                    errato = true;
                }

                EditText etPeso = (EditText) findViewById(R.id.editTextTextPersonName13);
                String etPesoTesto = (String) etPeso.getText().toString();
                int etPesoNum;
                if (TextUtils.isEmpty(etPesoTesto)){
                    etPeso.setError("Inserire Peso");
                    errato = true;
                }

                EditText etAltezza = (EditText) findViewById(R.id.editTextTextPersonName14);
                String etAltezzaTesto = (String) etAltezza.getText().toString();
                int etAltezzaNum;
                if (TextUtils.isEmpty(etAltezzaTesto)){
                    etAltezza.setError("Inserire Altezza");
                    errato = true;
                }

                Switch sw = (Switch) findViewById(R.id.switch1);
                String swSessoTesto = sw.getText().toString();

                if (errato == false){
                    boolean isClientUsernameInDatabase = databaseService.isClientUsernameInDatabase(etUsernameTesto);
                    if (isClientUsernameInDatabase == true){
                        etUsername.setError("Username già presente");
                    }else {
                        Cliente cliente = new Cliente(etUsernameTesto, etPasswordTesto, etMailTesto, etNomeTesto, etCognomeTesto,
                                swSessoTesto, Integer.parseInt(etEtaTesto), Integer.parseInt(etPesoTesto), Integer.parseInt(etAltezzaTesto), null);
                        if (databaseService.aggiungiCliente(cliente) == true){
                            Intent intent = new Intent(Register_benessere.this, com.interfacciabili.benessere.HomeCliente.class);
                            intent.putExtra("CLIENTE", cliente);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),"Errore: registrazione non riuscita." , Toast.LENGTH_LONG).show();
                        }




                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }
}