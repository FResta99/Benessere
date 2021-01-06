package com.interfacciabili.benessere;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DetailHomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private int mParam2;

    public static DetailHomeFragment newInstance(String param1, int layout) {
        DetailHomeFragment fragment = new DetailHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, layout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(mParam2, container, false);
        if(mParam2 == R.layout.dettagli_cliente) {

            TextView tvUsername = rootView.findViewById(R.id.tvDettaglioUsernameCliente);
            tvUsername.setText(mParam1);


            Button btnDettagli = rootView.findViewById(R.id.btnGestisciDieta);
            Button btnElimina = rootView.findViewById(R.id.btnElimina);

            btnDettagli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO Passare il parcelable cliente
                    Intent intentTo = new Intent(getContext(), InserimentoDieta.class);
                    intentTo.putExtra("USERNAME", mParam1);
                    startActivity(intentTo);

                }
            });

            btnElimina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EliminaClienteDialog ecd = new EliminaClienteDialog();
                    ecd.setUtente(mParam1);
                    ecd.show(getFragmentManager(), "Elimina cliente");
                }
            });

        }
        return rootView;
    }
}