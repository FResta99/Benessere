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
        setTheme(R.style.Theme_AppCompat_DayNight);
        setContentView(R.layout.activity_main);
        Button btnCoach = (Button) findViewById(R.id.btnCoach);
        Button btnDiet = (Button) findViewById(R.id.btnDiet);
        Button btnBenessere = (Button) findViewById(R.id.btnBenessere);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setBackgroundColor(0x703AC9BA);

        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        startService(intentDatabaseService);

        creaNotificheAlimenti();


        btnCoach.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RichiesteDietologo.class);
                startActivity(intent);
            }
        });


        btnDiet.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.interfacciabili.benessere.Login_Diet.class);
                startActivity(intent);
            }
        });

        btnBenessere.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.interfacciabili.benessere.Login_Benessere.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.interfacciabili.benessere.Register_Main.class);
                startActivity(intent);
            }
        });






    }

    public void creaNotificheAlimenti(){
        Calendar pranzo = Calendar.getInstance();
        pranzo.set(Calendar.HOUR_OF_DAY, 20);
        pranzo.set(Calendar.MINUTE, 0);
        pranzo.set(Calendar.SECOND, 0);

        Calendar cena = Calendar.getInstance();
        cena.set(Calendar.HOUR_OF_DAY, 21);
        cena.set(Calendar.MINUTE, 0);
        cena.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null){
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
            if (pranzo.before(Calendar.getInstance())) {
                pranzo.add(Calendar.DATE, 1);
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, pranzo.getTimeInMillis(), pendingIntent);

            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 2, intent, 0);
            if (cena.before(Calendar.getInstance())) {
                cena.add(Calendar.DATE, 1);
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cena.getTimeInMillis(), pendingIntent2);
        }
    }


}