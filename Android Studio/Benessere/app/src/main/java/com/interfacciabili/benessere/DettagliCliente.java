package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DettagliCliente extends AppCompatActivity {
    //TODO Mostrare gli dettagli del cliente
    TextView tvUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_cliente);
        tvUsername = findViewById(R.id.tvDettaglioUsernameCliente);

        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            String username =(String) bundle.get("USERNAME");
            tvUsername.setText(username);
        }

    }


    public void gestisciDieta(View v){
        //TODO aprire l'activity per gestire la dieta
    }
}