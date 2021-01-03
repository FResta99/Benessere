package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.interfacciabili.benessere.control.DietDBHelper;

public class DettagliRichiesta extends AppCompatActivity {
    TextView tvAlimentoModify;
    EditText etAlimentoModifier;

    String alimentoModify, alimentoModifier;
    int id;

    DietDBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_richiesta);
        tvAlimentoModify = findViewById(R.id.tvAlimentoModifica);
        etAlimentoModifier = findViewById(R.id.etAlimentoModifier);
        etAlimentoModifier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    dbh = new DietDBHelper(DettagliRichiesta.this);
                    dbh.approvaDieta(id, s.toString());
                }
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            id = (int) bundle.getInt("ID");
            alimentoModify =(String) bundle.get("ALIMENTO_MODIFY");
            tvAlimentoModify.setText(alimentoModify);
            alimentoModifier =(String) bundle.get("ALIMENTO_MODIFIER");
            etAlimentoModifier.setText(alimentoModifier);
        }
    }


    public void approvaRichiesta(View view) {
        dbh = new DietDBHelper(DettagliRichiesta.this);
        dbh.approvaDieta(id);
        //TODO Automodifica della dieta
    }

}