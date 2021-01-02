package com.interfacciabili.benessere;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dietologo;

public class homeDietologo extends AppCompatActivity {

    public Dietologo dietologo = new Dietologo("Boh", "password");
    TextView tvBenvenuto;
    ListView lvClienti;

    ArrayAdapter clientAdapter;
    DietDBHelper dietDBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dietologo);
        tvBenvenuto = findViewById(R.id.tvBenvenuto);
        tvBenvenuto.setText("Benvenuto " + dietologo.getUsername() + " !");
        lvClienti = findViewById(R.id.lvClienti);
        lvClienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cliente clienteCliccato = (Cliente) parent.getItemAtPosition(position);
                Intent intent = new Intent(com.interfacciabili.benessere.homeDietologo.this, com.interfacciabili.benessere.DettagliCliente.class);
                intent.putExtra("USERNAME", clienteCliccato.getUsername());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dietDBH = new DietDBHelper(com.interfacciabili.benessere.homeDietologo.this);
        ShowCustomersOnListView(dietDBH);
    }

    private void ShowCustomersOnListView(DietDBHelper dbh) {
        clientAdapter = new ArrayAdapter<Cliente>(com.interfacciabili.benessere.homeDietologo.this,
                android.R.layout.simple_list_item_1,
                dbh.recuperaClienti());
        lvClienti.setAdapter(clientAdapter);
    }
}