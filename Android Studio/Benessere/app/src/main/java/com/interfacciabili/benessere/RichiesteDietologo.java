package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dietologo;
import com.interfacciabili.benessere.model.RichiestaDieta;

public class RichiesteDietologo extends AppCompatActivity {

    public Dietologo dietologo = new Dietologo("Dietologo1", "password");
    ListView lvRichieste;

    ArrayAdapter requestAdapter;
    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();

            ShowRequestsOnListView();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

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
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        startService(intentDatabaseService);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);

    }

    private void ShowRequestsOnListView(){
        requestAdapter = new ArrayAdapter<RichiestaDieta>(RichiesteDietologo.this,
                android.R.layout.simple_list_item_1,
                databaseService.recuperaRichiesteDieta(dietologo.getUsername()));
        lvRichieste.setAdapter(requestAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

}