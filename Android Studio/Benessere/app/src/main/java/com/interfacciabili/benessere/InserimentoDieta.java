package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Dieta;

import java.util.List;

public class InserimentoDieta extends AppCompatActivity {
    String username;
    EditText etColazione1, etColazione2, etPranzo1, etPranzo2, etCena1, etCena2;
    String colazione1, colazione2, pranzo1, pranzo2, cena1, cena2;

    DietDBHelper dietDBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_dieta);

        etColazione1 = findViewById(R.id.etColazione1);
        etColazione2 = findViewById(R.id.etColazione2);
        etPranzo1 = findViewById(R.id.etPranzo1);
        etPranzo2 = findViewById(R.id.etPranzo2);
        etCena1 = findViewById(R.id.etCena1);
        etCena2 = findViewById(R.id.etCena2);

        dietDBH = new DietDBHelper(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            username =(String) bundle.get("USERNAME");
        }
    }

    public void inserisciDieta(View view) {

        try {
            colazione1 = etColazione1.getText().toString();
            colazione2 = etColazione2.getText().toString();
            pranzo1 = etPranzo1.getText().toString();
            pranzo2 = etPranzo2.getText().toString();
            cena1 = etCena1.getText().toString();
            cena2 = etCena2.getText().toString();
            Toast.makeText(this, "Dieta : " + username, Toast.LENGTH_SHORT).show();
            Dieta dietaDaAggiungere = new Dieta(username, colazione1, colazione2, pranzo1, pranzo2, cena1, cena2);
            boolean successo = dietDBH.aggiungiDieta(dietaDaAggiungere);
            Toast.makeText(this, "Dieta aggiunta: " + successo, Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<Dieta> dietaRecuperata = dietDBH.recuperaDieta(username);
        if(dietaRecuperata.size()>0){
            Dieta dieta = dietaRecuperata.get(0);
            etColazione1.setText(dieta.getColazione1());
            etColazione2.setText(dieta.getColazione2());
            etPranzo1.setText(dieta.getPranzo1());
            etPranzo2.setText(dieta.getPranzo2());
            etCena1.setText(dieta.getCena1());
            etCena2.setText(dieta.getCena2());
        }
    }
}