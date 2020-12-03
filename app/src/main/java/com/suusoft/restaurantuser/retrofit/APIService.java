package com.suusoft.restaurantuser.retrofit;


import android.content.Context;

import com.google.android.gms.common.api.Api;
import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.retrofit.respone.ResponeListOrder;
import com.suusoft.restaurantuser.retrofit.respone.ResponeOpenHouse;
import com.suusoft.restaurantuser.retrofit.respone.ResponeOrder;
import com.suusoft.restaurantuser.retrofit.respone.ResponeRegister;
import com.suusoft.restaurantuser.retrofit.respone.ResponeUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static com.suusoft.restaurantuser.retrofit.Param.ADDRESS;
import static com.suusoft.restaurantuser.retrofit.Param.EMAIL;
import static com.suusoft.restaurantuser.retrofit.Param.NAME;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_ADDRESS;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_DATA;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_DELIVERY_PRICE;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_LAST_NAME;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_LATITUDE;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_LONGITUDE;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_NAME;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_ORDER_DATE;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_PASSWORD;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_PAYMENT_TYPE;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_PHONE;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_PRICE_TOTAL;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_PROMOTION_CODE;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_RIDER_TIP;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_SHIPPING_TIME;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_TEL;
import static com.suusoft.restaurantuser.retrofit.Param.PARAM_USERNAME;
import static com.suusoft.restaurantuser.retrofit.Param.PHONE;
import static com.suusoft.restaurantuser.retrofit.Param.TOKEN;


public interface APIService {
    String DOMAIN = "backend/web/";
    String APP = "app/";
    String API = "api/";
    String ECOMMERCE = "ecommerce/";
    String URL_UPDATE_PROFILE = DOMAIN + APP + API + "update-profile";
    String URL_ORDER = DOMAIN + ECOMMERCE + API + "order";
    String URL_OPEN_HOURS = DOMAIN + ECOMMERCE + API + "open-hour";
    String URL_LIST_ORDER = DOMAIN + ECOMMERCE + API + "list-order";
    String URL_REGISTER = DOMAIN + APP + API + "dang-ky";







    @GET(URL_UPDATE_PROFILE)
    @Headers("Cache-Control:no-cache") // no cache
    Call<ResponeUser> updateprofile(
            @Query(NAME) String name,
            @Query(ADDRESS) String address,
            @Query(EMAIL) String email,
            @Query(PHONE) String phone,
            @Query(TOKEN) String token
    );

    @GET(URL_ORDER)
    @Headers("Cache-Control:no-cache") // no cache
    Call<ResponeOrder> order(
            @Query(PARAM_TEL) String tel,
            @Query(EMAIL) String email,
            @Query(ADDRESS) String address,
            @Query(PARAM_LATITUDE) String lat,
            @Query(PARAM_LONGITUDE) String longitude,
            @Query(NAME) String name,
            @Query(PARAM_LAST_NAME) String last_name,
            @Query(PARAM_DELIVERY_PRICE) String deliveryFee,
            @Query(PARAM_DATA) String data,
            @Query(PARAM_PROMOTION_CODE) String promotion_code,
            @Query(PARAM_RIDER_TIP) String rider_tip,
            @Query(PARAM_SHIPPING_TIME) String shipping_time,
            @Query(PARAM_PAYMENT_TYPE) String payment_type,
            @Query(PARAM_ORDER_DATE) String order_date,
            @Query(PARAM_PRICE_TOTAL) String price_total,
            @Query(TOKEN) String token
    );

    @GET(URL_LIST_ORDER)
    @Headers("Cache-Control:no-cache") // no cache
    Call<ResponeListOrder> getListOder(
            @Query(TOKEN) String token
    );

    @GET(URL_REGISTER)
    @Headers("Cache-Control:no-cache") // no cache
    Call<ResponeRegister> register(
            @Query(PARAM_USERNAME) String username,
            @Query(PARAM_PASSWORD) String password,
            @Query(PARAM_NAME) String name,
            @Query(PARAM_ADDRESS) String address,
            @Query(PARAM_PHONE) String phone
    );


}
