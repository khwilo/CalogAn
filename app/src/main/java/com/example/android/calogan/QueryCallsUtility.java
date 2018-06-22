package com.example.android.calogan;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;

import com.google.gson.Gson;

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

    public static ArrayList<Contact> showCallLogs(Context context) {

        // Create an ArrayList to hold the call logs details.
        ArrayList<Contact> contactsList = new ArrayList<>();

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
                Contact incomingCall = new Contact(contactName, phoneNumberValue,
                        callType, formattedDate);
                contactsList.add(incomingCall);
            } else if (Integer.parseInt(callTypeValue) == OUTGOING_CALL) {
                callType = "Outgoing";
                Contact outgoingCall = new Contact(contactName, phoneNumberValue,
                        callType, formattedDate);
                contactsList.add(outgoingCall);
            } else if (Integer.parseInt(callTypeValue) == MISSED_CALL) {
                callType = "Missed";
                Contact missedCall = new Contact(contactName, phoneNumberValue,
                        callType, formattedDate);
                contactsList.add(missedCall);
            }

        }

        mCursor.close();

        return contactsList;
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

    // Extract a call log JSON object from the contacts ArrayList.
    public static String extractJsonObject(ArrayList<Contact> contacts) {
        Gson gson = new Gson();
        return gson.toJson(contacts);
    }
}

