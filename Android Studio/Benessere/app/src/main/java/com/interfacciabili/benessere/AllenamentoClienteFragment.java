package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Alimento;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Esercizio;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

public class AllenamentoClienteFragment extends Fragment {
    private static final String CLIENTE = "CLIENTE";
    private static final String GIORNO = "GIORNO";

    private Cliente cliente;
    private String giorno;

    ListView lvEserciziCliente;
    ArrayAdapter trainingAdapter;

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();

            mostraAllenamentoCliente();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static AllenamentoClienteFragment newInstance(Cliente cliente, String giorno) {
        AllenamentoClienteFragment fragment = new AllenamentoClienteFragment();

        Bundle bundleFragment = new Bundle();
        bundleFragment.putParcelable(CLIENTE, cliente);
        bundleFragment.putString(GIORNO, giorno);

        fragment.setArguments(bundleFragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            cliente = getArguments().getParcelable(CLIENTE);
            giorno = getArguments().getString(GIORNO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_allenamento_cliente, container, false);

        if (cliente != null) {
            lvEserciziCliente = rootView.findViewById(R.id.lvEserciziCliente);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intentDatabaseService = new Intent(getActivity(), DatabaseService.class);
        getActivity().startService(intentDatabaseService);
        getActivity().bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    private void mostraAllenamentoCliente() {
        List<Esercizio> allenamentoRecuperato = databaseService.recuperaAllenamentoGiorno(cliente.getUsername(), giorno);

            trainingAdapter = new ArrayAdapter<Esercizio>(getContext(), android.R.layout.simple_list_item_1, allenamentoRecuperato);
            lvEserciziCliente.setAdapter(trainingAdapter);


    }

}
