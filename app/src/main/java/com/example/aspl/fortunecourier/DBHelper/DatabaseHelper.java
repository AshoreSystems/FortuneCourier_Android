package com.example.aspl.fortunecourier.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aspl on 5/2/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "FortuneCouriorApp";


    //*******************************************************************************

    public static final String TABLE_APP_INFO = "AppInfo";

    // Contacts Table Columns names
    public static final String COL_APP_INFO_ID = "Id";
    public static final String COL_APP_INFO_NAME = "Name";
    public static final String COL_APP_INFO_DESCRIPTION = "Description";

    String CREATE_APP_INFO_TABLE = "CREATE TABLE " + TABLE_APP_INFO + "("
            + COL_APP_INFO_ID + " INTEGER PRIMARY KEY," + COL_APP_INFO_NAME + " TEXT,"
            + COL_APP_INFO_DESCRIPTION + " TEXT" + ")";

    //************************************************************************************

    public static final String TABLE_COMMODITY = "Commodity";

    // Contacts Table Columns names
    public static final String COL_ID = "Id";
    public static final String COL_COMMODITY_DESC = "COL_COMMODITY_DESC";
    public static final String COL_UNIT_OF_MEASURES = "COL_UNIT_OF_MEASURES";
    public static final String COL_QUANTITY = "COL_QUANTITY";
    public static final String COL_COMMODITY_WEIGHT = "COL_COMMODITY_WEIGHT";
    public static final String COL_COMMODITY_WEIGHT_UNIT = "COL_COMMODITY_WEIGHT_UNIT";
    public static final String COL_CUSTOMS_VALUE = "COL_CUSTOMS_VALUE";
    public static final String COL_CUSTOMS_VALUE_UNIT = "COL_CUSTOMS_VALUE_UNIT";
    public static final String COL_COUNTRY_OF_MANUFACTURE = "COL_COUNTRY_OF_MANUFACTURE";
    public static final String COL_HARMONIZED_CODE = "COL_HARMONIZED_CODE";
    public static final String COL_EXPORT_LICENSE_NO = "COL_EXPORT_LICENSE_NO";
    public static final String COL_EXPIRATION_DATE = "COL_EXPIRATION_DATE";
    public static final String COL_IS_TOTALS = "COL_IS_TOTALS";


    String CREATE_COMMODITY_TABLE = "CREATE TABLE " + TABLE_COMMODITY + "("
            + COL_ID + " INTEGER PRIMARY KEY," + COL_COMMODITY_DESC + " TEXT,"
            + COL_UNIT_OF_MEASURES + " TEXT," + COL_QUANTITY + " TEXT,"
            + COL_COMMODITY_WEIGHT + " TEXT," + COL_COMMODITY_WEIGHT_UNIT + " TEXT,"
            + COL_CUSTOMS_VALUE + " TEXT," + COL_CUSTOMS_VALUE_UNIT + " TEXT,"
            + COL_COUNTRY_OF_MANUFACTURE + " TEXT," + COL_HARMONIZED_CODE + " TEXT,"
            + COL_EXPORT_LICENSE_NO + " TEXT," + COL_IS_TOTALS + " TEXT,"
            + COL_EXPIRATION_DATE + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_APP_INFO_TABLE);
        db.execSQL(CREATE_COMMODITY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase OpenWritable() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db;
    }

    public SQLiteDatabase OpenReadable() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db;
    }
}
