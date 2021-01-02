package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dieta;

import java.util.ArrayList;
import java.util.List;

public class DietaCliente extends AppCompatActivity {

    public Cliente cliente = new Cliente("Nicola", "password");
    TextView tvColazione1, tvColazione2, tvPranzo1, tvPranzo2, tvCena1, tvCena2;
    String colazione1, colazione2, pranzo1, pranzo2, cena1, cena2;

    ListView lvDieta;

    ArrayAdapter dietAdapter;
    DietDBHelper dietDBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieta_cliente);
        tvColazione1 = findViewById(R.id.tvColazione1);
        tvColazione2 = findViewById(R.id.tvColazione2);
        tvPranzo1 = findViewById(R.id.tvPranzo1);
        tvPranzo2 = findViewById(R.id.tvPranzo2);
        tvCena1 = findViewById(R.id.tvCena1);
        tvCena2 = findViewById(R.id.tvCena2);

        lvDieta = findViewById(R.id.lvDieta);
        lvDieta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String alimentoCliccato = (String) parent.getItemAtPosition(position);
                ModificaDietaDialog mdd = new ModificaDietaDialog();
                mdd.setAlimento(alimentoCliccato);
                mdd.setUtente(cliente.getUsername());
                mdd.setDietologo("Dietologo1");
                mdd.show(getSupportFragmentManager(), "Modifica");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        dietDBH = new DietDBHelper(DietaCliente.this);
        List<Dieta> dietaRecuperata = dietDBH.recuperaDieta(cliente.getUsername());
        List<String> alimentiDieta = new ArrayList<String>();
        if(dietaRecuperata.size()>0){
            Dieta dieta = dietaRecuperata.get(0);
            tvColazione1.setText(dieta.getColazione1());
            alimentiDieta.add(dieta.getColazione1());
            tvColazione2.setText(dieta.getColazione2());
            alimentiDieta.add(dieta.getColazione2());
            tvPranzo1.setText(dieta.getPranzo1());
            alimentiDieta.add(dieta.getPranzo1());
            tvPranzo2.setText(dieta.getPranzo2());
            alimentiDieta.add(dieta.getPranzo2());
            tvCena1.setText(dieta.getCena1());
            alimentiDieta.add(dieta.getCena1());
            tvCena2.setText(dieta.getCena2());
            alimentiDieta.add(dieta.getCena2());

            dietAdapter = new ArrayAdapter<String>(com.interfacciabili.benessere.DietaCliente.this,
                    android.R.layout.simple_list_item_1,
                    alimentiDieta);
            lvDieta.setAdapter(dietAdapter);

        } else {
            tvColazione1.setText("Tu non hai una dieta baccala'!");
        }
    }


}