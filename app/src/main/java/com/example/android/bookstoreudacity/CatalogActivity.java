package com.example.android.bookstoreudacity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.android.bookstoreudacity.data.ProductContract.ProductEntry;
import com.example.android.bookstoreudacity.data.ProductCursorAdapter;

// Icon used on FAB used from www.flaticon.com
// Direct Link https://www.flaticon.com/free-icon/book-and-plus-sign_14037

// Icon used for empty view is from https://dumielauxepices.net/wallpaper-137129
// There is no creator to credit so it goes to the site hosting the image.

public class CatalogActivity extends AppCompatActivity {

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

        // Find the ListView which will be populated with the product data
        ListView productListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);
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
        // Set up projection. In this case, we want them all.
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductEntry.COLUMN_SUPPLIER_PHONE
        };

        // Perform a query on the provider using the ContentResolver.
        // Use the {@link ProductEntry#CONTENT_URI} to access the product data.
        Cursor cursor = getContentResolver().query(
                ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        // Find the ListView which will be populated with the product data
        ListView productListView = (ListView) findViewById(R.id.list);

        // Setup an Adapter to create a list item for each row of product data in the Cursor.
        ProductCursorAdapter adapter = new ProductCursorAdapter(this, cursor);

        // Attach the adapter to the ListView
        productListView.setAdapter(adapter);
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

        // Create a new set of ContentValues, this equals one entry
        ContentValues values = new ContentValues();

        // Add Values to values
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, mTestName);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, mTestPrice);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, mTestQuantity);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, mTestSupplierName);
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE, mTestSupplierPhone);

        // Insert a new row for test product into the provider using the ContentResolver.
        // Use the {@link ProductEntry#CONTENT_URI} to indicate that we want to insert
        // into the products database table.
        // Receive the new content URI that will allow us to access "The Client's" data in the future.
        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

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