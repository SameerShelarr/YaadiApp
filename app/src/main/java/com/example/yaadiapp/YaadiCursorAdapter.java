package com.example.yaadiapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yaadiapp.data.YaadiContract;

public class YaadiCursorAdapter extends CursorAdapter {

    public YaadiCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView productNameTextView = view.findViewById(R.id.product_name_text_view);
        TextView productQuantityTextView = view.findViewById(R.id.list_item_quantity_text_view);
        TextView productPriceTextView = view.findViewById(R.id.list_item_price_text_view);
        TextView productUnitTextView = view.findViewById(R.id.list_item_unit_text_view);
        Button saleButton = view.findViewById(R.id.sale_button);

        final int position = cursor.getPosition();
        String name = cursor.getString(cursor.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_PRODUCT_NAME));
        final String quantity = "" + cursor.getInt(cursor.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_QUANTITY));
        String price = "" + cursor.getInt(cursor.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_PRICE));
        int unitInInt = cursor.getInt(cursor.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_UNIT));
        final int id = cursor.getInt(cursor.getColumnIndex(YaadiContract.YaadiEntry._ID));
        String unitToShow = "Units";
        switch (unitInInt){
            case 0:
                unitToShow = "Units";
                break;
            case 1:
                unitToShow = "Kgs";
                break;
            case 2:
                unitToShow = "Boxes";
                break;
            case 3:
                unitToShow = "Pcs";
                break;
            case 4:
                unitToShow = "Packets";
                break;
        }

        productNameTextView.setText(name);
        productQuantityTextView.setText(quantity);
        productUnitTextView.setText(unitToShow);
        productPriceTextView.setText(price);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(position);
                long idHere = cursor.getLong(cursor.getColumnIndex(YaadiContract.YaadiEntry._ID));
                int quantity = cursor.getInt(cursor.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_QUANTITY));
                if (quantity == 0){
                    Toast.makeText(context, "0 Products available", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    quantity = quantity - 1;
                }

                Uri uri = ContentUris.withAppendedId(YaadiContract.YaadiEntry.CONTENT_URI,idHere);

                String where = YaadiContract.YaadiEntry._ID + "=?";
                String[] selectionArgs = new String[]{String.valueOf(idHere)};

                ContentValues values = new ContentValues();
                values.put(YaadiContract.YaadiEntry.COLUMN_QUANTITY, quantity);

                context.getContentResolver().update(uri,values,where,selectionArgs);
            }
        });
    }
}
