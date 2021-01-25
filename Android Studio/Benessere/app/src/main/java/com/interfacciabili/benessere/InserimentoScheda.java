package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Alimento;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Esercizio;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class InserimentoScheda extends AppCompatActivity {

    private Cliente cliente;
    private Esercizio esercizioCliccato;
    private ListView lvEserciziCoach;
    private ArrayAdapter trainingAdapter;
    private static final String CLIENTE = "CLIENTE";
    public static final String ESERCIZIO = "ESERCIZIO";
    private taskMostraAllenamento mTask;


    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();
            mTask = new InserimentoScheda.taskMostraAllenamento(InserimentoScheda.this);
            mTask.execute(cliente.getUsername());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_scheda);

        lvEserciziCoach = findViewById(R.id.lvEserciziCoach);
        lvEserciziCoach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                esercizioCliccato = (Esercizio) parent.getItemAtPosition(position);
                modificaEsercizio();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            cliente =(Cliente) bundle.get(CLIENTE);
        }
    }

    public void inserisciAllenamento(View view) {
        Intent intent = new Intent(InserimentoScheda.this, InserimentoAllenamento.class);
        intent.putExtra(CLIENTE, cliente);
        startActivity(intent);
    }

    public void modificaEsercizio() {
        Intent intent = new Intent(InserimentoScheda.this, InserimentoAllenamento.class);
        intent.putExtra(CLIENTE, cliente);
        intent.putExtra(ESERCIZIO, esercizioCliccato);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);

        if(databaseService!=null){
            mTask = new taskMostraAllenamento(InserimentoScheda.this);
            mTask.execute(cliente.getUsername());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(databaseService!=null){
            mTask = new taskMostraAllenamento(InserimentoScheda.this);
            mTask.execute(cliente.getUsername());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        if(mTask != null ){mTask.cancel(true);}
    }

    public class taskMostraAllenamento extends AsyncTask<String,Void, List<Esercizio>> {

        private WeakReference<InserimentoScheda> weakReference;

        taskMostraAllenamento(InserimentoScheda activity) {
            weakReference = new WeakReference<InserimentoScheda>(activity);
        }

        protected List<Esercizio> doInBackground(String... params) {
            List<Esercizio> eserizi = new ArrayList<>();


            InserimentoScheda activity = weakReference.get();

            if (activity == null || activity.isFinishing()) {
                return eserizi;
            }

            eserizi = activity.databaseService.recuperaAlleamento(params[0]);

            return eserizi;

        }
        // Inserisci i dati nell'adapter appena il thread termina
        protected void onPostExecute(List<Esercizio> result) {

            InserimentoScheda activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.trainingAdapter = new ArrayAdapter<Esercizio>(InserimentoScheda.this,
                    android.R.layout.simple_list_item_1,
                    result);
            activity.lvEserciziCoach.setAdapter(trainingAdapter);
            activity.trainingAdapter.notifyDataSetChanged();
        }
    }
}