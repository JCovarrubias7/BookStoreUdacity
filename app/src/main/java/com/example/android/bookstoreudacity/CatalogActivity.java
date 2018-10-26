package com.example.android.bookstoreudacity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.bookstoreudacity.data.ProductContract.ProductEntry;
import com.example.android.bookstoreudacity.data.ProductDbHelper;

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

    private void insertTestProduct() {
        // Get the database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new set of ContentValues, this equals one entry
        ContentValues values = new ContentValues();

        // Add Values to values
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "The Client");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 9.99);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 7);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, "Amazon");
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE, "18882804331");

        // Insert the values into the Database
        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        Log.v("CatalogActivity", "New Row ID: " + newRowId);
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
