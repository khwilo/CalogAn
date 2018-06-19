package com.example.android.calogan;

public class Contact {
    private String mContactName;
    private String mPhoneNumber;

    public Contact(String contactName, String phoneNumber) {
        mContactName = contactName;
        mPhoneNumber = phoneNumber;
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
}
