package com.interfacciabili.benessere;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DettagliCliente extends AppCompatActivity {
    //TODO Mostrare gli dettagli del cliente
    public TextView tvUsername;
    public String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettagli_cliente);

        tvUsername = findViewById(R.id.tvDettaglioUsernameCliente);

        Intent intentFrom = getIntent();
        Bundle bundle = intentFrom.getExtras();
        if (bundle != null) {
            username = (String) bundle.get("USERNAME");
            tvUsername.setText(username);
        }

        /* Verifico l'orientamento del dispositivo e se esso è in orizzontale, lancio l'activity dell'home passando il cliente
         * che dovrà essere visualizzato nel fragment.
         */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            goToClientDetailActivity(username);
        }
    }

    /* Se viene premuto il pulsante "back", lancio l'activity della home passando il cliente che dovrà essere visualizzato se l'orientamento di quest'ultima
     * cambierà dal verticale all'orizzontale.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToClientDetailActivity(username);
    }

    private void goToClientDetailActivity(String param1) {
        Intent intentOut = new Intent(DettagliCliente.this, HomeDietologo.class);
        intentOut.putExtra("USERNAME", username);

        startActivity(intentOut);
        finish();
    }

    public void goToGestisciDieta(View v){
        Intent intentTo = new Intent(DettagliCliente.this, HomeDietologo.class);
        intentTo.putExtra("USERNAME", username);

        startActivity(intentTo);
        finish();
    }

    public void eliminaCliente(View view) {
        EliminaClienteDialog ecd = new EliminaClienteDialog();
        ecd.setUtente(username);
        ecd.show(getSupportFragmentManager(), "Elimina cliente");
    }
}