package com.suusoft.restaurantuser.base.model;

import com.google.gson.Gson;

/**
 * Created by Suusoft on 1/8/2016.
 */
public class BaseModel {

    public String toJSon(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
}
