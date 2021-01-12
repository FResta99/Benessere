package com.interfacciabili.benessere;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.interfacciabili.benessere.control.DatabaseService;

public class Register_Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        Button btnRegBenessere = (Button) findViewById(R.id.btnRegBenessere);


        btnRegBenessere.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_Main.this, com.interfacciabili.benessere.Register_benessere.class);
                startActivity(intent);
            }
        });
    }
}