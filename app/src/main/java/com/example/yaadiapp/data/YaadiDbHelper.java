package com.example.yaadiapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class YaadiDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "yaadi.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE "
            + YaadiContract.YaadiEntry.TABLE_NAME
            +"("
            + YaadiContract.YaadiEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + YaadiContract.YaadiEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL,"
            + YaadiContract.YaadiEntry.COLUMN_PRODUCT_DESCRIPTION + " TEXT,"
            + YaadiContract.YaadiEntry.COLUMN_PRODUCT_IMAGE + " BLOB,"
            + YaadiContract.YaadiEntry.COLUMN_QUANTITY + " INTEGER NOT NULL,"
            + YaadiContract.YaadiEntry.COLUMN_PRICE + " INTEGER NOT NULL,"
            + YaadiContract.YaadiEntry.COLUMN_UNIT + " INTEGER DEFAULT 0,"
            + YaadiContract.YaadiEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL,"
            + YaadiContract.YaadiEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL"
            + ");";

    private static final String DELETE_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + YaadiContract.YaadiEntry.TABLE_NAME;

    public YaadiDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_STATEMENT);
        onCreate(db);
    }
}
