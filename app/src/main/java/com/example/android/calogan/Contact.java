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

    @Override
    public String toString() {
        return "Contact Name: " + getContactName()  + "\n" +
                "Phone Number: " + getPhoneNumber() + "\n" +
                "Call Type: " + getCallType() + "\n" +
                "Call Date: " + getCallDate();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Contact) {
            Contact temp = (Contact) obj;
            if (this.mContactName.equals(temp.mContactName) &&
                    this.mPhoneNumber.equals(temp.mPhoneNumber) &&
                    this.mCallType.equals(temp.mCallType) &&
                    this.mCallDate.equals(temp.mCallDate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.mContactName.hashCode() +
                this.mPhoneNumber.hashCode() +
                this.mCallType.hashCode() +
                this.mCallDate.hashCode());
    }
}
