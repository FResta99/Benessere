package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.interfacciabili.benessere.model.Attrezzo;
import com.interfacciabili.benessere.model.Cliente;

public class EquipmentsActivity extends AppCompatActivity implements MasterEquipmentsFragment.MasterEquipmentFragmentCallback {
    private static final String ATTREZZO = "ATTREZZO";
    private static final String CLIENTE = "CLIENTE";

    //TODO: Rimuovere il costruttore perché il cliente verrà passato tramite Intent da un'altra activity.
    private Cliente cliente;
    private Attrezzo attrezzoCliccato;

    private boolean landscapeView;

    private FragmentManager fragmentManager = null;
    private Fragment detailFragment = null;
    private Fragment masterFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DATA FROM ANOTHER ACTIVITY
        Intent intentFrom = getIntent();
        if (intentFrom != null) {
            if (intentFrom.hasExtra(CLIENTE)) {
                cliente = intentFrom.getParcelableExtra(CLIENTE);
            }

            /*
            if (intentFrom.hasExtra(ATTREZZO)) {
                attrezzoCliccato = intentFrom.getParcelableExtra(EQUIPMENT);
            }
            */
        }

        // DATA FROM THIS ACTIVITY
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CLIENTE)) {
                cliente = savedInstanceState.getParcelable(CLIENTE);
            }

            if (savedInstanceState.containsKey(ATTREZZO)) {
                attrezzoCliccato = savedInstanceState.getParcelable(ATTREZZO);
            }
        }

        // VIEWING THE ACTIVITY ONLY IF EXIST AN CLIENT
        if (cliente != null) {
            setContentView(R.layout.activity_equipments);

            Toolbar homeToolbar = (Toolbar) findViewById(R.id.toolbar_main);
            setSupportActionBar(homeToolbar);
            homeToolbar.setSubtitle(cliente.getUsername());

            fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() != 0) {
                fragmentManager.popBackStack();
            }

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // VISUALIZZAZIONE ORIZZONTALE
                landscapeView = true;

                updateEquipmentMasterFragment(R.id.equipmentMaster);
                if (attrezzoCliccato != null) {
                    updateEquipmentDetailFragment(R.id.equipmentDetail, R.layout.fragment_detail_equipments);
                } else {
                    updateEquipmentDetailFragment(R.id.equipmentDetail, R.layout.fragment_detail_equipments_blank);
                }
            } else {
                // VISUALIZZAZIONE VERTICALE
                landscapeView = false;

                if (attrezzoCliccato != null) {
                    updateEquipmentDetailFragment(R.id.equipmentGeneric, R.layout.fragment_detail_equipments);
                } else {
                    updateEquipmentMasterFragment(R.id.equipmentGeneric);
                }
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(CLIENTE, cliente);
        if (attrezzoCliccato != null) {
            outState.putParcelable(ATTREZZO, attrezzoCliccato);
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
    public void updateMasterEquipmentFragmentCallback(Attrezzo attrezzo) {
        attrezzoCliccato = attrezzo;

        if (landscapeView) {
            updateEquipmentDetailFragment(R.id.equipmentDetail, R.layout.fragment_detail_equipments);
        } else {
            updateEquipmentDetailFragment(R.id.equipmentGeneric, R.layout.fragment_detail_equipments);
        }
    }

    private void updateEquipmentMasterFragment(int frameID) {
        masterFragment = MasterEquipmentsFragment.newInstance();
        fragmentManager.beginTransaction().add(frameID, masterFragment).commit();
    }

    private void updateEquipmentDetailFragment(int frameID, int layoutID) {
        detailFragment = DetailEquipmentsFragment.newInstance(layoutID);

        if (layoutID == R.layout.fragment_detail_equipments) {
            Bundle bundleFragment = detailFragment.getArguments();
            bundleFragment.putParcelable(ATTREZZO, attrezzoCliccato);
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!landscapeView) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.add(frameID, detailFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (!landscapeView) {
            if (fragmentManager.getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                attrezzoCliccato = null;
                fragmentManager.popBackStack();
            }
        } else {
            super.onBackPressed();
        }
    }
}