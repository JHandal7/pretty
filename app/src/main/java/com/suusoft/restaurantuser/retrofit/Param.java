package com.suusoft.restaurantuser.retrofit;

public interface Param {
    public static final String URL_GET_FACEBOOK_INFO = "https://graph.facebook.com/me";
    public static final int REQUEST_TIME_OUT = 15000;

    // PARAM
    String TOKEN = "token";
    String NAME  = "name";
    String ADDRESS = "address";
    String PHONE = "phone";
    String EMAIL = "email";

    String PARAM_LATITUDE = "lat";
    String PARAM_LONGITUDE = "long";
    String PARAM_TEL = "tel";
    String PARAM_DELIVERY_PRICE = "delivery_price";
    String PARAM_DATA = "data";
    String PARAM_PROMOTION_CODE = "promotion_code";
    String PARAM_RIDER_TIP = "rider_tip";
    String PARAM_SHIPPING_TIME = "shipping_time";
    String PARAM_PAYMENT_TYPE = "payment_type";
    String PARAM_ORDER_DATE = "order_date";
    String PARAM_PRICE_TOTAL = "price_total";
    String PARAM_METHOD = "method";
    String PARAM_AMOUNT = "amount";
    String PARAM_LAST_NAME = "last_name";

    String PARAM_USERNAME = "username";
    String PARAM_NAME = "name";
    String PARAM_ADDRESS = "address";
    String PARAM_PHONE = "phone";
    String PARAM_PASSWORD = "password";
}
