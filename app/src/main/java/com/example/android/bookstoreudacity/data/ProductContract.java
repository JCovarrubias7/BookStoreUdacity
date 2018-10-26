package com.example.android.bookstoreudacity.data;

import android.provider.BaseColumns;

/**
 * Build the table constants. Title and column names.
 */
public final class ProductContract {
    public static abstract class ProductEntry implements BaseColumns {
        public final static String TABLE_NAME = "products";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "product_name";
        public final static String COLUMN_PRODUCT_PRICE = "price";
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_SUPPLIER_PHONE = "supplier_phone";

        /**
         * Price and Quality defaults set at 0
         */
        public final static double DEFAULT_PRICE = 0.00;
        public final static int DEFAULT_QUANTITY = 0;
    }
}
