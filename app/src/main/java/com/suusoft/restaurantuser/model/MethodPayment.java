package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suusoft on 12/09/2017.
 */

public class MethodPayment implements Parcelable{
    private String id;
    private String description;
    private InfoImage image;

    protected MethodPayment(Parcel in) {
        id = in.readString();
        description = in.readString();
        image = in.readParcelable(InfoImage.class.getClassLoader());
    }

    public static final Creator<MethodPayment> CREATOR = new Creator<MethodPayment>() {
        @Override
        public MethodPayment createFromParcel(Parcel in) {
            return new MethodPayment(in);
        }

        @Override
        public MethodPayment[] newArray(int size) {
            return new MethodPayment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(description);
        parcel.writeParcelable(image, i);
    }


    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public InfoImage getImage() {
        return image;
    }
}
