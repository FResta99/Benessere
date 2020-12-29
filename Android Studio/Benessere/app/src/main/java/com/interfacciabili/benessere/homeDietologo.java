package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        dietDBH = new DietDBHelper(homeDietologo.this);
        ShowCustomersOnListView(dietDBH);
    }

    private void ShowCustomersOnListView(DietDBHelper dbh) {
        clientAdapter = new ArrayAdapter<Cliente>(homeDietologo.this,
                android.R.layout.simple_list_item_1,
                dbh.recuperaClienti());
        lvClienti.setAdapter(clientAdapter);
    }
}