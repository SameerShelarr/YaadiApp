package com.example.yaadiapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class YaadiContract {

    private YaadiContract(){}

    //Content authority & base uri
    public static final String AUTHORITY = "com.example.yaadiapp";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final class YaadiEntry implements BaseColumns{

        //Path
        public static final String PATH_YAADI = "yaadi";
        //Content Uri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_YAADI);
        //Table name
        public static final String TABLE_NAME = "yaadi";
        //URI's for the getType method in ContentResolver
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BASE_URI + "/" + PATH_YAADI;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_URI + "/" + PATH_YAADI;

        //Column names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRODUCT_DESCRIPTION = "product_description";
        public static final String COLUMN_PRODUCT_IMAGE = "product_image";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_EMAIL = "supplier_email";

        //Constants for units.
        public static final int UNIT_UNITS = 0;
        public static final int UNIT_KGS = 1;
        public static final int UNIT_BOXES = 2;
        public static final int UNIT_PCS = 3;
        public static final int UNIT_PACKETS = 4;


    }
}
