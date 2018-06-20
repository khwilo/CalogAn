package com.example.android.calogan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ContactDisplay extends AppCompatActivity {

    private TextView mContactNameTv;
    private TextView mIncomingCallsCountTv;
    private TextView mOutgoingCallsCountTv;
    private TextView mMissedCallsCountTv;

    private int incomingCallsCount;
    private int outgoingCallsCount;
    private int missedCallsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_display);

        mContactNameTv = (TextView) findViewById(R.id.contact_name_tv);
        mIncomingCallsCountTv = (TextView) findViewById(R.id.incoming_calls_count_tv);
        mOutgoingCallsCountTv = (TextView) findViewById(R.id.outgoing_calls_count_tv);
        mMissedCallsCountTv = (TextView) findViewById(R.id.missed_calls_count_tv);

        Intent intent = getIntent();
        String phoneNumberValue = intent.getExtras().getString("phone_number");

        String contactName = QueryCallsUtility.getContactName(getBaseContext(), phoneNumberValue);

        QueryCallsUtility.showCallLogs(this, phoneNumberValue);

        incomingCallsCount = QueryCallsUtility.getIncomingCallsCount();
        QueryCallsUtility.INCOMING_CALLS_ARRAY.clear();

        outgoingCallsCount = QueryCallsUtility.getOutgoingCallsCount();
        QueryCallsUtility.OUTGOING_CALLS_ARRAY.clear();

        missedCallsCount = QueryCallsUtility.getMissedCallsCount();
        QueryCallsUtility.MISSED_CALLS_ARRAY.clear();


        String incomingCalls = String.valueOf(incomingCallsCount);
        String outgoingCalls = String.valueOf(outgoingCallsCount);
        String missedCalls = String.valueOf(missedCallsCount);

        mContactNameTv.setText(contactName);
        mIncomingCallsCountTv.setText(incomingCalls);
        mOutgoingCallsCountTv.setText(outgoingCalls);
        mMissedCallsCountTv.setText(missedCalls);
    }




}
