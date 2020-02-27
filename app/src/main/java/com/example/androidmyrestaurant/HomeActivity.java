package com.example.androidmyrestaurant;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmyrestaurant.Adapter.MyRestaurantAdapter;
import com.example.androidmyrestaurant.Adapter.RestaurantSlideradapter;
import com.example.androidmyrestaurant.Common.Common;
import com.example.androidmyrestaurant.Model.EventBus.RestaurantLoadEvent;
import com.example.androidmyrestaurant.Model.Restaurant;
import com.example.androidmyrestaurant.Retrofit.IMyRestaurantAPI;
import com.example.androidmyrestaurant.Retrofit.RetrofitClient;
import com.example.androidmyrestaurant.Services.PicassoImageLoadingservice;
import com.facebook.accountkit.AccountKit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ss.com.bannerslider.Slider;

//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;


public class HomeActivity extends AppCompatActivity  {


    TextView txt_user_name,txt_user_phone;

    @BindView(R.id.banner_slider)
    Slider banner_slider;

    @BindView(R.id.recycler_restaurant)
    RecyclerView recycler_restaurant;

    IMyRestaurantAPI myRestaurantAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AlertDialog dialog;

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_nearby, R.id.nav_order_history, R.id.nav_update_info
                , R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.nav_home){


                }
                if (destination.getId() == R.id.nav_nearby){

                }
                if (destination.getId() == R.id.nav_order_history){

                }
                if (destination.getId() == R.id.nav_update_info){

                }
                if (destination.getId() == R.id.nav_send){
                    //Toast.makeText(HomeActivity.this, "cerrar sesion", Toast.LENGTH_SHORT).show();
                    signOut();
                }

            }
        });
        View headerView = navigationView.getHeaderView(0);
        txt_user_name = (TextView)headerView.findViewById(R.id.txt_user_name);
        txt_user_phone = (TextView)headerView.findViewById(R.id.txt_user_phone);

        txt_user_name.setText(Common.currentUser.getName());
        txt_user_phone.setText(Common.currentUser.getUserPhone());


       // ButterKnife.bind(this);
        init();
        initView();
        loadRestaurant();
    }

    private void loadRestaurant() {
        dialog.show();
        compositeDisposable.add(
                myRestaurantAPI.getRestaurant(Common.API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurantModel -> {
                        //event buss
                            EventBus.getDefault().post(new RestaurantLoadEvent(true,restaurantModel.getResult()));
                        },
                        throwable -> EventBus.getDefault().post(new RestaurantLoadEvent(false,throwable.getMessage())))
        );
       // Log.d("KEY_HASH", getRestaurant().getId());
    }

    private void initView() {
        ButterKnife.bind(this);
       // RecyclerView recycler_restaurant = (RecyclerView)findViewById(R.id.recycler_restaurant);
       // LinearLayoutManager layoutManager =new LinearLayoutManager(HomeActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        recycler_restaurant.setLayoutManager(layoutManager);
        recycler_restaurant.addItemDecoration(new DividerItemDecoration(HomeActivity.this,layoutManager.getOrientation()));

        //recycler_restaurant.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));
    }

    private void init() {
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        myRestaurantAPI = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(IMyRestaurantAPI.class);

        Slider.init(new PicassoImageLoadingservice());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }






    private void signOut() {
        AlertDialog confirmDialog = new AlertDialog.Builder(this)
                .setTitle("Cerrar Sesion")
                .setMessage("Esta seguro de Cerrar Sesion?")
                .setNegativeButton("CANCELAR", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("OK", (dialog, which) -> {
                    Common.currentUser = null;
                    Common.currentRestaurant = null;
                    AccountKit.logOut();
                    //FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //eliminar todas las actividades previas
                    startActivity(intent);
                    finish();
                }).create();
        confirmDialog.show();
    }

    /*
    REGISTER EVENT BUS
     */

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    //Listen EventBus
    @Subscribe (threadMode = ThreadMode.MAIN)
    public void processRestaurantLoadEvent(RestaurantLoadEvent event)
    {
        if (event.isSuccess())
        {
            displayBanner(event.getRestaurantList());
            displayRestaurant(event.getRestaurantList());
        }
        else {
            Toast.makeText(this, "[RESTAURANT LOAD]"+event.getMessage(), Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }

    private void displayRestaurant(List<Restaurant> restaurantList) {
        MyRestaurantAdapter adapter = new MyRestaurantAdapter(this,restaurantList);
        recycler_restaurant.setAdapter(adapter);
    }

    private void displayBanner(List<Restaurant> restaurantList){
      //  Slider banner_slider = (Slider) findViewById(R.id.banner_slider);
        banner_slider.setAdapter(new RestaurantSlideradapter(restaurantList));
    }

}
