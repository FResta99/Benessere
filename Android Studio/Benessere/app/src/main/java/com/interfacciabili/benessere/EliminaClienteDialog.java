package com.interfacciabili.benessere;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.interfacciabili.benessere.control.DietDBHelper;

public class EliminaClienteDialog extends AppCompatDialogFragment {

    String utente = "";
    TextView tvMessaggioElimina;
    DietDBHelper dbh;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_elimina_cliente, null);
        tvMessaggioElimina = view.findViewById(R.id.tvMessaggioEliminaCliente);
        if(!utente.isEmpty()){tvMessaggioElimina.append(utente);}
        dbh = new DietDBHelper(getActivity());

        if(savedInstanceState!=null){
            utente = savedInstanceState.getString("USERNAME");
            if(!utente.isEmpty()){tvMessaggioElimina.append(utente);}
        }

        builder.setView(view)
                .setTitle("Elimina cliente")
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO Chiama dbh
                        dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(!utente.isEmpty()){outState.putString("USERNAME", utente);}
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void setUtente(String valore){
        utente = valore;
    }


}
