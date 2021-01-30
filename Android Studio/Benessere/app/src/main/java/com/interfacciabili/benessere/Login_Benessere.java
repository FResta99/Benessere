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
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Coach;


public class Login_Benessere extends AppCompatActivity {

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
        setContentView(R.layout.login_benessere);
        Button client_Login_Button = (Button) findViewById(R.id.btnRegBenessere);

        client_Login_Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean errato = false;

                EditText etUsername = (EditText) findViewById(R.id.etUsername);
                String etUsernameTesto = (String) etUsername.getText().toString();
                if (TextUtils.isEmpty(etUsernameTesto)) {
                    etUsername.setError("Inserire Username");
                    errato = true;
                }

                EditText etPassword = (EditText) findViewById(R.id.etPassword);
                String etPasswordTesto = (String) etPassword.getText().toString();

                if (TextUtils.isEmpty(etPasswordTesto)) {
                    etPassword.setError("Inserire Password");
                    errato = true;
                }

                if (errato == false){
                    boolean isClientUsernameInDatabase = databaseService.isClientUsernameInDatabase(etUsernameTesto);
                    if (isClientUsernameInDatabase == false){
                        Toast.makeText(Login_Benessere.this, getString(R.string.clienteNonRegistrato), Toast.LENGTH_SHORT).show();
                    } else {
                        Cliente clienteCercato = new Cliente();
                        clienteCercato = databaseService.ricercaCliente(etUsernameTesto);
                        if (clienteCercato == null){
                            Toast.makeText(getApplicationContext(), getString(R.string.clienteNonRegistrato) , Toast.LENGTH_LONG).show();

                        } else {

                            if (clienteCercato.getPassword().equals(etPasswordTesto)) {
                                Intent intent = new Intent(Login_Benessere.this, com.interfacciabili.benessere.HomeCliente.class);
                                intent.putExtra("CLIENTE", clienteCercato);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.errorePassword2) , Toast.LENGTH_LONG).show();
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