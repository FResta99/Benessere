package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.interfacciabili.benessere.control.AlertReceiver;
import com.interfacciabili.benessere.control.DatabaseService;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        startService(intentDatabaseService);
    }

    public void goToLoginUtente(View view) {
        Intent intent = new Intent(MainActivity.this, Login_Benessere.class);
        startActivity(intent);
    }

    public void goToLoginDietologo(View view) {
        Intent intent = new Intent(MainActivity.this, Login_Diet.class);
        startActivity(intent);
    }

    public void goToLoginCoach(View view) {
        Intent intent = new Intent(MainActivity.this, Login_Coach.class);
        startActivity(intent);
    }

    public void goToRegistra(View view) {
        Intent intent = new Intent(MainActivity.this, Register_Main.class);
        startActivity(intent);
    }




}