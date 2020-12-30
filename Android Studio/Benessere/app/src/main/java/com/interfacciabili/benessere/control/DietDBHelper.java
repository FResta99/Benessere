package com.interfacciabili.benessere.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.interfacciabili.benessere.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class DietDBHelper extends SQLiteOpenHelper {

    public static final String CLIENT_TABLE = "CLIENT_TABLE";
    public static final String COLUMN_USERNAME = "CLIENT_USERNAME";
    public static final String COLUMN_PASSWORD = "CLIENT_PASSWORD";


    public DietDBHelper(@Nullable Context context) {
        super(context, "benessere.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CLIENT_TABLE + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT) ";
                //TODO Inserire gli altri campi
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean aggiungiCliente(Cliente cliente){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, cliente.getUsername());
        cv.put(COLUMN_PASSWORD, cliente.getPassword());

        long insert = db.insert(CLIENT_TABLE, null, cv);
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<Cliente> recuperaClienti(){
         List<Cliente> returnList = new ArrayList<>();

         // query per ottenere tutti i clienti

        String queryClienti = "SELECT * FROM " + CLIENT_TABLE;

        // prendiamo il db in lettura
        SQLiteDatabase db = this.getReadableDatabase();

        // Result set
        Cursor risultato = db.rawQuery(queryClienti, null);

        // Accediamo al primo elemento, se esiste
        if(risultato.moveToFirst()){
            // cicliamo sul risultato
            do{
                String username = risultato.getString(0);
                String password = risultato.getString(1);
                //TODO Recuparare gli altri campi
                Cliente clienteRestituito = new Cliente (username, password);
                returnList.add(clienteRestituito);
            } while (risultato.moveToNext());
        } else {
            // 0 risultati, ritorna una lista vuota
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
     return returnList;
    }


}
