package com.suusoft.restaurantuser.retrofit.respone;

import com.suusoft.restaurantuser.model.Order;

import java.util.ArrayList;

public class ResponeListOrder extends BaseModel {
    public ArrayList<Order> data;

    public ArrayList<Order> getData() {
        return data;
    }

    public void setData(ArrayList<Order> data) {
        this.data = data;
    }
}
