package com.interfacciabili.benessere;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Coach;
import com.interfacciabili.benessere.model.Dietologo;
import com.interfacciabili.benessere.model.Esperto;

import static android.content.Context.BIND_AUTO_CREATE;

public class InserisciClienteDialog extends AppCompatDialogFragment {
    private static final String TAG_LOG = "InserisciClienteDialog";


    private String usernameCliente;
    private Esperto esperto;

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
        setRetainInstance(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_solo_bottoni, null);

        if (esperto != null) {
            builder.setView(view)
                    .setTitle("Inserisci cliente")
                    .setMessage("Vuoi aggiungere " + usernameCliente + "?")
                    .setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    })
                    .setPositiveButton(getString(R.string.inserisci), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(esperto instanceof Dietologo){
                                databaseService.aggiungiClienteADietologo(usernameCliente, ((Dietologo) esperto).getUsername());
                            } else {
                                databaseService.aggiungiClienteACoach(usernameCliente, ((Coach) esperto).getUsername());
                            }

                            getActivity().finish();
                            dismiss();
                        }
                    });
            return builder.create();
        } else {
            Log.d(TAG_LOG, "There is not an expert.");
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intentDatabaseService = new Intent(getActivity(), DatabaseService.class);
        getActivity().bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(serviceConnection!= null){
            getActivity().unbindService(serviceConnection);
        }
    }

    public void setUsernameCliente(String username){
        usernameCliente = username;
    }

    public void setEsperto(Esperto esperto){
        this.esperto = esperto;
    }
}





