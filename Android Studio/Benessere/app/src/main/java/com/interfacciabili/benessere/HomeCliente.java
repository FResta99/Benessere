package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.interfacciabili.benessere.model.Cliente;

public class HomeCliente extends AppCompatActivity {
    //TODO Passare con l'intent
    private Cliente cliente = new Cliente("Silvio", "");
    public static final String CLIENTE = "CLIENTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        Toolbar homeToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        if (homeToolbar != null) {
            setSupportActionBar(homeToolbar);
            //getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        /*if (item.getItemId() == android.R.id.home) {
            Intent goToMainActivity = new Intent(HomeCliente.this, MainActivity.class);
            startActivity(goToMainActivity);
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void goToDietaCliente(View view) {
        Intent goToDieta = new Intent(HomeCliente.this, ClientFragmentContainer.class);
        goToDieta.putExtra(CLIENTE, cliente);
        goToDieta.putExtra("TIPO_CLIENTE", "DIETA");
        startActivity(goToDieta);
    }

    public void goToAllenamentoCliente(View view) {
        Intent goToAllenamento = new Intent(HomeCliente.this, ClientFragmentContainer.class);
        goToAllenamento.putExtra(CLIENTE, cliente);
        goToAllenamento.putExtra("TIPO_CLIENTE", "ALLENAMENTO");
        startActivity(goToAllenamento);
    }


}