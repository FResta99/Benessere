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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Alimento;
import com.interfacciabili.benessere.model.Cliente;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

public class DietaClienteFragment extends Fragment {
    private static final String CLIENTE = "CLIENTE";
    private static final String GIORNO = "GIORNO";

    private Cliente cliente;
    private String giorno, dietologo;

    ListView lvDietaColazione, lvDietaPranzo, lvDietaCena;
    TextView tvDietaColazione, tvDietaPranzo, tvDietaCena, tvAlimentiDieta;
    ArrayAdapter dietAdapterColazione, dietAdapterPranzo, dietAdapterCena;

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();
            dietologo = databaseService.recuperaDietologoDiCliente(cliente.getUsername());

            mostraDietaCliente();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static DietaClienteFragment newInstance(Cliente cliente, String giorno) {
        DietaClienteFragment fragment = new DietaClienteFragment();

        Bundle bundleFragment = new Bundle();
        bundleFragment.putParcelable(CLIENTE, cliente);
        bundleFragment.putString(GIORNO, giorno);

        fragment.setArguments(bundleFragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            cliente = getArguments().getParcelable(CLIENTE);
            giorno = getArguments().getString(GIORNO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dieta_cliente, container, false);

        if (cliente != null) {
            tvDietaColazione = rootView.findViewById(R.id.tvColazione);
            tvDietaPranzo = rootView.findViewById(R.id.tvPranzo);
            tvDietaCena = rootView.findViewById(R.id.tvCena);
            tvAlimentiDieta = rootView.findViewById(R.id.tvAlimentiDieta);

            lvDietaColazione = rootView.findViewById(R.id.lvDietaColazione);
            lvDietaColazione.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Alimento alimentoCliccato = (Alimento) parent.getItemAtPosition(position);
                    ModificaDietaDialog mdd = new ModificaDietaDialog();
                    mdd.setAlimento(alimentoCliccato);
                    mdd.setUtente(cliente.getUsername());
                    mdd.setDietologo(dietologo);
                    mdd.show(getFragmentManager(), "Modifica");
                }
            });

            lvDietaPranzo = rootView.findViewById(R.id.lvDietaPranzo);
            lvDietaPranzo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Alimento alimentoCliccato = (Alimento) parent.getItemAtPosition(position);
                    ModificaDietaDialog mdd = new ModificaDietaDialog();
                    mdd.setAlimento(alimentoCliccato);
                    mdd.setUtente(cliente.getUsername());
                    mdd.setDietologo("Dietologo1");
                    mdd.show(getFragmentManager(), "Modifica");
                }
            });

            lvDietaCena = rootView.findViewById(R.id.lvDietaCena);
            lvDietaCena.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Alimento alimentoCliccato = (Alimento) parent.getItemAtPosition(position);
                    ModificaDietaDialog mdd = new ModificaDietaDialog();
                    mdd.setAlimento(alimentoCliccato);
                    mdd.setUtente(cliente.getUsername());
                    mdd.setDietologo("Dietologo1");
                    mdd.show(getFragmentManager(), "Modifica");
                }
            });
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

    private void mostraDietaCliente() {
        List<Alimento> dietaRecuperata = databaseService.recuperaDietaGiorno(cliente.getUsername(), giorno);
        String alimenti = getResources().getQuantityString(R.plurals.alimenti_dieta, dietaRecuperata.size(), dietaRecuperata.size());
        tvAlimentiDieta.setText(alimenti);
        List<Alimento> listColazione = new ArrayList<>(), listPranzo = new ArrayList<>(), listCena = new ArrayList<>();
        for (Alimento a: dietaRecuperata) {
            switch (a.getTipoPasto()){
                case "Colazione":
                    listColazione.add(a);
                    break;
                case "Pranzo":
                    listPranzo.add(a);
                    break;
                case "Cena":
                    listCena.add(a);
                    break;
            }
        }

        if (listColazione.isEmpty()) {
            tvDietaColazione.setText("Digiuno mattutino");
            lvDietaColazione.setVisibility(View.GONE);
        } else {
            dietAdapterColazione = new ArrayAdapter<Alimento>(getContext(), android.R.layout.simple_list_item_1, listColazione);
            lvDietaColazione.setAdapter(dietAdapterColazione);
            adaptLayoutListView(lvDietaColazione, (listColazione.size() + 1));
        }

        if (listPranzo.isEmpty()) {
            tvDietaPranzo.setText("Digiuno a pranzo");
            lvDietaPranzo.setVisibility(View.GONE);
        } else {
            dietAdapterPranzo = new ArrayAdapter<Alimento>(getContext(), android.R.layout.simple_list_item_1, listPranzo);
            lvDietaPranzo.setAdapter(dietAdapterPranzo);
            adaptLayoutListView(lvDietaPranzo, (listPranzo.size() + 1));
        }

        if (listCena.isEmpty()) {
            tvDietaCena.setText("Digiuno serale");
            lvDietaCena.setVisibility(View.GONE);
        } else {
            dietAdapterCena = new ArrayAdapter<Alimento>(getContext(), android.R.layout.simple_list_item_1, listCena);
            lvDietaCena.setAdapter(dietAdapterCena);
            adaptLayoutListView(lvDietaCena, (listCena.size() + 1));
        }
    }

    private void adaptLayoutListView(ListView listView, int nItem) {
        ViewGroup.LayoutParams layout = listView.getLayoutParams();
        layout.height = 90 * nItem;
    }
}