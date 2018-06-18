package com.example.thyag.poiwifidata.util;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPRequests extends AsyncTask<String, Void, String>{

    public interface OKHttpNetwork {
        void onSuccess(String body);
        void onFailure();
    }

    public static void post(JSONObject object, String url, final OKHttpNetwork okHttpCallBack) {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();

        RequestBody body = RequestBody.create(MEDIA_TYPE, object.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                okHttpCallBack.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                okHttpCallBack.onSuccess(response.body().string());
            }
        });
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
