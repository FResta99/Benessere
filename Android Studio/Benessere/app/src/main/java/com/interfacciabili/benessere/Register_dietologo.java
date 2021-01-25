package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Dietologo;

public class Register_dietologo extends AppCompatActivity {

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
        setContentView(R.layout.register_dietologo);
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

        Button dietologo_Register_Button = (Button) findViewById(R.id.Register_Button);

        //TODO testare i bug
        dietologo_Register_Button.setOnClickListener(new Button.OnClickListener() {
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

                EditText etStudio = (EditText) findViewById(R.id.editTextTextPersonName14);
                String etStudioTesto = (String) etStudio.getText().toString();
                if (TextUtils.isEmpty(etStudioTesto)){
                    etStudio.setError("Inserire Inidirizzo Studio");
                    errato = true;
                }

                Switch sw = (Switch) findViewById(R.id.switch1);
                String swSessoTesto = sw.getText().toString();

                if (errato == false){
                    boolean isDietologistUsernameInDatabase = databaseService.isDietologistUsernameInDatabase(etUsernameTesto);
                    if (isDietologistUsernameInDatabase == true){
                        etUsername.setError("Username già presente");
                    }else {
                        Dietologo dietologo = new Dietologo(etUsernameTesto, etPasswordTesto, etMailTesto, etNomeTesto, etCognomeTesto,
                                 swSessoTesto, Integer.parseInt(etEtaTesto), etStudioTesto);
                        if (databaseService.aggiungiDietologo(dietologo) == true){
                            Intent intent = new Intent(Register_dietologo.this, com.interfacciabili.benessere.HomeDietologo.class);
                            //TODO passare il dietologo all'intent
                            //startActivity(intent);
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