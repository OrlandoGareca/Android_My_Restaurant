package com.example.androidmyrestaurant.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmyrestaurant.Interface.IFoodDetailOrderClickListenner;
import com.example.androidmyrestaurant.Model.Food;
import com.example.androidmyrestaurant.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyFoodAdapter extends RecyclerView.Adapter<MyFoodAdapter.MyViewHolder> {

    Context context;
    List<Food> foodList;

    public MyFoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.layout_foot,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(foodList.get(position).getImage())
                .placeholder(R.drawable.app_icon)
                .into(holder.img_food);
        holder.txt_food_name.setText(foodList.get(position).getName());
        holder.txt_food_price.setText(new StringBuilder(context.getString(R.string.money_sign)).append(foodList.get(position).getPrice()));
        holder.setListenner((view, position1, isDetail) -> {
            if (isDetail)
                Toast.makeText(context, "Detail Click", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "Cart click", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_food)
        ImageView img_food;
        @BindView(R.id.txt_food_name)
        TextView txt_food_name;
        @BindView(R.id.txt_food_price)
        TextView txt_food_price;
        @BindView(R.id.img_detail)
        ImageView img_detail;
        @BindView(R.id.img_cart)
        ImageView img_add_cart;

        IFoodDetailOrderClickListenner listenner;

        public void setListenner(IFoodDetailOrderClickListenner listenner) {
            this.listenner = listenner;
        }

        Unbinder unbinder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            img_detail.setOnClickListener(this);
            img_add_cart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.img_detail)
                listenner.onFoodItemClickListener(v,getAdapterPosition(),true);
            else if (v.getId() == R.id.img_cart)
                listenner.onFoodItemClickListener(v,getAdapterPosition(),false);
        }
    }
}
