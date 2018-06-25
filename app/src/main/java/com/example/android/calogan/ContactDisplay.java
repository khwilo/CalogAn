package com.example.android.calogan;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ContactDisplay extends AppCompatActivity {

    private static final String TAG = "CONTACT_DISPLAY";

    // The file name to be stored in disk.
    private static final String FILE_NAME = "_call_log.json";

    // The retrieved file names from the specific devices.
    private static final String CALL_LOG_FILE_NAME_1 = "289ce0d0d3ae576d_call_log.json";
    private static final String CALL_LOG_FILE_NAME_2 = "1aaa3b51b999ef6d_call_log.json";

    // Labels for each call type.
    private static final String INCOMING_CALL_LABEL = "Incoming";
    private static final String OUTGOING_CALL_LABEL = "Outgoing";
    private static final String MISSED_CALL_LABEL = "Missed";


    private TextView mFirstContactNameTv;
    private TextView mFirstIncomingCallsCountTv;
    private TextView mFirstOutgoingCallsCountTv;
    private TextView mFirstMissedCallsCountTv;

    private TextView mSecondContactNameTv;
    private TextView mSecondIncomingCallsCountTv;
    private TextView mSecondOutgoingCallsCountTv;
    private TextView mSecondMissedCallsCountTv;

    // private TextView mJsonDataViewTv;

    private String mCallLogsJsonObject;

    private String mDeviceId;

    private String mCallLog1;
    private String mCallLog2;

    // ArrayList to store call logs of a particular class
    private ArrayList<CallLog> mCurrentCallLogArrayList;

    // ArrayList to store the call logs of the first device.
    private ArrayList<CallLog> mFirstCallLogArrayList;

    // ArrayList to store the call logs of the second device.
    private ArrayList<CallLog> mSecondCallLogArrayList;

    // ArrayList to store the comparison between the first and the second call logs.
    private ArrayList<CallLog> mCompareCallLogArrayList;

    // Array list to store the first call log filtered by number.
    private ArrayList<CallLog> mFilteredCallLogArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_display);

        mFirstContactNameTv = (TextView) findViewById(R.id.contact_name_tv_1);
        mFirstIncomingCallsCountTv = (TextView) findViewById(R.id.my_call_log_incoming_calls_count);
        mFirstOutgoingCallsCountTv = (TextView) findViewById(R.id.my_call_log_outgoing_calls_count);
        mFirstMissedCallsCountTv = (TextView) findViewById(R.id.my_call_log_missed_call_count);

        mSecondContactNameTv = (TextView) findViewById(R.id.contact_name_tv_2);
        mSecondIncomingCallsCountTv = (TextView) findViewById(R.id.compared_call_log_incoming_calls_count);
        mSecondOutgoingCallsCountTv = (TextView) findViewById(R.id.compared_call_log_outgoing_calls_count);
        mSecondMissedCallsCountTv = (TextView) findViewById(R.id.compared_call_log_missed_calls_count);

        // mJsonDataViewTv = (TextView) findViewById(R.id.json_data_view_tv);

        Intent intent = getIntent();
        String phoneNumberValue = intent.getExtras().getString("phone_number");

        String contactName = QueryCallsUtility.getContactName(getBaseContext(), phoneNumberValue);

        mCurrentCallLogArrayList = QueryCallsUtility.showCallLogs(this);

        mDeviceId = getUsersPhoneID();

        // Load the respective call log files
        mCallLog1 = QueryCallsUtility.loadJSONFromAsset(this, CALL_LOG_FILE_NAME_1);
        mCallLog2 = QueryCallsUtility.loadJSONFromAsset(this, CALL_LOG_FILE_NAME_2);

        // Extract features from the JSON files
        mFirstCallLogArrayList = QueryCallsUtility.extractFeaturesFromJson(mCallLog1);
        mSecondCallLogArrayList = QueryCallsUtility.extractFeaturesFromJson(mCallLog2);

        mCompareCallLogArrayList = retrieveSimilarPhoneNumber(
                phoneNumberValue,
                mFirstCallLogArrayList,
                mSecondCallLogArrayList);

        mFilteredCallLogArrayList = filterCallLogByNumber(phoneNumberValue, mFirstCallLogArrayList);

        mCallLogsJsonObject = QueryCallsUtility.extractJsonObject(mCurrentCallLogArrayList);

        writeToExternalStorage(this, mCallLogsJsonObject, mDeviceId);

        mFirstContactNameTv.setText(contactName);
        mFirstIncomingCallsCountTv.append(String.valueOf(
                getCallCount(INCOMING_CALL_LABEL, mFilteredCallLogArrayList)));
        mFirstOutgoingCallsCountTv.append(String.valueOf(
                getCallCount(OUTGOING_CALL_LABEL, mFilteredCallLogArrayList)));
        mFirstMissedCallsCountTv.append(String.valueOf(
                getCallCount(MISSED_CALL_LABEL, mFilteredCallLogArrayList)));

        mSecondContactNameTv.setText(contactName);
        mSecondIncomingCallsCountTv.append(String.valueOf(
                getCallCount(INCOMING_CALL_LABEL, mCompareCallLogArrayList)));
        mSecondOutgoingCallsCountTv.append(String.valueOf(
                getCallCount(OUTGOING_CALL_LABEL, mCompareCallLogArrayList)));
        mSecondMissedCallsCountTv.append(String.valueOf(
                getCallCount(MISSED_CALL_LABEL, mCompareCallLogArrayList)));

        //mJsonDataViewTv.append(mCallLogsJsonObject);
    }

    private void writeToExternalStorage(Context context, String data, String prefix) {
        File file = new File(context.getExternalFilesDir(null), prefix + FILE_NAME);

        FileOutputStream outputStream;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            outputStream = new FileOutputStream(file, true);
            outputStream.write(data.getBytes());
            outputStream.close();

            MediaScannerConnection.scanFile(context,
                    new String[]{file.toString()},
                    null,
                    null);

            Toast.makeText(context, "File written to external storage",
                    Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getUsersPhoneID() {
        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    private ArrayList<CallLog> retrieveSimilarPhoneNumber(String phoneNumber,
                                                          ArrayList<CallLog> firstCallLog,
                                                          ArrayList<CallLog> secondCallLog) {

        Set<CallLog> callLogSet = new HashSet<CallLog>();

        ArrayList<CallLog> newCallLog = new ArrayList<>();

        for (CallLog initialCallLog : firstCallLog) {
            for (CallLog compareCallLog : secondCallLog) {
                if (initialCallLog.getPhoneNumber().equals(phoneNumber) &&
                        compareCallLog.getPhoneNumber().equals(phoneNumber)) {
                    newCallLog.add(compareCallLog);
                }
            }
        }

        // Remove the duplicates
        callLogSet.addAll(newCallLog);
        newCallLog = new ArrayList<CallLog>();
        newCallLog.addAll(callLogSet);

        return newCallLog;
    }

    private ArrayList<CallLog> filterCallLogByNumber(String phoneNumber,
                                                     ArrayList<CallLog> callLogs) {
        ArrayList<CallLog> callLogByCallLog = new ArrayList<>();

        for (CallLog callLog : callLogs) {
            if (callLog.getPhoneNumber().equals(phoneNumber)) {
                callLogByCallLog.add(callLog);
            }
        }

        return callLogByCallLog;
    }


    private int getCallCount(String callType, ArrayList<CallLog> callLogs) {
        int count = 0;
        for (int i = 0; i < callLogs.size(); i++) {
            if (callLogs.get(i).getCallType().equals(callType)) {
                count++;
            }
        }
        return count;
    }
}
