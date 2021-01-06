package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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



        btnCoach.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeDietologo.class);
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


}