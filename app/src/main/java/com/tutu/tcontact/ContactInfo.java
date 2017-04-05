package com.tutu.tcontact;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tutu on 2017/3/11.
 */

public class ContactInfo implements Parcelable {
    private String name;// 姓名

    private String number;// 电话号码

    public ContactInfo(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    @Override
    public String toString() {
        return "ContactInfo{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.number);
    }

    public ContactInfo() {
    }

    protected ContactInfo(Parcel in) {
        this.name = in.readString();
        this.number = in.readString();
    }

    public static final Parcelable.Creator<ContactInfo> CREATOR = new Parcelable.Creator<ContactInfo>() {
        @Override
        public ContactInfo createFromParcel(Parcel source) {
            return new ContactInfo(source);
        }

        @Override
        public ContactInfo[] newArray(int size) {
            return new ContactInfo[size];
        }
    };
}
