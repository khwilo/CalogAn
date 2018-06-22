package com.example.android.calogan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactDisplay extends AppCompatActivity {

    private TextView mContactNameTv;
    private TextView mJsonDataViewTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_display);

        mContactNameTv = (TextView) findViewById(R.id.contact_name_tv);
        mJsonDataViewTv = (TextView) findViewById(R.id.json_data_view_tv);

        ArrayList<Contact> contactArrayList = QueryCallsUtility.showCallLogs(this);

        String jsonObject = QueryCallsUtility.extractJsonObject(contactArrayList);

        Log.d("JSONOBJECT", jsonObject);

        mContactNameTv.setText("YOUR CALL LOGS!!");
        mJsonDataViewTv.append(jsonObject);
    }
}
