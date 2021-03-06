package com.example.androidmyrestaurant;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidmyrestaurant.Common.Common;
import com.example.androidmyrestaurant.Retrofit.IMyRestaurantAPI;
import com.example.androidmyrestaurant.Retrofit.RetrofitClient;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    //register
    //TextView register_button;
    IMyRestaurantAPI myRestaurantAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AlertDialog dialog;


    //public AlertDialog dialog;
    private static final String TAG = "MainActivity";
    private static final int APP_REQUEST_CODE = 1234;

   // IMyRestaurantAPI myAPI;
    //CompositeDisposable compositeDisposable = new CompositeDisposable();


    //MaterialEditText edt_email,edt_password;
    // MaterialButton btn_register,btn_login;
    //Button btn_login;
    //TextView btn_register;
    //EditText edt_email,edt_password;


    ////////////////////
    //@BindView(R.id.checkbox)
    //CheckBox checkbox;
    //@BindView(R.id.login_button)
    //Button login_button;
    @BindView(R.id.register_button)
    TextView register_button;
    //@BindView(R.id.edt_email)
   // EditText edt_email;
   // @BindView(R.id.edt_password)
   // EditText edt_password;

    ///////////////////////
    //

    @BindView(R.id.login_button)
    Button login_button;

    @OnClick(R.id.login_button)
    void loginUser(){
       Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                builder.build());
        startActivityForResult(intent,APP_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE)
        {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;

            if (loginResult.getError() != null){
                toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

            }
            else if (loginResult.wasCancelled()){
                toastMessage = "login Cancelled";
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            }
            else {
                dialog.show();
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        //dialog.show();
                        compositeDisposable.add(myRestaurantAPI.getUser(Common.API_KEY,
                                account.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(userModel -> {
                                            //si el usuario existe en la base de datos
                                            if (userModel.isSuccess()) {
                                                Common.currentUser = userModel.getResult().get(0);
                                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                                finish();

                                            }
                                            else {//si el usuario no esta registrado
                                                startActivity(new Intent(MainActivity.this,UpdateInfoActivity.class));
                                                finish();

                                            }
                                            dialog.dismiss();
                                        },
                                        throwable -> {
                                            dialog.dismiss();
                                            Toast.makeText(MainActivity.this, "[GET USER]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        })
                        );
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Toast.makeText(MainActivity.this, "[ACCOUNT KIT ERROR]", Toast.LENGTH_SHORT).show();
                    }
                });


            }


                //login success



        }
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        ButterKnife.bind(this);
        init();
        //Init API
       // Retrofit retrofit = RetrofitClient.getInstance();
        //myAPI = retrofit.create(IMyRestaurantAPI.class);
        //View
        //btn_login = (Button)findViewById(R.id.login_button);
        //btn_register = (TextView)findViewById(R.id.register_button);
        //edt_email = (EditText)findViewById(R.id.edt_email);
        //edt_password = (EditText)findViewById(R.id.edt_password);
       // edt_num_telf = (EditText)findViewById(R.id.edt_num_telf);
        //edt_nombre = (EditText)findViewById(R.id.edt_nombre);
//        edt_password_reg = (EditText)findViewById(R.id.edt_password_reg);
       // edt_direccion = (EditText)findViewById(R.id.edt_direccion);


        //Event
        //LOGIN
       // login_button.setOnClickListener(v -> loginUser(edt_email.getText().toString(),edt_password.getText().toString()));




//        register_button.setOnClickListener(v -> {
//                //Toast.makeText(this, "botton", Toast.LENGTH_SHORT).show();
//                //LayoutInflater inflater = (LayoutInflater) getSystemService(MainActivity.this);
//                final View register_layout = LayoutInflater.from(MainActivity.this)
//                        .inflate(R.layout.enter_post_dates,null);
//
//                AlertDialog.Builder dial = new AlertDialog.Builder(MainActivity.this);
//
//                dial.setView(register_layout);
//                dial.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
//
//
//                dial.setPositiveButton("Registrar", null);
//                AlertDialog dialog = dial.create();
//                dialog.show();
//
//
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // implement your code here
//
//                        Boolean valid = true;
//                        EditText edt_register_telefono = (EditText)register_layout.findViewById(R.id.edt_num_telf);
//                        EditText edt_register_nombre = (EditText)register_layout.findViewById(R.id.edt_nombre);
//                        EditText edt_register_correo = (EditText)register_layout.findViewById(R.id.edt_correo);
//                        EditText edt_register_password = (EditText)register_layout.findViewById(R.id.edt_password_reg);
//                        EditText edt_register_direccion = (EditText)register_layout.findViewById(R.id.edt_direccion);
//
//                        String telefono = edt_register_telefono.getText().toString();
//                        String nombre = edt_register_nombre.getText().toString();
//                        String correo = edt_register_correo.getText().toString();
//                        String password = edt_register_password.getText().toString();
//                        String direccion = edt_register_direccion.getText().toString();
//
//
//                        if (telefono.isEmpty() || !Patterns.PHONE.matcher(telefono).matches()) {
//                            edt_register_telefono.setError("Introduzca un numero valido");
//                            valid = false;
//                        } else {
//                            edt_register_telefono.setError(null);
//                        }
//
//                        if (nombre.isEmpty() || nombre.length() < 5 || nombre.length() > 30) {
//                            edt_register_nombre.setError("Introdusca su nombre completo ");
//                            valid = false;
//                        } else {
//                            edt_register_nombre.setError(null);
//                        }
//
//                        if (correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
//                            edt_register_correo.setError("Introduzca una dirección de correo electrónico válida");
//                            valid = false;
//                        } else {
//                            edt_register_correo.setError(null);
//                        }
//
//                        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//                            edt_register_password.setError("contraseña entre 4 y 10 caracteres ");
//                            valid = false;
//                        } else {
//                            edt_register_password.setError(null);
//                        }
//                        if (direccion.isEmpty() || direccion.length() < 5 || direccion.length() > 20) {
//                            edt_register_direccion.setError("Direccion Postal entre 5 y 20 caracteres ");
//                            valid = false;
//                        } else {
//                            edt_register_direccion.setError(null);
//                        }
//
//                        if (!valid) {
//                            onRegisterFailed();
//
//                            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
//                            return;
//                        }else if(valid) {
//                            registerUser( edt_register_telefono.getText().toString(),
//                                    edt_register_nombre.getText().toString(),
//                                    edt_register_correo.getText().toString(),
//                                    edt_register_password.getText().toString(),
//                                    edt_register_direccion.getText().toString()
//                            );
//
//                         }
//                        new Handler().postDelayed(new Runnable(){
//                            public void run(){
//                                //aqui agregas las sentencias que deseas que se ejecuten despues del tiempo transcurrido
//                                dialog.dismiss();//en mi caso solo se oculta el Dialog
//                            }
//                        }, 3000);/*temporizador*/
//
//
//
//
//
//                        // Otherwise the dialog will stay open.
//
//
//
//
//
//
//
//
//                    }
//
//
//                });
//            });
//
//        checkbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
//            if (isChecked) {
//                // show password
//                edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//            } else {
//                // hide password
//                edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            }
//        });

    }/*
    private void registerUser(final String UserPhone, final String Name, final String email, final String password, final String Address) {
        //with register, we need one more step to enter name because register need 3 params:
        //email, name, password
        //so we will create new Dialog with enter name, userphone, address value after fill email + password
        Log.d(TAG,"LOGIN");
        register_button.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registrando...");
        progressDialog.show();

        //String email = _emailText.getText().toString();
        //String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                () -> {
                    // On complete call either onLoginSuccess or onLoginFailed
                    onRegisterSuccess();
                    // onLoginFailed();
                    progressDialog.dismiss();
                    //dialog.dismiss();//en mi caso solo se oculta el Dialog
                    compositeDisposable.add(myAPI.registerUser(Common.API_KEY,UserPhone,Name,email,password,Address)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
                                if (s.isSuccess()) {



                                    Toast.makeText(MainActivity.this, "Registro success", Toast.LENGTH_SHORT).show();
                                }

                                else
                                    Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show(); //else just show error from API
                                    //onLoginFailed();
                            })

                    );


                }, 3000);


    }
/*
    private void loginUser(final String email, final String password ) {
        Log.d(TAG,"LOGIN");
        if (!validate()) {
            onLoginFailed();
            Toast.makeText(this, "Inserte un Usuario y Contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        login_button.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticando...");
        progressDialog.show();

        //String email = _emailText.getText().toString();
        //String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                () -> {
                    // On complete call either onLoginSuccess or onLoginFailed
                    onLoginSuccess();
                    // onLoginFailed();
                    progressDialog.dismiss();
                    compositeDisposable.add(myAPI.loginUser(email,password)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
                                if (s.contains("encrypted_password")){
                                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                    finish();
                                    //Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                }

                                else
                                    Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show(); //else just show error from API
                            })

                    );


                }, 3000);



    }

    /*public boolean validate() {
        boolean valid = true;

        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_email.setError("Introduzca una dirección de correo electrónico válida");
            valid = false;
        } else {
            edt_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edt_password.setError("contraseña entre 4 y 10 caracteres alfanuméricos");
            valid = false;
        } else {
            edt_password.setError(null);
        }

        return valid;
    }

    public void onLoginSuccess() {
        login_button.setEnabled(true);

    }
    public void onLoginFailed() {
        //Toast.makeText(getBaseContext(), "error al registrar", Toast.LENGTH_LONG).show();

        login_button.setEnabled(true);
    }*/

    private void init() {

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        myRestaurantAPI = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(IMyRestaurantAPI.class);
    }

    public void onRegisterSuccess() {
        register_button.setEnabled(true);

    }
    public void onRegisterFailed() {
        //Toast.makeText(getBaseContext(), "error al registrar", Toast.LENGTH_LONG).show();

        register_button.setEnabled(true);







    }

}

