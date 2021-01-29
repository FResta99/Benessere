package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Coach;

public class Register_benessere extends AppCompatActivity {
    String sesso = null;

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

        EditText etUsername = (EditText) findViewById(R.id.etUsername);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);
        EditText etMail = (EditText) findViewById(R.id.etEmail);
        EditText etNome = (EditText) findViewById(R.id.etNome);
        EditText etCognome = (EditText) findViewById(R.id.etCognome);
        EditText etEta = (EditText) findViewById(R.id.etEta);
        EditText etPeso = (EditText) findViewById(R.id.etPeso);
        EditText etAltezza = (EditText) findViewById(R.id.etAltezza);

        TextView tvGenere = (TextView) findViewById(R.id.tvGenere);

        RadioButton rbMaschio = (RadioButton) findViewById(R.id.rbMaschio);
        RadioButton rbFemmina = (RadioButton) findViewById(R.id.rbFemmina);
        RadioButton rbAltro = (RadioButton) findViewById(R.id.rbAltro);

        Button bennessereRegisterButton = (Button) findViewById(R.id.btnRegBenessere);
        bennessereRegisterButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean errato = false;

                if (etUsername.getText().toString().isEmpty()){
                    etUsername.setError(getString(R.string.erroreUsername));
                    errato = true;
                }

                if (etPassword.getText().toString().isEmpty()){
                    etPassword.setError(getString(R.string.errorePassword));
                    errato = true;
                }

                if (etMail.getText().toString().isEmpty()){
                    etMail.setError(getString(R.string.erroreEmail));
                    errato = true;
                }

                if (etNome.getText().toString().isEmpty()){
                    etNome.setError(getString(R.string.erroreNome));
                    errato = true;
                }

                if (etCognome.getText().toString().isEmpty()){
                    etCognome.setError(getString(R.string.erroreCognome));
                    errato = true;
                }

                if (etEta.getText().toString().isEmpty()){
                    etEta.setError(getString(R.string.erroreEta));
                    errato = true;
                }

                if (rbMaschio.isChecked()) {
                    sesso = "Maschio";
                } else if (rbFemmina.isChecked()){
                    sesso = "Femmina";
                } else if (rbAltro.isChecked()){
                    sesso = "Altro";
                }

                if (sesso == null) {
                    tvGenere.setTextColor(getColor(R.color.simplyRed));
                    tvGenere.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);

                    rbMaschio.setTextColor(getColor(R.color.simplyRed));
                    rbMaschio.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
                    rbFemmina.setTextColor(getColor(R.color.simplyRed));
                    rbFemmina.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
                    rbAltro.setTextColor(getColor(R.color.simplyRed));
                    rbAltro.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
                }

                if (etPeso.getText().toString().isEmpty()){
                    etPeso.setError(getString(R.string.errorePeso));
                    errato = true;
                }

                if (etAltezza.getText().toString().isEmpty()){
                    etAltezza.setError(getString(R.string.erroreAltezza));
                    errato = true;
                }

                if (!errato) {
                    boolean isClientUsernameInDatabase = databaseService.isClientUsernameInDatabase(etUsername.getText().toString());

                    if (isClientUsernameInDatabase){
                        etUsername.setError(getString(R.string.erroreUsernameEsistente));
                    } else {
                        Cliente cliente = new Cliente(
                                etUsername.getText().toString(),
                                etPassword.getText().toString(),
                                etMail.getText().toString(),
                                etNome.getText().toString(),
                                etCognome.getText().toString(),
                                sesso,
                                Integer.parseInt(etEta.getText().toString()),
                                Integer.parseInt(etPeso.getText().toString()),
                                Integer.parseInt(etAltezza.getText().toString()),
                                null
                        );

                        if (databaseService.aggiungiCliente(cliente)){
                            Intent intent = new Intent(Register_benessere.this, com.interfacciabili.benessere.HomeCliente.class);
                            intent.putExtra("CLIENTE", cliente);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.toastErroreRegistrazione), Toast.LENGTH_LONG).show();
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