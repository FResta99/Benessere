package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Dietologo;


public class Login_Diet extends AppCompatActivity {

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
        setContentView(R.layout.login_diet);

        Button dietologo_Login_Button = (Button) findViewById(R.id.btnRegBenessere);

        dietologo_Login_Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean errato = false;

                EditText etUsername = (EditText) findViewById(R.id.etUsername);
                String etUsernameTesto = (String) etUsername.getText().toString();
                if (TextUtils.isEmpty(etUsernameTesto)) {
                    etUsername.setError(getString(R.string.erroreUsername));
                    errato = true;
                }

                EditText etPassword = (EditText) findViewById(R.id.etPassword);
                String etPasswordTesto = (String) etPassword.getText().toString();

                if (TextUtils.isEmpty(etPasswordTesto)) {
                    etPassword.setError(getString(R.string.errorePassword));
                    errato = true;
                }

                if (errato == false){
                    boolean isDietologistUsernameInDatabase = databaseService.isDietologistUsernameInDatabase(etUsernameTesto);
                    if (isDietologistUsernameInDatabase == false){
                        Toast.makeText(Login_Diet.this, getString(R.string.dietologoNonRegistrato), Toast.LENGTH_SHORT).show();
                    } else {
                        Dietologo dietologoCercato = new Dietologo();
                        dietologoCercato = databaseService.ricercaDietologo(etUsernameTesto);
                        if (dietologoCercato == null){
                            Toast.makeText(getApplicationContext(),getString(R.string.dietologoNonRegistrato) , Toast.LENGTH_LONG).show();

                        } else {

                            if (dietologoCercato.getPassword().equals(etPasswordTesto)) {
                                Intent intent = new Intent(Login_Diet.this, com.interfacciabili.benessere.HomeDietologo.class);
                                intent.putExtra("EXPERT", dietologoCercato);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),getString(R.string.errorePassword2) , Toast.LENGTH_LONG).show();
                            }
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