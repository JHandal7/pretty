package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suusoft on 25/07/2017.
 */

public class ProductOption implements Parcelable {
    private String id;
    private String name;
    private String price;
    private boolean isSelected;

    public ProductOption() {
    }

    protected ProductOption(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readString();
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductOption> CREATOR = new Creator<ProductOption>() {
        @Override
        public ProductOption createFromParcel(Parcel in) {
            return new ProductOption(in);
        }

        @Override
        public ProductOption[] newArray(int size) {
            return new ProductOption[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        if (price == null || (price != null && price.isEmpty())) {
            price = "0";
        }
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
