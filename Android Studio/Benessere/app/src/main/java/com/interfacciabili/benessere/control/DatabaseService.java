package com.interfacciabili.benessere.control;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.util.Log;

import com.interfacciabili.benessere.RichiesteDietologo;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Dieta;
import com.interfacciabili.benessere.model.RichiestaDieta;

import java.util.ArrayList;
import java.util.List;

import static com.interfacciabili.benessere.control.DietDBHelper.getDbIstance;

public class DatabaseService extends Service {
    public static final int MSG_IMG_SET = 1;
    private DietDBHelper dietDB;
    public IBinder binder = new LocalBinder();
    private Handler mImagesProgressHandler;

    public class LocalBinder extends Binder {
        public DatabaseService getService() {
            return DatabaseService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DatabaseService", "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dietDB = getDbIstance(getBaseContext());
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DatabaseService", "Service destroyed");
    }



    public boolean aggiungiCliente(Cliente cliente){

        // ottengo un db in scrittura utilizzando il metodo della classe dbhelper
        SQLiteDatabase db = dietDB.getWritableDatabase();

        // creo un contenitore per i valori da inserire e li inserisco
        ContentValues cv = new ContentValues();

        cv.put(dietDB.COLUMN_USERNAME, cliente.getUsername());
        cv.put(dietDB.COLUMN_PASSWORD, cliente.getPassword());

        // inserisco i dati e controllo l'operazione, poi chiudo il db
        long insert = db.insert(dietDB.CLIENT_TABLE, null, cv);
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean aggiungiClienteADietologo(String cliente, String dietologo){

        // ottengo un db in scrittura utilizzando il metodo della classe dbhelper
        SQLiteDatabase db = dietDB.getWritableDatabase();

        // creo un contenitore per i valori da inserire e li inserisco
        ContentValues cv = new ContentValues();

        cv.put(dietDB.COLUMN_CLIENT_USERNAME, cliente);
        cv.put(dietDB.COLUMN_DIETOLOGIST_USERNAME, dietologo);

        // inserisco i dati e controllo l'operazione, poi chiudo il db
        long insert = db.insert(dietDB.CLIENT_DIETOLOGIST_TABLE, null, cv);
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }


    public boolean aggiungiDieta(Dieta dieta){

        SQLiteDatabase db = dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(dietDB.COLUMN_DIET_USERNAME, dieta.getClienteDieta());
        cv.put(dietDB.COLUMN_DIET_BREAKFAST1, dieta.getColazione1());
        cv.put(dietDB.COLUMN_DIET_BREAKFAST2, dieta.getColazione2());
        cv.put(dietDB.COLUMN_DIET_LUNCH1, dieta.getPranzo1());
        cv.put(dietDB.COLUMN_DIET_LUNCH2, dieta.getPranzo2());
        cv.put(dietDB.COLUMN_DIET_DINNER1, dieta.getCena1());
        cv.put(dietDB.COLUMN_DIET_DINNER2, dieta.getCena2());

        long insert = db.insert(dietDB.DIET_TABLE, null, cv);
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public boolean aggiungiRichestaDieta(RichiestaDieta richiesta){

        SQLiteDatabase db = dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(dietDB.COLUMN_REQUEST_DIET_CLIENT, richiesta.getUsernameCliente());
        cv.put(dietDB.COLUMN_REQUEST_DIET_DIETOLOGIST, richiesta.getUsernameDietologo());
        cv.put(dietDB.COLUMN_REQUEST_DIET_MODIFY, richiesta.getAlimentoDaModificare());
        cv.put(dietDB.COLUMN_REQUEST_DIET_MODIFIER, richiesta.getAlimentoRichiesto());
        cv.put(dietDB.COLUMN_REQUEST_DIET_APPROVED, "FALSE"); // all'inserimento la richiesta non e' approvata

        long insert = db.insert(dietDB.REQUEST_DIET_TABLE, null, cv);
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

        String queryClienti = "SELECT * FROM " + dietDB.CLIENT_TABLE;

        // prendiamo il db in lettura
        SQLiteDatabase db = dietDB.getReadableDatabase();

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

    public List<Cliente> recuperaClientiSenzaDietologo(String usernameCercato){
        List<Cliente> returnList = new ArrayList<>();

        String queryClienti = "SELECT * FROM " + dietDB.CLIENT_TABLE + " WHERE " + dietDB.COLUMN_USERNAME + " LIKE \'%" + usernameCercato + "%\' AND " +
                dietDB.CLIENT_TABLE + "."+dietDB.COLUMN_CLIENT_USERNAME + " NOT IN (SELECT " + dietDB.COLUMN_CLIENT_USERNAME +" FROM "+ dietDB.CLIENT_DIETOLOGIST_TABLE +");";

        // prendiamo il db in lettura
        SQLiteDatabase db = dietDB.getReadableDatabase();

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

    public List<Cliente> recuperaClientiDiDietologo(String usernameDietologo){
        List<Cliente> returnList = new ArrayList<>();

        String queryClienti = "SELECT * FROM " + dietDB.CLIENT_TABLE + " JOIN " + dietDB.CLIENT_DIETOLOGIST_TABLE + " WHERE " + dietDB.CLIENT_TABLE + "." + dietDB.COLUMN_USERNAME
                + " = " + dietDB.CLIENT_DIETOLOGIST_TABLE + "." + dietDB.COLUMN_CLIENT_USERNAME + " AND " + dietDB.CLIENT_DIETOLOGIST_TABLE + "." + dietDB.COLUMN_DIETOLOGIST_USERNAME
                + " = \'" + usernameDietologo + "\';";

        // prendiamo il db in lettura
        SQLiteDatabase db = dietDB.getReadableDatabase();

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
    /*
        public List<Cliente> ricercaClienti(String usernameCercato){
            List<Cliente> returnList = new ArrayList<>();

            // query per ottenere tutti i clienti

            //TODO exclude in sql
            String queryClienti = "SELECT * FROM " + CLIENT_TABLE + " WHERE " + COLUMN_USERNAME + " LIKE \'%" + usernameCercato + "%\' ;";

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
    */
    public boolean eliminaCliente(String usernameCliente){
        // se trova il cliente lo cancella e ritorna true
        // altrimenti se non lo trova ritorna false

        SQLiteDatabase db = dietDB.getWritableDatabase();
        String queryString = "DELETE FROM " + dietDB.CLIENT_DIETOLOGIST_TABLE + " WHERE " + dietDB.COLUMN_CLIENT_USERNAME + " = \'" + usernameCliente + "\';";

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public List<Dieta> recuperaDieta(String username){
        List<Dieta> returnList = new ArrayList<>();

        String queryDieta = "SELECT * FROM " + dietDB.DIET_TABLE + " WHERE " + dietDB.COLUMN_DIET_USERNAME + " = \"" + username + "\"";

        SQLiteDatabase db = dietDB.getReadableDatabase();

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

        String queryRichieste = "SELECT * FROM " + dietDB.REQUEST_DIET_TABLE +" WHERE " + dietDB.COLUMN_REQUEST_DIET_DIETOLOGIST + " = \"" + username + "\""
                + " AND " + dietDB.COLUMN_REQUEST_DIET_APPROVED +  " = \"FALSE\"";

        SQLiteDatabase db = dietDB.getReadableDatabase();
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
        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dietDB.COLUMN_REQUEST_DIET_APPROVED, "TRUE"); // approva la modifica
        return mDb.update(dietDB.REQUEST_DIET_TABLE, cv, dietDB.COLUMN_REQUEST_DIET_ID + "= " + idRichiesta, null)>0;
    }

    public boolean approvaDieta(int idRichiesta, String modifier){
        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dietDB.COLUMN_REQUEST_DIET_MODIFIER, modifier); //modifica l'alimento
        cv.put(dietDB.COLUMN_REQUEST_DIET_APPROVED, "TRUE"); // approva la modifica
        return mDb.update(dietDB.REQUEST_DIET_TABLE, cv, dietDB.COLUMN_REQUEST_DIET_ID + "= " + idRichiesta, null)>0;
    }

    public boolean modificaDieta(Dieta dieta){
        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dietDB.COLUMN_DIET_USERNAME, dieta.getClienteDieta());
        cv.put(dietDB.COLUMN_DIET_BREAKFAST1, dieta.getColazione1());
        cv.put(dietDB.COLUMN_DIET_BREAKFAST2, dieta.getColazione2());
        cv.put(dietDB.COLUMN_DIET_LUNCH1, dieta.getPranzo1());
        cv.put(dietDB.COLUMN_DIET_LUNCH2, dieta.getPranzo2());
        cv.put(dietDB.COLUMN_DIET_DINNER1, dieta.getCena1());
        cv.put(dietDB.COLUMN_DIET_DINNER2, dieta.getCena2());
        return mDb.update(dietDB.DIET_TABLE, cv, dietDB.COLUMN_DIET_USERNAME + "= \"" + dieta.getClienteDieta() + "\"", null)>0;
    }

}