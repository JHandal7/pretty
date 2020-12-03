package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suusoft on 18/07/2017.
 */

public class Promotion implements Parcelable {
    private String id;
    @SerializedName("sale")
    private int discount;
    @SerializedName("overview")
    private String description;
    @SerializedName("favourite")
    private int favourite;
    @SerializedName("quantity_comment")
    private int numComment;
    @SerializedName("created_date")
    private String timeComment;
    private String image;
    private String sale_text;
    private int is_favourite;
    @SerializedName("code")
    private String promoCode;
    private String name;

    public Promotion(String id, int discount, String description, int like, int comment, String timeComment, String image) {
        this.id = id;
        this.discount = discount;
        this.description = description;
        this.favourite = like;
        this.numComment = comment;
        this.timeComment = timeComment;
        this.image = image;
    }

    protected Promotion(Parcel in) {
        id = in.readString();
        discount = in.readInt();
        description = in.readString();
        favourite = in.readInt();
        numComment = in.readInt();
        timeComment = in.readString();
        image = in.readString();
        sale_text = in.readString();
        is_favourite = in.readInt();
        promoCode = in.readString();
        name = in.readString();
    }

    public static final Creator<Promotion> CREATOR = new Creator<Promotion>() {
        @Override
        public Promotion createFromParcel(Parcel in) {
            return new Promotion(in);
        }

        @Override
        public Promotion[] newArray(int size) {
            return new Promotion[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getSale_text() {
        return sale_text;
    }

    public String getDetailSale() {
        if (discount == 0 || discount > 100) {
            return sale_text;
        }
        return String.format("%1$s%2$s Discount", String.valueOf(discount), "%");
    }

    public void setSale_text(String sale_text) {
        this.sale_text = sale_text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public int getNumComment() {
        return numComment;
    }

    public void setNumComment(int numComment) {
        this.numComment = numComment;
    }

    public String getTimeComment() {
        return timeComment;
    }

    public void setTimeComment(String timeComment) {
        this.timeComment = timeComment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getIs_favourite() {
        return is_favourite == 1 ? true : false;
    }

    public void setIs_favourite(int is_favourite) {
        this.is_favourite = is_favourite;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeInt(discount);
        parcel.writeString(description);
        parcel.writeInt(favourite);
        parcel.writeInt(numComment);
        parcel.writeString(timeComment);
        parcel.writeString(image);
        parcel.writeString(sale_text);
        parcel.writeInt(is_favourite);
        parcel.writeString(promoCode);
        parcel.writeString(name);
    }
}
