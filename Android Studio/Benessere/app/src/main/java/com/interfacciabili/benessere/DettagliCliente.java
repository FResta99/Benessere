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
    private static final String TAG_LOG = "DettagliCliente";

    public TextView tvUsername;

    public Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettagli_cliente);

        tvUsername = findViewById(R.id.tvDettaglioUsernameCliente);

        Intent intentFrom = getIntent();
        Bundle bundle = intentFrom.getExtras();
        if ((bundle != null) && (bundle.containsKey(CLIENTE))) {
            cliente = (Cliente) bundle.get(CLIENTE);
            tvUsername.setText(cliente.getUsername());
        } else {
            Log.d(TAG_LOG, "The bundle doesn't contain a client.");
        }

        if (cliente != null) {
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
        } else {
            Log.d(TAG_LOG, "There is not a client.");
            finish();
        }
    }

    @Override
    public void updateEliminaClienteDialogCallback() {
        finish();
    }

    private void goToHomeDietologolActivity(Cliente cliente) {
        Intent intentOut = new Intent(DettagliCliente.this, HomeDietologo.class);
        intentOut.putExtra(CLIENTE, cliente);

        startActivity(intentOut);
    }

    // TODO: Fabio, qui dovrai implementare qualcosa.
    private void goToGestisciActivity(Cliente cliente) {

    }

    public void goToEliminaCliente() {
        EliminaClienteDialog ecd = new EliminaClienteDialog();
        ecd.setCliente(cliente);
        ecd.show(getSupportFragmentManager(), "Elimina cliente");
    }
}