package com.example.nutritionscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;

import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Toolbar toolbar;
    DBHelper mDBHelper;
    SQLiteDatabase mBD;
    ListView lv;
    Cursor kursor;


    SimpleCursorAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.this_toolbar);
        setSupportActionBar(toolbar);

        lv = findViewById(R.id.lista);

        mDBHelper = new DBHelper(this);
        mBD = mDBHelper.getWritableDatabase();
        //mBD.execSQL( DBHelper.DROP_TAB );
        //mBD.execSQL( DBHelper.CREATE);

        kursor = mBD.query(true, DBHelper.TABLE,
                new String[]{DBHelper.ID , DBHelper.COL2  ,DBHelper.COL3},
               null, null, null,null,null,null );

        startLoader();

        lv.setClickable(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product product;
                //Cursor cursor = mBD.rawQuery(DBHelper.SELECT_ROW, new String[] { String.valueOf( position ) } );
                Cursor cursor2 =  mBD.rawQuery("select * from " + DBHelper.TABLE + " where " + DBHelper.ID + "='" + String.valueOf(position + 1) + "'" , null);

                if (cursor2.moveToFirst()){

                    String p1 = cursor2.getString(cursor2.getColumnIndex( DBHelper.COL1 ) );
                    String p2 = cursor2.getString(cursor2.getColumnIndex( DBHelper.COL2 ) );
                    String p3 = cursor2.getString(cursor2.getColumnIndex( DBHelper.COL3 ) );
                    String p4 = cursor2.getString(cursor2.getColumnIndex( DBHelper.COL4 ) );
                    String p5 = cursor2.getString(cursor2.getColumnIndex( DBHelper.COL5 ) );
                    String p6 = cursor2.getString(cursor2.getColumnIndex( DBHelper.COL6 ) );

                    product = new Product(p1,p2,p3,p4,p5,p6);
                } else {

                    product = new Product();
                }


                Intent intent = new Intent(getApplicationContext(), AddingDataActivity.class);
                intent.putExtra("barcode", "");
                intent.putExtra("product", product); // "PRODUCT_MODE for bar codes
                startActivityForResult(intent, 3);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
                startActivityForResult(intent, 0);
                return true;

            case R.id.item2:
                Intent intent2 = new Intent(getApplicationContext(), AddingDataActivity.class);
                intent2.putExtra("listElement", "");
                intent2.putExtra("barcode", ""); // "PRODUCT_MODE for bar codes
                startActivityForResult(intent2, 2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {

                String contents = data.getStringExtra("SCAN_RESULT");

                Intent intent = new Intent(getApplicationContext(), AddingDataActivity.class );
                intent.putExtra("barcode", contents); // "PRODUCT_MODE for bar codes
                intent.putExtra("listElement", "");
                startActivityForResult( intent, 2);

            }
            if (resultCode == RESULT_CANCELED) {
            }

        }

        if( requestCode == 2 ){

            if(resultCode == RESULT_OK){


                Product product = (Product) data.getSerializableExtra( "product");
                product.putValues();

                long row = mBD.insert(DBHelper.TABLE,null, product.getValue() );

            }
        }
    }

    private void startLoader(){

        //getLoaderManager().initLoader(0,null,getBaseContext());
        String[] mapFrom = new String[]{DBHelper.COL2,DBHelper.COL3};
        int[] mapTo = new int[]{R.id.productName,R.id.productLink};
        dbAdapter = new SimpleCursorAdapter(this, R.layout.list_table,kursor,mapFrom,mapTo, 0);

        lv.setAdapter(dbAdapter);


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        dbAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        dbAdapter.swapCursor(null);
    }
}







