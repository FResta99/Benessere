package com.interfacciabili.benessere;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class AperturaTornello extends AppCompatActivity implements View.OnClickListener {
    private static Button buttonTurnstile;

    private static final String serverIP = "192.168.0.16";
    private static final int serverPort = 125;
    private Socket socket;
    private Thread thread;
    PrintWriter output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        buttonTurnstile = (Button) findViewById(R.id.buttonLed);
        buttonTurnstile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
}