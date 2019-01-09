package com.example.baruch.android5779_6256_4843_part2.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.entities.CurrentDriver;

public class ContactUsFragment extends Fragment {
    private View view;
    private EditText mMail;
    private EditText mName;
    private EditText mPhone;
    private EditText mMessage;
    private Button mSubmit;
    private String mCustomerMessage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact_us, container, false) ;
        mMail=(EditText)view.findViewById(R.id.contact_us_mail);
        mMail.setText("Email: "+CurrentDriver.getDriver().getEmail());
        mPhone=(EditText)view.findViewById(R.id.contact_us_phone);
        mPhone.setText("Phone Number: "+CurrentDriver.getDriver().getTelephone());
        mName=(EditText)view.findViewById(R.id.contact_us_name);
        mName.setText("Name: "+CurrentDriver.getDriver().getFirstName()+ " "+CurrentDriver.getDriver().getLastName());
        mMessage=(EditText)view.findViewById(R.id.contact_us_message);
        mMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    mCustomerMessage=s.toString();
            }
        });
        mSubmit=(Button)view.findViewById(R.id.contact_us_submit_button);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
        return view;
    }

    private void sendMail() {
        if(mCustomerMessage==null)
        {
            Toast.makeText(getActivity(),"There is no message",Toast.LENGTH_LONG).show();
                return;
        }

        Intent emailIntent=new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"ridetaxi.customerservice@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Drivers customer service");
        emailIntent.putExtra(Intent.EXTRA_TEXT,mCustomerMessage);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent,"Choose your mail box"));
    }
}
