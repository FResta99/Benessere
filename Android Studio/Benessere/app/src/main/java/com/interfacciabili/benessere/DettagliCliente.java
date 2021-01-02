package com.interfacciabili.benessere;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DettagliCliente extends AppCompatActivity {
    //TODO Mostrare gli dettagli del cliente
    TextView tvUsername;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_cliente);
        tvUsername = findViewById(R.id.tvDettaglioUsernameCliente);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            username =(String) bundle.get("USERNAME");
            tvUsername.setText(username);
        }

    }


    public void gestisciDieta(View v){
        Intent intent = new Intent(com.interfacciabili.benessere.DettagliCliente.this, com.interfacciabili.benessere.InserimentoDieta.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }
}