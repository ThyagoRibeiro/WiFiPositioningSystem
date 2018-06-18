package com.example.thyag.poiwifidata.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thyag.poiwifidata.R;
import com.example.thyag.poiwifidata.controller.MainController;
import com.example.thyag.poiwifidata.model.ThingProbability;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private static SharedPreferences preferences;
    private static boolean discoverThingActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ListView thingsListView = (ListView) findViewById(R.id.thingsList);
        ArrayList<ThingProbability> thingProbabilityArrayList = new ArrayList<>();

        final AdapterThingProbability adapter = new AdapterThingProbability(thingProbabilityArrayList, this);
        thingsListView.setAdapter(adapter);

        verifyWifi();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (discoverThingActive) {
                            MainController.discoverThing(wifiManager.getScanResults(), adapter);
                            sleep(Integer.parseInt(preferences.getString("preference_interval_time", "10")) * 1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t.start();

        final Button btnDiscoverThing = (Button) findViewById(R.id.buttonDiscoverThing);
        btnDiscoverThing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (discoverThingActive) {
                    discoverThingActive = false;
                    btnDiscoverThing.setText("Ativar Descoberta");
                } else {
                    discoverThingActive = true;
                    btnDiscoverThing.setText("Desativar Descoberta");
                    verifyWifi();
                }
            }
        });

        Button btnCollectWifi = (Button) findViewById(R.id.buttonCollectWifi);
        btnCollectWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(intent, 0);
            }
        });

        //MainController.discoverThing(wifiManager.getScanResults(), adapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                int thingID = Integer.parseInt(data.getStringExtra("SCAN_RESULT"));
                MainController.wifiCollector(wifiManager.getScanResults(), thingID);
            }
        }
    }

    private void verifyWifi() {
        if (wifiManager.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "Ativando wifi...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
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
            Intent preferencesIntent = new Intent(this, SettingsActivity.class);
            startActivity(preferencesIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }
}
