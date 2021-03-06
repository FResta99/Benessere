package com.interfacciabili.benessere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.interfacciabili.benessere.adapter.ShoppingListAdapter;
import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Prodotto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ShoppingList extends AppCompatActivity implements InserisciProdottoDialog.OnDialogCloseListener, PopupMenu.OnMenuItemClickListener,
        ModificaProdottoDialog.OnDialogCloseListener, EliminaProdottoDialog.OnDialogCloseListener {
    private RecyclerView rvShoppingList;
    private ShoppingListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Prodotto> mList;
    public static final String CLIENTE = "CLIENTE";
    private int posizioneCliccata = -1;
    private Cliente cliente;

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();

            mAdapter = new ShoppingListAdapter(databaseService, ShoppingList.this);
            rvShoppingList.setAdapter(mAdapter);
            mLayoutManager = new LinearLayoutManager(ShoppingList.this);

            mList = databaseService.ottieniProdotti(cliente.getUsername());
            Collections.reverse(mList); //mostra i piu' recenti sopra
            mAdapter.setProductList(mList);

            rvShoppingList.setLayoutManager(mLayoutManager);

            mAdapter.setOnItemClickListener(new ShoppingListAdapter.setOnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    posizioneCliccata = position;
                    View v = mLayoutManager.findViewByPosition(position);
                    PopupMenu popupMenu = new PopupMenu(ShoppingList.this, v);
                    popupMenu.setOnMenuItemClickListener(ShoppingList.this);
                    popupMenu.inflate(R.menu.menu_popup);
                    popupMenu.show();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        // DATA FROM ANOTHER ACTIVITY
        Intent intentFrom = getIntent();
        if (intentFrom != null) {
            if (intentFrom.hasExtra(CLIENTE)) {
                cliente = intentFrom.getParcelableExtra(CLIENTE);
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvShoppingList = findViewById(R.id.rvShoppingList);
        rvShoppingList.setHasFixedSize(true); //rv non cambia di dimensione

        //mAdapter = new ShoppingListAdapter();
        //rvShoppingList.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    public void aggiungiProdotto(View view) {
        InserisciProdottoDialog ipd = new InserisciProdottoDialog();
        ipd.setCliente(cliente.getUsername());
        ipd.show(getSupportFragmentManager(), getString(R.string.inserireProdotto));
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = databaseService.ottieniProdotti(cliente.getUsername());
        Collections.reverse(mList); //mostra i piu' recenti sopra
        mAdapter.setProductList(mList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pmModifica:
                mAdapter.aggiornaProdotto(posizioneCliccata);
                return true;
            case R.id.pmCancella:
                mAdapter.cancellaProdotto(posizioneCliccata);
                return true;
            default:
                return false;
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shopping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        if(item.getItemId() == R.id.shareButton){
            shareViaWhatsApp();
        }

        return super.onOptionsItemSelected(item);
    }

    public void shareViaWhatsApp() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        List<Prodotto> spesa = databaseService.ottieniProdotti(cliente.getUsername());
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Spesa: " + Arrays.toString(spesa.toArray()));
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
        }
    }
}
