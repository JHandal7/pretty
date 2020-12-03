package com.suusoft.restaurantuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Suusoft on 14/08/2017.
 */

public class Order implements Parcelable {
    private String id;
    @SerializedName("order_status")
    private String status;
    @SerializedName("order_date")
    private String created_date;
    @SerializedName("order_item")
    private ArrayList<Food> listFoods;
    @SerializedName("price_total")
    private double totalPrice;
    @SerializedName("billing_address")
    private String delivery_address;
    @SerializedName("payment_method")
    private String method_payment;
    @SerializedName("billing_name")
    private String name;
    @SerializedName("billing_phone")
    private String phone;
    private String delivery_from;
    private String shipping_time;
    private double price_shipping;
    private double shipping_rider_tip;
    private String promotion_code;
    private int sale;
    private String sale_text;


    public Order(String id, String status, String created_date, ArrayList<Food> listFoods, double totalPrice, String delivery_address, String method_payment, String delivery_from) {
        this.id = id;
        this.status = status;
        this.created_date = created_date;
        this.listFoods = listFoods;
        this.totalPrice = totalPrice;
        this.delivery_address = delivery_address;
        this.method_payment = method_payment;
        this.delivery_from = delivery_from;
    }


    protected Order(Parcel in) {
        id = in.readString();
        status = in.readString();
        created_date = in.readString();
        listFoods = in.createTypedArrayList(Food.CREATOR);
        totalPrice = in.readDouble();
        delivery_address = in.readString();
        method_payment = in.readString();
        delivery_from = in.readString();
        shipping_time = in.readString();
        price_shipping = in.readDouble();
        shipping_rider_tip = in.readDouble();
        promotion_code = in.readString();
        sale = in.readInt();
        sale_text = in.readString();
        name = in.readString();
        phone = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public ArrayList<Food> getListFoods() {
        if (listFoods == null) {
            listFoods = new ArrayList<>();
        }
        return listFoods;
    }

    public void setListFoods(ArrayList<Food> listFoods) {
        this.listFoods = listFoods;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getMethod_payment() {
        return method_payment;
    }

    public void setMethod_payment(String method_payment) {
        this.method_payment = method_payment;
    }

    public String getDelivery_from() {
        return delivery_from;
    }

    public void setDelivery_from(String delivery_from) {
        this.delivery_from = delivery_from;
    }

    public long getShipping_time() {
        if (shipping_time == null) {
            return 0;
        } else if (shipping_time.isEmpty()) {
            return 0;
        } else {
            return Long.parseLong(shipping_time);
        }

    }

    public void setShipping_time(String shipping_time) {
        this.shipping_time = shipping_time;
    }

    public double getPrice_shipping() {
        return price_shipping;
    }

    public void setPrice_shipping(double price_shipping) {
        this.price_shipping = price_shipping;
    }

    public double getShipping_rider_tip() {
        return shipping_rider_tip;
    }

    public void setShipping_rider_tip(double shipping_rider_tip) {
        this.shipping_rider_tip = shipping_rider_tip;
    }

    public String getPromotion_code() {
        return promotion_code;
    }

    public int getSale() {
        return sale;
    }

    public String getSale_text() {
        return sale_text;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(status);
        parcel.writeString(created_date);
        parcel.writeTypedList(listFoods);
        parcel.writeDouble(totalPrice);
        parcel.writeString(delivery_address);
        parcel.writeString(method_payment);
        parcel.writeString(delivery_from);
        parcel.writeString(shipping_time);
        parcel.writeDouble(price_shipping);
        parcel.writeDouble(shipping_rider_tip);
        parcel.writeString(promotion_code);
        parcel.writeInt(sale);
        parcel.writeString(sale_text);
        parcel.writeString(name);
        parcel.writeString(phone);
    }
}
