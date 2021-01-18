package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.interfacciabili.benessere.adapter.ShoppingListAdapter;
import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Prodotto;

import java.util.Collections;
import java.util.List;

public class ShoppingList extends AppCompatActivity implements InserisciProdottoDialog.OnDialogCloseListener, PopupMenu.OnMenuItemClickListener,
        ModificaProdottoDialog.OnDialogCloseListener, EliminaProdottoDialog.OnDialogCloseListener {
    private RecyclerView rvShoppingList;
    private ShoppingListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Prodotto> mList;
    private FloatingActionButton fabAggiungiProdotto;
    private int posizioneCliccata = -1;
    private Cliente cliente = new Cliente("Silvio", ""); // TODO Passare cliente tramite intent

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

        fabAggiungiProdotto = findViewById(R.id.fabAggiungiProdotto);

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
        ipd.show(getSupportFragmentManager(), "Aggiungi prodotto");
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
}
