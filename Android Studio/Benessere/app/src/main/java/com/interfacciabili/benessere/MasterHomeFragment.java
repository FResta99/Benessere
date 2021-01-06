package com.interfacciabili.benessere;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Cliente;

public class MasterHomeFragment extends Fragment {
    ListView lvClienti;
    private DietDBHelper dietDBH;
    private ArrayAdapter clientAdapter;

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
                ((HomeDietologo) getActivity()).updateClientDetailFragment(((HomeDietologo) getActivity()).clienteCliccato.getUsername(), R.layout.dettagli_cliente);
            }
        });
        dietDBH = new DietDBHelper(getContext());
        clientAdapter = new ArrayAdapter<Cliente>(getContext(), android.R.layout.simple_list_item_1, dietDBH.recuperaClientiDiDietologo(((HomeDietologo) getActivity()).dietologo.getUsername()));
        lvClienti.setAdapter(clientAdapter);
        return rootView;
    }
}