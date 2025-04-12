package com.example.maldeeplink;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/* loaded from: classes3.dex */
public class ApiClient {
    private static OkHttpClient instance = null;

    public static OkHttpClient getInstance() {
        if (instance == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            instance = new OkHttpClient.Builder().connectTimeout(30L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS).writeTimeout(30L, TimeUnit.SECONDS).addInterceptor(logging).build();
        }
        return instance;
    }
}