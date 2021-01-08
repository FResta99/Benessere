package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dietologo;

public class HomeDietologo extends AppCompatActivity implements EliminaClienteDialog.EliminaClienteDialogCallback, MasterHomeFragment.MasterHomeFragmentCallback {
    private static final String EXPERT = "EXPERT";
    private static final String CLIENTE = "CLIENTE";

    public Dietologo dietologo = new Dietologo("Dietologo1", "password");
    public Cliente clienteCliccato;

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();

            if(!landscapeView){
                updateListview();
            }


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private TextView tvBenvenuto;
    private ListView lvClienti;

    private ArrayAdapter clientAdapter;

    private FragmentManager fragmentManager = null;
    private Fragment detailFragment = null;
    private Fragment masterFragment = null;
    Intent intentFrom;

    private boolean landscapeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // BUNDLE OF THIS ACTIVITY
        if (savedInstanceState != null && savedInstanceState.containsKey(CLIENTE)) {
            clienteCliccato = savedInstanceState.getParcelable(CLIENTE);
        }

        setContentView(R.layout.activity_home_dietologo);
        tvBenvenuto = findViewById(R.id.tvBenvenuto);
        tvBenvenuto.setText("Benvenuto " + dietologo.getUsername() + "!");

        fragmentManager = getSupportFragmentManager();

        /* Verifico l'orientamento del dispositivo per poi impostare di conseguenza l'activity. La variabile "landscapeView" sarà
         *  aggiornata di conseguenza ed utilizzata per scopi successivi.
         */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // VISUALIZZAZIONE ORIZZONTALE
            landscapeView = true;

            /* Recupero l'intent, verifico se vi è un cliente specifico da visualizzare ed aggiorno il fragment se la condizione è rispettata. Quest'activity, infatti,
             *  potrà essere lanciata da un'applicazione esterna o da un'altra activity come "DettagliCliente".
             */
            intentFrom = getIntent();
            if (intentFrom != null && intentFrom.hasExtra(CLIENTE)) {
                clienteCliccato = intentFrom.getParcelableExtra(CLIENTE);
                updateClientDetailFragment(R.layout.dettagli_cliente);
            } else {
                updateClientDetailFragment(R.layout.dettagli_cliente_blank);
            }

            updateMasterFragment();
        } else {
            // VISUALIZZAZIONE VERTICALE
            landscapeView = false;

            if (clienteCliccato != null) {
                goToClientDetailActivity();
            }

            lvClienti = findViewById(R.id.lvClienti);
            lvClienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    clienteCliccato = (Cliente) parent.getItemAtPosition(position);
                    goToClientDetailActivity();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
        if ((!landscapeView) && (databaseService != null)){
            updateListview();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
        if(databaseService != null){
            if(!landscapeView){
                updateListview();
            } else {
                updateMasterFragment();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /* Verifico se è stato creato il fragment, indice di una visualizzazione orizzontale. A questo proposito, salvo nell'oggetto Bundle
         *  il cliente da visualizzare per il successivo riavvio dell'activity.
         */
        if (detailFragment != null && clienteCliccato != null) {
            outState.putParcelable(CLIENTE, clienteCliccato);
        } else if (clienteCliccato == null && outState.containsKey(CLIENTE)){
            outState.remove(CLIENTE);
        }
    }

    private void updateListview() {
            clientAdapter = new ArrayAdapter<Cliente>(HomeDietologo.this,
                    android.R.layout.simple_list_item_1,
                    databaseService.recuperaClientiDiDietologo(dietologo.getUsername()));

            lvClienti.setAdapter(clientAdapter);
    }

    private void goToClientDetailActivity() {
        Intent intentTo = new Intent(HomeDietologo.this, DettagliCliente.class);
        intentTo.putExtra(CLIENTE, clienteCliccato);
        startActivity(intentTo);
    }

    public void updateMasterFragment() {
        masterFragment = MasterHomeFragment.newInstance();

        Bundle bundleFragment = masterFragment.getArguments();
        bundleFragment.putString(EXPERT, dietologo.getUsername());

        fragmentManager.beginTransaction().replace(R.id.homeMaster, masterFragment).commit();
    }

    public void updateClientDetailFragment(int layoutID) {
        detailFragment = DetailHomeFragment.newInstance(layoutID);
        if (layoutID == R.layout.dettagli_cliente) {
            Bundle bundleFragment = detailFragment.getArguments();
            bundleFragment.putParcelable(CLIENTE, clienteCliccato);
        }

        fragmentManager.beginTransaction().replace(R.id.homeDetail, detailFragment).commit();
    }

    //FLOATING ACTION BUTTON LISTENER
    public void aggiungiCliente(View view) {
        Intent aggiungiCliente = new Intent(HomeDietologo.this, RicercaCliente.class);
        startActivity(aggiungiCliente);
    }

    @Override
    public void updateEliminaClienteDialogCallback() {
        clienteCliccato = null;
        intentFrom.removeExtra(CLIENTE);
        updateMasterFragment();
        updateClientDetailFragment(R.layout.dettagli_cliente_blank);
    }

    @Override
    public void updateMasterHomeFragmentCallback(Cliente clienteCliccato) {
        this.clienteCliccato = clienteCliccato;
        updateClientDetailFragment(R.layout.dettagli_cliente);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}