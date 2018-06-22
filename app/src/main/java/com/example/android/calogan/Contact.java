package com.example.android.calogan;

public class Contact {
    private String mContactName;
    private String mPhoneNumber;
    private String mCallType;
    private String mCallDate;

    public Contact(String contactName, String phoneNumber, String callType, String callDate) {
        mContactName = contactName;
        mPhoneNumber = phoneNumber;
        mCallType = callType;
        mCallDate = callDate;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String contactName) {
        mContactName = contactName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getCallType() {
        return mCallType;
    }

    public void setCallType(String callType) {
        mCallType = callType;
    }

    public String getCallDate() {
        return mCallDate;
    }

    public void setCallDate(String callDate) {
        mCallDate = callDate;
    }
}
