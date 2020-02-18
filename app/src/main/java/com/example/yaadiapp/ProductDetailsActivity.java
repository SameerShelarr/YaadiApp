package com.example.yaadiapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.UriMatcher;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.yaadiapp.data.YaadiContract;

import java.io.ByteArrayOutputStream;

public class ProductDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private int mUnit = 0;
    private int PICK_IMAGE_REQUEST = 1;
    public static final int YAADI = 1;
    public static final int YAADI_ID = 2;
    boolean isClicked = false;
    static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(YaadiContract.AUTHORITY, YaadiContract.YaadiEntry.PATH_YAADI, YAADI);
        matcher.addURI(YaadiContract.AUTHORITY, YaadiContract.YaadiEntry.PATH_YAADI + "/#", YAADI_ID);
    }
    Uri uriReceived;
    boolean hasProductChanged = false;
    View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.performClick();
            hasProductChanged = true;
            return false;
        }
    };

    private ImageButton mImageButton;
    private EditText mProductNameEditText;
    private EditText mProductDescriptionEditText;
    private EditText mProductQuantityEditText;
    private EditText mPriceEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierEmailEditText;
    private Button mOrderFromSupplierButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mProductNameEditText = findViewById(R.id.product_name_edit_text);
        mProductDescriptionEditText = findViewById(R.id.product_description_edit_text);
        mImageButton = findViewById(R.id.image_button);
        mProductQuantityEditText = findViewById(R.id.quantity_edit_text);
        mPriceEditText = findViewById(R.id.price_edit_text);
        mSupplierNameEditText = findViewById(R.id.supplier_name_edit_text);
        mSupplierEmailEditText = findViewById(R.id.supplier_email_edit_text);
        Spinner mUnitSpinner = findViewById(R.id.unit_spinner);

        mProductNameEditText.setOnTouchListener(mTouchListener);
        mProductDescriptionEditText.setOnTouchListener(mTouchListener);
        mImageButton.setOnTouchListener(mTouchListener);
        mProductQuantityEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierEmailEditText.setOnTouchListener(mTouchListener);
        mUnitSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();

        mImageButton = findViewById(R.id.image_button);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, PICK_IMAGE_REQUEST);
            }
        });

        uriReceived = getIntent().getData();
        mOrderFromSupplierButton = findViewById(R.id.order_from_supplier_button);
        if (uriReceived == null){
            setTitle("Add Product");
            mOrderFromSupplierButton.setVisibility(View.GONE);
        }else {
            setTitle("Edit Product");
            getLoaderManager().initLoader(1,null,this);
        }

        Button increaseQuantityButton = findViewById(R.id.button_increase_quantity);
        Button decreaseQuantityButton = findViewById(R.id.button_decrease_quantity);

        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductQuantityEditText = findViewById(R.id.quantity_edit_text);
                String testText = mProductQuantityEditText.getText().toString().trim();
                if (testText.equals("")){
                    mProductQuantityEditText.setText("" + 0);
                    return;
                }
                int quantity = Integer.parseInt(mProductQuantityEditText.getText().toString().trim());
                quantity = quantity + 1;
                mProductQuantityEditText.setText(String.valueOf(quantity));
            }
        });
        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductQuantityEditText = findViewById(R.id.quantity_edit_text);
                String testText = mProductQuantityEditText.getText().toString().trim();
                if (testText.equals("")){
                    mProductQuantityEditText.setText("" + 0);
                    return;
                }
                int quantity = Integer.parseInt(mProductQuantityEditText.getText().toString().trim());
                if (quantity == 0){
                    Toast.makeText(ProductDetailsActivity.this, "Minimum value reached",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    quantity = quantity - 1;
                }
                mProductQuantityEditText.setText(String.valueOf(quantity));
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (uriReceived == null){
            MenuItem deleteItem = menu.findItem(R.id.delete);
            deleteItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //for setting up the spinner
    private void setupSpinner(){
        Spinner mUnitSpinner = findViewById(R.id.unit_spinner);
        mUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                switch (selection){
                    case "Kgs":
                        mUnit = YaadiContract.YaadiEntry.UNIT_KGS;
                        break;
                    case "Boxes":
                        mUnit = YaadiContract.YaadiEntry.UNIT_BOXES;
                        break;
                    case "Pcs":
                        mUnit = YaadiContract.YaadiEntry.UNIT_PCS;
                        break;
                    case "Packets":
                        mUnit = YaadiContract.YaadiEntry.UNIT_PACKETS;
                        break;
                    case "Units":
                        mUnit = YaadiContract.YaadiEntry.UNIT_UNITS;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUnit = YaadiContract.YaadiEntry.UNIT_UNITS;
            }
        });
    }

    //For converting image (bitmap) into blob.
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, stream);
            return stream.toByteArray();
        }
        return null;
    }

    //When the camera intent clicks the picture and returns the data to be used.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mImageButton.setImageBitmap(photo);
        }
    }

    @Override
    public void onBackPressed() {
        if (hasProductChanged){
            DialogInterface.OnClickListener discardButtonClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // User clicked "Discard" button, navigate to parent activity.
                            NavUtils.navigateUpFromSameTask(ProductDetailsActivity.this);
                        }
                    };

            // Show a dialog that notifies the user they have unsaved changes
            showUnsavedChangesDialog(discardButtonClickListener);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                if (!isClicked){
                    isClicked = true;
                    insertData();
                    return true;
                }
                isClicked = false;
                return true;

            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;

            case android.R.id.home:
                if (!hasProductChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(ProductDetailsActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertData(){
        mProductNameEditText = findViewById(R.id.product_name_edit_text);
        mProductDescriptionEditText = findViewById(R.id.product_description_edit_text);
        mImageButton = findViewById(R.id.image_button);
        mProductQuantityEditText = findViewById(R.id.quantity_edit_text);
        mPriceEditText = findViewById(R.id.price_edit_text);
        mSupplierNameEditText = findViewById(R.id.supplier_name_edit_text);
        mSupplierEmailEditText = findViewById(R.id.supplier_email_edit_text);

        String productName = mProductNameEditText.getText().toString().trim();
        String productDescription = mProductDescriptionEditText.getText().toString().trim();
        Bitmap bitmap = ((BitmapDrawable)mImageButton.getDrawable()).getBitmap();
        byte[] imageBLOB = getBytesFromBitmap(bitmap);
        Integer quantity;
        if (mProductQuantityEditText.getText().toString().isEmpty()){
            quantity = 0;
        }else {
            quantity = Integer.parseInt(mProductQuantityEditText.getText().toString());
        }
        Integer price;
        if (mPriceEditText.getText().toString().trim().isEmpty()){
            price = 0;
        }else {
            price = Integer.parseInt(mPriceEditText.getText().toString());
        }
        String supplierName = mSupplierNameEditText.getText().toString().trim();
        String supplierEmail = mSupplierEmailEditText.getText().toString().trim();

        if (productName.isEmpty()){
            Toast.makeText(this, "Product name cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantity <= 0 || quantity == null){
            Toast.makeText(this, "Quantity cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (price <= 0 || price == null){
            Toast.makeText(this, "Price cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (supplierName.isEmpty()){
            Toast.makeText(this, "Supplier name cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (supplierEmail.isEmpty()){
            Toast.makeText(this, "Supplier email cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(YaadiContract.YaadiEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(YaadiContract.YaadiEntry.COLUMN_PRODUCT_DESCRIPTION, productDescription);
        values.put(YaadiContract.YaadiEntry.COLUMN_PRODUCT_IMAGE, imageBLOB);
        values.put(YaadiContract.YaadiEntry.COLUMN_QUANTITY, quantity);
        values.put(YaadiContract.YaadiEntry.COLUMN_PRICE, price);
        values.put(YaadiContract.YaadiEntry.COLUMN_UNIT, mUnit);
        values.put(YaadiContract.YaadiEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(YaadiContract.YaadiEntry.COLUMN_SUPPLIER_EMAIL, supplierEmail);

        if (getTitle() == "Add Product"){
            getContentResolver().insert(YaadiContract.YaadiEntry.CONTENT_URI, values);
        }else if (getTitle() == "Edit Product"){
            int affectedRows =  getContentResolver().update(uriReceived,values,null,null);
            Toast.makeText(this,"Updated " + affectedRows + " rows", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void clearAllFields(){
        mProductNameEditText.setText("");
        mProductDescriptionEditText.setText("");
        mImageButton.setImageBitmap(null);
        mProductQuantityEditText.setText(String.valueOf(0));
        mPriceEditText.setText(String.valueOf(0));
        mSupplierNameEditText.setText("");
        mSupplierEmailEditText.setText("");
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_dialog);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                if (uriReceived != null){
                    getContentResolver().delete(uriReceived,null,null);
                    finish();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,uriReceived,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null){
            return;
        }else {
            data.moveToFirst();
        }
        String productName = data.getString(data.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_PRODUCT_NAME));
        String productDescription = data.getString(data.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_PRODUCT_DESCRIPTION));
        byte[] imageInBytes = data.getBlob(data.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_PRODUCT_IMAGE));
        Bitmap imageInBitmap = BitmapFactory.decodeByteArray(imageInBytes,0, imageInBytes.length);
        int quantity = data.getInt(data.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_QUANTITY));
        int price = data.getInt(data.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_PRICE));
        String supplierName = data.getString(data.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_SUPPLIER_NAME));
        String supplierEmail = data.getString(data.getColumnIndex(YaadiContract.YaadiEntry.COLUMN_SUPPLIER_EMAIL));
        data.close();

        mProductNameEditText = findViewById(R.id.product_name_edit_text);
        mProductDescriptionEditText = findViewById(R.id.product_description_edit_text);
        mImageButton = findViewById(R.id.image_button);
        mProductQuantityEditText = findViewById(R.id.quantity_edit_text);
        mPriceEditText = findViewById(R.id.price_edit_text);
        mSupplierNameEditText = findViewById(R.id.supplier_name_edit_text);
        mSupplierEmailEditText = findViewById(R.id.supplier_email_edit_text);

        mProductNameEditText.setText(productName);
        mProductDescriptionEditText.setText(productDescription);
        mImageButton.setImageBitmap(imageInBitmap);
        mProductQuantityEditText.setText(String.valueOf(quantity));
        mPriceEditText.setText(String.valueOf(price));
        mSupplierNameEditText.setText(supplierName);
        mSupplierEmailEditText.setText(supplierEmail);

        mProductNameEditText = findViewById(R.id.product_name_edit_text);
        mSupplierEmailEditText = findViewById(R.id.supplier_email_edit_text);
        mSupplierNameEditText = findViewById(R.id.supplier_name_edit_text);

        Spinner unitSpinner = findViewById(R.id.unit_spinner);
        String unit = unitSpinner.getSelectedItem().toString();
        final String[] email = new String[]{supplierEmail};
        final String subject = "Placement Of Order";
        final String emailBody =
                "Hello " + supplierName + ",\n\n"
                        + "Please accept the order for the following product and deliver as soon as possible.\n"
                        + "Product Name: " + productName + ", Quantity: QTY " + unit
                        +"\n\nThank You.";

        mOrderFromSupplierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, email);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, emailBody);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        clearAllFields();
    }
}
