package com.interfacciabili.benessere;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.interfacciabili.benessere.control.DietDBHelper;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dietologo;

public class HomeDietologo extends AppCompatActivity {

    public Dietologo dietologo = new Dietologo("Dietologo1", "password");
    public Cliente clienteCliccato;

    public TextView tvBenvenuto;
    public ListView lvClienti;

    public ArrayAdapter clientAdapter;
    public DietDBHelper dietDBH;

    public FragmentManager fragmentManager = null;
    public Fragment detailFragment = null;
    public Fragment masterFragment = null;

    private boolean landscapeView;


}