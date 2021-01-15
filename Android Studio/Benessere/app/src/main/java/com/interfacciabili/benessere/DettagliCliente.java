package com.interfacciabili.benessere;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.interfacciabili.benessere.model.Cliente;

public class DettagliCliente extends AppCompatActivity implements EliminaClienteDialog.EliminaClienteDialogCallback {
    private static final String EXPERT = "EXPERT";
    private static final String CLIENTE = "CLIENTE";
    private static final String TAG_LOG = "DettagliCliente";

    public TextView tvUsername;

    public Cliente cliente;
    public String usernameExpert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettagli_cliente);

        Toolbar homeToolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(homeToolbar);

        ActionBar mainActionbar = getSupportActionBar();
        mainActionbar.setDisplayHomeAsUpEnabled(true);

        tvUsername = findViewById(R.id.tvDettaglioUsernameCliente);

        Intent intentFrom = getIntent();
        Bundle bundle = intentFrom.getExtras();
        if ((bundle != null) && (bundle.containsKey(EXPERT)) && (bundle.containsKey(CLIENTE))) {
            usernameExpert = (String) bundle.getString(EXPERT);
            cliente = (Cliente) bundle.get(CLIENTE);
        } else {
            Log.d(TAG_LOG, "The bundle doesn't contain a client and an expert.");
        }

        if (cliente != null && usernameExpert != null) {
            /* Verifico l'orientamento del dispositivo e se esso è in orizzontale, lancio l'activity dell'home passando il cliente
             * che dovrà essere visualizzato nel fragment.
             */
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                goToHomeDietologolActivity();
            }

            homeToolbar.setSubtitle(usernameExpert);
            tvUsername.setText(cliente.getUsername());

            Button btnGestisci = findViewById(R.id.btnGestisciDieta);
            Button btnElimina = findViewById(R.id.btnElimina);

            btnGestisci.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToGestisciActivity();
                }
            });

            btnElimina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToEliminaCliente();
                }
            });
        } else {
            Log.d(TAG_LOG, "There is not a client.");
            finish();
        }
    }

    @Override
    public void updateEliminaClienteDialogCallback() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.actionbar_button_1) {
            Log.d(TAG_LOG, "Button one pressed");
        } else if (item.getItemId() == R.id.actionbar_button_2) {
            Log.d(TAG_LOG, "Button two pressed");
        } else if (item.getItemId() == R.id.actionbar_button_3) {
            Log.d(TAG_LOG, "Button three pressed");
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToHomeDietologolActivity() {
        Intent intentOut = new Intent(DettagliCliente.this, HomeDietologo.class);
        intentOut.putExtra(CLIENTE, cliente);

        startActivity(intentOut);
    }

    private void goToGestisciActivity() {
        Intent intentTo = new Intent(DettagliCliente.this, InserimentoDieta.class);
        intentTo.putExtra(CLIENTE, cliente);
        startActivity(intentTo);

    }

    public void goToEliminaCliente() {
        EliminaClienteDialog ecd = new EliminaClienteDialog();
        ecd.setCliente(cliente);
        ecd.show(getSupportFragmentManager(), "Elimina cliente");
    }
}