package com.example.android.calogan;

public class Contact {
    private String mContactName;
    private String mPhoneNumber;
    private int mCallCount;

    public Contact(String contactName, String phoneNumber, int callCount) {
        mContactName = contactName;
        mPhoneNumber = phoneNumber;
        mCallCount = callCount;
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

    public int getCallCount() {
        return mCallCount;
    }

    public void setCallCount(int callCount) {
        mCallCount = callCount;
    }
}
