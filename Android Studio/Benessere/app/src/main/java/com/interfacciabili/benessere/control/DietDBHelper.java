package com.interfacciabili.benessere.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dieta;
import com.interfacciabili.benessere.model.RichiestaDieta;

import java.util.ArrayList;
import java.util.List;

public class DietDBHelper extends SQLiteOpenHelper {

    public static final String CLIENT_TABLE = "CLIENT_TABLE";
    public static final String COLUMN_USERNAME = "CLIENT_USERNAME";
    public static final String COLUMN_PASSWORD = "CLIENT_PASSWORD";

    public static final String DIETOLOGIST_TABLE = "DIETOLOGIST_TABLE";
    public static final String COLUMN_DIETOLOGIST_USERNAME = "DIETOLOGIST_USERNAME";
    public static final String COLUMN_DIETOLOGIST_PASSWORD = "DIETOLOGIST_PASSWORD";

    public static final String DIET_TABLE = "DIET_TABLE";
    public static final String COLUMN_DIET_USERNAME = "DIET_USERNAME";
    public static final String COLUMN_DIET_BREAKFAST1 = "DIET_BREAKFAST1";
    public static final String COLUMN_DIET_BREAKFAST2 = "DIET_BREAKFAST2";
    public static final String COLUMN_DIET_LUNCH1 = "DIET_LUNCH1";
    public static final String COLUMN_DIET_LUNCH2 = "DIET_LUNCH2";
    public static final String COLUMN_DIET_DINNER1 = "DIET_DINNER1";
    public static final String COLUMN_DIET_DINNER2 = "DIET_DINNER2";

    String createDietTableStatement = "CREATE TABLE " + DIET_TABLE +" (" +
            COLUMN_DIET_USERNAME + " TEXT PRIMARY KEY, " +
            COLUMN_DIET_BREAKFAST1 + " TEXT, "  +
            COLUMN_DIET_BREAKFAST2 + " TEXT, "  +
            COLUMN_DIET_LUNCH1 + " TEXT, "  +
            COLUMN_DIET_LUNCH2 + " TEXT, "  +
            COLUMN_DIET_DINNER1 + " TEXT, "  +
            COLUMN_DIET_DINNER2 + " TEXT, "  +
            " FOREIGN KEY ("+COLUMN_DIET_USERNAME+") REFERENCES "+CLIENT_TABLE+"("+COLUMN_USERNAME+"));";

    public static final String REQUEST_DIET_TABLE = "REQUEST_DIET_TABLE";
    public static final String COLUMN_REQUEST_DIET_ID = "REQUEST_ID";
    public static final String COLUMN_REQUEST_DIET_CLIENT = "REQUEST_CLIENT";
    public static final String COLUMN_REQUEST_DIET_DIETOLOGIST = "REQUEST_DIETOLOGIST";
    public static final String COLUMN_REQUEST_DIET_MODIFY = "REQUEST_MODIFY";
    public static final String COLUMN_REQUEST_DIET_MODIFIER = "REQUEST_MODIFIER";
    public static final String COLUMN_REQUEST_DIET_APPROVED = "REQUEST_APPROVED";

    String createRequestDietTable = "CREATE TABLE " + REQUEST_DIET_TABLE +" (" +
            COLUMN_REQUEST_DIET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_REQUEST_DIET_CLIENT + " TEXT, "  +
            COLUMN_REQUEST_DIET_DIETOLOGIST + " TEXT, "  +
            COLUMN_REQUEST_DIET_MODIFY + " TEXT NOT NULL, "  +
            COLUMN_REQUEST_DIET_MODIFIER + " TEXT NOT NULL, "  +
            COLUMN_REQUEST_DIET_APPROVED + " TEXT) ";

    public static final String CLIENT_DIETOLOGIST_TABLE = "CLIENT_DIETOLOGIST_TABLE";
    public static final String COLUMN_CLIENT_USERNAME = "CLIENT_USERNAME";

    public DietDBHelper(@Nullable Context context) {
        super(context, "benessere.db", null, 4);
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
        if (newVersion > oldVersion) {

            String createDietologistTable = "CREATE TABLE " + DIETOLOGIST_TABLE + " (" +
                    COLUMN_DIETOLOGIST_USERNAME + " TEXT PRIMARY KEY, " +
                    COLUMN_DIETOLOGIST_PASSWORD + " TEXT) ";
            //TODO Inserire gli altri campi
            db.execSQL(createDietologistTable);

            String createClientDietologistTable = "CREATE TABLE " + CLIENT_DIETOLOGIST_TABLE +" (" +
                    COLUMN_CLIENT_USERNAME + " TEXT , " +
                    COLUMN_DIETOLOGIST_USERNAME + " TEXT , "  +
                    " PRIMARY KEY(" + COLUMN_CLIENT_USERNAME + ", " + COLUMN_DIETOLOGIST_USERNAME + "), " +
                    " FOREIGN KEY ("+COLUMN_CLIENT_USERNAME+") REFERENCES "+CLIENT_TABLE+"("+COLUMN_USERNAME+"), " +
                    " FOREIGN KEY ("+COLUMN_DIETOLOGIST_USERNAME+") REFERENCES "+DIETOLOGIST_TABLE+"("+COLUMN_DIETOLOGIST_USERNAME+"));";
            db.execSQL(createClientDietologistTable);

        }
    }


    public boolean aggiungiCliente(Cliente cliente){

        // ottengo un db in scrittura utilizzando il metodo della classe dbhelper
        SQLiteDatabase db = this.getWritableDatabase();

        // creo un contenitore per i valori da inserire e li inserisco
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, cliente.getUsername());
        cv.put(COLUMN_PASSWORD, cliente.getPassword());

        // inserisco i dati e controllo l'operazione, poi chiudo il db
        long insert = db.insert(CLIENT_TABLE, null, cv);
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean aggiungiDieta(Dieta dieta){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DIET_USERNAME, dieta.getClienteDieta());
        cv.put(COLUMN_DIET_BREAKFAST1, dieta.getColazione1());
        cv.put(COLUMN_DIET_BREAKFAST2, dieta.getColazione2());
        cv.put(COLUMN_DIET_LUNCH1, dieta.getPranzo1());
        cv.put(COLUMN_DIET_LUNCH2, dieta.getPranzo2());
        cv.put(COLUMN_DIET_DINNER1, dieta.getCena1());
        cv.put(COLUMN_DIET_DINNER2, dieta.getCena2());

        long insert = db.insert(DIET_TABLE, null, cv);
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public boolean aggiungiRichestaDieta(RichiestaDieta richiesta){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_REQUEST_DIET_CLIENT, richiesta.getUsernameCliente());
        cv.put(COLUMN_REQUEST_DIET_DIETOLOGIST, richiesta.getUsernameDietologo());
        cv.put(COLUMN_REQUEST_DIET_MODIFY, richiesta.getAlimentoDaModificare());
        cv.put(COLUMN_REQUEST_DIET_MODIFIER, richiesta.getAlimentoRichiesto());
        cv.put(COLUMN_REQUEST_DIET_APPROVED, "FALSE"); // all'inserimento la richiesta non e' approvata

        long insert = db.insert(REQUEST_DIET_TABLE, null, cv);
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

    public List<Dieta> recuperaDieta(String username){
        List<Dieta> returnList = new ArrayList<>();

        String queryDieta = "SELECT * FROM " + DIET_TABLE + " WHERE " + COLUMN_DIET_USERNAME + " = \"" + username + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor risultato = db.rawQuery(queryDieta, null);

        if(risultato.moveToFirst()){
            do{
                String usernameDieta = risultato.getString(0);
                String colazione1 = risultato.getString(1);
                String colazione2 = risultato.getString(2);
                String pranzo1 = risultato.getString(3);
                String pranzo2 = risultato.getString(4);
                String cena1 = risultato.getString(5);
                String cena2 = risultato.getString(6);

                //TODO Recuparare gli altri campi
                Dieta dietaRestituita = new Dieta (usernameDieta, colazione1, colazione2, pranzo1, pranzo2, cena1, cena2);
                returnList.add(dietaRestituita);
            } while (risultato.moveToNext());
        } else {
            // 0 risultati, ritorna una lista vuota
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return returnList;
    }

    public List<RichiestaDieta> recuperaRichiesteDieta(String username){
        List<RichiestaDieta> returnList = new ArrayList<>();

        String queryRichieste = "SELECT * FROM " + REQUEST_DIET_TABLE +" WHERE " + COLUMN_REQUEST_DIET_DIETOLOGIST + " = \"" + username + "\""
                + " AND " + COLUMN_REQUEST_DIET_APPROVED +  " = \"FALSE\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor risultato = db.rawQuery(queryRichieste, null);

        if(risultato.moveToFirst()){
            do{
                int idRichiesta = risultato.getInt(0);
                String usernameCliente = risultato.getString(1);
                String usernameDietologo = risultato.getString(2);
                String alimentoDaModificare = risultato.getString(3);
                String alimentoRichiesto = risultato.getString(4);
                boolean isApprovata = risultato.getInt(5) == 1 ? true: false;

                RichiestaDieta richiestaRestituita = new RichiestaDieta(idRichiesta, usernameCliente, usernameDietologo, alimentoDaModificare, alimentoRichiesto, isApprovata);
                returnList.add(richiestaRestituita);
            } while (risultato.moveToNext());
        } else {
            // 0 risultati, ritorna una lista vuota
        }
        risultato.close();
        db.close();

        return returnList;
    }

    public boolean approvaDieta(int idRichiesta){
        SQLiteDatabase mDb= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_REQUEST_DIET_APPROVED, "TRUE"); // approva la modifica
        return mDb.update(REQUEST_DIET_TABLE, cv, COLUMN_REQUEST_DIET_ID + "= " + idRichiesta, null)>0;
    }

    public boolean approvaDieta(int idRichiesta, String modifier){
        SQLiteDatabase mDb= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_REQUEST_DIET_MODIFIER, modifier); //modifica l'alimento
        cv.put(COLUMN_REQUEST_DIET_APPROVED, "TRUE"); // approva la modifica
        return mDb.update(REQUEST_DIET_TABLE, cv, COLUMN_REQUEST_DIET_ID + "= " + idRichiesta, null)>0;
    }

    public boolean modificaDieta(Dieta dieta){
        SQLiteDatabase mDb= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DIET_USERNAME, dieta.getClienteDieta());
        cv.put(COLUMN_DIET_BREAKFAST1, dieta.getColazione1());
        cv.put(COLUMN_DIET_BREAKFAST2, dieta.getColazione2());
        cv.put(COLUMN_DIET_LUNCH1, dieta.getPranzo1());
        cv.put(COLUMN_DIET_LUNCH2, dieta.getPranzo2());
        cv.put(COLUMN_DIET_DINNER1, dieta.getCena1());
        cv.put(COLUMN_DIET_DINNER2, dieta.getCena2());
        return mDb.update(DIET_TABLE, cv, COLUMN_DIET_USERNAME + "= \"" + dieta.getClienteDieta() + "\"", null)>0;
    }

}
