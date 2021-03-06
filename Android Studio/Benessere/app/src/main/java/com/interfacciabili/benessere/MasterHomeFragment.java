package com.interfacciabili.benessere;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Coach;
import com.interfacciabili.benessere.model.Dietologo;
import com.interfacciabili.benessere.model.Esperto;

public class MasterHomeFragment extends Fragment {
    private static final String EXPERT = "EXPERT";

    public static final String DIETOLOGO = "DIETOLOGO";
    public static final String COACH = "COACH";
    private static final String TAG_LOG = "MasterHomeFragment";

    public interface MasterHomeFragmentCallback {
        public void updateMasterHomeFragmentCallback(Cliente clienteCliccato);
    }
    public MasterHomeFragmentCallback listener;

    private Esperto esperto;              // Può essere dietologo o coach.

    private ListView lvClienti;
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

        Bundle bundleFragment = new Bundle();
        fragment.setArguments(bundleFragment);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getArguments() != null) && (getArguments().containsKey(EXPERT))) {
            esperto = getArguments().getParcelable(EXPERT);
        } else {
            Log.d(TAG_LOG, "The bundle doesn't contain an expert.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_home, container, false);

        lvClienti = rootView.findViewById(R.id.lvClienti);
        lvClienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.updateMasterHomeFragmentCallback((Cliente) parent.getItemAtPosition(position));
                } else {
                    Log.d(TAG_LOG, "There is not a listener for the activity.");
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

        if (context instanceof MasterHomeFragmentCallback) {
            listener = (MasterHomeFragmentCallback) context;
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
        if (esperto != null) {
            if(esperto instanceof Dietologo){
                clientAdapter = new ArrayAdapter<Cliente>(getContext(), android.R.layout.simple_list_item_1, databaseService.recuperaClientiDiDietologo((((Dietologo) esperto).getUsername())));
            } else {
                clientAdapter = new ArrayAdapter<Cliente>(getContext(), android.R.layout.simple_list_item_1, databaseService.recuperaClientiDiCoach((((Coach) esperto).getUsername())));
            }
            lvClienti.setAdapter(clientAdapter);
        } else {
            Log.d(TAG_LOG, "There is not an expert.");
        }
    }
}