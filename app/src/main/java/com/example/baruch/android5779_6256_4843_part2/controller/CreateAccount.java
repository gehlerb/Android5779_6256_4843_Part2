package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Exceptions;

import static android.widget.Toast.LENGTH_LONG;

public class CreateAccount extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button createAccountButton;
    private Driver driver;
    private static Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        backend= BackendFactory.getBackend();
        driver=new Driver();
        findViews();
    }

    private void findViews() {
        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Exceptions.checkOnlyLetters(s.toString()))
                {
                    firstNameEditText.setError("Only letters");
                }
                else
                {
                    firstNameEditText.setError(null);

                }
            }
        });
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Exceptions.checkOnlyLetters(s.toString())){
                    lastNameEditText.setError("Only letters");
                }
                else{
                    lastNameEditText.setError(null);
                }
            }
        });
        emailEditText = (EditText) findViewById(R.id.emailEditText);
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
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        passwordEditText=(EditText)findViewById(R.id.createPasswordEditText);
        confirmPasswordEditText=(EditText)findViewById(R.id.confirmPasswordEditText);
        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(passwordEditText.getText().toString()))
                    confirmPasswordEditText.setError("The password is not similar");
                else{
                 confirmPasswordEditText.setError(null);
                }
            }
        });
        createAccountButton=(Button)findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isErrorInput()){
                    return;
                }
                if(isEmptyInput()){
                    Toast.makeText(getBaseContext(), R.string.all_fields_required,LENGTH_LONG).show();
                    return;
                }
                setNewDriver();
                isDiverInDatabase(driver);

            }
        });
    }

    private void isDiverInDatabase(final Driver driver) {
        createAccountButton.setEnabled(false);
        backend.isDriverAlreadyRegistered(driver, new Backend.Action() {
            @Override
            public void onSuccess() {
                addNewDriverToDataBase(driver);
            }

            @Override
            public void onFailure() {
                Toast.makeText(getBaseContext(),"Sorry, this email already has an account!",LENGTH_LONG).show();
                createAccountButton.setEnabled(true);
            }
        });

    }

    private void addNewDriverToDataBase(Driver driver) {
        backend.addNewDriverToDataBase(driver, new Backend.Action() {
            @Override
            public void onSuccess() {
                Toast.makeText(getBaseContext(),"Welcome to Ride Taxi Community!",LENGTH_LONG).show();
                Intent intent=new Intent(CreateAccount.this,driver_rides_manager.class);
                startActivity(intent);
            }

            @Override
            public void onFailure() {
            }
        });


    }

    private void setNewDriver() {
        driver.setFirstName(firstNameEditText.getText().toString());
        driver.setLastName(lastNameEditText.getText().toString());
        driver.setTelephone(phoneNumberEditText.getText().toString());
        driver.setEmail(emailEditText.getText().toString());
        driver.setPassword(passwordEditText.getText().toString());

    }

    private boolean isEmptyInput() {
        return TextUtils.isEmpty(firstNameEditText.getText())||
                TextUtils.isEmpty(lastNameEditText.getText())||
                TextUtils.isEmpty(emailEditText.getText())||
                TextUtils.isEmpty(phoneNumberEditText.getText())||
                TextUtils.isEmpty(passwordEditText.getText());
    }

    private boolean isErrorInput() {
        return firstNameEditText.getError()!=null||lastNameEditText.getError()!=null
                ||emailEditText.getError()!=null||phoneNumberEditText.getError()!=null;
    }
}
