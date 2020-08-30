package com.example.demoaccessories.api;

import androidx.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static String URL = "http://192.168.1.8:3000/";
    private static Retrofit retrofit;
    @NonNull
    public static Retrofit getInstance(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
