package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

public class InfoImage implements Parcelable {
    private String normal;
    private String bigger;

    protected InfoImage(Parcel in) {
        normal = in.readString();
        bigger = in.readString();
    }

    public static final Creator<InfoImage> CREATOR = new Creator<InfoImage>() {
        @Override
        public InfoImage createFromParcel(Parcel in) {
            return new InfoImage(in);
        }

        @Override
        public InfoImage[] newArray(int size) {
            return new InfoImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(normal);
        parcel.writeString(bigger);
    }

    public String getNormal() {
        return normal;
    }

    public String getBigger() {
        return bigger;
    }
}