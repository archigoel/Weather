package com.app.weatherforecast;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    private String TAG = WeatherActivity.class.getSimpleName();
    ImageView search;
    EditText editText;
    TextView temperature;
    TextView currentRegion;
    TextView currentTimeText;
    TextView map;
    ImageView weather_icon;
    private ProgressDialog pDialog;
    private String url;
    String time;
    String name;
    String region;
    String lat;
    String lon;
    String image;
    String temp_c;
    String location;
    String s;
    String celsius;
    String icon;

    // URL to get contacts JSON
//    private static String web_url = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String web_url = "https://api.apixu.com/v1/current.json?key=e504781461c94cd9ae0214227170203&q=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        search = (ImageView)findViewById(R.id.search_icon);
        editText = (EditText) findViewById(R.id.autocomplete_country);
        temperature = (TextView) findViewById(R.id.city_temperature);
        currentRegion = (TextView) findViewById(R.id.region);
        currentTimeText = (TextView)findViewById(R.id.current_time);
        map = (TextView)findViewById(R.id.map);
        weather_icon = (ImageView)findViewById(R.id.weather_icon1);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editText.getText().toString();
//                url = web_url + name + "&appid=75ca67d363eb00d26e7bcc64d6deccd4";
                map.setText("Map Directions");
                url = web_url + name;
                url = url.replaceAll(" ", "%20");

                new WeatherInfo().execute();
                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri location = Uri.parse("geo:0,0?q=" +name +"," +region);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                        startActivity(mapIntent);
                    }
                });
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);


            }
        });
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class WeatherInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(WeatherActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);


            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    String name = jsonObj.getString("cod");
//                    JSONObject temp = jsonObj.getJSONObject("main");
//                    JSONArray weather_icon = jsonObj.getJSONArray("weather");
//                    JSONObject icon = weather_icon.getJSONObject(0);
//                    i = icon.getString("icon");
//                    DateFormat df = DateFormat.getDateTimeInstance();
//                    currentTime = df.format(new Date(jsonObj.getLong("dt") * 1000));
//                    float kelvin = Float.parseFloat(t);
//
//                    float celsius =  kelvin - 273.15F;
//                    int roundOff = Math.round(celsius);
//                    s = Integer.toString(roundOff);
                    JSONObject temp = jsonObj.getJSONObject("location");
                    name = temp.getString("name");
                    region = temp.getString("region");
                    lat = temp.getString("lat");
                    lon = temp.getString("lon");
                    time = temp.getString("localtime");
                    JSONObject current = jsonObj.getJSONObject("current");
                    temp_c = current.getString("temp_c");
                    JSONObject condition = current.getJSONObject("condition");
                    icon = condition.getString("icon");
                    location = name + " , " + region ;

                    Log.d(TAG, location);
                    Log.d(TAG, temp_c);

                } catch (final JSONException e) {
                    startHomeActivity();
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Sorry no data found: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }

                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }

                });




            }return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
//            ListAdapter adapter = new SimpleAdapter(Main2Activity.this, weatherInfo, R.layout.list_item, new String[]{"name"},
//                    new int[]{R.id.name});

            celsius = temp_c + (char) 0x00B0 + "C";
            if (temp_c!= null)
            temperature.setText(celsius);
            currentRegion.setText(location);
            String image = "http:" + icon;
//            String imageUri = "http://openweathermap.org/img/w/" +i + ".png";
            Picasso.with(WeatherActivity.this).load(image).into(weather_icon);

            currentTimeText.setText(time);

            }


        }
    private void startHomeActivity() {

            WeatherActivity.this.finish();
            Intent mIntent = new Intent(WeatherActivity.this, WeatherActivity.class);
            startActivity(mIntent);
    }

}
