package com.interfacciabili.benessere;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.interfacciabili.benessere.control.DietDBHelper;

public class InserisciClienteDialog extends AppCompatDialogFragment {
    DietDBHelper dbh;
    TextView tvMessaggioInserisciCliente;
    String username, dietologo;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_inserisci_cliente, null);

        tvMessaggioInserisciCliente = view.findViewById(R.id.tvMessaggioInserisciCliente);
        tvMessaggioInserisciCliente.append(username +  "?");

        dbh = new DietDBHelper(getActivity());

        builder.setView(view)
                .setTitle("Inserisci cliente")
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Inserisci", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbh.aggiungiClienteADietologo(username, dietologo);
                        dismiss();
                    }
                });
        return builder.create();
    }

    public void setUsername(String valore){
        username = valore;
    }

    public void setDietologo(String valore){
        dietologo = valore;
    }
}





