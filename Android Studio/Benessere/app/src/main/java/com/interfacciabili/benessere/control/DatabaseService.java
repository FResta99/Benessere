package com.interfacciabili.benessere.control;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.interfacciabili.benessere.model.Alimento;
import com.interfacciabili.benessere.model.Attrezzo;
import com.interfacciabili.benessere.model.Cliente;
import com.interfacciabili.benessere.model.Coach;
import com.interfacciabili.benessere.model.Dietologo;
import com.interfacciabili.benessere.model.Esercizio;
import com.interfacciabili.benessere.model.Prodotto;
import com.interfacciabili.benessere.model.RichiestaDieta;

import java.util.ArrayList;
import java.util.List;

import static com.interfacciabili.benessere.control.DBHelper.getDbIstance;

public class DatabaseService extends Service {

    private DBHelper dietDB;
    public IBinder binder = new LocalBinder();


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
        cv.put(dietDB.COLUMN_CLIENT_MAIL, cliente.getEmail());
        cv.put(dietDB.COLUMN_CLIENT_NAME, cliente.getNome());
        cv.put(dietDB.COLUMN_CLIENT_SURNAME, cliente.getCognome());
        cv.put(dietDB.COLUMN_CLIENT_AGE, cliente.getEta());
        cv.put(dietDB.COLUMN_CLIENT_GENDER, cliente.getSesso());
        cv.put(dietDB.COLUMN_CLIENT_WEIGHT, cliente.getPeso());
        cv.put(dietDB.COLUMN_CLIENT_HEIGHT, cliente.getAltezza());
        cv.put(dietDB.COLUMN_CLIENT_PROPIC, cliente.getFotoProfilo());

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
        cv.put(dietDB.COLUMN_CLIENT_ARCHIVED, "FALSE");
        // inserisco i dati e controllo l'operazione, poi chiudo il db
        long insert = db.replace(dietDB.CLIENT_DIETOLOGIST_TABLE, null, cv);
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }


    public boolean aggiungiAlimentoADieta(Cliente cliente, Alimento alimento){


        SQLiteDatabase db = dietDB.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(dietDB.COLUMN_DIET_USERNAME, cliente.getUsername());
        cv.put(dietDB.COLUMN_EDIBLE_NAME, alimento.getNome());
        cv.put(dietDB.COLUMN_EDIBLE_PORTION, alimento.getPorzione());
        cv.put(dietDB.COLUMN_EDIBLE_PORTION_TYPE, alimento.getTipoPorzione());
        cv.put(dietDB.COLUMN_EDIBLE_TYPE, alimento.getTipoPasto());
        cv.put(dietDB.COLUMN_EDIBLE_DAY, alimento.getGiorno());

        long insert = db.insert(dietDB.DIET_TABLE, null, cv);
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean eliminaAlimento(Alimento alimento){
        // se trova il cliente lo cancella e ritorna true
        // altrimenti se non lo trova ritorna false

        SQLiteDatabase db = dietDB.getWritableDatabase();
        String queryString = "DELETE FROM " + dietDB.DIET_TABLE + " WHERE " + dietDB.COLUMN_EDIBLE_ID + " = \'" + alimento.getId() + "\';";

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
                String email = risultato.getString(2);
                String nome = risultato.getString(3);
                String cognome = risultato.getString(4);
                String sesso = risultato.getString(5);
                int eta = risultato.getInt(6);
                int peso = risultato.getInt(7);
                int altezza = risultato.getInt(8);
                String uri = risultato.getString(9);

                Cliente clienteRestituito = new Cliente (username, password, email, nome, cognome, sesso, eta, peso, altezza, uri);
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

    public String recuperaDietologoDiCliente(String usernameCliente){
        String dietologoRestituito;

        // query per ottenere tutti i clienti

        String queryClienti = "SELECT DIETOLOGIST_USERNAME FROM " + dietDB.CLIENT_DIETOLOGIST_TABLE + " WHERE " + dietDB.COLUMN_CLIENT_USERNAME + " = \'" + usernameCliente + "\';";

        // prendiamo il db in lettura
        SQLiteDatabase db = dietDB.getReadableDatabase();

        // Result set
        Cursor risultato = db.rawQuery(queryClienti, null);

        // Accediamo al primo elemento, se esiste
        if(risultato.moveToFirst()){
            dietologoRestituito = risultato.getString(0);
        } else {
            dietologoRestituito = "";
        }

        risultato.close();
        db.close();
        return dietologoRestituito;
    }

    public List<Cliente> recuperaClientiSenzaDietologo(String usernameCercato){
        List<Cliente> returnList = new ArrayList<>();

        String queryClienti = "SELECT * FROM " + dietDB.CLIENT_TABLE + " WHERE " + dietDB.COLUMN_USERNAME + " LIKE \'%" + usernameCercato + "%\' AND " +
                dietDB.CLIENT_TABLE + "."+dietDB.COLUMN_CLIENT_USERNAME + " NOT IN (SELECT " + dietDB.COLUMN_CLIENT_USERNAME +" FROM "+ dietDB.CLIENT_DIETOLOGIST_TABLE +")" +
                " OR CLIENT_TABLE.CLIENT_USERNAME IN (SELECT CLIENT_USERNAME FROM CLIENT_DIETOLOGIST_TABLE WHERE CLIENT_ARCHIVED = \"TRUE\");";

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
                String email = risultato.getString(2);
                String nome = risultato.getString(3);
                String cognome = risultato.getString(4);
                String sesso = risultato.getString(5);
                int eta = risultato.getInt(6);
                int peso = risultato.getInt(7);
                int altezza = risultato.getInt(8);
                String uri = risultato.getString(9);

                Cliente clienteRestituito = new Cliente (username, password, email, nome, cognome, sesso, eta, peso, altezza, uri);
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
                + " = \'" + usernameDietologo + "\' AND CLIENT_ARCHIVED = \"FALSE\";";

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
                String email = risultato.getString(2);
                String nome = risultato.getString(3);
                String cognome = risultato.getString(4);
                String sesso = risultato.getString(5);
                int eta = risultato.getInt(6);
                int peso = risultato.getInt(7);
                int altezza = risultato.getInt(8);
                String uri = risultato.getString(9);

                Cliente clienteRestituito = new Cliente (username, password, email, nome, cognome, sesso, eta, peso, altezza, uri);
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

    public boolean eliminaClienteDaDietologo(String usernameCliente){
        // se trova il cliente lo archivia e ritorna true
        // altrimenti se non lo trova ritorna false

        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CLIENT_ARCHIVED", "TRUE");



        return mDb.update(dietDB.CLIENT_DIETOLOGIST_TABLE, cv, dietDB.COLUMN_CLIENT_USERNAME + "= '" + usernameCliente + "'", null)>0;
    }


    public List<Alimento> recuperaDieta(String username){
        List<Alimento> returnList = new ArrayList<>();

        String queryDieta = "SELECT * FROM " + dietDB.DIET_TABLE + " WHERE " + dietDB.COLUMN_DIET_USERNAME + " = \"" + username + "\"";

        SQLiteDatabase db = dietDB.getReadableDatabase();

        Cursor risultato = db.rawQuery(queryDieta, null);

        if(risultato.moveToFirst()){
            do{
                int idAlimento = risultato.getInt(0);
                String nomeAlimento = risultato.getString(2);
                String porzioneAlimento = risultato.getString(3);
                String tipoPorzione = risultato.getString(4);
                String tipoAlimento = risultato.getString(5);
                String giornoAlimeno = risultato.getString(6);
                Alimento alimentoRestituito = new Alimento(idAlimento, nomeAlimento, porzioneAlimento, tipoPorzione, tipoAlimento, giornoAlimeno);
                returnList.add(alimentoRestituito);
            } while (risultato.moveToNext());
        } else {
            // 0 risultati, ritorna una lista vuota
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return returnList;
    }

    public List<Alimento> recuperaDietaGiorno(String username, String giorno){
        List<Alimento> returnList = new ArrayList<>();

        String queryDieta = "SELECT * FROM " + dietDB.DIET_TABLE + " WHERE " + dietDB.COLUMN_DIET_USERNAME + " = \"" + username + "\" AND " + dietDB.COLUMN_EDIBLE_DAY  + "=\'" + giorno + "\'";

        SQLiteDatabase db = dietDB.getReadableDatabase();

        Cursor risultato = db.rawQuery(queryDieta, null);

        if(risultato.moveToFirst()){
            do{
                int idAlimento = risultato.getInt(0);
                String nomeAlimento = risultato.getString(2);
                String porzioneAlimento = risultato.getString(3);
                String tipoPorzione = risultato.getString(4);
                String tipoAlimento = risultato.getString(5);

                Alimento alimentoRestituito = new Alimento(idAlimento, nomeAlimento, porzioneAlimento, tipoPorzione, tipoAlimento, "");
                returnList.add(alimentoRestituito);
            } while (risultato.moveToNext());
        } else {
            // 0 risultati, ritorna una lista vuota
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return returnList;
    }


    public boolean modificaAlimentoDieta(Alimento alimento, String nome, int porzione, String tipoPorzione, String tipoPasto, String giornoPasto){
        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dietDB.COLUMN_EDIBLE_NAME, nome);
        cv.put(dietDB.COLUMN_EDIBLE_PORTION, porzione);
        cv.put(dietDB.COLUMN_EDIBLE_PORTION_TYPE, tipoPorzione);
        cv.put(dietDB.COLUMN_EDIBLE_TYPE, tipoPasto);
        cv.put(dietDB.COLUMN_EDIBLE_DAY, giornoPasto);
        return mDb.update(dietDB.DIET_TABLE, cv, dietDB.COLUMN_EDIBLE_ID + "= \"" + alimento.getId() + "\"", null)>0;
    }

    public boolean aggiungiRichestaDieta(RichiestaDieta richiesta){

        SQLiteDatabase db = dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(dietDB.COLUMN_REQUEST_DIET_CLIENT, richiesta.getUsernameCliente());
        cv.put(dietDB.COLUMN_REQUEST_DIET_CLIENT_NAME, richiesta.getNomeCliente());
        cv.put(dietDB.COLUMN_REQUEST_DIET_CLIENT_SURNAME, richiesta.getCognomeCliente());
        cv.put(dietDB.COLUMN_REQUEST_DIET_DIETOLOGIST, richiesta.getUsernameDietologo());
        cv.put(dietDB.COLUMN_REQUEST_DIET_ID_MODIFY, richiesta.getIdAlimentoDaModificare());
        cv.put(dietDB.COLUMN_REQUEST_DIET_MODIFY, richiesta.getAlimentoDaModificare());
        cv.put(dietDB.COLUMN_REQUEST_DIET_MODIFIER, richiesta.getAlimentoRichiesto());
        cv.put(dietDB.COLUMN_REQUEST_DIET_PORTION, richiesta.getAlimentoRichiestoPorzione());
        cv.put(dietDB.COLUMN_REQUEST_DIET_PORTION_TYPE, richiesta.getAlimentoRichiestoTipoPorzione());
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
                String nomeCliente = risultato.getString(2);
                String cognomeCliente = risultato.getString(3);
                String usernameDietologo = risultato.getString(4);
                int idAlimentoDaModificare = risultato.getInt(5);
                String alimentoDaModificare = risultato.getString(6);
                String alimentoRichiesto = risultato.getString(7);
                String alimentoRichiestoPorzione = risultato.getString(8);
                String alimentoRichiestoTipoPorzione = risultato.getString(9);
                boolean isApprovata = risultato.getInt(10) == 1 ? true: false;

                RichiestaDieta richiestaRestituita = new RichiestaDieta(idRichiesta, usernameCliente, nomeCliente, cognomeCliente, usernameDietologo, idAlimentoDaModificare,alimentoDaModificare, alimentoRichiesto,
                        alimentoRichiestoPorzione, alimentoRichiestoTipoPorzione, isApprovata);
                returnList.add(richiestaRestituita);
            } while (risultato.moveToNext());

        } else {

        }

        risultato.close();
        db.close();
        return returnList;
    }


    public boolean disapprovaDieta(RichiestaDieta richiesta){
        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        return mDb.delete(dietDB.REQUEST_DIET_TABLE, dietDB.COLUMN_REQUEST_DIET_ID + "= " + richiesta.getId(), null)>0;
    }


    public boolean approvaDieta(RichiestaDieta richiesta, String alimentoRichiesto, int porzione, String tipoPorzione){
        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dietDB.COLUMN_REQUEST_DIET_MODIFIER, richiesta.getAlimentoRichiesto()); //modifica l'alimento
        cv.put(dietDB.COLUMN_REQUEST_DIET_PORTION, richiesta.getAlimentoRichiestoPorzione()); //modifica l'alimento
        cv.put(dietDB.COLUMN_REQUEST_DIET_PORTION_TYPE, richiesta.getAlimentoRichiestoTipoPorzione()); //modifica l'alimento
        cv.put(dietDB.COLUMN_REQUEST_DIET_APPROVED, "TRUE"); // approva la modifica

        ContentValues cv2 = new ContentValues();
        cv2.put(dietDB.COLUMN_EDIBLE_NAME, alimentoRichiesto);
        cv2.put(dietDB.COLUMN_EDIBLE_PORTION, porzione);
        cv2.put(dietDB.COLUMN_EDIBLE_PORTION_TYPE, tipoPorzione);

        mDb.update(dietDB.DIET_TABLE, cv2, dietDB.COLUMN_EDIBLE_ID + "= " + richiesta.getIdAlimentoDaModificare() + " AND " + dietDB.COLUMN_DIET_USERNAME + " = \"" + richiesta.getUsernameCliente() + "\"", null);

        return mDb.update(dietDB.REQUEST_DIET_TABLE, cv, dietDB.COLUMN_REQUEST_DIET_ID + "= " + richiesta.getId(), null)>0;
    }


    //QUERY SHOPPING LIST
    public void inserisciProdotto(Prodotto prodotto){
        SQLiteDatabase db = dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dietDB.COLUMN_PRODUCT_NAME, prodotto.getNome());
        cv.put(dietDB.COLUMN_PRODUCT_STATUS, 0); //0 = non selezionato
        cv.put(dietDB.COLUMN_PRODUCT_CLIENT, prodotto.getCliente());
        db.insert(dietDB.PRODUCT_TABLE, null, cv);
    }

    public void aggiornaProdotto(int id, String nome){
        SQLiteDatabase db = dietDB.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(dietDB.COLUMN_PRODUCT_NAME, nome);

        db.update(dietDB.PRODUCT_TABLE, cv, "PRODUCT_ID=?", new String[]{String.valueOf(id)});
    }

    public void aggiornaStatus(int id, int status){
        SQLiteDatabase db = dietDB.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(dietDB.COLUMN_PRODUCT_STATUS, status);

        db.update(dietDB.PRODUCT_TABLE, cv, "PRODUCT_ID=?", new String[]{String.valueOf(id)});
    }

    public void eliminaProdotto(int id){
        SQLiteDatabase db = dietDB.getWritableDatabase();
        db.delete(dietDB.PRODUCT_TABLE, "PRODUCT_ID=?", new String[]{String.valueOf(id)});
    }

    public List<Prodotto> ottieniProdotti(String usernameCliente){
        List<Prodotto> returnList = new ArrayList<>();

        String queryDieta = "SELECT * FROM " + dietDB.PRODUCT_TABLE + " WHERE PRODUCT_CLIENT " +" = \"" + usernameCliente + "\"";
        SQLiteDatabase db = dietDB.getReadableDatabase();

        Cursor risultato = db.rawQuery(queryDieta, null);

        if(risultato.moveToFirst()){
            do{
                int idProdotto = risultato.getInt(0);
                String nomeProdotto = risultato.getString(1);
                int statusProdotto = risultato.getInt(2);
                String clienteProdotto = risultato.getString(3);

                Prodotto prodottoRestituito = new Prodotto(idProdotto, nomeProdotto, statusProdotto, clienteProdotto);
                returnList.add(prodottoRestituito);
            } while (risultato.moveToNext());
        } else {

        }

        risultato.close();
        db.close();
        return returnList;

    }

    public List<Attrezzo> recuperaAttrezzi(){
        List<Attrezzo> returnList = new ArrayList<>();

        String queryAttrezzi = "SELECT * FROM " + dietDB.EQUIP_TABLE + ";";

        SQLiteDatabase db = dietDB.getReadableDatabase();
        Cursor risultato = db.rawQuery(queryAttrezzi, null);

        if (risultato.moveToFirst()) {
            do {
                String nome = risultato.getString(0);
                String descrizione = risultato.getString(1);

                Attrezzo attrezzoRestituito = new Attrezzo(nome, descrizione);
                returnList.add(attrezzoRestituito);
            } while (risultato.moveToNext());
        }

        risultato.close();
        db.close();

        return returnList;
    }

    public boolean aggiungiEsercizioASchedaAllenamento(Cliente cliente, Esercizio esercizio){

        SQLiteDatabase db = dietDB.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("EXERCISE_CLIENT", cliente.getUsername());
        cv.put(dietDB.COLUMN_EXERCISE_NAME, esercizio.getNome());
        cv.put(dietDB.COLUMN_EXERCISE_REPS, esercizio.getRipetizioni());
        cv.put(dietDB.COLUMN_EXERCISE_DAY, esercizio.getGiorno());

        long insert = db.insert(dietDB.EXERCISE_TABLE, null, cv);
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean aggiungiClienteACoach (String cliente, String coach){

        // ottengo un db in scrittura utilizzando il metodo della classe dbhelper
        SQLiteDatabase db = dietDB.getWritableDatabase();

        // creo un contenitore per i valori da inserire e li inserisco
        ContentValues cv = new ContentValues();

        cv.put(dietDB.COLUMN_CLIENT_USERNAME, cliente);
        cv.put(dietDB.COLUMN_COACH_USERNAME, coach);
        cv.put(dietDB.COLUMN_CLIENT_ARCHIVED, "FALSE");

        // inserisco i dati e controllo l'operazione, poi chiudo il db
        long insert = db.replace(dietDB.CLIENT_COACH_TABLE, null, cv);
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean eliminaEsercizio (Esercizio esercizio){
        // se trova il cliente lo cancella e ritorna true
        // altrimenti se non lo trova ritorna false

        SQLiteDatabase db = dietDB.getWritableDatabase();
        String queryString = "DELETE FROM " + dietDB.EXERCISE_TABLE + " WHERE " + dietDB.COLUMN_EXERCISE_ID + " = \'" + esercizio.getId() + "\';";

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

    public List<Cliente> recuperaClientiSenzaCoach (String usernameCercato){
        List<Cliente> returnList = new ArrayList<>();

        String queryClienti = "SELECT * FROM " + dietDB.CLIENT_TABLE + " WHERE " + dietDB.COLUMN_CLIENT_USERNAME + " LIKE \'%" + usernameCercato + "%\' AND " +
                dietDB.CLIENT_TABLE + "."+dietDB.COLUMN_CLIENT_USERNAME + " NOT IN (SELECT " + dietDB.COLUMN_CLIENT_USERNAME +" FROM "+ dietDB.CLIENT_COACH_TABLE +
                " ) OR CLIENT_TABLE.CLIENT_USERNAME IN (SELECT CLIENT_USERNAME FROM CLIENT_COACH_TABLE WHERE CLIENT_ARCHIVED = \"TRUE\");";

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
                String email = risultato.getString(2);
                String nome = risultato.getString(3);
                String cognome = risultato.getString(4);
                String sesso = risultato.getString(5);
                int eta = risultato.getInt(6);
                int peso = risultato.getInt(7);
                int altezza = risultato.getInt(8);
                String uri = risultato.getString(9);

                Cliente clienteRestituito = new Cliente (username, password, email, nome, cognome, sesso, eta, peso, altezza, uri);
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

    public List<Cliente> recuperaClientiDiCoach (String usernameCoach){
        List<Cliente> returnList = new ArrayList<>();

        String queryClienti = "SELECT * FROM " + dietDB.CLIENT_TABLE + " JOIN " + dietDB.CLIENT_COACH_TABLE + " WHERE " + dietDB.CLIENT_TABLE + "." + dietDB.COLUMN_CLIENT_USERNAME
                + " = " + dietDB.CLIENT_COACH_TABLE + "." + dietDB.COLUMN_CLIENT_USERNAME + " AND " + dietDB.CLIENT_COACH_TABLE + "." + dietDB.COLUMN_COACH_USERNAME
                + " = \'" + usernameCoach + "\' AND CLIENT_ARCHIVED = \"FALSE\";";

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
                String email = risultato.getString(2);
                String nome = risultato.getString(3);
                String cognome = risultato.getString(4);
                String sesso = risultato.getString(5);
                int eta = risultato.getInt(6);
                int peso = risultato.getInt(7);
                int altezza = risultato.getInt(8);
                String uri = risultato.getString(9);

                Cliente clienteRestituito = new Cliente (username, password, email, nome, cognome, sesso, eta, peso, altezza, uri);
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

    public boolean eliminaClienteDaCoach (String usernameCliente){
        // se trova il cliente lo cancella e ritorna true
        // altrimenti se non lo trova ritorna false

        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CLIENT_ARCHIVED", "TRUE");



        return mDb.update(dietDB.CLIENT_COACH_TABLE, cv, dietDB.COLUMN_CLIENT_USERNAME + "= '" + usernameCliente + "'", null)>0;
    }

    public List<Esercizio> recuperaAlleamento (String username){
        List<Esercizio> returnList = new ArrayList<>();

        String queryDieta = "SELECT * FROM EXERCISE_TABLE WHERE EXERCISE_CLIENT = "+  "\'" + username + "\'";

        SQLiteDatabase db = dietDB.getReadableDatabase();

        Cursor risultato = db.rawQuery(queryDieta, null);

        if(risultato.moveToFirst()){
            do{
                int idEsercizio = risultato.getInt(0);
                String nomeEsercizio = risultato.getString(2);

                int numeroRipetizioni = risultato.getInt(3);
                String giornoEsercizio = risultato.getString(4);

                String spiegazione = risultato.getString(5);

                Esercizio esercizioRestituito = new Esercizio(idEsercizio, nomeEsercizio, numeroRipetizioni,giornoEsercizio, spiegazione);
                returnList.add(esercizioRestituito);
            } while (risultato.moveToNext());
        } else {
            // 0 risultati, ritorna una lista vuota
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return returnList;
    }

    public boolean modificaEsercizioScheda(Esercizio esercizio, String nome, int ripetizioni, String giorno){
        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("EXERCISE_NAME", nome);
        cv.put("EXERCISE_REPS", ripetizioni);
        cv.put("EXERCISE_DAY", giorno);
        return mDb.update(dietDB.EXERCISE_TABLE, cv, dietDB.COLUMN_EXERCISE_ID + "= \"" + esercizio.getId() + "\"", null)>0;
    }

    public String recuperaCoachDiCliente(String usernameCliente){
        String coachRestituito;

        // query per ottenere tutti i clienti

        String queryClienti = "SELECT COACH_USERNAME FROM " + dietDB.CLIENT_COACH_TABLE + " WHERE " + dietDB.COLUMN_CLIENT_USERNAME + " = \'" + usernameCliente + "\';";

        // prendiamo il db in lettura
        SQLiteDatabase db = dietDB.getReadableDatabase();

        // Result set
        Cursor risultato = db.rawQuery(queryClienti, null);

        // Accediamo al primo elemento, se esiste
        if(risultato.moveToFirst()){
            coachRestituito = risultato.getString(0);
        } else {
            coachRestituito = "";
        }

        risultato.close();
        db.close();
        return coachRestituito;
    }

    public List<Esercizio> recuperaAllenamentoGiorno(String username, String giorno){
        List<Esercizio> returnList = new ArrayList<>();

        String queryDieta = "SELECT * FROM " + dietDB.EXERCISE_TABLE + " WHERE EXERCISE_CLIENT" + " = \"" + username + "\" AND " + dietDB.COLUMN_EXERCISE_DAY  + "=\'" + giorno + "\'";

        SQLiteDatabase db = dietDB.getReadableDatabase();

        Cursor risultato = db.rawQuery(queryDieta, null);

        if(risultato.moveToFirst()){
            do{
                int idEsercizio = risultato.getInt(0);
                String nomeEsercizio = risultato.getString(2);
                int ripetizioniEsercizio = risultato.getInt(3);

                Esercizio esercizioRestituito = new Esercizio(idEsercizio, nomeEsercizio, ripetizioniEsercizio, "", "");
                returnList.add(esercizioRestituito);
            } while (risultato.moveToNext());
        } else {
            // 0 risultati, ritorna una lista vuota
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return returnList;
    }

    public void aggiungiClienteFoto(String cliente, String uri){
        SQLiteDatabase db = dietDB.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("PICTURE_URI", uri);

        db.update(dietDB.CLIENT_TABLE, cv, "CLIENT_USERNAME=?", new String[]{cliente});
    }


    public String recuperaClienteFoto(String username){
        String uri = "";

        String queryDieta = "SELECT PICTURE_URI FROM CLIENT_TABLE" + " WHERE CLIENT_USERNAME" + " = \'" + username + "\'";

        SQLiteDatabase db = dietDB.getReadableDatabase();

        Cursor risultato = db.rawQuery(queryDieta, null);

        if(risultato.moveToFirst()){
                uri = risultato.getString(0);
        } else {
            // 0 risultati, ritorna una lista vuota
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return uri;
    }


    // METODI REGISTRAZIONE


    public boolean isDietologistUsernameInDatabase (String usernameCercato){
        boolean isPresente = false;

        String queryClienti = "SELECT * FROM " + dietDB.DIETOLOGIST_TABLE + " WHERE " + dietDB.COLUMN_DIETOLOGIST_USERNAME + " = \"" + usernameCercato + "\"; ";

        // prendiamo il db in lettura
        SQLiteDatabase db = dietDB.getReadableDatabase();

        // Result set
        Cursor risultato = db.rawQuery(queryClienti, null);

        // Accediamo al primo elemento, se esiste
        if(risultato.moveToFirst()){
            isPresente = true;
        } else {

            // 0 risultati, non ha trovato nulla
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return isPresente;
    }

    public boolean isClientUsernameInDatabase (String usernameCercato){
        boolean isPresente = false;

        String queryClienti = "SELECT * FROM " + dietDB.CLIENT_TABLE + " WHERE " + dietDB.COLUMN_CLIENT_USERNAME + " = \"" + usernameCercato + "\"; ";

        // prendiamo il db in lettura
        SQLiteDatabase db = dietDB.getReadableDatabase();

        // Result set
        Cursor risultato = db.rawQuery(queryClienti, null);

        // Accediamo al primo elemento, se esiste
        if(risultato.moveToFirst()){
            isPresente = true;
        } else {

            // 0 risultati, non ha trovato nulla
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return isPresente;
    }

    public boolean aggiungiDietologo(Dietologo dietologo){

        // ottengo un db in scrittura utilizzando il metodo della classe dbhelper
        SQLiteDatabase db = dietDB.getWritableDatabase();

        // creo un contenitore per i valori da inserire e li inserisco
        ContentValues cv = new ContentValues();

        cv.put(dietDB.COLUMN_DIETOLOGIST_USERNAME, dietologo.getUsername());
        cv.put(dietDB.COLUMN_DIETOLOGIST_PASSWORD, dietologo.getPassword());
        cv.put(dietDB.COLUMN_DIETOLOGIST_MAIL, dietologo.getEmail());
        cv.put(dietDB.COLUMN_DIETOLOGIST_NAME, dietologo.getNome());
        cv.put(dietDB.COLUMN_DIETOLOGIST_SURNAME, dietologo.getCognome());
        cv.put(dietDB.COLUMN_DIETOLOGIST_GENDER, dietologo.getSesso());
        cv.put(dietDB.COLUMN_DIETOLOGIST_AGE, dietologo.getEta());
        cv.put(dietDB.COLUMN_DIETOLOGIST_STUDIO, dietologo.getStudio());

        // inserisco i dati e controllo l'operazione, poi chiudo il db
        long insert = db.insert(dietDB.DIETOLOGIST_TABLE, null, cv);
        Log.d("RESULTDB", Long.toString(insert) );
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    //Funzione che restituisce un dietologo nel database in base all'usernme cercato, se non esiste restituisce null
    public Dietologo ricercaDietologo (String usernameCercato){
        Dietologo dietologoCercato = new Dietologo();

        String queryClienti = "SELECT * FROM " + dietDB.DIETOLOGIST_TABLE + " WHERE " + dietDB.COLUMN_DIETOLOGIST_USERNAME + " = \"" + usernameCercato + "\"; ";

        // prendiamo il db in lettura
        SQLiteDatabase db = dietDB.getReadableDatabase();

        // Result set
        Cursor risultato = db.rawQuery(queryClienti, null);

        // Accediamo al primo elemento, se esiste
        if(risultato.moveToFirst()){
            String username = risultato.getString(0);
            String password = risultato.getString(1);
            String mail = risultato.getString(2);
            String nome = risultato.getString(3);
            String cognome = risultato.getString(4);
            String sesso = risultato.getString(5);
            int eta = risultato.getInt(6);
            String studio = risultato.getString(7);

            dietologoCercato.setUsername(username);
            dietologoCercato.setPassword(password);
            dietologoCercato.setEmail(mail);
            dietologoCercato.setNome(nome);
            dietologoCercato.setCognome(cognome);
            dietologoCercato.setEta(eta);
            dietologoCercato.setSesso(sesso);
            dietologoCercato.setStudio(studio);
        } else {

            // 0 risultati, non ha trovato nulla
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return dietologoCercato;
    }

    //Funzione che restituisce un coach nel database in base all'usernme cercato
    public Coach ricercaCoach (String usernameCercato){
        Coach coachCercato = new Coach();

        String queryClienti = "SELECT * FROM " + dietDB.COACH_TABLE + " WHERE " + dietDB.COLUMN_COACH_USERNAME + " = \"" + usernameCercato + "\"; ";

        // prendiamo il db in lettura
        SQLiteDatabase db = dietDB.getReadableDatabase();

        // Result set
        Cursor risultato = db.rawQuery(queryClienti, null);

        // Accediamo al primo elemento, se esiste
        if(risultato.moveToFirst()){
            String username = risultato.getString(0);
            String password = risultato.getString(1);
            String mail = risultato.getString(2);
            String nome = risultato.getString(3);
            String cognome = risultato.getString(4);
            String sesso = risultato.getString(5);
            int eta = risultato.getInt(6);
            String palestra = risultato.getString(7);

            coachCercato.setUsername(username);
            coachCercato.setPassword(password);
            coachCercato.setEmail(mail);
            coachCercato.setNome(nome);
            coachCercato.setCognome(cognome);
            coachCercato.setEta(eta);
            coachCercato.setSesso(sesso);
            coachCercato.setPalestra(palestra);
        } else {

            // 0 risultati, non ha trovato nulla
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return coachCercato;
    }

    public boolean aggiungiCoach(Coach coach){

        // ottengo un db in scrittura utilizzando il metodo della classe dbhelper
        SQLiteDatabase db = dietDB.getWritableDatabase();

        // creo un contenitore per i valori da inserire e li inserisco
        ContentValues cv = new ContentValues();

        cv.put(dietDB.COLUMN_COACH_USERNAME, coach.getUsername());
        cv.put(dietDB.COLUMN_COACH_PASSWORD, coach.getPassword());
        cv.put(dietDB.COLUMN_COACH_MAIL, coach.getEmail());
        cv.put(dietDB.COLUMN_COACH_NAME, coach.getNome());
        cv.put(dietDB.COLUMN_COACH_SURNAME, coach.getCognome());
        cv.put(dietDB.COLUMN_COACH_GENDER, coach.getSesso());
        cv.put(dietDB.COLUMN_COACH_AGE, coach.getEta());
        cv.put(dietDB.COLUMN_COACH_GYM, coach.getPalestra());

        // inserisco i dati e controllo l'operazione, poi chiudo il db
        long insert = db.insert(dietDB.COACH_TABLE, null, cv);
        Log.d("RESULTDB", Long.toString(insert) );
        if(insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean isCoachUsernameInDatabase (String usernameCercato){
        boolean isPresente = false;

        String queryClienti = "SELECT * FROM " + dietDB.COACH_TABLE + " WHERE " + dietDB.COLUMN_COACH_USERNAME + " = \"" + usernameCercato + "\"; ";

        // prendiamo il db in lettura
        SQLiteDatabase db = dietDB.getReadableDatabase();

        // Result set
        Cursor risultato = db.rawQuery(queryClienti, null);

        // Accediamo al primo elemento, se esiste
        if(risultato.moveToFirst()){
            isPresente = true;
        } else {

            // 0 risultati, non ha trovato nulla
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return isPresente;
    }

    //Funzione che restituisce un coach nel database in base all'usernme cercato
    public Cliente ricercaCliente (String usernameCercato){
        Cliente clienteCercato = new Cliente();

        String queryClienti = "SELECT * FROM " + dietDB.CLIENT_TABLE + " WHERE " + dietDB.COLUMN_CLIENT_USERNAME +  " = \"" + usernameCercato + "\"; ";

        // prendiamo il db in lettura
        SQLiteDatabase db = dietDB.getReadableDatabase();

        // Result set
        Cursor risultato = db.rawQuery(queryClienti, null);

        // Accediamo al primo elemento, se esiste
        if(risultato.moveToFirst()){
            String username = risultato.getString(0);
            String password = risultato.getString(1);
            String mail = risultato.getString(2);
            String nome = risultato.getString(3);
            String cognome = risultato.getString(4);
            String sesso = risultato.getString(5);
            int eta = risultato.getInt(6);
            int peso = risultato.getInt(7);
            int altezza = risultato.getInt(8);
            String fotoProfilo = risultato.getString(9);

            clienteCercato.setUsername(username);
            clienteCercato.setPassword(password);
            clienteCercato.setEmail(mail);
            clienteCercato.setNome(nome);
            clienteCercato.setCognome(cognome);
            clienteCercato.setEta(eta);
            clienteCercato.setSesso(sesso);
            clienteCercato.setFotoProfilo(fotoProfilo);
            clienteCercato.setPeso(peso);
            clienteCercato.setAltezza(altezza);

        } else {

            // 0 risultati, non ha trovato nulla
        }
        // a fine query, chiudiamo il cursore e il db
        risultato.close();
        db.close();
        return clienteCercato;
    }

    public boolean modificaPesoAltezzaCliente(Cliente cliente, int peso, int altezza){
        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dietDB.COLUMN_CLIENT_WEIGHT, peso);
        cv.put(dietDB.COLUMN_CLIENT_HEIGHT, altezza);
        return mDb.update(dietDB.CLIENT_TABLE, cv, dietDB.COLUMN_CLIENT_USERNAME + "= \"" + cliente.getUsername() + "\"", null)>0;
    }

    public boolean modificaDietologo(String username, String password, String email, String nome, String cognome, String sesso, int eta, String studio){
        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dietDB.COLUMN_DIETOLOGIST_PASSWORD, password);
        cv.put(dietDB.COLUMN_DIETOLOGIST_MAIL, email);
        cv.put(dietDB.COLUMN_DIETOLOGIST_NAME, nome);
        cv.put(dietDB.COLUMN_DIETOLOGIST_SURNAME, cognome);
        cv.put(dietDB.COLUMN_DIETOLOGIST_GENDER, sesso);
        cv.put(dietDB.COLUMN_DIETOLOGIST_AGE, eta);
        cv.put(dietDB.COLUMN_DIETOLOGIST_STUDIO, studio);


        return mDb.update(dietDB.DIETOLOGIST_TABLE, cv, dietDB.COLUMN_DIETOLOGIST_USERNAME + "= \'" + username + "\'", null)>0;
    }

    public boolean modificaCoach(String username, String password, String email, String nome, String cognome, String sesso, int eta, String IP){
        SQLiteDatabase mDb= dietDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dietDB.COLUMN_COACH_PASSWORD, password);
        cv.put(dietDB.COLUMN_COACH_MAIL, email);
        cv.put(dietDB.COLUMN_COACH_NAME, nome);
        cv.put(dietDB.COLUMN_COACH_SURNAME, cognome);
        cv.put(dietDB.COLUMN_COACH_GENDER, sesso);
        cv.put(dietDB.COLUMN_COACH_AGE, eta);
        cv.put(dietDB.COLUMN_COACH_GYM, IP);


        return mDb.update(dietDB.COACH_TABLE, cv, dietDB.COLUMN_COACH_USERNAME + "= \'" + username + "\'", null)>0;
    }


}