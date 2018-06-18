package com.example.thyag.poiwifidata.controller;

import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Looper;
import android.preference.ListPreference;

import com.example.thyag.poiwifidata.model.Thing;
import com.example.thyag.poiwifidata.model.ThingProbability;
import com.example.thyag.poiwifidata.model.WifiData;
import com.example.thyag.poiwifidata.util.Constants;
import com.example.thyag.poiwifidata.util.GsonUTCDateAdapter;
import com.example.thyag.poiwifidata.util.HTTPRequests;
import com.example.thyag.poiwifidata.view.AdapterThingProbability;
import com.example.thyag.poiwifidata.view.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainController {

    public static void requestAlgorithmsList(final ListPreference listPreference) {

        HTTPRequests.post(new JSONObject(), Constants.LIST_CLASSIFIERS, new HTTPRequests.OKHttpNetwork() {
            @Override
            public void onSuccess(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    ArrayList<String> algorithmsList = new Gson().fromJson(jsonObject.get("algorithms").toString(), new TypeToken<ArrayList<String>>() {
                    }.getType());

                    CharSequence[] entries = new CharSequence[algorithmsList.size()];

                    for (int i = 0; i < entries.length; i++) {
                        entries[i] = algorithmsList.get(i);
                    }

                    listPreference.setEntries(entries);
                    listPreference.setEntryValues(entries);
                    listPreference.setValueIndex(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public static void wifiCollector(List<ScanResult> scanResults, int thingID) {

        WifiData wifiData = scanResultToWifiData(scanResults);
        wifiData.setIDThing(thingID);

        try {
            JSONObject dataJSON = new JSONObject();
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("wifiData", new JSONObject(gson.toJson(wifiData).toString()));

            HTTPRequests.post(dataJSON, Constants.WIFI_COLLECTOR, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {
                }

                @Override
                public void onFailure() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void discoverThing(List<ScanResult> scanResults, final AdapterThingProbability adapter) {

        WifiData wifiData = scanResultToWifiData(scanResults);

        try {

            SharedPreferences prefs = MainActivity.getPreferences();

            JSONObject dataJSON = new JSONObject();
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            dataJSON.put("algorithm", prefs.getString("pref_algorithm_list", "J48"));
            dataJSON.put("wifiData", new JSONObject(gson.toJson(wifiData)));

            HTTPRequests.post(dataJSON, Constants.POI_DISCOVER, new HTTPRequests.OKHttpNetwork() {
                @Override
                public void onSuccess(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("thingProbability");

                        final ArrayList<ThingProbability> thingProbabilityList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
                            Thing thing = gson.fromJson(jsonArray.getJSONObject(i).getJSONObject("thing").toString(), Thing.class);
                            double probability = jsonArray.getJSONObject(i).getDouble("probability");

                            thingProbabilityList.add(new ThingProbability(thing, probability));
                        }

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.clear();
                                adapter.addAll(thingProbabilityList);
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure() {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private static WifiData scanResultToWifiData(List<ScanResult> scanResult) {

        WifiData wifiDataList = new WifiData();

        for (ScanResult sr : scanResult) {
            wifiDataList.put(sr.SSID + "&" + sr.BSSID, sr.level);
        }

        return wifiDataList;
    }

}

