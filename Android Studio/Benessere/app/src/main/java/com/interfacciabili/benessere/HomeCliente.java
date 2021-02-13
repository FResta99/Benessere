package com.interfacciabili.benessere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.interfacciabili.benessere.control.AlertReceiver;
import com.interfacciabili.benessere.model.Cliente;

import java.util.Calendar;

public class HomeCliente extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    private Cliente cliente;
    public static final String CLIENTE = "CLIENTE";
    private TextView tvBenvenutoCliente;
    private ImageView ivCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);


        creaNotificheAlimenti();

        drawer = findViewById(R.id.drawer_layout);

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, homeToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        finish();
    }

    public void creaNotificheAlimenti(){
        Calendar pranzo = Calendar.getInstance();
        pranzo.set(Calendar.HOUR_OF_DAY, 12);
        pranzo.set(Calendar.MINUTE, 0);
        pranzo.set(Calendar.SECOND, 0);

        Calendar cena = Calendar.getInstance();
        cena.set(Calendar.HOUR_OF_DAY, 20);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_spesa:
                Intent goToShoppingList = new Intent(HomeCliente.this, ShoppingList.class);
                goToShoppingList.putExtra(CLIENTE, cliente);
                startActivity(goToShoppingList);
                break;
            case R.id.nav_diario:
                Intent goToDiarioAlimentare = new Intent(HomeCliente.this, DiarioAlimentare.class);
                goToDiarioAlimentare.putExtra(CLIENTE, cliente);
                startActivity(goToDiarioAlimentare);
                break;
            case R.id.nav_contapassi:
                Intent goToContapassi = new Intent(HomeCliente.this, ContapassiActivity.class);
                startActivity(goToContapassi);
                break;
            case R.id.nav_meteo:
                Intent goToMeteo = new Intent(HomeCliente.this, RestMeteo.class);
                startActivity(goToMeteo);
                break;
            case R.id.nav_attrezzi:
                Intent goToAttrezzi = new Intent(HomeCliente.this, EquipmentsActivity.class);
                goToAttrezzi.putExtra(CLIENTE, cliente);
                startActivity(goToAttrezzi);
                break;
            case R.id.nav_tornello:
                Intent goToTornello = new Intent(HomeCliente.this, AperturaTornello.class);
                startActivity(goToTornello);
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

