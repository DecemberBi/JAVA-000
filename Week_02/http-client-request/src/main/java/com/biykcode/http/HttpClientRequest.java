package com.biykcode.http;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpClientRequest {

    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url("http://localhost:8801/").build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String string = response.body().string();
            System.out.println(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
