package com.example.nutritionscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.this_toolbar);
        setSupportActionBar(toolbar);

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

                TextView textView = findViewById(R.id.textView2);
                textView.setText(contents);

                Intent intent = new Intent(getApplicationContext(), AddingDataActivity.class );
                intent.putExtra("barcode", contents); // "PRODUCT_MODE for bar codes

                startActivityForResult( intent, 2);

            }
            if (resultCode == RESULT_CANCELED) {
            }

        }

        if( requestCode == 2 ){

            if(resultCode == RESULT_OK){


                Product product = (Product) data.getSerializableExtra( "product");
               // String get = data.getStringExtra("product");

                TextView text = findViewById(R.id.textView2);
                text.setText( product.getCarbohydrates() );
            }
        }
    }




}







