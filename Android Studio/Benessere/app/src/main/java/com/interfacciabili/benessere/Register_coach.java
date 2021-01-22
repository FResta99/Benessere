package com.interfacciabili.benessere;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class Register_coach extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_coach);
        Switch sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    sw.setText("Maschio");
                } else {
                    // The toggle is disabled
                    sw.setText("Femmina");
                }
            }
        });

        Button coach_Register_Button = (Button) findViewById(R.id.Register_Button);

        //TODO mettere i dati nel database
        coach_Register_Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_coach.this, com.interfacciabili.benessere.HomeCoach.class);
                startActivity(intent);
            }
        });
    }
}