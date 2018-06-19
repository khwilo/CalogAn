package com.example.android.calogan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ContactDisplay extends AppCompatActivity {

    private TextView mPhoneNumberTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_display);

        mPhoneNumberTv = (TextView) findViewById(R.id.phone_number_value_tv);

        Intent intent = getIntent();
        String phoneNumberValue = intent.getExtras().getString("phone_number");
        mPhoneNumberTv.setText(phoneNumberValue);
    }
}
