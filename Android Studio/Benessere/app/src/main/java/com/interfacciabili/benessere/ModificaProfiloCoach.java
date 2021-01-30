package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Coach;
import com.interfacciabili.benessere.model.Dietologo;

public class ModificaProfiloCoach extends AppCompatActivity {

    private String sesso = null;
    private Coach coach;
    private EditText etPassword, etMail, etNome, etCognome, etEta, etTornello;
    private TextView tvGenere;
    private RadioButton rbMaschio, rbFemmina, rbAltro;
    private Button btnAggiorna;

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
        setContentView(R.layout.activity_modifica_profilo_coach);

        Toolbar homeToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(homeToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        etPassword = (EditText) findViewById(R.id.etPassword);
        etMail = (EditText) findViewById(R.id.etEmail);
        etNome = (EditText) findViewById(R.id.etNome);
        etCognome = (EditText) findViewById(R.id.etCognome);
        etEta = (EditText) findViewById(R.id.etEta);
        etTornello = (EditText) findViewById(R.id.etTornelloIndirizzoIP);

        tvGenere = (TextView) findViewById(R.id.tvGenere);

        rbMaschio = (RadioButton) findViewById(R.id.rbMaschio);
        rbFemmina = (RadioButton) findViewById(R.id.rbFemmina);
        rbAltro = (RadioButton) findViewById(R.id.rbAltro);

        //INTENT DA HOMECOACH
        Intent intentFrom = getIntent();
        coach = intentFrom.getParcelableExtra("EXPERT");

        btnAggiorna = findViewById(R.id.btnAggiorna);

        if(coach!=null){
            etPassword.setText(coach.getPassword());
            etMail.setText(coach.getEmail());
            etNome.setText(coach.getNome());
            etCognome.setText(coach.getCognome());
            etEta.setText(String.valueOf(coach.getEta()));
            if(!coach.getPalestra().isEmpty()){
                etTornello.setText(coach.getPalestra());
            }


            sesso = coach.getSesso();
            switch (sesso){
                case "Maschio":
                    rbMaschio.setChecked(true);
                    break;
                case "Femmina":
                    rbFemmina.setChecked(true);
                    break;
                case "Altro":
                    rbAltro.setChecked(true);
                    break;
            }

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    public void aggiornaProfiloDietologo(View view) {
        boolean errato = false;

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

        if (etEta.getText().toString().isEmpty()){
            etEta.setError(getString(R.string.erroreEta));
            errato = true;
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

        if (!errato) {
            if(databaseService.modificaCoach(coach.getUsername(),
                    etPassword.getText().toString(),
                    etMail.getText().toString(),
                    etNome.getText().toString(),
                    etCognome.getText().toString(),
                    sesso,
                    Integer.parseInt(etEta.getText().toString()),
                    etTornello.getText().toString())){
                Toast.makeText(ModificaProfiloCoach.this, "Dati aggiornati", Toast.LENGTH_LONG).show();

                coach.setPassword(etPassword.getText().toString());
                coach.setEmail(etMail.getText().toString());
                coach.setNome(etNome.getText().toString());
                coach.setSesso(sesso);
                coach.setEta(Integer.parseInt(etEta.getText().toString()));
                coach.setPalestra(etTornello.getText().toString());


            } else {
                Toast.makeText(ModificaProfiloCoach.this, "Errore", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void onBackPressed() {
        Intent goToHomeCoach = new Intent(ModificaProfiloCoach.this, HomeCoach.class);
        goToHomeCoach.putExtra("EXPERT", coach);
        startActivity(goToHomeCoach);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
