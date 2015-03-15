package com.hackaton.adim.watchdogskrajina;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends Activity {
    String latitude, longitude;
    String latString,lonString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Acquire a reference to the system Location Manager
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Location lokacija;
        double lat,lon;
        boolean isGPSEnabled;
        boolean isNetworkEnabled;
        boolean LocationAwalible;



        //minimalna distanca prije nego sto GPS sensor radi refresh vrijednosti lat i lon
         final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        //minimalno vrijeme izmedju update-a u milisekundama
        final long MIN_TIME_BW_UPDATES = 1000 * 2 * 1;  //2 sekunde
            // getting GPS status
            isGPSEnabled = lm
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = lm
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                LocationAwalible = true;

            }
            if (isNetworkEnabled) {
                Log.d("Network", "Network");
                if (lm != null) {
                    lokacija = lm
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (lokacija != null) {
                        //String.valueof(tmpInt);
                        lat = lokacija.getLatitude();
                        lon = lokacija.getLongitude();
                        latString=String.valueOf(lat);
                        lonString=String.valueOf(lon);

                        Toast.makeText(getApplicationContext(),latString+"/"+ lonString,Toast.LENGTH_SHORT).show();
                    }
                }
            }

// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {



            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        //incijalizacija i osnovne postavke web browsera
        final WebView web= (WebView) findViewById(R.id.webView);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        web.loadUrl("http://watchdogskrajina.herokuapp.com/mobile/index.html");

        //inicijalizacija i osnovne postavke za button centriraj
        final Button btn_center=(Button)findViewById(R.id.button);
        btn_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String javasc="javascript:centriraj("+latString+","+lonString+")";
                web.evaluateJavascript(javasc, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("LogName", s); // Log is written, but s is always null
                    }
                });
                Toast.makeText(getApplicationContext(),"Centrirano",Toast.LENGTH_SHORT).show();
            }});

        // inicijalizacija i osnovne postavke switch button-a
        final Switch swc_info=(Switch)findViewById(R.id.switch1);
        swc_info.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(swc_info.isChecked())
                {
                    //poziv javascript funkcije za promjenu layouta mape
                    web.evaluateJavascript("javascript:zamijeni();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            Log.d("LogName", s); // Log is written, but s is always null
                        }
                    });

                }
                //poziv javascript funkcije za promjenu layouta mape
                else
                {
                    web.evaluateJavascript("javascript:zamijeni();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            Log.d("LogName", s); // Log is written, but s is always null
                        }});

                }
            }});

        if(swc_info.isChecked()){
            swc_info.setText("Info Map");
        }
        else {
            swc_info.setText("Transport map");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }}
