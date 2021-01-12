package com.interfacciabili.benessere;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.RichiestaDieta;

import static android.content.Context.BIND_AUTO_CREATE;
import static java.lang.Boolean.FALSE;

public class ModificaDietaDialog extends AppCompatDialogFragment {
    String alimento;
    String utente;
    String dietologo;
    EditText etAlimentoModifica;
    TextView tvTestoDialog;

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
        View view = inflater.inflate(R.layout.dialog_modificadieta, null);
        etAlimentoModifica = view.findViewById(R.id.etAlimentoModifica);
        tvTestoDialog = view.findViewById(R.id.tvTestoDialog);
        tvTestoDialog.append(alimento + "?");

        builder.setView(view)
                .setTitle("Modifica alimento")
                .setNegativeButton("Annulla", null)
                .setPositiveButton("Inserisci", null);
        return builder.create();
    }

    //Previene che il dialog sia chiuso premendo il bottone ok se l'input e' errato
    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog dialog = (AlertDialog)getDialog();
        if(dialog!=null){
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean wantToCloseDialog = false;
                    if(etAlimentoModifica.getText().toString().length()>0)
                    {
                        String alimentoModifier = etAlimentoModifica.getText().toString();
                        RichiestaDieta rd = new RichiestaDieta(utente, dietologo, alimento, alimentoModifier, FALSE);
                        databaseService.aggiungiRichestaDieta(rd);
                        wantToCloseDialog = true;

                    }else {
                        etAlimentoModifica.setError("Inserisci alimento");
                    }
                    if(wantToCloseDialog)
                        dialog.dismiss();
                }
            });
        }
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

    @Override
    public void onStart() {
        super.onStart();

        Intent intentDatabaseService = new Intent(getActivity(), DatabaseService.class);
        getActivity().startService(intentDatabaseService);
        getActivity().bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(serviceConnection);
    }

}
