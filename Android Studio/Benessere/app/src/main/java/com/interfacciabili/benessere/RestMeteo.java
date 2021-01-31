package com.interfacciabili.benessere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RestMeteo extends AppCompatActivity {

    public ImageView ivPrevisioni;
    public TextView tvPrevisioni;
    private OttieniDatiREST meteoTask;
    private Boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_meteo);

        Toolbar homeToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(homeToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivPrevisioni = findViewById(R.id.ivPrevisioni);
        tvPrevisioni = findViewById(R.id.tvPrevisioni);

        controllainternet();
        if(isConnected){
            meteoTask = new OttieniDatiREST(this);
            meteoTask.execute();
        } else {
            mostraDialogConnessione();
        }

    }

    public void controllainternet(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void mostraDialogConnessione(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.connettiInternet)
                .setMessage(R.string.avvisoInternet)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        if(meteoTask!=null){
            meteoTask.cancel(true);
        }
        super.onDestroy();
    }

    public class OttieniDatiREST extends AsyncTask<Void, Void, Void> {
        private WeakReference<RestMeteo> weakReference;
        String jsonData = "";
        String datiRecuperati = "";
        String riga = "";

        OttieniDatiREST(RestMeteo activity) {
            weakReference = new WeakReference<RestMeteo>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String uri = Uri.parse("https://api.openweathermap.org/data/2.5/weather?")
                    .buildUpon()
                    .appendQueryParameter("q", "Bari,it")
                    .appendQueryParameter("appid", "b3d054e4604ec445e24c8ea2b5af53a4")
                    .build()
                    .toString();

            URL url = null;
            try {
                url = new URL(uri);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {



                //Apri una connessione HTTP
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //httpURLConnection.setRequestMethod("GET");
                InputStream inputStream;
                int responeCode = httpURLConnection.getResponseCode();
                if(responeCode == HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getInputStream();                                   //Ottieni input stream
                } else {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));         //Leggi dati
                StringBuilder sb = new StringBuilder();                                                 //Crea riga e popola
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                jsonData = sb.toString();

                JSONObject object = new JSONObject(jsonData);
                JSONArray JSArray  = object.getJSONArray("weather");                                   //Chiave json

                for(int i=0; i < JSArray.length(); i++){
                    JSONObject JObject = (JSONObject) JSArray.getJSONObject(i);
                    riga =  "Previsioni:" + JObject.get("main") + "\n";
                    datiRecuperati = datiRecuperati + riga +"\n" ;
                }
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            RestMeteo activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            if(datiRecuperati.contains("Clear") || datiRecuperati.contains("clouds") ){
                activity.ivPrevisioni.setImageDrawable(getDrawable(R.drawable.ic_baseline_wb_sunny_24));
                activity.tvPrevisioni.setText("Oggi e' una bella giornata per correre");
            } else {
                activity.ivPrevisioni.setImageDrawable(getDrawable(R.drawable.ic_baseline_cloud_24));
                activity.tvPrevisioni.setText("Oggi e' meglio rimanere a casa");
            }

        }


    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}