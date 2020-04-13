package com.example.nutritionscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class EditingDataActivity extends AppCompatActivity {

    private static final Pattern HTTPS_PATTERN = Pattern.compile("https?://.+");

    EditText usProductName;
    EditText usKcal;
    EditText usFat;
    EditText usCarbohydrates;
    EditText usProteins;
    EditText usLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_data);

        usProductName = findViewById(R.id.productNameEdit);
        usKcal = findViewById(R.id.kcalEdit);
        usFat = findViewById(R.id.fatEdit);
        usCarbohydrates = findViewById(R.id.carbohydratesEdit);
        usProteins = findViewById(R.id.proteinEdit);
        usLink = findViewById(R.id.linkEdit);

        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");
        final String data_id = intent.getStringExtra("id");

        if( product != null ){

            usProductName.setText( product.getProductName() );
            usKcal.setText(product.getKcal());
            usFat.setText(product.getFat());
            usCarbohydrates.setText(product.getCarbohydrates());
            usLink.setText(product.getLink());
            usProteins.setText(product.getProteins());
        }

        Button result = findViewById(R.id.resultIntent);

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String productName = usProductName.getText().toString();
                final String kcal = usKcal.getText().toString();
                final String fat = usFat.getText().toString();
                final String carbohydrates = usCarbohydrates.getText().toString();
                final String proteins = usProteins.getText().toString();
                final String link = usLink.getText().toString();

                if (productName.equals("") || kcal.equals("") || fat.equals("") || carbohydrates.equals("") || proteins.equals("") || link.equals("")) {

                    Toast.makeText(EditingDataActivity.this, "FIELDS CANNOT BE EMPTY", Toast.LENGTH_LONG).show();
                } else if (!HTTPS_PATTERN.matcher(link).matches()) {

                    Toast.makeText(EditingDataActivity.this, "LINK MUST BE FORMATET 'https://...'", Toast.LENGTH_LONG).show();
                } else if (productName.length() < 3) {

                    Toast.makeText(EditingDataActivity.this, "PRODUCT NAME MUST BE AT LEAST 3 CHAR", Toast.LENGTH_LONG).show();
                } else {

                    Product productResult = new Product(productName, kcal, fat, carbohydrates, proteins, link);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("product", productResult);
                    resultIntent.putExtra("id", data_id);
                    setResult(RESULT_OK, resultIntent);
                    finish();

                }
            }
        });

        Button www = findViewById(R.id.www);
        www.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( HTTPS_PATTERN.matcher(usLink.getText().toString() ).matches()){

                    Intent browser = new Intent("android.intent.action.VIEW", Uri.parse(usLink.getText().toString()));
                    startActivity(browser);
                } else {

                    Toast.makeText(EditingDataActivity.this, "LINK MUST BE FORMATET 'https://...'", Toast.LENGTH_LONG).show();
                }
            }
        });


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
