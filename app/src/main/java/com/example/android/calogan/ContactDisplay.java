package com.example.android.calogan;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private static final String FILE_NAME = "_call_log.json";

    private static final String CALL_LOG_FILE_NAME_1 = "289ce0d0d3ae576d_call_log.json";
    private static final String CALL_LOG_FILE_NAME_2 = "1aaa3b51b999ef6d_call_log.json";


    private TextView mContactNameTv;
    private TextView mJsonDataViewTv;

    private ArrayList<Contact> mCallLogsArrayList;
    private String mCallLogsJsonObject;

    private String mDeviceId;

    private String mCallLog1;
    private String mCallLog2;

    private ArrayList<Contact> mFirstCallLogArrayList;
    private ArrayList<Contact> mSecondCallLogArrayList;

    private ArrayList<Contact> mCompareCallLogArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_display);

        mContactNameTv = (TextView) findViewById(R.id.contact_name_tv);
        mJsonDataViewTv = (TextView) findViewById(R.id.json_data_view_tv);

        mDeviceId = getUsersPhoneID();
        Log.d(TAG, mDeviceId);

        mCallLogsArrayList = QueryCallsUtility.showCallLogs(this);

        // Load the respective call log files
        mCallLog1 = QueryCallsUtility.loadJSONFromAsset(this, CALL_LOG_FILE_NAME_1);
        mCallLog2 = QueryCallsUtility.loadJSONFromAsset(this, CALL_LOG_FILE_NAME_2);

        // Extract features from the JSON files
        mFirstCallLogArrayList = QueryCallsUtility.extractFeaturesFromJson(mCallLog1);
        mSecondCallLogArrayList = QueryCallsUtility.extractFeaturesFromJson(mCallLog2);

        mCompareCallLogArrayList = findNumber("0722273085",
                mFirstCallLogArrayList,
                mSecondCallLogArrayList);

        mCallLogsJsonObject = QueryCallsUtility.extractJsonObject(mCompareCallLogArrayList);
        writeToExternalStorage(this, mCallLogsJsonObject, mDeviceId);

        Log.d("JSONOBJECT", mCallLogsJsonObject);


        int incomingCallsCount = getCallCount("Incoming", mCompareCallLogArrayList);

        mContactNameTv.setText(String.valueOf(incomingCallsCount));
        mJsonDataViewTv.append(mCallLogsJsonObject);
        // mJsonDataViewTv.append(String.valueOf(mSecondCallLogArrayList.size()));
        // mJsonDataViewTv.append(String.valueOf(mCompareCallLogArrayList.size()));
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

    private ArrayList<Contact> findNumber(String phoneNumber,
                                          ArrayList<Contact> firstContact,
                                          ArrayList<Contact> secondContact) {

        Set<Contact> contactSet = new HashSet<Contact>();

        ArrayList<Contact> newContact = new ArrayList<>();

        for (Contact initialContact : firstContact) {
            for (Contact compareContact : secondContact) {
                if (initialContact.getPhoneNumber().equals(phoneNumber) &&
                        compareContact.getPhoneNumber().equals(phoneNumber)) {
                    newContact.add(compareContact);
                }
            }
        }

        // Remove the duplicates
        contactSet.addAll(newContact);
        newContact = new ArrayList<Contact>();
        newContact.addAll(contactSet);

        return newContact;
    }

    private int getCallCount(String callType, ArrayList<Contact> contacts) {
        int count = 0;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getCallType().equals(callType)) {
                count++;
            }
        }
        return count;
    }
}
