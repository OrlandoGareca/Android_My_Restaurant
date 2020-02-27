package com.example.androidmyrestaurant.Retrofit;

import com.example.androidmyrestaurant.Model.FoodModel;
import com.example.androidmyrestaurant.Model.MenuModel;
import com.example.androidmyrestaurant.Model.RestaurantModel;
import com.example.androidmyrestaurant.Model.UpdateUserModel;
import com.example.androidmyrestaurant.Model.UserModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IMyRestaurantAPI {

    //
    //@POST("register")
   // @FormUrlEncoded
//   Observable<String> registerUser(@Field("key") String apiKey,
//                                    @Field("UserPhone") String UserPhone,
//                                    @Field("Name") String Name,
//                                    @Field("email") String email,
//                                    @Field("password") String paswword,
//                                    @Field("Address") String Address);
//   //
    //login
    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                 @Field("password") String password);
    //
    @GET("user")
    Observable<UserModel>getUser(@Query("key") String apiKey,
                                 @Query("fbid") String fbid);

    @GET("restaurant")
    Observable<RestaurantModel> getRestaurant(@Query("key") String apiKey
                                              );

    @GET("menu")
    Observable<MenuModel> getCategories(@Query("key") String apiKey,
                                        @Query("restaurantId") int restaurantId);

    @GET("food")
    Observable<FoodModel> getFoodOfMenu (@Query("key") String apiKey,
                                         @Query("menuId") int menuId);

    //POST
    @POST("user")
    @FormUrlEncoded
    Observable<UpdateUserModel> updateUserInfo(@Field("key") String apiKey,
                                               @Field("userPhone") String userPhone,
                                               @Field("userName") String userName,
                                               @Field("userAddres") String userAddress,
                                               @Field("fbid") String fbid);


}
