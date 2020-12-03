package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suusoft on 17/07/2017.
 */

public class Category implements Parcelable {
    private String id;
    private String name;
    @SerializedName("object_type")
    private String nameTypeFood;
    @SerializedName("food_count")
    private int quantityFoods;
    private String image;

    public Category() {

    }

    public Category(String id, String name, String nameTypeFood, int quantityFoods, String image) {
        this.id = id;
        this.name = name;
        this.nameTypeFood = nameTypeFood;
        this.quantityFoods = quantityFoods;
        this.image = image;
    }

    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
        nameTypeFood = in.readString();
        quantityFoods = in.readInt();
        image = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
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

    public String getNameTypeFood() {
        return nameTypeFood;
    }

    public void setNameTypeFood(String nameTypeFood) {
        this.nameTypeFood = nameTypeFood;
    }

    public int getQuantityFoods() {
        return quantityFoods;
    }

    public void setQuantityFoods(int quantityFoods) {
        this.quantityFoods = quantityFoods;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(nameTypeFood);
        parcel.writeInt(quantityFoods);
        parcel.writeString(image);
    }
}
