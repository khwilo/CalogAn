package com.example.android.calogan;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class QueryCallsUtility {
    // Constant for outgoing calls.
    private static final int OUTGOING_CALL = Calls.OUTGOING_TYPE;

    // Constant for incoming calls.
    private static final int INCOMING_CALL = Calls.INCOMING_TYPE;

    // Constant for missed calls.
    private static final int MISSED_CALL = Calls.MISSED_TYPE;

    // Create ArrayLists for outgoing calls, incoming calls and missed calls
    public static final ArrayList<Contact> OUTGOING_CALLS_ARRAY = new ArrayList<>();
    public static final ArrayList<Contact> INCOMING_CALLS_ARRAY = new ArrayList<>();
    public static final ArrayList<Contact> MISSED_CALLS_ARRAY = new ArrayList<>();


    // Make the constructor private because
    // the intention is not to create new objects from this class.
    private QueryCallsUtility() {}

    public static void showCallLogs(Context context , String phoneNumber) {

        // Fetch the Uri of the call logs
        Uri callLogUri = Calls.CONTENT_URI;

        // Define a cursor object to retrieve the call logs
        Cursor mCursor = new CursorLoader(context, callLogUri,
                null, null, null, null).loadInBackground();

        // Fetch the index of the phone number
        int phoneNumberIndex = mCursor.getColumnIndex(Calls.NUMBER);
        // Fetch the index of the call type
        int callTypeIndex = mCursor.getColumnIndex(Calls.TYPE);


        while (mCursor.moveToNext()) {
            // Fetch the value of the phone number
            String phoneNumberValue = mCursor.getString(phoneNumberIndex);
            // Fetch the value of the call type
            String callTypeValue = mCursor.getString(callTypeIndex);

            // Get the contact name of the phone number retrieved from the phone address book.
            String contactName = getContactName(context, phoneNumberValue);


            String callType = "";
            if (Integer.parseInt(callTypeValue) == INCOMING_CALL
                    && phoneNumberValue.equals(phoneNumber)) {
                callType = "Incoming";
                Contact incomingCall = new Contact(contactName, phoneNumberValue, callType);
                INCOMING_CALLS_ARRAY.add(incomingCall);
            } else if (Integer.parseInt(callTypeValue) == OUTGOING_CALL
                    && phoneNumberValue.equals(phoneNumber)) {
                callType = "Outgoing";
                Contact outgoingCall = new Contact(contactName, phoneNumberValue, callType);
                OUTGOING_CALLS_ARRAY.add(outgoingCall);
            } else if (Integer.parseInt(callTypeValue) == MISSED_CALL
                    && phoneNumberValue.equals(phoneNumber)) {
                callType = "Missed";
                Contact missedCall = new Contact(contactName, phoneNumberValue, callType);
                MISSED_CALLS_ARRAY.add(missedCall);
            }
        }

        mCursor.close();

    }

    public static int getIncomingCallsCount() {
        return INCOMING_CALLS_ARRAY.size();
    }

    public static int getOutgoingCallsCount() {
        return OUTGOING_CALLS_ARRAY.size();
    }

    public static int getMissedCallsCount() {
        return MISSED_CALLS_ARRAY.size();
    }


    // Retrieve the contact name from the phone address book.
    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));

        // Filter the columns by the display name.
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName = "";

        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }

}

