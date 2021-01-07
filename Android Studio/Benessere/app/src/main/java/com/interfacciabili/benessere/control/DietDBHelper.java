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

    private static DietDBHelper dbIstance = null;

    private DietDBHelper(@Nullable Context context) {
        super(context, "benessere.db", null, 4);
    }

    public static synchronized DietDBHelper getDbIstance(@Nullable Context context) {
        if (dbIstance == null) {
            dbIstance = new DietDBHelper(context);
        }
        return dbIstance;
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


}
