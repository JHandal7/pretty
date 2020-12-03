package com.suusoft.restaurantuser.retrofit.respone;

import com.suusoft.restaurantuser.model.OpenHour;

import java.util.ArrayList;

public class ResponeOpenHouse extends BaseModel {
    public ArrayList<OpenHour> data;

    public ArrayList<OpenHour> getData() {
        return data;
    }

    public void setData(ArrayList<OpenHour> data) {
        this.data = data;
    }
}
