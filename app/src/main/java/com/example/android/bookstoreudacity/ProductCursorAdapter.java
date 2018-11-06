package com.example.android.bookstoreudacity;

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

import com.example.android.bookstoreudacity.data.ProductContract.ProductEntry;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the products data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // First, find the TextViews via id
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);

        // Next we need product data from the current row on the cursor
        // Find the columns of product attributes that we'ere interested on
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        // Read out the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);

        // Update the TextView with the attributes for the current product
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);

        /**
         * Sale Button logic.
         * This was achieved with help from
         * https://stackoverflow.com/questions/44034208/updating-listview-with-cursoradapter-after-an-onclick-changes-a-value-in-sqlite
         * Post Author: P Fuster
         */
        // Find the saleButton
        Button saleButton = view.findViewById(R.id.sale_button);
        // Get the current ID from the cursor
        int currentId = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
        // Get the Uri with the current ID
        final Uri contentUri = Uri.withAppendedPath(ProductEntry.CONTENT_URI, Integer.toString(currentId));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.valueOf(quantityTextView.getText().toString());

                if (quantity > 0) {
                    quantity--;
                }
                // Show toast if product is at zero so the user is reminded to order more products.
                // Get the right context
                // https://stackoverflow.com/questions/13927601/how-to-show-toast-in-a-class-extended-by-baseadapter-get-view-method
                // Post Author: Andro Selva and edited: Steve Kamau
                if (quantity == 0) {
                    Toast.makeText(v.getContext(), R.string.order_more_products_toast, Toast.LENGTH_SHORT).show();
                }
                // Content Values to update quantity like {@link CatalogActivity} line 110
                ContentValues values = new ContentValues();
                // Add the quantity to the values variable.
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
                // Update the database with the new values from above.
                context.getContentResolver().update(contentUri, values,null,null);
            }
        });
    }
}