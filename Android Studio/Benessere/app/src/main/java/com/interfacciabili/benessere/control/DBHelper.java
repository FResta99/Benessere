package com.interfacciabili.benessere.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String CLIENT_TABLE = "CLIENT_TABLE";
    public static final String COLUMN_USERNAME = "CLIENT_USERNAME";
    public static final String COLUMN_PASSWORD = "CLIENT_PASSWORD";
    public static final String COLUMN_CLIENT_MAIL = "CLIENT_MAIL";
    public static final String COLUMN_CLIENT_NAME = "CLIENT_NAME";
    public static final String COLUMN_CLIENT_SURNAME = "CLIENT_SURNAME";
    public static final String COLUMN_CLIENT_AGE = "CLIENT_AGE";
    public static final String COLUMN_CLIENT_PROPIC = "PICTURE_URI";
    public static final String COLUMN_CLIENT_GENDER = "CLIENT_SEX";
    public static final String COLUMN_CLIENT_WEIGHT = "CLIENT_WEIGHT";
    public static final String COLUMN_CLIENT_HEIGHT = "CLIENT_HEIGHT";

    public static final String DIETOLOGIST_TABLE = "DIETOLOGIST_TABLE";
    public static final String COLUMN_DIETOLOGIST_USERNAME = "DIETOLOGIST_USERNAME";
    public static final String COLUMN_DIETOLOGIST_PASSWORD = "DIETOLOGIST_PASSWORD";
    public static final String COLUMN_DIETOLOGIST_MAIL = "DIETOLOGIST_MAIL";
    public static final String COLUMN_DIETOLOGIST_NAME = "DIETOLOGIST_NAME";
    public static final String COLUMN_DIETOLOGIST_SURNAME = "DIETOLOGIST_SURNAME";
    public static final String COLUMN_DIETOLOGIST_AGE = "DIETOLOGIST_AGE";
    public static final String COLUMN_DIETOLOGIST_GENDER = "DIETOLOGIST_SEX";
    public static final String COLUMN_DIETOLOGIST_STUDIO = "DIETOLOGIST_STUDIO";

    /*
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
*/
    /*
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
*/
    public static final String DIET_TABLE = "DIET_TABLE";
    public static final String COLUMN_EDIBLE_ID = "EDIBLE_ID";
    public static final String COLUMN_DIET_USERNAME = "DIET_USERNAME";
    public static final String COLUMN_EDIBLE_NAME = "EDIBLE_NAME";
    public static final String COLUMN_EDIBLE_PORTION = "EDIBLE_PORTION";
    public static final String COLUMN_EDIBLE_PORTION_TYPE = "EDIBLE_PORTION_TYPE";
    public static final String COLUMN_EDIBLE_TYPE = "EDIBLE_TYPE";
    public static final String COLUMN_EDIBLE_DAY = "EDIBLE_DAY";

    public static final String REQUEST_DIET_TABLE = "REQUEST_DIET_TABLE";
    public static final String COLUMN_REQUEST_DIET_ID = "REQUEST_ID";
    public static final String COLUMN_REQUEST_DIET_CLIENT = "REQUEST_CLIENT";
    public static final String COLUMN_REQUEST_DIET_DIETOLOGIST = "REQUEST_DIETOLOGIST";
    public static final String COLUMN_REQUEST_DIET_ID_MODIFY = "REQUEST_ID_MODIFY";
    public static final String COLUMN_REQUEST_DIET_MODIFY = "REQUEST_MODIFY";
    public static final String COLUMN_REQUEST_DIET_MODIFIER = "REQUEST_MODIFIER";
    public static final String COLUMN_REQUEST_DIET_PORTION = "REQUEST_PORTION";
    public static final String COLUMN_REQUEST_DIET_PORTION_TYPE = "REQUEST_PORTION_TYPE";
    public static final String COLUMN_REQUEST_DIET_APPROVED = "REQUEST_APPROVED";
    public static final String COLUMN_REQUEST_DIET_CLIENT_NAME = "REQUEST_CLIENT_NAME";
    public static final String COLUMN_REQUEST_DIET_CLIENT_SURNAME = "REQUEST_CLIENT_SURNAME";

    public static final String CLIENT_DIETOLOGIST_TABLE = "CLIENT_DIETOLOGIST_TABLE";
    public static final String COLUMN_CLIENT_USERNAME = "CLIENT_USERNAME";

    public static final String PRODUCT_TABLE = "PRODUCT_TABLE";
    public static final String COLUMN_PRODUCT_ID = "PRODUCT_ID";
    public static final String COLUMN_PRODUCT_NAME = "PRODUCT_NAME";
    public static final String COLUMN_PRODUCT_STATUS = "PRODUCT_STATUS";
    public static final String COLUMN_PRODUCT_CLIENT = "PRODUCT_CLIENT";

    public static final String EQUIP_TABLE = "EQUIP_TABLE";
    public static final String COLUMN_EQUIP_NAME = "EQUIP_NAME";
    public static final String COLUMN_EQUIP_DESCRIPTION = "EQUIP_DESCRIPTION";

    String createEquipmentsTableStatement = "CREATE TABLE " + EQUIP_TABLE + " (" + COLUMN_EQUIP_NAME + " TEXT PRIMARY KEY, " + COLUMN_EQUIP_DESCRIPTION + " TEXT)";

    public static final String COLUMN_DIETOLOGISTCLIENT_USERNAME = "DIETOLOGISTCLIENT_USERNAME";

    public static final String COACH_TABLE = "COACH_TABLE";
    public static final String COLUMN_COACH_USERNAME = "COACH_USERNAME";
    public static final String COLUMN_COACH_PASSWORD = "COACH_PASSWORD";


    public static final String EXERCISE_TABLE = "EXERCISE_TABLE";
    public static final String COLUMN_EXERCISE_ID = "EXERCISE_ID";
    public static final String COLUMN_EXERCISE_USERNAME = "EXERCISE_USERNAME";
    public static final String COLUMN_EXERCISE_NAME = "EXERCISE_NAME";
    public static final String COLUMN_EXERCISE_REPS = "EXERCISE_REPS";
    public static final String COLUMN_EXERCISE_EXPLAINATION = "EXERCISE_EXPLAINATION";
    public static final String COLUMN_EXERCISE_DAY = "EXERCISE_DAY";

    public static final String REQUEST_EXERCISE_TABLE = "REQUEST_EXERCISE_TABLE";
    public static final String COLUMN_REQUEST_EXERCISE_ID = "REQUEST_ID";
    public static final String COLUMN_REQUEST_EXERCISE_CLIENT = "REQUEST_CLIENT";
    public static final String COLUMN_REQUEST_EXERCISE_COACH = "REQUEST_COACH";
    public static final String COLUMN_REQUEST_EXERCISE_ID_MODIFY = "REQUEST_ID_MODIFY";
    public static final String COLUMN_REQUEST_EXERCISE_MODIFY = "REQUEST_MODIFY";
    public static final String COLUMN_REQUEST_EXERCISE_MODIFIER = "REQUEST_MODIFIER";
    public static final String COLUMN_REQUEST_EXERCISE_REPS = "REQUEST_REPS";
    public static final String COLUMN_REQUEST_EXERCISE_APPROVED = "REQUEST_APPROVED";

    public static final String CLIENT_COACH_TABLE = "CLIENT_COACH_TABLE";
    public static final String COLUMN_COACHCLIENT_USERNAME = "COAHCLIENT_USERNAME";



    private static DBHelper dbIstance = null;

    private DBHelper(@Nullable Context context) {
        super(context, "benessere.db", null, 4);
    }

    public static synchronized DBHelper getDbIstance(@Nullable Context context) {
        if (dbIstance == null) {
            dbIstance = new DBHelper(context);
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
