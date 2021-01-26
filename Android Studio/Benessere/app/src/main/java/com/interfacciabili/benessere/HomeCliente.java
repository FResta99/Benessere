package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.interfacciabili.benessere.model.Cliente;

public class HomeCliente extends AppCompatActivity {

    private Cliente cliente;
    public static final String CLIENTE = "CLIENTE";
    private TextView tvBenvenutoCliente;
    private ImageView ivCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        tvBenvenutoCliente = findViewById(R.id.tvBenvenutoCliente);
        ivCliente = findViewById(R.id.ivCliente);

        //INTENT DA LOGIN
        Intent intentFrom = getIntent();
        Bundle clienteBundle = intentFrom.getExtras();
        if(clienteBundle!=null){
            cliente = clienteBundle.getParcelable(CLIENTE);
            tvBenvenutoCliente.setText(cliente.toString());
            if(cliente.getFotoProfilo()!= null){
                ivCliente.setImageURI(Uri.parse(cliente.getFotoProfilo()));
            }

        }

        Toolbar homeToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        if (homeToolbar != null) {
            setSupportActionBar(homeToolbar);
        }

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


    public void goToPersonalizzaProfilo(View view) {
        Intent goToPersonalizza = new Intent(HomeCliente.this, ProfiloUtenteActivity.class);
        goToPersonalizza.putExtra(CLIENTE, cliente);
        startActivity(goToPersonalizza);
    }
}