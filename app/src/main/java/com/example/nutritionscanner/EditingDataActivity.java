package com.example.nutritionscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditingDataActivity extends AppCompatActivity {

    EditText productName;
    EditText kcal;
    EditText fat;
    EditText carbohydrates;
    EditText proteins;
    EditText link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_data);


        productName = findViewById(R.id.productNameEdit);
        kcal = findViewById(R.id.kcalEdit);
        fat = findViewById(R.id.fatEdit);
        carbohydrates = findViewById(R.id.carbohydratesEdit);
        proteins = findViewById(R.id.proteinEdit);
        link = findViewById(R.id.linkEdit);

        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");
        final String data_id = intent.getStringExtra("id");

        if( product != null ){

            productName.setText( product.getProductName() );
            kcal.setText(product.getKcal());
            fat.setText(product.getFat());
            carbohydrates.setText(product.getCarbohydrates());
            link.setText(product.getLink());
            proteins.setText(product.getProteins());
        }

        Button result = findViewById(R.id.resultIntent);

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Product productResult = new Product( productName.getText().toString(),
                        kcal.getText().toString(),
                        fat.getText().toString(),
                        carbohydrates.getText().toString(),
                        proteins.getText().toString(),
                        link.getText().toString() );

                Intent resultIntent = new Intent();
                resultIntent.putExtra("product", productResult);
                resultIntent.putExtra("id", data_id);
                setResult(RESULT_OK, resultIntent);
                finish();

            }
        });

        Button www = findViewById(R.id.www);



        Button delete = findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("id", data_id);
                setResult(RESULT_FIRST_USER, resultIntent);
                finish();

            }
        });
    }
}
