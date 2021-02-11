package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Alimento;
import com.interfacciabili.benessere.model.Cliente;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class InserimentoDieta extends AppCompatActivity {
    private Cliente cliente;
    private Alimento alimentoCliccato;
    private ListView lvDietaDietologo;
    private ArrayAdapter dietAdapter;
    private static final String CLIENTE = "CLIENTE";
    public static final String ALIMENTO = "ALIMENTO";
    private taskMostraAlimento mTask;



    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();
            mTask = new taskMostraAlimento(InserimentoDieta.this);
            mTask.execute(cliente.getUsername());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_dieta);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvDietaDietologo = findViewById(R.id.lvDietaDietologo);
        lvDietaDietologo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alimentoCliccato = (Alimento) parent.getItemAtPosition(position);
                modificaAlimento();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            cliente =(Cliente) bundle.get(CLIENTE);
        }
    }

    public void inserisciAlimento(View view) {
        Intent intent = new Intent(InserimentoDieta.this, InserimentoAlimento.class);
        intent.putExtra(CLIENTE, cliente);
        startActivity(intent);
    }

    public void modificaAlimento() {
        Intent intent = new Intent(InserimentoDieta.this, InserimentoAlimento.class);
        intent.putExtra(CLIENTE, cliente);
        intent.putExtra(ALIMENTO, alimentoCliccato);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);

        if(databaseService!=null){
            mTask = new taskMostraAlimento(InserimentoDieta.this);
            mTask.execute(cliente.getUsername());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(databaseService!=null){
            mTask = new taskMostraAlimento(InserimentoDieta.this);
            mTask.execute(cliente.getUsername());
        }
    }
    /*
        private void recuperaDietaCliente() {
            dietAdapter = new ArrayAdapter<Alimento>(this, android.R.layout.simple_list_item_1, databaseService.recuperaDieta(cliente.getUsername()));
            lvDietaDietologo.setAdapter(dietAdapter);
        }
    */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        if(mTask != null ){mTask.cancel(true);}
    }

    public class taskMostraAlimento extends AsyncTask<String,Void, List<Alimento>> {

        private WeakReference<InserimentoDieta> weakReference;
        taskMostraAlimento(InserimentoDieta activity) {
            weakReference = new WeakReference<InserimentoDieta>(activity);
        }

        protected List<Alimento> doInBackground(String... params) {
            List<Alimento> richieste = new ArrayList<>();


            InserimentoDieta activity = weakReference.get();

            if (activity == null || activity.isFinishing()) {
                return richieste;
            }

            richieste = activity.databaseService.recuperaDieta(params[0]);

            return richieste;

        }
        // Inserisci i dati nell'adapter appena il thread termina
        protected void onPostExecute(List<Alimento> result) {

            InserimentoDieta activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.dietAdapter = new ArrayAdapter<Alimento>(InserimentoDieta.this,
                    android.R.layout.simple_list_item_1,
                    result);
            activity.lvDietaDietologo.setAdapter(dietAdapter);
            activity.dietAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
