package com.interfacciabili.benessere;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.interfacciabili.benessere.control.DatabaseService;

import static android.content.Context.BIND_AUTO_CREATE;

public class EliminaProdottoDialog extends AppCompatDialogFragment {

    private int idProdotto;
    private String nomeProdotto;


    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_solo_bottoni, null);

        Bundle prodottoBundle = getArguments();
        if (prodottoBundle!=null){
            idProdotto = prodottoBundle.getInt("ID");
            nomeProdotto = prodottoBundle.getString("NOME");
        }
        builder.setView(view)
                .setTitle("Elimina prodotto")
                .setMessage("Vuoi davvero eliminare " + nomeProdotto + "?")
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseService.eliminaProdotto(idProdotto);
                        dismiss();
                    }
                });

        return builder.create();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(getActivity(), DatabaseService.class);
        getActivity().bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    public interface OnDialogCloseListener {

        void onDialogClose(DialogInterface dialogInterface);
    }

    //aggiorna recyclerview
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof InserisciProdottoDialog.OnDialogCloseListener){
            ((EliminaProdottoDialog.OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(serviceConnection!= null){
            getActivity().unbindService(serviceConnection);
        }
    }
}
