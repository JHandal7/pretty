package com.suusoft.restaurantuser.retrofit.respone;

import com.suusoft.restaurantuser.model.Order;

public class ResponeOrder extends BaseModel {
    public Order data;

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }
}
