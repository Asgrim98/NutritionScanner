package com.example.nutritionscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;


import android.content.ContentUris;
import android.content.Intent;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    ListView lv;
    SimpleCursorAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///Kod wykorzystywany gdy chcemy stwozryc nowa tabele w BD
        //mDBHelper = new DBHelper(this);
        //mBD = mDBHelper.getWritableDatabase();
        //mBD.execSQL( DBHelper.DROP_TAB );
        //mBD.execSQL( DBHelper.CREATE);

        lv = findViewById(R.id.lista);  ///Tworze obiekt list View i dodaje odpowiednie listenery
        startLoader();

        lv.setClickable(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product product;
                String[] projection = { DBHelper.ID, DBHelper.COL1, DBHelper.COL2, DBHelper.COL3, DBHelper.COL4, DBHelper.COL5, DBHelper.COL6 };
                Cursor cursor = getContentResolver().query( Provider.URI_ZAWARTOSCI, projection, null , null,null);

                //Cursor cursor2 =  mBD.rawQuery("select * from " + DBHelper.TABLE, null);

                cursor.moveToFirst();

                for(int i = 0; i < position; i++){  ///Ustawiam kursor na pozycje odpowiadajaca mu na listView
                    cursor.moveToNext();            ///W przypadku gdy usuwamy elementy i uzywamy auto increment do settowania id
                }                                   ///Wartosci te moga sie od siebie roznica np. id = 10 na 3 pozycji na liscie

                ArrayList<String> list = new ArrayList<>();

                for (String  x : projection) {
                    list.add( cursor.getString( cursor.getColumnIndex( x )));
                }
                product = new Product(list);        ///Zapelniamy liste elementami z bazy danych i tworzymy obiekt produkt

                cursor.close();

                Intent intent = new Intent(getApplicationContext(), EditingDataActivity.class); ///Przechodzimy do nowej aktywnosci do ktorej wysylamy obiekt produkt oraz jego id z bd
                intent.putExtra("product", product);
                intent.putExtra("id", list.get(0));
                startActivityForResult(intent, 3);
            }
        });

        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                                          @Override
                                          public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

                                              MenuInflater inflater = actionMode.getMenuInflater(); ///Po wejsciu w tryb multiselect zmieniamy menu w toolbar
                                              inflater.inflate(R.menu.delete_menu, menu);
                                              return true;
                                          }

                                          @Override
                                          public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                                                return false;
                                          }

                                          @Override
                                          public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                                              switch (menuItem.getItemId()) {       ///Dodajemy odpowiednie akcje na wcisniecie przyicsku
                                                  case R.id.multiple_delete:
                                                      deleteSelected();
                                                      return true;
                                              }

                                              return false;
                                          }

                                          @Override
                                          public void onDestroyActionMode(ActionMode actionMode) {

                                          }

                                          @Override
                                          public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                                          }
                                      });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { ///Dodajemy podstawowe menu do toolbara
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   ///Menu sklada sie z dwóch elementów
        //Jednym jest dodanie produktu za posrednictwem Skanu QR a drugie przez podanie dnaych przez uzytkownika
        //W przypadku skanu qr dane mozna pozniej oczywiscie edytowac

        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); //SKAN QR
                startActivityForResult(intent, 0);
                return true;

            case R.id.item2:
                Intent intent2 = new Intent(this, AddingDataActivity.class);
                intent2.putExtra("listElement", "");
                intent2.putExtra("barcode", ""); //ZWYKLE DODANIE
                startActivityForResult(intent2, 2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { ///Reakcja na powrot z actviity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) { ///SKAN QR

            if (resultCode == RESULT_OK) {

                String contents = data.getStringExtra("SCAN_RESULT");

                ///W przypadku pobrania kodu QR produktu przechodzimy do nowego wspolnego ativity gdzie mozemy dodac nowy obiekt do bazy

                Intent intent = new Intent(getApplicationContext(), AddingDataActivity.class );
                intent.putExtra("barcode", contents); // "PRODUCT_MODE for bar codes
                intent.putExtra("listElement", "");
                startActivityForResult( intent, 2);

            }
        }

        if( requestCode == 2 ){ //ZWYKLE DODANIE

            if(resultCode == RESULT_OK) {

                Product product = (Product) data.getSerializableExtra("product");
                product.putValues();

                Uri newUri = getContentResolver().insert( Provider.URI_ZAWARTOSCI, product.getValue() );    ///DODANIE DO BD
            }
        }


        if( requestCode == 3 ){ ///EDYCJA

            if(resultCode == RESULT_OK){ ///ZATWIERDZENIE EDYCJI

                Product product = (Product) data.getSerializableExtra( "product");
                String id = data.getStringExtra("id");

                product.putValues();
                getContentResolver().update( Provider.URI_ZAWARTOSCI, product.getValue(),DBHelper.ID+"=" + id , null );

                //mBD.update(DBHelper.TABLE, product.getValue(), DBHelper.ID+"=" + id, null);
            }

            if(resultCode == RESULT_FIRST_USER){ ///USUNIECIE ELEMENTU

                String id = data.getStringExtra("id");

                getContentResolver().delete( Provider.URI_ZAWARTOSCI, DBHelper.ID+"=" + id, null);

               // mBD.delete(DBHelper.TABLE, DBHelper.ID + " = '" + id + "';", null);

            }
        }
    }

    private void deleteSelected() { ///USUNICIE WIELU ELEMENTOW
        long[] zaznaczone = lv.getCheckedItemIds(); ///Pobieramy liste id zaznaczonych elementow

        for (int i = 0; i < zaznaczone.length; i++) {

            getContentResolver().delete( Provider.URI_ZAWARTOSCI, DBHelper.ID + "=" + zaznaczone[i], null);
        }
    }

    private void startLoader(){

        getSupportLoaderManager().initLoader(0, null, this);

        String[] mapFrom = new String[]{DBHelper.COL1,DBHelper.COL6};
        int[] mapTo = new int[]{R.id.productName,R.id.productLink};
        dbAdapter = new SimpleCursorAdapter(this, R.layout.list_table, null ,mapFrom,mapTo, 0);

        lv.setAdapter(dbAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = { DBHelper.ID, DBHelper.COL1, DBHelper.COL6 };
        CursorLoader loaderKursora = new CursorLoader(this,
                Provider.URI_ZAWARTOSCI, projection, null,null, null);

        return loaderKursora;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        dbAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        dbAdapter.swapCursor(null);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}







