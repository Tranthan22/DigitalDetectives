package com.example.myapplication2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Sign_up extends AppCompatActivity {

    private EditText user_name,password,repass,email,phone_number;
    private String BaseUrl="https://digitaldectectives.azdigi.blog/Users/";
    private Insert_user api_insert_user;
    private Button back, signup_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        user_name = (EditText)findViewById(R.id.username_signup);
        password = (EditText)findViewById(R.id.password_signup);
        repass = (EditText)findViewById(R.id.repassword_signup);
        email=(EditText)findViewById(R.id.email_signup);
        phone_number=(EditText)findViewById(R.id.phonenumber_signup);
        back = (Button)findViewById(R.id.back_button);
        signup_btn = (Button)findViewById(R.id.signup_btn);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Sign_up.this, Welcome_login.class);
                startActivity(i);
            }
        });


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_nameInput = user_name.getText().toString();
                String passwordInput = password.getText().toString();
                String repassInput = repass.getText().toString();
                String emailInput = email.getText().toString();
                String phone_numberInput = phone_number.getText().toString();


                if (user_nameInput.trim().isEmpty() || passwordInput.trim().isEmpty() || repassInput.trim().isEmpty()
                        || emailInput.trim().isEmpty() || phone_numberInput.trim().isEmpty()) {
                    Toast.makeText(Sign_up.this, "Please provide all necessary information", Toast.LENGTH_LONG).show();
                }

                else if (!passwordInput.equals(repassInput)){
                    Toast.makeText(Sign_up.this,"The confirmed password does not match.", Toast.LENGTH_LONG).show();
                }
                else if(!checkEmail(emailInput)){

                    Toast.makeText(Sign_up.this, "Invalid email", Toast.LENGTH_LONG).show();

                }
                else{insertData();}

            }

            private void insertData() {

                String user_nameInput = user_name.getText().toString();
                String passwordInput = password.getText().toString();
                String emailInput = email.getText().toString();
                String repassInput = repass.getText().toString();
                String phone_numberInput = phone_number.getText().toString();

                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl(BaseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                api_insert_user = retrofit.create(Insert_user.class);

                Call<Users>insertUserCall = api_insert_user.insert_Data(user_nameInput,passwordInput,emailInput,phone_numberInput);
//                Toast toast = Toast.makeText(Sign_up.this,"",Toast.LENGTH_LONG);
//                toast.setText(user_nameInput+passwordInput+emailInput+phone_numberInput);
//                toast.show();

                insertUserCall.enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        Users user = response.body();

                        if (user != null && user.isSuccess()){

                                Toast.makeText(Sign_up.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                user_name.setText("");
                                password.setText("");
                                repass.setText("");
                                email.setText("");
                                phone_number.setText("");

                                Intent k = new Intent(Sign_up.this, Home.class);
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("usernameSignup", user_nameInput);
                                editor.apply();


                                startActivity(k);

                        }
                        else {
                            Toast.makeText(Sign_up.this, "This username is already taken.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        Toast.makeText(Sign_up.this, "Error, unable to connect to database.", Toast.LENGTH_LONG).show();
                    }

                });
            }
        });


    }
    //Ham check email hop le
    public static boolean checkEmail(String email) {

        Pattern EMAIL_ADDRESS_PATTERN = Pattern
                .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


}