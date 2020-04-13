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

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AddingDataActivity extends AppCompatActivity {


    private Product product3;
    EditText productName;
    EditText kcal;
    EditText fat;
    EditText carbohydrates;
    EditText proteins;
    EditText link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_data);

        Product product;

        productName = findViewById(R.id.productNameEdit);
        kcal = findViewById(R.id.kcalEdit);
        fat = findViewById(R.id.fatEdit);
        carbohydrates = findViewById(R.id.carbohydratesEdit);
        proteins = findViewById(R.id.proteinEdit);
        link = findViewById(R.id.linkEdit);

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

                product3 = new Product( productName.getText().toString(),
                        kcal.getText().toString(),
                        fat.getText().toString(),
                        carbohydrates.getText().toString(),
                        proteins.getText().toString(),
                        link.getText().toString() );

                Intent resultIntent = new Intent();
                resultIntent.putExtra("product", product3);
                setResult(RESULT_OK, resultIntent);
                finish();

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
            String str="http://poland.openfoodfacts.org/api/v0/product/" + barcode + ".json";

            try {
                json = new JSONObject(IOUtils.toString(new URL(str), StandardCharsets.UTF_8));

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

                    kcal.setText(  nutriments.getString("energy-kcal_value") );
                    fat.setText( nutriments.getString("fat") );
                    carbohydrates.setText( nutriments.getString("carbohydrates") );
                    proteins.setText( nutriments.getString("proteins") );

                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
            }
        }
    }
}
