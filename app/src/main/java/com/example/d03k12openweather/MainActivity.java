package com.example.d03k12openweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText edtCity;
    private ImageView imgCity;
    private TextView tvTemp;

    // API KEY OPEN WEATHER
    private final static String API_KEY = "xxxxxxxxxxx";

    // Request
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtCity = findViewById(R.id.edtCity);
        tvTemp = findViewById(R.id.tvCityTemp);
        imgCity = findViewById(R.id.imgCityWeather);
        // Init request Volley
        requestQueue = Volley.newRequestQueue(this);
    }

    public String createURL(String city) {
        return String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=vi",
                city, API_KEY);
    }

    public String createIconURL(String icon){
        return String.format("https://openweathermap.org/img/wn/%s@4x.png", icon);
    }


    public void getWeatherByCityName(View view) {
        // Click  vào button sẽ chạy
        String city = edtCity.getText().toString().trim();
        if (city.isEmpty()) {
            edtCity.setError("Hãy nhập thành phố!");
            return;
        }
        // Tạo ra URL
        String myURL = createURL(city);
        Log.d("myURL", myURL);

        // Tạo request lên Openweather
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       // Bóc tách dữ liệu và đổ ra view
                        Toast.makeText(MainActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                        try {
                            String description = response.getJSONArray("weather")
                                    .getJSONObject(0).getString("description");

                            String icon = response.getJSONArray("weather")
                                    .getJSONObject(0).getString("icon");

                            String iconURL = createIconURL(icon);
                            Glide.with(MainActivity.this).load(iconURL).into(imgCity);

                            String temp = response.getJSONObject("main").getString("temp");

                            tvTemp.setText(temp+" ℃");

                            String humidity = response.getJSONObject("main").getString("humidity");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Không có dữ liệu",Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);

    }
}