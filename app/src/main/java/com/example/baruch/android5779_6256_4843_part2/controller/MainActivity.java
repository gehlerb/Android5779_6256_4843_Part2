package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;

public class MainActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView createAccountTextView;

    private static final String userPreferences="userPreferences";
    private static final String userEmail="email";
    private static final String userPassword="password";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        loginButton=(Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeUserData();
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


}
