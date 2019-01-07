package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.entities.CurrentDriver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.CurrentLocation;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Exceptions;

import static android.widget.Toast.LENGTH_LONG;

public class CreateAccount extends AppCompatActivity {

    private EditText idEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button createAccountButton;
    private Driver mDriver;
    private static Backend backend;
    private boolean isLocated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        if(CurrentLocation.getCurrentLocation()!=null)
            isLocated=true;
        else
        {
            CurrentLocation.setCurrentLocation(this, new Backend.Action() {
                @Override
                public void onSuccess() {
                    isLocated=true;
                }

                @Override
                public void onFailure() {

                }
            });
        }

        backend= BackendFactory.getBackend();
        mDriver =new Driver();
        findViews();
    }

    private void findViews() {
        idEditText=(EditText) findViewById(R.id.idEditText);
        idEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Exceptions.checkOnlyNumbers(s.toString()))
                {
                    idEditText.setError("Only Digits");
                }
                else
                {
                    idEditText.setError(null);
                }
            }
        });

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
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Exceptions.checkOnlyNumbers(s.toString()))
                {
                    phoneNumberEditText.setError("Only Digits");
                }
                else
                {
                    phoneNumberEditText.setError(null);
                }
            }
        });

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
                backend.register(mDriver,passwordEditText.getText().toString(),new Backend.Action(){

                   @Override
                   public void onSuccess() {
                       Toast.makeText(getBaseContext(), "Welcome to RideTaxi Community!", LENGTH_LONG).show();
                       CurrentDriver.setDriver(mDriver);
                       if (isLocated) {
                           Intent intent = new Intent(CreateAccount.this, RidesManagerActivity.class);
                           startActivity(intent);
                       }
                   }
                   @Override
                   public void onFailure() {

                   }
               });

            }
        });
    }


    private void setNewDriver() {
        mDriver.setId(idEditText.getText().toString());
        mDriver.setFirstName(firstNameEditText.getText().toString());
        mDriver.setLastName(lastNameEditText.getText().toString());
        mDriver.setTelephone(phoneNumberEditText.getText().toString());
        mDriver.setEmail(emailEditText.getText().toString());

    }

    private boolean isEmptyInput() {
        return TextUtils.isEmpty(idEditText.getText())||
                TextUtils.isEmpty(firstNameEditText.getText())||
                TextUtils.isEmpty(lastNameEditText.getText())||
                TextUtils.isEmpty(emailEditText.getText())||
                TextUtils.isEmpty(phoneNumberEditText.getText())||
                TextUtils.isEmpty(passwordEditText.getText())||
                TextUtils.isEmpty(confirmPasswordEditText.getText());
    }

    private boolean isErrorInput() {
        return idEditText.getError()!=null||firstNameEditText.getError()!=null
                ||phoneNumberEditText.getError()!=null
                ||lastNameEditText.getError()!=null
                ||emailEditText.getError()!=null
                ||phoneNumberEditText.getError()!=null;
    }
}
