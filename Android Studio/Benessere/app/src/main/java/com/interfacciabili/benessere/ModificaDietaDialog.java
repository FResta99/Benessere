package com.interfacciabili.benessere;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.RichiestaDieta;

public class ModificaDietaDialog extends AppCompatDialogFragment {
    String alimento;
    String utente;
    String dietologo;
    EditText etAlimentoModifica;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_modificadieta, null);
        etAlimentoModifica = view.findViewById(R.id.etAlimentoModifica);
        DietDBHelper dbh = new DietDBHelper(getActivity());

        builder.setView(view)
                .setTitle("Modifica alimento " + alimento)
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Inserisci", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String alimentoModifier = etAlimentoModifica.getText().toString();
                        RichiestaDieta rd = new RichiestaDieta(utente, dietologo, alimento, alimentoModifier);
                       dbh.aggiungiRichestaDieta(rd);
                       dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public void setAlimento(String valore){
        alimento = valore;
    }

    public void setUtente(String valore){
        utente = valore;
    }

    public void setDietologo(String valore){
        dietologo = valore;
    }
}
