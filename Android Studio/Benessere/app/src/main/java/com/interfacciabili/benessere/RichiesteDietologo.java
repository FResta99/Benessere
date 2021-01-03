package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dietologo;
import com.interfacciabili.benessere.model.RichiestaDieta;

public class RichiesteDietologo extends AppCompatActivity {

    public Dietologo dietologo = new Dietologo("Dietologo1", "password");
    ListView lvRichieste;

    ArrayAdapter requestAdapter;
    DietDBHelper dietDBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_richieste_dietologo);

        lvRichieste = findViewById(R.id.lvRichieste);
        lvRichieste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RichiestaDieta richiestaCliccata = (RichiestaDieta) parent.getItemAtPosition(position);
                Intent intent = new Intent(com.interfacciabili.benessere.RichiesteDietologo.this, com.interfacciabili.benessere.DettagliRichiesta.class);
                intent.putExtra("ID", richiestaCliccata.getId());
                intent.putExtra("ALIMENTO_MODIFY", richiestaCliccata.getAlimentoDaModificare());
                intent.putExtra("ALIMENTO_MODIFIER", richiestaCliccata.getAlimentoRichiesto());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dietDBH = new DietDBHelper(RichiesteDietologo.this);
        ShowRequestsOnListView(dietDBH);
    }

    private void ShowRequestsOnListView(DietDBHelper dbh){
        requestAdapter = new ArrayAdapter<RichiestaDieta>(RichiesteDietologo.this,
                android.R.layout.simple_list_item_1,
                dbh.recuperaRichiesteDieta(dietologo.getUsername()));
        lvRichieste.setAdapter(requestAdapter);
    }
}