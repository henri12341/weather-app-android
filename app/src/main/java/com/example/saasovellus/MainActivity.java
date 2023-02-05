package com.example.saasovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String requestUrl = "http://api.openweathermap.org/data/2.5/weather?q=Tampere&APPID=APIKEY";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        getWeatherData();
    }

    private void getWeatherData() {
        // Haetaan säädata verkosta JSON -string -> string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                response -> {
                    // Repsonse sisältää response stringin (JSON)
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    parseJsonAndUpdateUi(response);
                },
                error -> {
                    // error sisältää volleylta tulleen mahdollisen virheen
                    Toast.makeText(this, "Verkkovirhe", Toast.LENGTH_LONG).show();
            }
        );
        // Lisätään request jonoon.
        if (requestQueue != null) {
            requestQueue.add(stringRequest);
        }

    }

    private void parseJsonAndUpdateUi(String jsonResponse) {
        // Kaivetaan JSON ja asetetaan se käyttöliittymään
        try {
            JSONObject weatherObject = new JSONObject(jsonResponse);
            JSONObject main = weatherObject.getJSONObject("main");
            double temperature = main.getDouble("temp");
            JSONObject wind = weatherObject.getJSONObject("wind");
            double windSpeed = wind.getDouble("speed");
            JSONArray weatherArray = weatherObject.getJSONArray("weather");
            String description = weatherArray.getJSONObject(0).getString("description");

            // Päivitetään köyttöliittymä

            TextView descriptionTextView = (TextView) findViewById(R.id.textViewDescription);
            descriptionTextView.setText(description);

            TextView temperatureView = (TextView) findViewById(R.id.textViewLampotila);
            temperatureView.setText("" + temperature + " C");

            TextView windView = (TextView) findViewById(R.id.textViewTuuli);
            windView.setText("" + windSpeed + " m/s");

        } catch (JSONException e) {

        }

    }

    public void refreshData(View view) {
        getWeatherData();
    }
}