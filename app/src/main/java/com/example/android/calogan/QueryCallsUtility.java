package com.example.android.calogan;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QueryCallsUtility {

    // Constant for outgoing calls.
    private static final int OUTGOING_CALL = Calls.OUTGOING_TYPE;

    // Constant for incoming calls.
    private static final int INCOMING_CALL = Calls.INCOMING_TYPE;

    // Constant for missed calls.
    private static final int MISSED_CALL = Calls.MISSED_TYPE;

    // Make the constructor private because
    // the intention is not to create new objects from this class.
    private QueryCallsUtility() {}

    public static ArrayList<CallLog> showCallLogs(Context context) {

        // Create an ArrayList to hold the call logs details.
        ArrayList<CallLog> callLogArrayList = new ArrayList<>();

        // Fetch the Uri of the call logs
        Uri callLogUri = Calls.CONTENT_URI;

        // Define a cursor object to retrieve the call logs
        Cursor mCursor = new CursorLoader(context, callLogUri,
                null, null, null, null).loadInBackground();

        // Fetch the index of the phone number
        int phoneNumberIndex = mCursor.getColumnIndex(Calls.NUMBER);
        // Fetch the index of the call type
        int callTypeIndex = mCursor.getColumnIndex(Calls.TYPE);
        // Fetch the index of the date.
        int callDateIndex = mCursor.getColumnIndex(Calls.DATE);


        while (mCursor.moveToNext()) {
            // Fetch the value of the phone number
            String phoneNumberValue = mCursor.getString(phoneNumberIndex);
            // Fetch the value of the call type
            String callTypeValue = mCursor.getString(callTypeIndex);
            // Fetch the value of the call date
            String callDateValue = mCursor.getString(callDateIndex);

            // Create a new Date object
            Date date = new Date(Long.valueOf(callDateValue));
            // Format the date
            String formattedDate = formatDate(date);

            // Get the contact name of the phone number retrieved from the phone address book.
            String contactName = getContactName(context, phoneNumberValue);

            String callType = "";
            if (Integer.parseInt(callTypeValue) == INCOMING_CALL) {
                callType = "Incoming";
                CallLog incomingCall = new CallLog(contactName, phoneNumberValue,
                        callType, formattedDate);
                callLogArrayList.add(incomingCall);
            } else if (Integer.parseInt(callTypeValue) == OUTGOING_CALL) {
                callType = "Outgoing";
                CallLog outgoingCall = new CallLog(contactName, phoneNumberValue,
                        callType, formattedDate);
                callLogArrayList.add(outgoingCall);
            } else if (Integer.parseInt(callTypeValue) == MISSED_CALL) {
                callType = "Missed";
                CallLog missedCall = new CallLog(contactName, phoneNumberValue,
                        callType, formattedDate);
                callLogArrayList.add(missedCall);
            }

        }

        mCursor.close();

        return callLogArrayList;
    }

    // Retrieve the contact name from the phone address book.
    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));

        // Filter the columns by the display name.
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName = "";

        Cursor cursor = contentResolver.query(uri, projection, null,
                null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }

    // Format the date
    private static String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(dateObject);
    }

    // Extract a call log JSON object from the callLogs ArrayList.
    public static String extractJsonObject(ArrayList<CallLog> callLogs) {
        Gson gson = new Gson();
        return gson.toJson(callLogs);
    }

    // Read a JSON file from the assets folder.
    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;

        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    // Parse the JSON file.
    // The String callLogJson is gotten from the loadJSONFromAsset method.
    public static ArrayList<CallLog> extractFeaturesFromJson(String callLogJson) {

        ArrayList<CallLog> callLogs = new ArrayList<>();

        if (TextUtils.isEmpty(callLogJson)) {
            return null;
        }

        try {
            JSONArray callLogJsonArray = new JSONArray(callLogJson);
            for (int i = 0; i < callLogJsonArray.length(); i++) {
                JSONObject callLogJsonObject = callLogJsonArray.getJSONObject(i);
                String callDate = callLogJsonObject.getString("mCallDate");
                String callType = callLogJsonObject.getString("mCallType");
                String contactName = callLogJsonObject.getString("mContactName");
                String phoneNumber = callLogJsonObject.getString("mPhoneNumber");

                CallLog callLog = new CallLog(contactName, phoneNumber, callType, callDate);

                callLogs.add(callLog);
            }
        } catch (JSONException e) {
            Log.d("JSON_OBJECT", "Problem parsing the call logs JSON results", e);
        }

        return callLogs;
    }
}

