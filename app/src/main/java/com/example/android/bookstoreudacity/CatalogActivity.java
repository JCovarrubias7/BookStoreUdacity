package com.example.android.bookstoreudacity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.bookstoreudacity.data.ProductContract.ProductEntry;
import com.example.android.bookstoreudacity.data.ProductDbHelper;

// Icon used on FAB used from www.flaticon.com
// Direct Link https://www.flaticon.com/free-icon/book-and-plus-sign_14037

public class CatalogActivity extends AppCompatActivity {

    private ProductDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new ProductDbHelper(this);
    }

    /**
     * When you come back from adding a new product, reload the Activity
     */
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Method to display information in the onscreen TextView about the state of the bookstore
     * database.
     */
    private void displayDatabaseInfo() {
        // Open database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Set up projection. In this case, we want them all.
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductEntry.COLUMN_SUPPLIER_PHONE
        };
        // Set the Cursor aka the query.
        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        // Find the view in activity_catalog so we can display the Database
        TextView displayView = (TextView) findViewById(R.id.text_view_product);

        try {
            // Set up strings for header
            String mDisplayViewTableContains = getResources().getString(R.string.display_view_table_contains);
            String mContainsProducts = getResources().getString(R.string.contains_products);
            String displayViewHeader = mDisplayViewTableContains + cursor.getCount() + mContainsProducts;
            String mDashDivider = getResources().getString(R.string.dash_divider);
            // Create a header that looks like this
            // The products table contains <number of rows in Cursor> products.
            //_id - product_name - price - quantity - supplier_name - supplier_phone
            displayView.setText(displayViewHeader);
            displayView.append(ProductEntry._ID + mDashDivider +
                    ProductEntry.COLUMN_PRODUCT_NAME + mDashDivider +
                    ProductEntry.COLUMN_PRODUCT_PRICE + mDashDivider +
                    ProductEntry.COLUMN_PRODUCT_QUANTITY + mDashDivider +
                    ProductEntry.COLUMN_SUPPLIER_NAME + mDashDivider +
                    ProductEntry.COLUMN_SUPPLIER_PHONE + " \n"
            );

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int productNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int productPrizeColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int productQuantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use the index to extract the String or Int value of the word
                // at the current row the cursor is on.
                // Since it starts in row one, it will go through the columns and get
                // the value at each index. row 1 column 1-5, then row 2 column 1-5 etc...
                int currentId = cursor.getInt(idColumnIndex);
                String currentProductName = cursor.getString(productNameColumnIndex);
                double currentProductPrize = cursor.getDouble(productPrizeColumnIndex);
                int currentProductQuantity = cursor.getInt(productQuantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentId + mDashDivider +
                        currentProductName + mDashDivider +
                        currentProductPrize + mDashDivider +
                        currentProductQuantity + mDashDivider +
                        currentSupplierName + mDashDivider +
                        currentSupplierPhone));
            }
        } finally {
            // Always close the cursor when you're done reading from it. this releases all its
            // resources and make it invalid.
            cursor.close();
        }
    }


    /**
     * Test Data input
     */
    private void insertTestProduct() {
        // Strings for insertTestProduct method
        String mTestName = getResources().getString(R.string.test_name);
        String mTestPrice = getResources().getString(R.string.test_price);
        String mTestQuantity = getResources().getString(R.string.test_quantity);
        String mTestSupplierName = getResources().getString(R.string.test_supplier_name);
        String mTestSupplierPhone = getResources().getString(R.string.test_supplier_phone);

        // Get the database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new set of ContentValues, this equals one entry
        ContentValues values = new ContentValues();

        // Add Values to values
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, mTestName);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, mTestPrice);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, mTestQuantity);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, mTestSupplierName);
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE, mTestSupplierPhone);

        // Insert the values into the Database
        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        Log.v("CatalogActivity", "New Row ID: " + newRowId);

        // Update displayView after test data has been inserted
        displayDatabaseInfo();
    }

    /**
     * Create the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    /**
     * Create the action after a menu item is clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert test data" menu option
            case R.id.action_inset_test_data:
                insertTestProduct();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
