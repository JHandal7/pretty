package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Suusoft on 25/07/2017.
 */

public class ProductVersion implements Parcelable {
    private String id;
    private String name;
    private String price;
    @SerializedName("topping")
    private ArrayList<ProductOption> product_option;
    private boolean isSelected;
    private int is_default;

    protected ProductVersion(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readString();
        product_option = in.createTypedArrayList(ProductOption.CREATOR);
        isSelected = in.readByte() != 0;
        is_default = in.readInt();
    }

    public ProductVersion() {
    }

    public static final Creator<ProductVersion> CREATOR = new Creator<ProductVersion>() {
        @Override
        public ProductVersion createFromParcel(Parcel in) {
            return new ProductVersion(in);
        }

        @Override
        public ProductVersion[] newArray(int size) {
            return new ProductVersion[size];
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
            return "0";
        }
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<ProductOption> getProduct_option() {
        if (product_option == null) {
            product_option = new ArrayList<>();
        }
        return product_option;
    }

    public void setProduct_option(ArrayList<ProductOption> product_option) {
        this.product_option = product_option;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isDefault() {
        return is_default == 0 ? false : true;
    }

    public boolean isHasOption() {
        if (product_option == null) {
            product_option = new ArrayList<>();
        }
        return product_option.isEmpty() ? false : true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeTypedList(product_option);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
        parcel.writeInt(is_default);
    }

}
