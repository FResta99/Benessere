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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Prodotto;

import static android.content.Context.BIND_AUTO_CREATE;

public class InserisciProdottoDialog extends AppCompatDialogFragment {
    private EditText etInserisciProdotto;
    private String usernameCliente;

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
        View view = inflater.inflate(R.layout.dialog_inserisci_prodotto, null);

        etInserisciProdotto = view.findViewById(R.id.etAggiungiProdotto);

            builder.setView(view)
                    .setTitle(getString(R.string.inserireProdotto))
                    .setMessage(R.string.richiestaProdottoInserire)
                    .setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    })
                    .setPositiveButton(getString(R.string.inserisci), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(etInserisciProdotto.getText().toString().length()>0){
                                String prodottoInserito = etInserisciProdotto.getText().toString();
                                databaseService.inserisciProdotto(new Prodotto(prodottoInserito, 0, usernameCliente));
                            } else {
                                etInserisciProdotto.setError(getString(R.string.inserireProdotto));
                            }

                            dismiss();
                        }
                    });
            return builder.create();

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
        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(serviceConnection!= null){
            getActivity().unbindService(serviceConnection);
        }
    }

    public void setCliente (String cliente){
        this.usernameCliente = cliente;
    }

}
