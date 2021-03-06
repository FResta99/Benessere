package com.interfacciabili.benessere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Cliente;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.GONE;

public class ProfiloUtenteActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 5;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private String currentPhotoPath;
    private ImageView imageView;
    private EditText etPeso, etAltezza;
    private Cliente cliente;
    private static final String CLIENTE = "CLIENTE";

    public DatabaseService databaseService;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseService.LocalBinder localBinder = (DatabaseService.LocalBinder) service;
            databaseService = localBinder.getService();
            String risultato = databaseService.recuperaClienteFoto(cliente.getUsername());
            if(risultato!=null){
                imageView.setImageURI(Uri.parse(risultato));
            }
            Cliente clienteDB = databaseService.ricercaCliente(cliente.getUsername());
            etPeso.setText(String.valueOf(clienteDB.getPeso()));
            etAltezza.setText(String.valueOf(clienteDB.getAltezza()));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_utente);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //INTENT DA HOME
        Intent intentFrom = getIntent();
        Bundle clienteBundle = intentFrom.getExtras();
        if(clienteBundle!=null){
            cliente = clienteBundle.getParcelable(CLIENTE);
        }

        imageView=(ImageView)findViewById(R.id.imProfilo);
        etPeso = findViewById(R.id.etPesoUtente);
        etAltezza = findViewById(R.id.etAltezzaUtente);

        PackageManager pm = this.getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            imageView.setVisibility(GONE);
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.sensoreAssente))
                    .setMessage(R.string.avvisoMancanzaFotocamera)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }


    public void getImage(View view){
        controllaPermessoStorage();
    }

    private void controllaPermessoStorage() {
        if (ContextCompat.checkSelfPermission(ProfiloUtenteActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(ProfiloUtenteActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                new AlertDialog.Builder(ProfiloUtenteActivity.this)
                        .setTitle(getString(R.string.richiestaPermesso))
                        .setMessage(R.string.richiestaPermessoMessaggioFotocamera)
                        .setPositiveButton(R.string.richiestaPermessoConsenti, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                ActivityCompat.requestPermissions(ProfiloUtenteActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        }).setNegativeButton(R.string.richiestaPermessoRifiuta, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

            } else {
                ActivityCompat.requestPermissions(ProfiloUtenteActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            apriCamera();
        }
    }

    private void apriCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.interfacciabili.benessere",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    apriCamera();
                } else {
                    Toast.makeText(this, getString(R.string.permessoNegato), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);

            cliente.setFotoProfilo(contentUri.toString());
            databaseService.aggiungiClienteFoto(cliente.getUsername(), contentUri.toString());

            setPic();

        }
    }

    private void setPic() {

        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDatabaseService = new Intent(this, DatabaseService.class);
        bindService(intentDatabaseService, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(databaseService!=null){
            unbindService(serviceConnection);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            goToHomeUtente();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goToHomeUtente();
    }

    private void goToHomeUtente() {
        Intent intentTo = new Intent(this, HomeCliente.class);
        intentTo.putExtra(CLIENTE, cliente);
        startActivity(intentTo);
        finish();
    }

    public void aggiornaProfiloUtente(View view) {
        int mPeso = Integer.parseInt(etPeso.getText().toString());
        int mAltezza = Integer.parseInt(etAltezza.getText().toString());

        if(mPeso<=0){
            etPeso.setError("inserisci un peso corretto");
            return;
        }

        if(mAltezza<=0){
            etAltezza.setError("inserisci un altezza corretta");
        }

        databaseService.modificaPesoAltezzaCliente(cliente, mPeso, mAltezza);
        Toast.makeText(ProfiloUtenteActivity.this, getString(R.string.datiAggiornati), Toast.LENGTH_LONG).show();
    }
}