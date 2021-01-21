package com.interfacciabili.benessere.control;

import android.app.Application;
import android.content.Intent;

import com.interfacciabili.benessere.control.DatabaseService;

public class BenessereInit extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        startService(intentDatabaseService);
    }
}