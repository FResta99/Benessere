package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Cliente;

public class MasterHomeFragment extends Fragment {
    ListView lvClienti;
    private ArrayAdapter clientAdapter;

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();

            showCustomerOnListView();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static MasterHomeFragment newInstance() {
        MasterHomeFragment fragment = new MasterHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_home, container, false);

        lvClienti = rootView.findViewById(R.id.lvClienti);
        lvClienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((HomeDietologo) getActivity()).clienteCliccato = (Cliente) parent.getItemAtPosition(position);
                //((HomeDietologo) getActivity()).updateClientDetailFragment(((HomeDietologo) getActivity()).clienteCliccato.getUsername(), R.layout.dettagli_cliente);
            }
        });

        showCustomerOnListView();
        return rootView;
    }

    private void showCustomerOnListView() {
        clientAdapter = new ArrayAdapter<Cliente>(getContext(),
                android.R.layout.simple_list_item_1,
                databaseService.recuperaClientiDiDietologo(((HomeDietologo) getActivity()).dietologo.getUsername()));
        lvClienti.setAdapter(clientAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(getActivity(), DatabaseService.class);
        getActivity().startService(intentDatabaseService);
        getActivity().bindService(intentDatabaseService, serviceConnection, getActivity().BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}