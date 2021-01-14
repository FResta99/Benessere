package com.interfacciabili.benessere;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.interfacciabili.benessere.model.Cliente;

import java.util.Set;

public class GameMainActivity extends AppCompatActivity {
    private static final String TAG_LOG = "GameMainActivity";

    private BluetoothAdapter bluetoothAdapter;
    private TextView textDebug;
    private ListView listViewPairedDevices;
    private ArrayAdapter adapterPairedDevices;

    private final BroadcastReceiver broadcastReceiverBluetooth = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            switch (state) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    textDebug.setText("on state->STATE_TURNING_ON");
                    break;
                case BluetoothAdapter.STATE_ON:
                    /* Acquisisco la lista dei dispositivi abbinati. */
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        adapterPairedDevices = new ArrayAdapter<Cliente>(getApplicationContext(), android.R.layout.simple_list_item_1);

                        for (BluetoothDevice device :pairedDevices) {
                            adapterPairedDevices.add(device.getName() + " - " + device.getAddress());
                        }
                        listViewPairedDevices.setAdapter(adapterPairedDevices);
                    }

                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    textDebug.setText("on state->STATE_TURNING_OFF");
                    break;
                case BluetoothAdapter.STATE_OFF:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        textDebug = (TextView) findViewById(R.id.textDebug);
        listViewPairedDevices = (ListView) findViewById(R.id.listViewPairedDevices);

        /* Verifico se il dispositivo possiede il Bluetooth. */
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            Log.d(TAG_LOG, "This device has the bluetooth.");

            /* Verifico se è abilitato e se così non fosse, lo abilito mediante Intent. */
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBluetoothIntent = new Intent (BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, 1);

                IntentFilter filterBluetooth = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(broadcastReceiverBluetooth, filterBluetooth);
            }
        } else {
            Log.d(TAG_LOG, "This device has not the bluetooth.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            switch (resultCode) {
                case RESULT_OK:
                    Log.d(TAG_LOG, "Bluetooth activated by intent.");
                    break;
                case RESULT_CANCELED:
                    Log.d(TAG_LOG, "Bluetooth refused by user.");
                    break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiverBluetooth);
    }
}