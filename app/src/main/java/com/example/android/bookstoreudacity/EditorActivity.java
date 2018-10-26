package com.example.android.bookstoreudacity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bookstoreudacity.data.ProductContract.ProductEntry;
import com.example.android.bookstoreudacity.data.ProductDbHelper;

/**
 * Allows the user to create a new pet.
 */
public class EditorActivity extends AppCompatActivity {
    /**
     * EditText field to enter product name
     */
    private EditText mProductNameEditText;

    /**
     * EditText field to enter product price
     */
    private EditText mProductPriceEditText;

    /**
     * EditText field to enter product quantity
     */
    private EditText mProductQuantityEditText;

    /**
     * EditText field to enter the supplier name
     */
    private EditText mSupplierNameEditText;

    /**
     * EditText field to enter the supplier phone number
     */
    private EditText mSupplierPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mProductNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mProductPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mProductQuantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierPhoneNumber = (EditText) findViewById(R.id.edit_supplier_phone);
    }

    private void insertProduct() {
        String productNameString = mProductNameEditText.getText().toString().trim();
        String productPriceString = mProductPriceEditText.getText().toString().trim();
        double productPriceDouble = Double.parseDouble(productPriceString);
        String productQualityString = mProductQuantityEditText.getText().toString().trim();
        int productQualityInt = Integer.parseInt(productQualityString);
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneNumberString = mSupplierPhoneNumber.getText().toString().trim();

        ProductDbHelper mDbHelper = new ProductDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, productNameString);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPriceDouble);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQualityInt);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneNumberString);

        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        Log.v("EditorActivity", "New Row ID: " + newRowId);

        //Toast message
        String mErrorSaving = getResources().getString(R.string.error_saving);
        String mSuccessfulSaving = getResources().getString(R.string.successful_saving);
        String successfulMessage = mSuccessfulSaving + newRowId;
        if (newRowId == -1) {
            Toast.makeText(this, mErrorSaving, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, successfulMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save product to DB
                insertProduct();
                // Exit activity
                finish();
                // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
