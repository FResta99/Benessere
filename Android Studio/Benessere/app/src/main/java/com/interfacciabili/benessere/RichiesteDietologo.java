package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Dietologo;
import com.interfacciabili.benessere.model.RichiestaDieta;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RichiesteDietologo extends AppCompatActivity {

    public Dietologo dietologo = new Dietologo("Dietologo1", "password");
    ListView lvRichieste;
    private static final String RICHIESTA = "RICHIESTA";

    taskMostraRichieste mTask;

    ArrayAdapter requestAdapter;

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();
            mTask = new taskMostraRichieste(RichiesteDietologo.this);
            mTask.execute(dietologo.getUsername());
            //ShowRequestsOnListView();
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
                Intent intent = new Intent(RichiesteDietologo.this, DettagliRichiesta.class);
                intent.putExtra(RICHIESTA, richiestaCliccata);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
        /** Se sei al primo avvio il database service non esiste, esegui dopo il task quando il service e' creato
         * altrimenti riesegui per tenere la lista aggiornata
         */
        if(databaseService!=null){
            mTask = new taskMostraRichieste(RichiesteDietologo.this);
            mTask.execute(dietologo.getUsername());
        }
        Log.d("TAG", "Onstart");
    }
/*
    public void ShowRequestsOnListView(){

                requestAdapter = new ArrayAdapter<RichiestaDieta>(RichiesteDietologo.this,
                        android.R.layout.simple_list_item_1,
                        databaseService.recuperaRichiesteDieta(dietologo.getUsername()));
                lvRichieste.setAdapter(requestAdapter);

    }
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        if (mTask != null) mTask.cancel(true);
    }

    public class taskMostraRichieste extends AsyncTask<String,Void, List<RichiestaDieta>> {

        //Weak reference permette al garbage collector di eliminare i riferimenti all'acitivty principale
        //Senza weak reference la classe dell'AsyncTask potrebbe rimanere in memoria e causare problemi
        private WeakReference<RichiesteDietologo> weakReference;

        //Costruttore per ottenere il riferimento all'activity e memorizzarlo come weak reference
        taskMostraRichieste(RichiesteDietologo activity) {
            weakReference = new WeakReference<RichiesteDietologo>(activity);
        }

        protected List<RichiestaDieta> doInBackground(String... params) {
            List<RichiestaDieta> richieste = new ArrayList<>();

            //Ottengo un riferimento all'activity dalla weak reference
            RichiesteDietologo activity = weakReference.get();
            //Se l'activty non esiste o sta per essere distrutta ritorno una lista vuota
            if (activity == null || activity.isFinishing()) {
                return richieste;
            }
            // Ottengo i dati richiamando il database service
            richieste = activity.databaseService.recuperaRichiesteDieta(params[0]);

            return richieste;

        }
            // Inserisci i dati nell'adapter appena il thread termina
        protected void onPostExecute(List<RichiestaDieta> result) {

            RichiesteDietologo activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.requestAdapter = new ArrayAdapter<RichiestaDieta>(RichiesteDietologo.this,
                    android.R.layout.simple_list_item_1,
                    result);
            activity.lvRichieste.setAdapter(requestAdapter);
            activity.requestAdapter.notifyDataSetChanged();
        }
    }

}