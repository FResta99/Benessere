package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.interfacciabili.benessere.model.Dietologo;

public class RichiesteDietologo extends AppCompatActivity {

    public Dietologo dietologo = new Dietologo("Dietologo1", "password");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_richieste_dietologo);


    }
}