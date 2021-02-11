package com.interfacciabili.benessere;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.interfacciabili.benessere.model.Attrezzo;
import com.interfacciabili.benessere.model.Cliente;

public class DetailEquipmentsFragment extends Fragment {
    private static final String LAYOUT_ID = "LAYOUT_ID";
    private static final String ATTREZZO = "ATTREZZO";

    private int layoutID;
    private Attrezzo attrezzo;

    public static DetailEquipmentsFragment newInstance(int layoutID) {
        DetailEquipmentsFragment fragment = new DetailEquipmentsFragment();

        Bundle bundleFragment = new Bundle();
        bundleFragment.putInt(LAYOUT_ID, layoutID);

        fragment.setArguments(bundleFragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            layoutID = getArguments().getInt(LAYOUT_ID);

            if (getArguments().containsKey(ATTREZZO)) {
                attrezzo = getArguments().getParcelable(ATTREZZO);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(layoutID, container, false);

        if ((layoutID == R.layout.fragment_detail_equipments) && (attrezzo != null)) {
            TextView textViewTitle = rootView.findViewById(R.id.tcEquipmentTitle);
            textViewTitle.setText(attrezzo.getNome());

            TextView textViewDescription = rootView.findViewById(R.id.tvEquipmentDescription);
            textViewDescription.setText(attrezzo.getDescrizione());
        }

        return rootView;
    }
}