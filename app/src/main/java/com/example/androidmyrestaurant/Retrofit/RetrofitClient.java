package com.example.androidmyrestaurant.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.androidmyrestaurant.Common.Common.API_RESTAURANT_ENDPOINT;

public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstance(){
        if (instance == null)
            instance = new Retrofit.Builder()
                    .baseUrl(API_RESTAURANT_ENDPOINT)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }
}
