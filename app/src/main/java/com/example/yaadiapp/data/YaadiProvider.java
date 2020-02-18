package com.example.yaadiapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class YaadiProvider extends ContentProvider {

    public static final int YAADI = 1;
    public static final int YAADI_ID = 2;
    YaadiDbHelper dbHelper;
    static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(YaadiContract.AUTHORITY, YaadiContract.YaadiEntry.PATH_YAADI, YAADI);
        matcher.addURI(YaadiContract.AUTHORITY, YaadiContract.YaadiEntry.PATH_YAADI + "/#", YAADI_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new YaadiDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        int matchResult = matcher.match(uri);
        switch (matchResult){
            case YAADI:
                cursor = database.query(YaadiContract.YaadiEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case YAADI_ID:
                String selectionHere = YaadiContract.YaadiEntry._ID + "=?";
                String[] selectionArgsHere= new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(YaadiContract.YaadiEntry.TABLE_NAME,projection,selectionHere, selectionArgsHere,null,null,sortOrder);
                break;

                default:
                    throw new IllegalArgumentException("Unknown URI passed" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), YaadiContract.YaadiEntry.CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (values == null){
            throw new IllegalArgumentException("Content Values passed is null");
        }
        String productName = values.getAsString(YaadiContract.YaadiEntry.COLUMN_PRODUCT_NAME);
        Integer quantity = values.getAsInteger(YaadiContract.YaadiEntry.COLUMN_QUANTITY);
        Integer price = values.getAsInteger(YaadiContract.YaadiEntry.COLUMN_PRICE);
        String supplierName = values.getAsString(YaadiContract.YaadiEntry.COLUMN_SUPPLIER_NAME);
        String supplierEmail = values.getAsString(YaadiContract.YaadiEntry.COLUMN_SUPPLIER_EMAIL);

        if (productName.isEmpty()){
            throw new IllegalArgumentException("Product name cannot be empty");
        }else if (quantity == null){
            throw new IllegalArgumentException("Quantity cannot be empty");
        }else if (price == null){
            throw new IllegalArgumentException("Price cannot be empty");
        }else if (supplierName.isEmpty()){
            throw new IllegalArgumentException("Supplier name cannot be empty");
        }else if (supplierEmail.isEmpty()){
            throw new IllegalArgumentException("Supplier email cannot be empty");
        }

        int matchResult = matcher.match(uri);
        long idForUri;
        if (matchResult == YAADI){
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            //Inset operation on the database.
            idForUri = database.insert(YaadiContract.YaadiEntry.TABLE_NAME, null, values);
            getContext().getContentResolver().notifyChange(YaadiContract.YaadiEntry.CONTENT_URI, null);
            Toast.makeText(getContext(), "Product Added",Toast.LENGTH_SHORT).show();
        }else{
            throw new IllegalArgumentException("Cannot perform INSERT on the uri parsed" + uri);
        }
        return ContentUris.withAppendedId(YaadiContract.YaadiEntry.CONTENT_URI, idForUri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (values == null){
            throw new IllegalArgumentException("Content values passed are NULL");
        }
        String productName = values.getAsString(YaadiContract.YaadiEntry.COLUMN_PRODUCT_NAME);
        Integer quantity = values.getAsInteger(YaadiContract.YaadiEntry.COLUMN_QUANTITY);
        Integer price = values.getAsInteger(YaadiContract.YaadiEntry.COLUMN_PRICE);
        String supplierName = values.getAsString(YaadiContract.YaadiEntry.COLUMN_SUPPLIER_NAME);
        String supplierEmail = values.getAsString(YaadiContract.YaadiEntry.COLUMN_SUPPLIER_EMAIL);

        if (selection == null && selectionArgs == null){
            if (productName.isEmpty()){
                throw new IllegalArgumentException("Product name cannot be empty");
            }else if (quantity == null){
                throw new IllegalArgumentException("Quantity cannot be empty");
            }else if (price == null){
                throw new IllegalArgumentException("Price cannot be empty");
            }else if (supplierName.isEmpty()){
                throw new IllegalArgumentException("Supplier name cannot be empty");
            }else if (supplierEmail.isEmpty()){
                throw new IllegalArgumentException("Supplier email cannot be empty");
            }
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int matchResult = matcher.match(uri);
        switch (matchResult){
            case YAADI:
                Toast.makeText(getContext(), "Cannot perform UPDATE on this URI",Toast.LENGTH_SHORT).show();
                return 0;
            case YAADI_ID:
                String selectionHere = YaadiContract.YaadiEntry._ID + "=?";
                String[] selectionArgsHere = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int rowsAffected = database.update(YaadiContract.YaadiEntry.TABLE_NAME, values, selectionHere, selectionArgsHere);
                getContext().getContentResolver().notifyChange(YaadiContract.YaadiEntry.CONTENT_URI, null);
                return rowsAffected;
            default:
                throw new IllegalArgumentException("Invalid URI passed" + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsAffected;
        int matchResult = matcher.match(uri);
        switch (matchResult){
            case YAADI:
                rowsAffected = database.delete(YaadiContract.YaadiEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case YAADI_ID:
                String selectionHere = YaadiContract.YaadiEntry._ID + "=?";
                String[] selectionArgsHere = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsAffected = database.delete(YaadiContract.YaadiEntry.TABLE_NAME, selectionHere, selectionArgsHere);
                break;
                default:
                    throw new IllegalArgumentException("Invalid URI passed" + uri);
        }
        getContext().getContentResolver().notifyChange(YaadiContract.YaadiEntry.CONTENT_URI, null);
        if (rowsAffected == 1){
            Toast.makeText(getContext(), "Deleted " + rowsAffected + " product",Toast.LENGTH_SHORT).show();
        }else if(rowsAffected == 0 || rowsAffected > 1) {Toast.makeText(getContext(), "Deleted " + rowsAffected + " products",Toast.LENGTH_SHORT).show();}
        return rowsAffected;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int matchResult = matcher.match(uri);
        switch (matchResult){
            case YAADI:
                return YaadiContract.YaadiEntry.CONTENT_LIST_TYPE;
            case YAADI_ID:
                return YaadiContract.YaadiEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalArgumentException("Invalid URI passed" + uri);
        }
    }


}
