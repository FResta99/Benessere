package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Attrezzo;
import com.interfacciabili.benessere.model.Cliente;

public class MasterEquipmentsFragment extends Fragment {
    public interface MasterEquipmentFragmentCallback {
        public void updateMasterEquipmentFragmentCallback(Attrezzo attrezzoCliccato);
    }
    public MasterEquipmentFragmentCallback listener;

    private ListView listViewEquipments;
    private ArrayAdapter equipmentsAdapter;

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

    public static MasterEquipmentsFragment newInstance() {
        MasterEquipmentsFragment fragment = new MasterEquipmentsFragment();

        Bundle bundleFragment = new Bundle();
        fragment.setArguments(bundleFragment);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_equipments, container, false);

        listViewEquipments = (ListView) rootView.findViewById(R.id.listViewEquipments);
        listViewEquipments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.updateMasterEquipmentFragmentCallback((Attrezzo) parent.getItemAtPosition(position));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intentDatabaseService = new Intent(getActivity(), DatabaseService.class);
        getActivity().bindService(intentDatabaseService, serviceConnection, getActivity().BIND_AUTO_CREATE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MasterEquipmentFragmentCallback) {
            listener = (MasterEquipmentFragmentCallback) context;
        } else {
            throw new RuntimeException(context.toString() + " you must implements \"MasterHomeFragmentCallback\".");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    private void showCustomerOnListView() {
        equipmentsAdapter = new ArrayAdapter<Attrezzo>(getContext(), android.R.layout.simple_list_item_1, databaseService.recuperaAttrezzi());
        listViewEquipments.setAdapter(equipmentsAdapter);
    }
}