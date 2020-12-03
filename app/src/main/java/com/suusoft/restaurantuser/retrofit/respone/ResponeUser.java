package com.suusoft.restaurantuser.retrofit.respone;

import com.suusoft.restaurantuser.model.User;

public class ResponeUser extends BaseModel{
    public User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
