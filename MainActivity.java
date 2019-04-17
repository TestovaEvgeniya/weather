package com.example.list;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import com.example.list.DBHelper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    Spinner spinner;
    TextView tvTemp;
    TextView tvSunrise;
    TextView tvSunset;
    TextView tvDay;
    TextView tvHumidity;
    TextView tvPressure;
    SQLiteDatabase db;
    DBHelper dbHelper;
    String city;
    EditText editText;
    ArrayAdapter adapter;
    ArrayList<String> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTemp = findViewById(R.id.temp);
        tvSunset = findViewById(R.id.sunset);
        tvSunrise = findViewById(R.id.sunrise);
        tvDay = findViewById(R.id.day);
        tvHumidity = findViewById(R.id.humidity);
        tvPressure = findViewById(R.id.pressue);
        editText = findViewById(R.id.editText);


        spinner = findViewById(R.id.spinner);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT * FROM cities;", null);
        while(data.moveToNext()){
            listData.add(data.getString(1));
        }
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listData);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                city = (String)parent.getItemAtPosition(position);
                //Toast.makeText(getBaseContext(), "City = " + selection, Toast.LENGTH_SHORT).show();
                GetWeatherTask task = new GetWeatherTask();
                task.execute();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void onAdd(View view) {

        Random random = new Random();
        String addCity =editText.getText().toString();
        Boolean kek=dbHelper.addData(random.nextInt(), addCity,"RU");
        Log.d("kek", "addData: Adding " + kek);
        listData.add(addCity);
         adapter.notifyDataSetChanged();
        //spinner.setAdapter(adapter);

    }


    class GetWeatherTask extends AsyncTask<Integer, Void, OpenWeatherMap> {
        String APPID = "d6843ab8ee963f5d372296dfff62aed7";

        @Override
        protected OpenWeatherMap doInBackground(Integer... integers) {
            String result = "";
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + APPID);
                Scanner in = new Scanner((InputStream) url.getContent());

                while (in.hasNext()) {
                    result += in.nextLine();
                }
                Gson gson = new Gson();
                if (result.length() > 0) {
                    Log.d("mytag", result);
                    OpenWeatherMap owm = gson.fromJson(result, OpenWeatherMap.class);
                    return owm;
                }
            } catch (IOException e) {
            }
            return new OpenWeatherMap();
        }

        @Override
        protected void onPostExecute(OpenWeatherMap openWeatherMap) {
            try {
                float temp = openWeatherMap.main.temp;
                float humidity = openWeatherMap.main.humidity;
                float pressure = openWeatherMap.main.pressure;
                int sunrise = openWeatherMap.sys.sunrise;
                int sunset = openWeatherMap.sys.sunset;
                int day_in_seconds = sunset - sunrise;
                String pattern = "HH:mm:ss";
                DateFormat df = new SimpleDateFormat(pattern);

                String ssunset = df.format(new Date(sunset));
                String ssunrise = df.format(new Date(sunrise));

                tvTemp.setText("Temp: " + String.format("%.0f", temp - 273.15) + " °C");
                tvSunrise.setText("Sunrise: " + ssunrise);
                tvSunset.setText("Sunset: " + ssunset);
                tvDay.setText("Day: " + Integer.toString(day_in_seconds / 3600) + "h " + Integer.toString((day_in_seconds % 3600) / 60) + "m");
                tvHumidity.setText("Humidity: " + String.format("%.0f", humidity) + " %");
                tvPressure.setText("Pressure: " + String.format("%.0f", pressure * 0.75006375541921) + " mmHg");
            }catch (Exception e){}

        }
    }
}

class OpenWeatherMap {

    Weather[] weather;
    M main;
    Sys sys;
}

class Weather {
    int id;
    String main, description, icon;
}

class M {
    float temp, temp_min, temp_max, pressure, humidity;
    int  sunset, sunrise;

    @Override
    public String toString() {
        return "M{" +
                "temp=" + temp +
                ", temp_min=" + temp_min +
                ", temp_max=" + temp_max +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                '}';
    }
}

class Sys {
    float message;
    int id, type, sunset, sunrise;
    String country;

    @Override
    public String toString() {
        return "Sys{" +
                "type=" + type +
                ", id=" + id +
                ", message=" + message +
                ", country=" + country +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }
}