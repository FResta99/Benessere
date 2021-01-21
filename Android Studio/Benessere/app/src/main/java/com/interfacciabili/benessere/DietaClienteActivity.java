package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.interfacciabili.benessere.model.Cliente;

public class DietaClienteActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    //TODO: Eliminare quando si implementa il passaggio dell'oggetto dall'altra activity.
    public Cliente cliente = new Cliente("Silvio", "password");

    private static final String CLIENTE = "CLIENTE";

    private FragmentManager fragmentManager = null;
    private Fragment dayFragment = null;

    String[] menutabWeekLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DATA FROM ANOTHER ACTIVITY
        Intent intentFrom = getIntent();
        if (intentFrom != null) {
            if (intentFrom.hasExtra(CLIENTE)) {
                cliente = intentFrom.getParcelableExtra(CLIENTE);
            }
        }

        // DATA FROM THIS ACTIVITY
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CLIENTE)) {
                cliente = savedInstanceState.getParcelable(CLIENTE);
            }
        }

        if (cliente != null) {
            setContentView(R.layout.activity_dieta_cliente);

            fragmentManager = getSupportFragmentManager();

            // Setting main toolbar
            Toolbar toolbarHome = (Toolbar) findViewById(R.id.toolbar_main);
            setSupportActionBar(toolbarHome);
            toolbarHome.setSubtitle(cliente.getUsername());

            // Setting menutab
            menutabWeekLabels = getResources().getStringArray(R.array.week);
            TabLayout menutabWeek = (TabLayout) findViewById(R.id.menutab_week);
            menutabWeek.addOnTabSelectedListener(this);

            for (int i = 0; i < menutabWeekLabels.length; i++) {
                menutabWeek.addTab(menutabWeek.newTab().setText(menutabWeekLabels[i]).setTag(i));
            }

            if (savedInstanceState == null) {
                dayFragment = DietaClienteFragment.newInstance(cliente, menutabWeekLabels[0]);
                fragmentManager.beginTransaction().add(R.id.fragment_week, dayFragment).commit();
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (cliente != null) {
            outState.putParcelable(CLIENTE, cliente);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.actionbar_button_1) {
        } else if (item.getItemId() == R.id.actionbar_button_2) {
        } else if (item.getItemId() == R.id.actionbar_button_3) {
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int tagValue = (Integer) tab.getTag();
        dayFragment = DietaClienteFragment.newInstance(cliente, menutabWeekLabels[tagValue]);
        fragmentManager.beginTransaction().add(R.id.fragment_week, dayFragment).commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}