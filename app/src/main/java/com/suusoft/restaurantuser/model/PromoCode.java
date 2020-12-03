package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suusoft on 22/08/2017.
 */

public class PromoCode implements Parcelable {
    private String id;
    private String code;
    private String title;
    private int sale;
    @SerializedName("overview")
    private String sale_text;
    @SerializedName("is_active_code")
    private int isActive;
    private boolean isSelected;
    private String category_id;
    private double amount_lowest;


    protected PromoCode(Parcel in) {
        id = in.readString();
        code = in.readString();
        title = in.readString();
        sale = in.readInt();
        isActive = in.readInt();
        isSelected = in.readByte() != 0;
        sale_text = in.readString();
        category_id = in.readString();
        amount_lowest = in.readDouble();
    }

    public static final Creator<PromoCode> CREATOR = new Creator<PromoCode>() {
        @Override
        public PromoCode createFromParcel(Parcel in) {
            return new PromoCode(in);
        }

        @Override
        public PromoCode[] newArray(int size) {
            return new PromoCode[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSale() {
        return sale;
    }

    public String getDetailPromoCode() {
        if (sale == 0) {
            return sale_text;
        }
        return "Sale " + sale + "%";
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public boolean getIsActive() {
        return isActive == 1 ? true : false;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSale_text() {
        return sale_text;
    }

    public void setSale_text(String sale_text) {
        this.sale_text = sale_text;
    }

    public String getCategory_id() {
        return category_id;
    }

    public double getAmount_lowest() {
        return amount_lowest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(code);
        parcel.writeString(title);
        parcel.writeInt(sale);
        parcel.writeInt(isActive);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
        parcel.writeString(sale_text);
        parcel.writeString(category_id);
        parcel.writeDouble(amount_lowest);
    }
}
