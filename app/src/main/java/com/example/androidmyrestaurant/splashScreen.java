package com.example.androidmyrestaurant;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidmyrestaurant.Common.Common;
import com.example.androidmyrestaurant.Retrofit.IMyRestaurantAPI;
import com.example.androidmyrestaurant.Retrofit.RetrofitClient;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class splashScreen extends AppCompatActivity {
    IMyRestaurantAPI myRestaurantAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AlertDialog dialog;
    public static final String idface="2739799736047038";

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


    //@RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        init();

       // printKeyHash();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                            @Override
                            public void onSuccess(Account account) {
                                dialog.show();
                                compositeDisposable.add(myRestaurantAPI.getUser(Common.API_KEY,account.getId())

                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(userModel -> {

                                                    if (userModel.isSuccess()){// si user esta disponible in la base de datos
                                                        Common.currentUser = userModel.getResult().get(0);
                                                        Intent intent = new Intent(splashScreen.this,HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }else {// si el user no esta disponible en la base de datos, iniciar MainActivity en Register
                                                        Intent intent = new Intent(splashScreen.this,UpdateInfoActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                    dialog.dismiss();
                                                },
                                                throwable -> {
                                                    dialog.dismiss();
                                                    Toast.makeText(splashScreen.this, "[GET USER API]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                }));


                                Log.d("KEY_HASH", account.getId());
                            }

                            @Override
                            public void onError(AccountKitError accountKitError) {
                                Toast.makeText(splashScreen.this, "¡no inicies sesión! Por favor, registrese", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(splashScreen.this, MainActivity.class));
                                finish();
                            }
                        });


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(splashScreen.this, "Usted puede aceptar los parmisos para usar la aplicacion", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();



       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splashScreen.this, MainActivity.class));
                finish();
            }
        }, 3000);*/
    }

    private void init() {
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        myRestaurantAPI = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(IMyRestaurantAPI.class);

    }

    // @RequiresApi(api = Build.VERSION_CODES.P)
//   private void printKeyHash() {
//        try {
//            PackageInfo info= getPackageManager().getPackageInfo(getPackageName(),
//                    PackageManager.GET_SIGNING_CERTIFICATES);
//
//            for (Signature signature:info.signatures)
//            {
//                MessageDigest md= MessageDigest.getInstance("SHA");
//
//                md.update(signature.toByteArray());
//                Log.d("KEY_HASH", Base64.encodeToString(md.digest(),Base64.DEFAULT));
//            }
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }
}
