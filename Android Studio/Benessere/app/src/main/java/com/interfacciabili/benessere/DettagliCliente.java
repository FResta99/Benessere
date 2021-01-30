package com.interfacciabili.benessere;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Coach;
import com.interfacciabili.benessere.model.Dietologo;
import com.interfacciabili.benessere.model.Esperto;

public class DettagliCliente extends AppCompatActivity implements EliminaClienteDialog.EliminaClienteDialogCallback {
    private static final String EXPERT = "EXPERT";
    private static final String CLIENTE = "CLIENTE";
    private static final String TAG_LOG = "DettagliCliente";

    private TextView tvUsername;
    private ImageView ivFotoCliente;

    private Cliente cliente;
    private Esperto esperto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettagli_cliente);

        Toolbar homeToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(homeToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvUsername = findViewById(R.id.tvDettaglioUsernameCliente);
        ivFotoCliente = findViewById(R.id.ivFotoCliente);

        Intent intentFrom = getIntent();
        Bundle bundle = intentFrom.getExtras();
        if ((bundle != null) && (bundle.containsKey(EXPERT)) && (bundle.containsKey(CLIENTE))) {
            esperto = bundle.getParcelable(EXPERT);
            cliente = (Cliente) bundle.get(CLIENTE);
        } else {
            Log.d(TAG_LOG, "The bundle doesn't contain a client and an expert.");
        }

        if (cliente != null && esperto != null) {
            /* Verifico l'orientamento del dispositivo e se esso è in orizzontale, lancio l'activity dell'home passando il cliente
             * che dovrà essere visualizzato nel fragment.
             */
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if(esperto instanceof Dietologo){
                    goToHomeDietologolActivity();
                } else {
                    goToHomeCoachActivity();
                }
            }

            if(esperto instanceof Dietologo){
                homeToolbar.setSubtitle((((Dietologo) esperto).getUsername()));
            } else {
                homeToolbar.setSubtitle((((Coach) esperto).getUsername()));
            }
            if(cliente.getFotoProfilo()!=null){
                ivFotoCliente.setImageURI(Uri.parse(cliente.getFotoProfilo()));
            }

            tvUsername.setText(cliente.toString());
        } else {
            Log.d(TAG_LOG, "There is not a client.");
            finish();
        }
    }

    @Override
    public void updateEliminaClienteDialogCallback() {
        if(esperto instanceof Dietologo){
            goToHomeDietologolActivity();
        } else {
            goToHomeCoachActivity();
        }

        finish();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToHomeDietologolActivity() {
        Intent intentOut = new Intent(DettagliCliente.this, HomeDietologo.class);
        intentOut.putExtra(EXPERT, esperto);
        intentOut.putExtra(CLIENTE, cliente);
        startActivity(intentOut);
        finish();
    }

    private void goToHomeCoachActivity() {
        Intent intentOut = new Intent(DettagliCliente.this, HomeCoach.class);
        intentOut.putExtra(EXPERT, esperto);
        intentOut.putExtra(CLIENTE, cliente);
        startActivity(intentOut);
        finish();
    }

    public void goToGestisciActivity(View v) {
        Intent intentTo;

        if(esperto instanceof Dietologo){
            intentTo = new Intent(DettagliCliente.this, InserimentoDieta.class);
        } else {
            intentTo = new Intent(DettagliCliente.this, InserimentoScheda.class);
        }
        intentTo.putExtra(CLIENTE, cliente);
        startActivity(intentTo);
    }

    public void goToEliminaCliente(View v) {
        EliminaClienteDialog ecd = new EliminaClienteDialog();
        ecd.setCliente(cliente);
        ecd.setEsperto(esperto);
        ecd.show(getSupportFragmentManager(), getString(R.string.eliminareCliente));
    }

    @Override
    public void onBackPressed() {
        if (esperto instanceof Dietologo) {
            goToHomeDietologolActivity();
        } else {
            goToHomeCoachActivity();
        }
    }
}