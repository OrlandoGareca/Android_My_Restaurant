package com.example.androidmyrestaurant.Common;

import com.example.androidmyrestaurant.Model.Restaurant;
import com.example.androidmyrestaurant.Model.User;

public class Common {
    public static final String API_RESTAURANT_ENDPOINT="http://192.168.100.37:3000/";
    public static final String API_KEY="1234";
    public static final int DEFAULT_COLUMN_COUNT = 0 ;
    public static final int FULL_WIDTH_COLUMN = 1 ;
    public static User currentUser;
    public static Restaurant currentRestaurant;
}
