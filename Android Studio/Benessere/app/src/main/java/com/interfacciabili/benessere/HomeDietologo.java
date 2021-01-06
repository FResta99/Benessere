package com.interfacciabili.benessere;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dietologo;

public class HomeDietologo extends AppCompatActivity {
    public Dietologo dietologo = new Dietologo("Dietologo1", "password");
    public Cliente clienteCliccato;

    public TextView tvBenvenuto;
    public ListView lvClienti;

    public ArrayAdapter clientAdapter;
    public DietDBHelper dietDBH;

    public FragmentManager fragmentManager = null;
    public Fragment detailFragment = null;
    public Fragment masterFragment = null;

    private boolean landscapeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Verifico l'esistenza dell'oggetto Bundle e se possiede il cliente specifico. Se la condizione è rispettata, lancio l'activity per la visualizzazione
         *  del cliente al dettaglio. Questo perché potrei aver visualizzato l'activity in orizzontale, insieme ad un cliente in dettaglio, e decido successivamente
         *  di visualizzare il dettaglio in verticale.
         */
        if (savedInstanceState != null && savedInstanceState.containsKey("CLIENTE")) {
            clienteCliccato = savedInstanceState.getParcelable("CLIENTE");
        }

        setContentView(R.layout.activity_home_dietologo);
        tvBenvenuto = findViewById(R.id.tvBenvenuto);
        tvBenvenuto.setText("Benvenuto " + dietologo.getUsername() + "!");

        fragmentManager = getSupportFragmentManager();


        /* Verifico l'orientamento del dispositivo, per poi impostare di conseguenza l'activity. La variabile "landscapeView" sarà
         *  aggiornata di conseguenza.
         */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // VISUALIZZAZIONE ORIZZONTALE
            landscapeView = true;

            /* Recupero l'intent, verifico se vi è un cliente specifico da visualizzare ed aggiorno il fragment se la condizione è rispettata. Quest'activity, infatti,
             *  potrà essere lanciata da un'applicazione esterna o da un'altra activity come "DettagliCliente".
             */
            Intent intentFrom = getIntent();
            if (intentFrom != null && intentFrom.hasExtra("CLIENTE")) {
                clienteCliccato = intentFrom.getParcelableExtra("CLIENTE");
                updateClientDetailFragment(clienteCliccato.getUsername(), R.layout.dettagli_cliente);
            }

            updateMasterFragment();
            updateClientDetailFragment("", R.layout.dettagli_cliente_blank);

        } else {
            // VISUALIZZAZIONE VERTICALE
            landscapeView = false;

            if(clienteCliccato!=null){
                goToClientDetailActivity(clienteCliccato.getUsername());
            }

            lvClienti = findViewById(R.id.lvClienti);
            lvClienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    clienteCliccato = (Cliente) parent.getItemAtPosition(position);
                    goToClientDetailActivity(clienteCliccato.getUsername());
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!landscapeView){
            updateListview();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(landscapeView){
            updateMasterFragment();
        }
    }

    private void updateListview() {
        dietDBH = new DietDBHelper(HomeDietologo.this);
        clientAdapter = new ArrayAdapter<Cliente>(HomeDietologo.this, android.R.layout.simple_list_item_1, dietDBH.recuperaClientiDiDietologo(dietologo.getUsername()));
        lvClienti.setAdapter(clientAdapter);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /* Verifico se è stato creato il fragment, indice di una visualizzazione orizzontale. A questo proposito, salvo nell'oggetto Bundle
         *  il cliente da visualizzare per il successivo riavvio dell'activity.
         */
        if (detailFragment != null && clienteCliccato != null) {
            outState.putParcelable("CLIENTE", clienteCliccato);
        } else if (clienteCliccato == null && outState.containsKey("CLIENTE")){
            outState.remove("CLIENTE");
        }
    }

    private void goToClientDetailActivity(String param1) {
        Intent intentTo = new Intent(HomeDietologo.this, DettagliCliente.class);
        intentTo.putExtra("CLIENTE", clienteCliccato);
        startActivity(intentTo);
    }

    public void updateClientDetailFragment(String param1, int layout) {
        detailFragment = DetailHomeFragment.newInstance(param1, layout);
        fragmentManager.beginTransaction().replace(R.id.homeDetail, detailFragment).commit();
    }

    public void updateMasterFragment() {
        masterFragment = MasterHomeFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.homeMaster, masterFragment).commit();
    }

    //FLOATING ACTION BUTTON LISTENER
    public void aggiungiCliente(View view) {
        Intent aggiungiCliente = new Intent(HomeDietologo.this, RicercaCliente.class);
        startActivity(aggiungiCliente);
    }

}