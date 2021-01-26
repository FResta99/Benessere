package com.interfacciabili.benessere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.android.material.tabs.TabLayout;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.personalView.ScrollViewTab;

public class ClientFragmentContainer extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ScrollViewTab.ScrollViewTabCallback {

    public Cliente cliente = new Cliente("aaa", "aaa");
    private String tipoCliente;

    private static final String CLIENTE = "CLIENTE";
    private static final String TIPO_CLIENTE = "TIPO_CLIENTE";
    private static final String TAB_SELECTED = "TAB_SELECTED";

    private FragmentManager fragmentManager = null;
    private Fragment dayFragment = null;

    private TabLayout tabLayoutWeek = null;

    private String[] tabLayoutDaysLabel = null;
    int tabSelected = 0;

    private float swipePositionDownX, swipePositionUpX;
    private int swipeMinDistance = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DATA FROM ANOTHER ACTIVITY
        Intent intentFrom = getIntent();
        if (intentFrom != null) {
            if (intentFrom.hasExtra(CLIENTE)) {
                cliente = intentFrom.getParcelableExtra(CLIENTE);
                tipoCliente = intentFrom.getStringExtra(TIPO_CLIENTE);
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbarHome.setSubtitle(cliente.getUsername());
            if(tipoCliente.equals("ALLENAMENTO")){
                toolbarHome.setTitle("Il tuo allenamento");
            }


            // Setting menutab
            tabLayoutDaysLabel = getResources().getStringArray(R.array.week);
            tabLayoutWeek = (TabLayout) findViewById(R.id.menutab_week);
            tabLayoutWeek.addOnTabSelectedListener(this);

            for (int i = 0; i < tabLayoutDaysLabel.length; i++) {
                tabLayoutWeek.addTab(tabLayoutWeek.newTab().setText(tabLayoutDaysLabel[i]).setTag(i));
            }

            if ((savedInstanceState != null) && (savedInstanceState.containsKey(TAB_SELECTED))) {
                tabLayoutWeek.selectTab(tabLayoutWeek.getTabAt(savedInstanceState.getInt(TAB_SELECTED)));
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (cliente != null) {
            outState.putParcelable(CLIENTE, cliente);
        }


        if (tabLayoutWeek != null) {
            outState.putInt(TAB_SELECTED, tabSelected);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(tipoCliente.equals("DIETA")){
            getMenuInflater().inflate(R.menu.menu_cliente, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_allenamento_cliente, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (tipoCliente.equals("DIETA") && item.getItemId() == R.id.shoppingListButton) {
            Intent goToShoppingList = new Intent(ClientFragmentContainer.this, ShoppingList.class);
            goToShoppingList.putExtra(CLIENTE, cliente);
            startActivity(goToShoppingList);
        }
        if (tipoCliente.equals("ALLENAMENTO") && item.getItemId() == R.id.contapassiButton){
            Intent goToContapassi = new Intent(ClientFragmentContainer.this, ContapassiActivity.class);
            startActivity(goToContapassi);
        }
        if (tipoCliente.equals("ALLENAMENTO") && item.getItemId() == R.id.attrezziButton){
            Intent goToAttrezzi = new Intent(ClientFragmentContainer.this, EquipmentsActivity.class);
            goToAttrezzi.putExtra(CLIENTE, cliente);
            startActivity(goToAttrezzi);
        }
        if (tipoCliente.equals("ALLENAMENTO") && item.getItemId() == R.id.tornelloButton){
            Intent goToTornello = new Intent(ClientFragmentContainer.this, AperturaTornello.class);
            startActivity(goToTornello);
        }
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tabSelected = (Integer) tab.getTag();
        String daySelected = null;

        switch (tabSelected) {
            case 0:
                daySelected = "LUN";
                break;
            case 1:
                daySelected = "MAR";
                break;
            case 2:
                daySelected = "MER";
                break;
            case 3:
                daySelected = "GIO";
                break;
            case 4:
                daySelected = "VEN";
                break;
            case 5:
                daySelected = "SAB";
                break;
            case 6:
                daySelected = "DOM";
                break;
        }

        if (daySelected != null && tipoCliente.equals("DIETA")) {
            dayFragment = DietaClienteFragment.newInstance(cliente, daySelected);
            fragmentManager.beginTransaction().replace(R.id.fragment_week, dayFragment).commit();
        } else if (daySelected != null && tipoCliente.equals("ALLENAMENTO")){
            dayFragment = AllenamentoClienteFragment.newInstance(cliente, daySelected);
            fragmentManager.beginTransaction().replace(R.id.fragment_week, dayFragment).commit();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                swipePositionDownX = event.getX();
            case MotionEvent.ACTION_UP:
                swipePositionUpX = event.getX();
                float deltaX = swipePositionUpX - swipePositionDownX;
                if (Math.abs(deltaX) > swipeMinDistance) {
                    if (swipePositionUpX > swipePositionDownX) {
                        swipeRight();
                    } else {
                        swipeLeft();
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    public void swipeRight() {
        if (tabSelected != 0) {
            tabSelected--;
            tabLayoutWeek.selectTab(tabLayoutWeek.getTabAt(tabSelected));
        } else {
            tabLayoutWeek.selectTab(tabLayoutWeek.getTabAt(0));
        }
    }

    public void swipeLeft() {
        if (tabSelected != tabLayoutDaysLabel.length) {
            tabSelected++;
            tabLayoutWeek.selectTab(tabLayoutWeek.getTabAt(tabSelected));
        } else {
            tabLayoutWeek.selectTab(tabLayoutWeek.getTabAt(tabLayoutDaysLabel.length));
        }
    }

    @Override
    public void swipeRightScrollView() {
        swipeRight();
    }

    @Override
    public void swipeLeftScrollView() {
        swipeLeft();
    }

}