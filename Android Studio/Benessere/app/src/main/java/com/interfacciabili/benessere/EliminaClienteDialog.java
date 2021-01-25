package com.interfacciabili.benessere;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
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
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dietologo;
import com.interfacciabili.benessere.model.Esperto;

import static android.content.Context.BIND_AUTO_CREATE;

public class EliminaClienteDialog extends AppCompatDialogFragment {
    private static final String TAG_LOG = "EliminaClienteDialog";
    private static final String CLIENTE = "CLIENTE";

    public interface EliminaClienteDialogCallback {
        public void updateEliminaClienteDialogCallback();
    }
    public EliminaClienteDialogCallback listener;

    private Cliente cliente;
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



        if (cliente != null) {
            //tvMessaggioElimina.append(cliente.getUsername());
        }

        if (savedInstanceState != null) {
            cliente = savedInstanceState.getParcelable(CLIENTE);
            if (cliente != null) {
                //tvMessaggioElimina.append(cliente.getUsername());
            }
        }

        if (cliente != null) {
            builder.setView(view)
                    .setTitle("Elimina cliente")
                    .setMessage("Vuoi davvero eliminare " + cliente.getUsername() + "?")
                    .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    })
                    .setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(esperto instanceof Dietologo){
                                databaseService.eliminaClienteDaDietologo(cliente.getUsername());
                            } else {
                                databaseService.eliminaClienteDaCoach(cliente.getUsername());
                            }
                            listener.updateEliminaClienteDialogCallback();
                            dismiss();
                        }
                    });
            return builder.create();
        } else {
            Log.d(TAG_LOG, "There is not a client.");
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (cliente != null) {
            outState.putParcelable(CLIENTE, cliente);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof EliminaClienteDialogCallback) {
            listener = (EliminaClienteDialogCallback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement \"EliminaClienteDialogCallback\".");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public void setEsperto (Esperto tipo) { this.esperto = tipo; }
}
