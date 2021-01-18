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
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Prodotto;

import static android.content.Context.BIND_AUTO_CREATE;

public class ModificaProdottoDialog extends AppCompatDialogFragment {
    private EditText etModificaProdotto;
    private String prodottoVecchioNome = "";
    private int prodottoVecchioId;

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
        View view = inflater.inflate(R.layout.dialog_modifica_prodotto, null);

        etModificaProdotto = view.findViewById(R.id.etModificaProdotto);

        Bundle prodottoBundle = getArguments();
        if (prodottoBundle!=null){
            prodottoVecchioId = prodottoBundle.getInt("ID");
            prodottoVecchioNome = prodottoBundle.getString("NOME");
        }

        builder.setView(view)
                .setTitle("Modifica prodotto")
                .setMessage("Con cosa vuoi modificare " + prodottoVecchioNome + "?")
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Modifica", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(etModificaProdotto.getText().toString().length()>0){
                            String prodottoModificato = etModificaProdotto.getText().toString();
                            databaseService.aggiornaProdotto(prodottoVecchioId, prodottoModificato);
                        } else {
                            etModificaProdotto.setError("Inserisci prodotto");
                        }
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
            ((ModificaProdottoDialog.OnDialogCloseListener)activity).onDialogClose(dialog);
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
