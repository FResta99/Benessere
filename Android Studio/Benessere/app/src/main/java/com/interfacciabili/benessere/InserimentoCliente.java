package com.interfacciabili.benessere;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Cliente;

public class InserimentoCliente extends AppCompatActivity {

    TextView etName, etPassword;
    Button btnAggiungi;

    DietDBHelper dietDBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_cliente);

        btnAggiungi = findViewById(R.id.btnAggiungi);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);

        dietDBH = new DietDBHelper(com.interfacciabili.benessere.InserimentoCliente.this);
    }

    public void aggiungiCliente(View v){
        Cliente clienteDaAggiungere;
        try {
            clienteDaAggiungere = new Cliente(etName.getText().toString(), etPassword.getText().toString());
            boolean successo = dietDBH.aggiungiCliente(clienteDaAggiungere);
            Toast.makeText(this, "Cliente aggiunto: " + successo, Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, "Cliente non aggiunto", Toast.LENGTH_SHORT).show();
        }


    }
}