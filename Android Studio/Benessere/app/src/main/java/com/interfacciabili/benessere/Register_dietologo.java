package com.interfacciabili.benessere;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Register_dietologo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_dietologo);
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

        Button dietologo_Register_Button = (Button) findViewById(R.id.Register_Button);

        //TODO mettere i dati nel database
        dietologo_Register_Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Register_dietologo.this, com.interfacciabili.benessere.HomeDietologo.class);
                startActivity(intent);
            }
        });

    }
}