package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suusoft on 11/08/2017.
 */

public class Comment implements Parcelable {
    private String id;
    @SerializedName("created_user")
    private String nameUser;
    @SerializedName("created_date")
    private String created_date;
    @SerializedName("comment")
    private String content;
    private String image;

    public Comment(String id, String nameUser, String created_date, String content) {
        this.id = id;
        this.nameUser = nameUser;
        this.created_date = created_date;
        this.content = content;
    }

    protected Comment(Parcel in) {
        id = in.readString();
        nameUser = in.readString();
        created_date = in.readString();
        content = in.readString();
        image = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        if (image == null) {
            image = "";
        }
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nameUser);
        parcel.writeString(created_date);
        parcel.writeString(content);
        parcel.writeString(image);
    }
}
