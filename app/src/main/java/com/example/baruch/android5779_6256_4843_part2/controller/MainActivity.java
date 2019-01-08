package com.example.baruch.android5779_6256_4843_part2.controller;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.entities.CurrentDriver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.CurrentLocation;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Exceptions;


public class MainActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView createAccountTextView;
    private Driver mDriver;
    private boolean isLocated=false;
    private static Backend backend;


    private static final String userPreferences="userPreferences";
    private static final String userEmail="email";
    private static final String userPassword="password";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CurrentLocation.setCurrentLocation(this, new Backend.Action() {
            @Override
            public void onSuccess() {
                isLocated=true;
            }

            @Override
            public void onFailure() {

            }
        });

        backend= BackendFactory.getBackend();
        findViews();
        sharedPreferences=getSharedPreferences(userPreferences, Context.MODE_PRIVATE);
        showUserData();
    }


    //Show data in EditTexts when app is launched, if data is there in Android Shared Preferences
    private void showUserData() {
        if (sharedPreferences.contains(userEmail))
            emailEditText.setText(sharedPreferences.getString(userEmail, ""));
        if (sharedPreferences.contains(userPassword))
            passwordEditText.setText(sharedPreferences.getString(userPassword, ""));

    }

    private void findViews() {
        emailEditText=(EditText)findViewById(R.id.emailEditText);
        passwordEditText=(EditText)findViewById(R.id.passwordEditText);
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Exceptions.checkEmail(s.toString())){
                    emailEditText.setError("Email not valid");
                }
                else{
                    emailEditText.setError(null);
                }

            }
        });
        loginButton=(Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RelativeLayout)findViewById(R.id.loadingPanel)).setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                storeUserData();
                backend.signIn(emailEditText.getText().toString(), passwordEditText.getText().toString(),
                        new Backend.Action() {
                    @Override
                    public void onSuccess() {
                        openNextActivity();
                    }

                    @Override
                    public void onFailure() {
                        ((RelativeLayout)findViewById(R.id.loadingPanel)).setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(),"Incorrect email or password!",Toast.LENGTH_LONG).show();
                        loginButton.setEnabled(true);
                    }
                });


            }
        });
        createAccountTextView=(TextView)findViewById(R.id.createAccountTextView);
        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAccountActivity();
            }
        });

    }

    private void openNextActivity() {
        if (isLocated) {
            final Intent intent = new Intent(MainActivity.this, RidesManagerActivity.class);
            backend.getCurrentUser(new Backend.ActionResult() {
                @Override
                public void onSuccess(Driver driver) {
                    mDriver = driver;

                    CurrentDriver.setDriver(mDriver);
                    startActivity(intent);
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    private void storeUserData() {
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(userEmail,email);
        editor.putString(userPassword,password);
        editor.commit();
    }

    private void openCreateAccountActivity() {
        Intent intent=new Intent(MainActivity.this,CreateAccount.class);
        startActivity(intent);
    }

    private boolean isEmptyInput() {
        return TextUtils.isEmpty(emailEditText.getText())||
                TextUtils.isEmpty(passwordEditText.getText());
    }

    private boolean isErrorInput() {
        return emailEditText.getError()!=null;
    }


}
