package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Coach;
import com.interfacciabili.benessere.model.Dietologo;

public class Login_Coach extends AppCompatActivity {

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
        setContentView(R.layout.login_coach);

        Button coach_Login_Button = (Button) findViewById(R.id.btnRegBenessere);

        coach_Login_Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean errato = false;

                EditText etUsername = (EditText) findViewById(R.id.editTextTextPersonName);
                String etUsernameTesto = (String) etUsername.getText().toString();
                if (TextUtils.isEmpty(etUsernameTesto)) {
                    etUsername.setError("Inserire Username");
                    errato = true;
                }

                EditText etPassword = (EditText) findViewById(R.id.editTextTextPersonName2);
                String etPasswordTesto = (String) etPassword.getText().toString();

                if (TextUtils.isEmpty(etPasswordTesto)) {
                    etPassword.setError("Inserire Password");
                    errato = true;
                }

                if (errato == false){
                    boolean isCoachUsernameInDatabase = databaseService.isCoachUsernameInDatabase(etUsernameTesto);
                    if (isCoachUsernameInDatabase == false){
                        Toast.makeText(Login_Coach.this, "Coach non registrato", Toast.LENGTH_SHORT).show();
                    } else {
                        Coach coachCercato = new Coach();
                        coachCercato = databaseService.ricercaCoach(etUsernameTesto);
                        if (coachCercato == null){
                            Toast.makeText(getApplicationContext(),"Coach non registrato" , Toast.LENGTH_LONG).show();

                        } else {

                            if (coachCercato.getPassword().equals(etPasswordTesto)) {
                                Intent intent = new Intent(Login_Coach.this, com.interfacciabili.benessere.HomeCoach.class);
                                intent.putExtra("EXPERT", coachCercato);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),"Password errata" , Toast.LENGTH_LONG).show();
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