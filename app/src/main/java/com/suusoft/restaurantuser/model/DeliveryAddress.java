package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suusoft on 25/08/2017.
 */

public class DeliveryAddress implements Parcelable {
    private String id;
    private String address;
    private double lat;
    @SerializedName("long")
    private double longitude;
    private boolean isSelected;

    public DeliveryAddress(String address, double lat, double longitude) {
        this.address = address;
        this.lat = lat;
        this.longitude = longitude;
    }


    protected DeliveryAddress(Parcel in) {
        address = in.readString();
        lat = in.readDouble();
        longitude = in.readDouble();
        isSelected = in.readByte() != 0;
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeDouble(lat);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryAddress> CREATOR = new Creator<DeliveryAddress>() {
        @Override
        public DeliveryAddress createFromParcel(Parcel in) {
            return new DeliveryAddress(in);
        }

        @Override
        public DeliveryAddress[] newArray(int size) {
            return new DeliveryAddress[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
