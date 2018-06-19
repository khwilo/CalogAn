package com.example.android.calogan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText mPhoneSearchEt;
    private Button mPhoneSearchBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhoneSearchEt = (EditText) findViewById(R.id.phone_number_search_et);
        mPhoneSearchBtn = (Button) findViewById(R.id.phone_number_search_btn);


        mPhoneSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Fetch the value of the EditText.
                String phoneNumber = mPhoneSearchEt.getText().toString();

                Intent intent = new Intent(MainActivity.this, ContactDisplay.class);

                intent.putExtra("phone_number", phoneNumber);

                startActivity(intent);
            }
        });
    }


}
