package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Welcome_login extends AppCompatActivity {

    private EditText usernameLogin, passwordLogin;
    private String BaseUrl="https://digitaldectectives.azdigi.blog/Users/";
    private Check_user api_check_user;
    private Button register_now,login;
    private boolean isLoggedIn= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            startHomeActivity();
            finish();
        }

        else{
            login = (Button) findViewById(R.id.login_btn);
            usernameLogin=(EditText)findViewById(R.id.username_login);
            passwordLogin=(EditText)findViewById(R.id.password_login);
            register_now = (Button) findViewById(R.id.register_now);


            register_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent k = new Intent(Welcome_login.this, Sign_up.class);
                    startActivity(k);
                }
            });
            login.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    String usernameInput = usernameLogin.getText().toString();
                    String passwordInput = passwordLogin.getText().toString();
//                Toast.makeText(Welcome_login.this, usernameInput+passwordInput,Toast.LENGTH_LONG).show();
                    checkUser();
                }

                private void checkUser(){
                    String usernameInput = usernameLogin.getText().toString();
                    String passwordInput = passwordLogin.getText().toString();

                    Retrofit retrofit_check = new Retrofit.Builder()
                            .baseUrl(BaseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    api_check_user = retrofit_check.create(Check_user.class);

                    Call<Users_check>checkUserCall = api_check_user.check_user(usernameInput,passwordInput);
//                Toast.makeText(Welcome_login.this, usernameInput+passwordInput,Toast.LENGTH_LONG).show();
                    checkUserCall.enqueue(new Callback<Users_check>() {
                        @Override
                        public void onResponse(Call<Users_check> call, Response<Users_check> response) {
                            Users_check user = response.body();
                            if (usernameInput.trim().isEmpty()|| passwordInput.trim().isEmpty()){
                                Toast.makeText(Welcome_login.this, "Please provide all necessary information",Toast.LENGTH_LONG).show();

                            }
                            else if(user != null && user.isSuccess()){
                                Toast.makeText(Welcome_login.this, "Login successful",Toast.LENGTH_SHORT).show();


                                Intent i = new Intent(Welcome_login.this, Home.class);
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("usernameLogin", usernameInput);
                                editor.apply();

                                startActivity(i);

                            }
                            else {
                                Toast.makeText(Welcome_login.this, "Invalid username or account",Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Users_check> call, Throwable t) {

                        }
                    });
                }

            });
        }




    }
    private void startHomeActivity() {
        Intent intent = new Intent(Welcome_login.this, Home.class);
        startActivity(intent);
    }
}