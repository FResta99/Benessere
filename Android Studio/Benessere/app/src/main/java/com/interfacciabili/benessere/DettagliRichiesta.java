package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DettagliRichiesta extends AppCompatActivity {
    TextView tvAlimentoModify;
    EditText etAlimentoModifier;

    String alimentoModify, alimentoModifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_richiesta);
        tvAlimentoModify = findViewById(R.id.tvAlimentoModifica);
        etAlimentoModifier = findViewById(R.id.etAlimentoModifier);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            alimentoModify =(String) bundle.get("ALIMENTO_MODIFY");
            tvAlimentoModify.setText(alimentoModify);
            alimentoModifier =(String) bundle.get("ALIMENTO_MODIFIER");
            etAlimentoModifier.setText(alimentoModifier);
        }
    }
}