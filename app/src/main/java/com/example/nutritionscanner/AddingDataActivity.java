package com.example.nutritionscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class AddingDataActivity extends AppCompatActivity {


    private static final Pattern HTTPS_PATTERN = Pattern.compile("https?://.+");

    private Product product;
    EditText usProductName;
    EditText usKcal;
    EditText usFat;
    EditText usCarbohydrates;
    EditText usProteins;
    EditText usLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_data);

        usProductName = findViewById(R.id.productNameEdit);
        usKcal = findViewById(R.id.kcalEdit);
        usFat = findViewById(R.id.fatEdit);
        usCarbohydrates = findViewById(R.id.carbohydratesEdit);
        usProteins = findViewById(R.id.proteinEdit);
        usLink = findViewById(R.id.linkEdit);

        Intent intent = getIntent();
        final String barcode = intent.getStringExtra("barcode");

        if( !barcode.isEmpty() ){

            GetUrlDataTask task = new GetUrlDataTask(barcode);
            task.execute();
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

                    Toast.makeText(AddingDataActivity.this, "FIELDS CANNOT BE EMPTY",Toast.LENGTH_LONG).show();
                } else if ( !HTTPS_PATTERN.matcher(link).matches() ) {

                    Toast.makeText(AddingDataActivity.this, "LINK MUST BE FORMATET 'https://...'",Toast.LENGTH_LONG).show();
                } else if ( productName.length() < 3) {

                    Toast.makeText(AddingDataActivity.this, "PRODUCT NAME MUST BE AT LEAST 3 CHAR",Toast.LENGTH_LONG).show();
                } else {

                    product = new Product( productName, kcal, fat, carbohydrates, proteins, link );

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("product", product);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    protected class GetUrlDataTask extends AsyncTask<Void, Void, JSONObject>
    {
        private String barcode;

        public GetUrlDataTask(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected JSONObject doInBackground(Void... params)
        {
            JSONObject json = null;
            String str="http://poland.openfoodfacts.org/api/v0/product/" + barcode + ".json";   ///Korzystam z zewnętrznego darmowego APi
            //Niestety nie wszystkie produkty są uzględnione

            try {
                json = new JSONObject(IOUtils.toString(new URL(str), StandardCharsets.UTF_8));  ///Dane dostarczane są w formacie json

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject response)
        {
            if(response != null)
            {
                try {

                    JSONObject product =  response.getJSONObject( "product" );
                    JSONObject nutriments = product.getJSONObject("nutriments");

                    if( product.getString("product_name_pl").equals("") ){

                        Toast.makeText(AddingDataActivity.this, "PRODUCT NOT FOUND",Toast.LENGTH_LONG).show();
                    } else {

                        usProductName.setText( product.getString("product_name_pl"));

                        if( nutriments.getString("energy-kcal_value").equals("") ){

                            usKcal.setText( "0" );
                            usFat.setText( "0" );
                            usCarbohydrates.setText( "0" );
                            usProteins.setText( "0" );
                        } else {

                            usKcal.setText(  nutriments.getString("energy-kcal_value") );
                            usFat.setText( nutriments.getString("fat") );
                            usCarbohydrates.setText( nutriments.getString("carbohydrates") );
                            usProteins.setText( nutriments.getString("proteins") );
                        }
                    }


                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
            }
        }
    }
}
