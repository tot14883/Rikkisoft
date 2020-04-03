package com.rikkeisoft.rikkonbi.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClass {
    private static Retrofit retrofit = null;
    private static Gson gson = null;

    public static synchronized Retrofit getClient(String base_url){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(5, TimeUnit.MINUTES).connectTimeout(5, TimeUnit.MINUTES).writeTimeout(5, TimeUnit.MINUTES);

        httpClient.addInterceptor(logging);


        if (gson == null) {
            gson = new GsonBuilder().setLenient().create();
        }

        if(retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .client(httpClient.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
