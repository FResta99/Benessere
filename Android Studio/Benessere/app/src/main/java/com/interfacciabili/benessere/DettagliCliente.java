package com.interfacciabili.benessere;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.interfacciabili.benessere.model.Cliente;

public class DettagliCliente extends AppCompatActivity implements EliminaClienteDialog.EliminaClienteDialogCallback {
    private static final String CLIENTE = "CLIENTE";
    public TextView tvUsername;
    public Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettagli_cliente);

        tvUsername = findViewById(R.id.tvDettaglioUsernameCliente);

        Intent intentFrom = getIntent();
        Bundle bundle = intentFrom.getExtras();
        if (bundle != null) {
            cliente = (Cliente) bundle.get(CLIENTE);
            tvUsername.setText(cliente.getUsername());
        }

        /* Verifico l'orientamento del dispositivo e se esso è in orizzontale, lancio l'activity dell'home passando il cliente
         * che dovrà essere visualizzato nel fragment.
         */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            goToHomeDietologolActivity(cliente);
        }

        Button btnGestisci = findViewById(R.id.btnGestisciDieta);
        Button btnElimina = findViewById(R.id.btnElimina);

        btnGestisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGestisciActivity(cliente);
            }
        });

        btnElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEliminaCliente();
            }
        });
    }

    /* Se viene premuto il pulsante "back", lancio l'activity della home passando il cliente che dovrà essere visualizzato se l'orientamento di quest'ultima
     * cambierà dal verticale all'orizzontale.
     */
    /*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToHomeDietologolActivity(cliente);
    }
    */

    private void goToHomeDietologolActivity(Cliente cliente) {
        Intent intentOut = new Intent(DettagliCliente.this, HomeDietologo.class);
        intentOut.putExtra(CLIENTE, cliente);

        startActivity(intentOut);
    }

    // TODO: Fabio, metti il nome di quello implementato
    private void goToGestisciActivity(Cliente cliente) {

    }

    public void goToEliminaCliente() {
        EliminaClienteDialog ecd = new EliminaClienteDialog();
        ecd.setUtente(cliente);
        ecd.show(getSupportFragmentManager(), "Elimina cliente");
    }

    @Override
    public void updateEliminaClienteDialogCallback() {
        finish();
    }
}