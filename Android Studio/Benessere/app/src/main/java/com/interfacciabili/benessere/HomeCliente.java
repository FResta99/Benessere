package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.interfacciabili.benessere.control.AlertReceiver;
import com.interfacciabili.benessere.model.Cliente;

import java.util.Calendar;

public class HomeCliente extends AppCompatActivity {

    private Cliente cliente;
    public static final String CLIENTE = "CLIENTE";
    private TextView tvBenvenutoCliente;
    private ImageView ivCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        creaNotificheAlimenti();

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
}