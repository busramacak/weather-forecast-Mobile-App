package com.example.havadurumu;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public EditText sehirGirdi;
    public TextView sehirAd;
    public TextView derece;
    public TextView havaTahmini;
    public FloatingActionButton buton;
    public ImageView durumFoti;
    public String sehirTut;
    public String url = "api.openweathermap.org/data/2.5/weather?q={city name},38000&appid={API key}";
    public String apiKey="b075f81c8b49f9d7dc7f07891a03a9ed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.barcolor));
        actionBar.hide();

        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.status_bar));
        }


        sehirGirdi = (EditText) findViewById(R.id.sehirGirdi);
        sehirAd = (TextView) findViewById(R.id.sehirAd);
        derece = (TextView) findViewById(R.id.derece);
        havaTahmini = (TextView) findViewById(R.id.havaTahmini);
        buton = (FloatingActionButton) findViewById(R.id.buton);
        durumFoti = (ImageView) findViewById(R.id.durumFoti);




        buton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                klavyeGizle();
                sehirTut=sehirGirdi.getText().toString();

                sehirGirdi.setText("");

                sehirAd.setText(sehirTut.toUpperCase());
                getData(sehirTut);

            }
        });
    }


    public void getData(String City){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
        .url("https://api.openweathermap.org/data/2.5/weather?q=" + City + ",38000&appid=b075f81c8b49f9d7dc7f07891a03a9ed&units=metric")
        .get().build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "LÜTFEN GEÇERLİ BİR ŞEHİR GİRİNİZ.", Toast.LENGTH_LONG).show();
                }

                String responseData = response.body().string();
                try {
                    JSONObject json = new JSONObject(responseData);
                    JSONArray array = json.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String icons = object.getString("icon");

                    JSONObject temp1 = json.getJSONObject("main");
                    Double Temperature = temp1.getDouble("temp");

                    setText(derece, Temperature.toString());

                    setImage(durumFoti, havaTahmini, icons);

                } catch (JSONException e) { e.printStackTrace(); }
            }
        });
    }


    private void setText(final TextView derece, final String deger){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                derece.setText(deger+"°C");
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void klavyeGizle() {
        //
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void setImage(final ImageView durumFoti, final TextView havaTahmini, final String deger){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                paste switch

                switch (deger){
                    case "01d": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.gunesli));
                        havaTahmini.setText("Güneşli");
                        break;
                    }
                    case "01n": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.gunesli));
                        havaTahmini.setText("Güneşli");
                        break;
                    }
                    case "02d": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.parcalibulut));
                        havaTahmini.setText("Az Parçalı Bulutlu");
                        break;
                    }
                    case "02n": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.parcalibulut));
                        havaTahmini.setText("Az Parçalı Bulutlu");
                        break;
                    }
                    case "03d": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.parcalibulut2));
                        havaTahmini.setText("Çok Parçalı Bulutlu");
                        break;
                    }
                    case "03n": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.parcalibulut2));
                        havaTahmini.setText("Çok Parçalı Bulutlu");
                        break;
                    }
                    case "04d": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.bulut));
                        havaTahmini.setText("Bulutlu");
                        break;
                    }
                    case "04n": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.bulut));
                        havaTahmini.setText("Bulutlu");
                        break;
                    }
                    case "09d": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.yagmur1));
                        havaTahmini.setText("Yağmurlu");
                        break;
                    }
                    case "09n": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.yagmur1));
                        havaTahmini.setText("Yağmurlu");
                        break;
                    }
                    case "10d": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.yagmur2));
                        havaTahmini.setText("Sağanak Yağışlı");
                        break;
                    }
                    case "10n": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.yagmur2));
                        havaTahmini.setText("Sağanak Yağışlı");
                        break;
                    }
                    case "11d": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.simsek));
                        havaTahmini.setText("Gök Gürültülü");
                        break;
                    }
                    case "11n": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.simsek));
                        havaTahmini.setText("Gök Gürültülü");
                        break;
                    }
                    case "13d": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.kar));
                        havaTahmini.setText("Kar Yağışı");
                        break;
                    }
                    case "13n": {
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.kar));
                        havaTahmini.setText("Kar Yağışı");
                        break;
                    }
                    default:
                        durumFoti.setImageDrawable(getResources().getDrawable(R.drawable.cicek));

                }

            }
        });
    }
}