package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";
    private final String PUBLIC_KEY = BuildConfig.PUBLIC_KEY;

    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Bitcoin", "onCreate executed");

        mPriceTextView = (TextView) findViewById(R.id.price_textView);
        final Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);

                Log.d("Bitcoin", item + " was selected, executing request");
                executeNetworkCall(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Bitcoin", "Nothing was selected");
            }
        });
    }

    private void executeNetworkCall(final String currency) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-ba-key", PUBLIC_KEY);
        client.get(BASE_URL + currency, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Bitcoin", "JSON response: " + response.toString());
                DataModel data = DataModel.fromJson(response);
                updateUI(data);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.d("Bitcoin", "Request failed, code " + statusCode + ". Response: " + response);
                Log.e("ERROR", e.toString());
                Toast.makeText(MainActivity.this, "Problem with network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(DataModel data) {
        mPriceTextView.setText(String.valueOf(data.getPrice()));
    }
}
