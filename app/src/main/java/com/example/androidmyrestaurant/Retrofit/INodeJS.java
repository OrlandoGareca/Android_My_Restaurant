package com.example.androidmyrestaurant.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("key") String apiKey,
                                    @Field("UserPhone") String UserPhone,
                                    @Field("Name") String Name,
                                    @Field("email") String email,
                                    @Field("password") String paswword,
                                    @Field("Address") String Address);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                 @Field("password") String password);
}
