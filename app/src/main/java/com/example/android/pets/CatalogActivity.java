/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;

import java.net.URI;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PET_LOADER = 0;

    PetCursorAdapter mCursorAdapter;

    private PetDbHelper mDbHelper;

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

        // Find the ListView which will be populated with the pet data
        ListView petListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

//        mDbHelper = new PetDbHelper(this);


        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new PetCursorAdapter(this, null);
        petListView.setAdapter(mCursorAdapter);

        // Setup item click listener
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create a new intent to go to {@link EditorActivity.class}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                // From the content URI that represents the specific pet that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link PetEntry#CONTENT_URI.
                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentPetUri);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
                }
        });

        // Kick off the loader
        getLoaderManager().initLoader(PET_LOADER, null, this);





//        displayDatabaseInfo();            // no longer needed after implementing cursor adapter

//        PetDbHelper mDbHelper = new PetDbHelper(this);
//
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();

    }

//    // no longer needed after implementing cursor loader
//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayDatabaseInfo();
//    }


//    // no longer needed after implementing cursor loader
//    /**
//     * Temporary helper method to display information in the onscreen TextView about the state of
//     * the pets database.
//     */
//    private void displayDatabaseInfo() {
////        // To access our database, we instantiate our subclass of SQLiteOpenHelper
////        // and pass the context, which is the current activity.
////        PetDbHelper mDbHelper = new PetDbHelper(this);
////
////        // Create and/or open a database to read from it
////        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
////        // Perform this raw SQL query "SELECT * FROM pets"
////        // to get a Cursor that contains all rows from the pets table.
////        Cursor cursor = db.rawQuery("SELECT * FROM " + PetContract.PetEntry.TABLE_NAME, null);
//
//        String[] projection = {
//                PetEntry._ID,
//                PetEntry.COLUMN_PET_NAME,
//                PetEntry.COLUMN_PET_BREED,
//                PetEntry.COLUMN_PET_GENDER,
//                PetEntry.COLUMN_PET_WEIGHT
//        };
//
////        Cursor cursor = db.query(
////                PetEntry.TABLE_NAME,
////                projection,
////                null,
////                null,
////                null,
////                null,
////                null
////        );
//
//        // Perform a query on the provider using the ContentResolver.
//        // Use the {@link PetEntry#CONTENT_URI} to access the pet data.
//        Cursor cursor = getContentResolver().query(
//                PetEntry.CONTENT_URI,
//                projection,
//                null,
//                null,
//                null);
//
//        // Find the list view which will be populated with the pet data
//        ListView petListView = (ListView) findViewById(R.id.list);
//
//        // Set up an Adapter to create a list item for each row of pet data in the cursor.
//        PetCursorAdapter adapter = new PetCursorAdapter(this, cursor);
//
//        // Attach the adapter to the ListView
//        petListView.setAdapter(adapter);
//
//
////        // Before using the cursor adapter, we were using the text view instead of list view with this approach
////        TextView displayView = (TextView) findViewById(R.id.text_view_pet);
////
////        try {
////            // Create a header in the Text View that looks like this:
////            //
////            // The pets table contains <number of rows in Cursor> pets.
////            // _id - name - breed - gender - weight
////            //
////            // In the while loop below, iterate through the rows of the cursor and display
////            // the information from each column in this order.
////
////            // Display the number of rows in the Cursor (which reflects the number of rows in the
////            // pets table in the database).
////            displayView.setText("Number of rows in pets database table: " + cursor.getCount() + " pets.\n\n");
////
////            displayView.append(PetEntry._ID + " - " +
////                    PetEntry.COLUMN_PET_NAME + "-" +
////                    PetEntry.COLUMN_PET_BREED + "-" +
////                    PetEntry.COLUMN_PET_GENDER + "-" +
////                    PetEntry.COLUMN_PET_WEIGHT + "\n");
////
////            // Figure out the index of each column
////            int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
////            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
////            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
////            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
////            int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);
////
////            // Iterate through all the returned rows in the cursor
////            while (cursor.moveToNext()) {
////                // Use that index to extract the String or Int value of the word
////                // at the current row the cursor is on.
////                int currentID = cursor.getInt(idColumnIndex);
////                String currentName = cursor.getString(nameColumnIndex);
////                String currentBreed = cursor.getString(breedColumnIndex);
////                String currentGender = cursor.getString(genderColumnIndex);
////                String currentWeight = cursor.getString(weightColumnIndex);
////
////                // Display the values from each column of the current row in the cursor in the TextView
////                displayView.append(("\n" + currentID + " - " +
////                        currentName + " - " +
////                        currentBreed + " - " +
////                        currentGender + " - " +
////                        currentWeight));
////            }
////        } finally {
////            // Always close the cursor when you're done reading from it. This releases all its
////            // resources and makes it invalid.
////            cursor.close();
////        }
//
//
//    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertPet(){
//        // Gets the database in write mode
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();

        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 8);


        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for db.insert() is the pets name.
        // The second argument provides the name of the column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if this is set
        // to "null", then the framework will not insert a row when there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
//        long newRowId = db.insert(PetEntry.TABLE_NAME, null, values);

        Uri newRowUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);


        Log.v("CatalogActivity", "New row Uri " + newRowUri);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
//                displayDatabaseInfo();          // no longer needed after implementing cursor loader
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the tables we care about.
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent Activity Context
                PetEntry.CONTENT_URI,          // Provider Content URI to query
                projection,                    // Columns to include in the resulting cursor
                null,                  // No selection cause
                null,               // No selection arguments
                null);                 // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Update {@link PetCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }


    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_pets);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllPets();
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


    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.catalog_error_with_deleting_all_pets),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.catalog_all_pets_deleted),
                    Toast.LENGTH_SHORT).show();
        }
    }


}
