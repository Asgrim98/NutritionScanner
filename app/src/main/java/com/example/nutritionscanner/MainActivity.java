package com.example.nutritionscanner;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    Button scan;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan = (Button) findViewById(R.id.qrScan);
        scan.setText("SQ SCAN");

        add = findViewById(R.id.add);
        add.setText("Adding elements");


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
                startActivityForResult(intent, 0);

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddingDataActivity.class);
                intent.putExtra("barcode", ""); // "PRODUCT_MODE for bar codes
                startActivityForResult(intent, 2);

            }
        });

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







