package com.example.androidmyrestaurant.Adapter;

import com.example.androidmyrestaurant.Model.Restaurant;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class RestaurantSlideradapter extends SliderAdapter {

    List<Restaurant> restaurantList;

    public RestaurantSlideradapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        imageSlideViewHolder.bindImageSlide(restaurantList.get(position).getImage());

    }
}
