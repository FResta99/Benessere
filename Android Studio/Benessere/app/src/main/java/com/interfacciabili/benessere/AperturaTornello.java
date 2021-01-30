package com.interfacciabili.benessere;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class AperturaTornello extends AppCompatActivity implements View.OnClickListener {
    private static Button buttonTurnstile;

    private static final String serverIP = "192.168.0.15";
    private static final int serverPort = 125;
    private Socket socket;
    private Thread thread;
    private boolean isConnected;
    PrintWriter output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aperturatornello);
        Toolbar homeToolbar = (Toolbar) findViewById(R.id.toolbar_main);

        if (homeToolbar != null) {
            setSupportActionBar(homeToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        controllainternet();
        if(!isConnected){
            mostraDialogConnessione();
        }

        buttonTurnstile = (Button) findViewById(R.id.buttonLed);
        buttonTurnstile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        controllainternet();
        if(!isConnected){
            mostraDialogConnessione();
            return;
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket == null) {
                        socket = new Socket(serverIP, serverPort);
                        output = new PrintWriter(socket.getOutputStream(), true);
                    }
                    output.println((int) 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void mostraDialogConnessione(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.connettiInternet)
                .setMessage(R.string.avvisoInternet)
                .setPositiveButton("OK", null)
                .show();
    }

    public void controllainternet(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}