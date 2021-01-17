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
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.interfacciabili.benessere.adapter.ShoppingListAdapter;
import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Prodotto;

import java.util.Collections;
import java.util.List;

public class ShoppingList extends AppCompatActivity implements InserisciProdottoDialog.OnDialogCloseListener {
    private RecyclerView rvShoppingList;
    private ShoppingListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Prodotto> mList;
    FloatingActionButton fabAggiungiProdotto;

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();

            mAdapter = new ShoppingListAdapter(databaseService);
            rvShoppingList.setAdapter(mAdapter);
            mLayoutManager = new LinearLayoutManager(ShoppingList.this);

            mList = databaseService.ottieniProdotti();
            Collections.reverse(mList); //mostra i piu' recenti sopra
            mAdapter.setProductList(mList);

            rvShoppingList.setLayoutManager(mLayoutManager);

            mAdapter.setOnItemClickListener(new ShoppingListAdapter.setOnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Toast.makeText(ShoppingList.this, "Posizione" + position, Toast.LENGTH_SHORT).show();
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
        ipd.show(getSupportFragmentManager(), "Aggiungi prodotto");
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = databaseService.ottieniProdotti();
        Collections.reverse(mList); //mostra i piu' recenti sopra
        mAdapter.setProductList(mList);
        mAdapter.notifyDataSetChanged();
    }
}
