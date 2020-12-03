package com.suusoft.restaurantuser.modelmanager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.configs.Apis;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.model.User;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;

import java.util.HashMap;

/**
 */
public class RequestManager extends BaseRequest {

    private static final String TAG = RequestManager.class.getSimpleName();

    // Params
    private static final String PARAM_GCM_ID = "gcm_id";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_STATUS = "status";
    private static final String PARAM_IMEI = "ime";
    //register account, login,logout, change password,reset password
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_LAST_NAME = "last_name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_LOGIN_TYPE = "login_type";
    private static final String PARAM_TOKEN = "token";
    private static final String PARAM_CURRENT_PASSWORD = "current_password";
    private static final String PARAM_NEW_PASSWORD = "new_password";
    private static final String PARAM_APP_TYPE = "app_type";

    //GET DATA
    private static final String PARAM_CATEGORY_ID = "category_Id";
    private static final String PARAM_KEY_WORD = "keyword";

    private static final String PARAM_EMAIL = "email";

    private static final String PARAM_PROMO_CODE = "code";

    private static final String PARAM_OBJECTID = "objectid";
    private static final String PARAM_OBJECT_ID = "object_id";
    private static final String PARAM_OBJECT_TYPE = "object_type";
    private static final String PARAM_COMMENT = "comment";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_CREATED_USER = "created_user";

    //order
    private static final String PARAM_LATITUDE = "lat";
    private static final String PARAM_LONGITUDE = "long";
    public static final String PARAM_TEL = "tel";
    public static final String PARAM_DELIVERY_PRICE = "delivery_price";
    public static final String PARAM_DATA = "data";
    public static final String PARAM_PROMOTION_CODE = "promotion_code";
    public static final String PARAM_RIDER_TIP = "rider_tip";
    public static final String PARAM_SHIPPING_TIME = "shipping_time";
    public static final String PARAM_PAYMENT_TYPE = "payment_type";
    public static final String PARAM_ORDER_DATE = "order_date";
    public static final String PARAM_PRICE_TOTAL = "price_total";
    public static final String PARAM_METHOD = "method";
    public static final String PARAM_AMOUNT = "amount";


    private static final String PARAM_FULLNAME = "fullname";
    private static final String PARAM_IMAGE = "image";


    private static final String PARAM_MESSAGE = "message";


    private static final String PARAM_PAGE = "page";
    private static final String PARAM_ID = "id";
    private static final String PARAM_DESCRIPTION = "description";

    //temp
    private static final String PARAM_TASK_ID = "task_id";

    ///////////////////////////////////////////////////////////////////////////
    // Add functions connecting to server
    ///////////////////////////////////////////////////////////////////////////

    public static void registerDevice(String gcm, String ime, final CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_IMEI, ime);
        params.put(PARAM_GCM_ID, gcm);
        params.put(PARAM_TYPE, "1");// 1 is android
        params.put(PARAM_STATUS, "1");

        get(Apis.URL_REGISTER_DEVICE, params, false, completeListener);
    }

    public static void register(Context context, String email, String password, String name, String address, String phone, final CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_USERNAME, email);
        params.put(PARAM_PASSWORD, password);
        params.put(PARAM_NAME, name);
//        params.put(PARAM_LAST_NAME, lastName);
        params.put(PARAM_ADDRESS, address);
        params.put(PARAM_PHONE, phone);
        get(Apis.URL_REGISTER, params, completeListener);
    }

    public static void login(Context context, String username, String password, String force_login, final CompleteListener completeListener) {
        login(context, username, password, "n", null, null, null, null, force_login, completeListener);
    }

    public static void login(Context context, String username, String firstName, String lastName, String phone, String avatar, String force_login, final CompleteListener completeListener) {
        login(context, username, null, "s", firstName, lastName, phone, avatar, force_login, completeListener);
    }

    public static void login(Context context, String username, String password, String type, String firstName, String lastName, String phone, String avatar, String force_login, final CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_LOGIN_TYPE, type);
        if (type.equals("s")) {
            params.put(PARAM_NAME, firstName);
            params.put(PARAM_LAST_NAME, lastName);
            params.put(PARAM_PHONE, phone);
            params.put("avatar", avatar);
        } else if (type.equals("n")) {
            params.put(PARAM_PASSWORD, password);
        }
        params.put(PARAM_APP_TYPE, "user");
        params.put(PARAM_USERNAME, username);
        params.put(PARAM_IMEI, AppUtil.getMacAddress(context));
        params.put(PARAM_GCM_ID, DataStoreManager.getTokenFCM());
        params.put(PARAM_TYPE, "1");
        params.put(PARAM_STATUS, "1");
        params.put("force_login", force_login);
        get(Apis.URL_LOGIN, params, completeListener);
    }

    public static void resetPassword(String email, CompleteListener completeListener) {

        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_EMAIL, email);
        get(Apis.URL_RESET_PASSWORD, params, completeListener);
    }

    public static void changePassword(String currentPassword, String newPassword, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        params.put(PARAM_CURRENT_PASSWORD, currentPassword);
        params.put(PARAM_NEW_PASSWORD, newPassword);
        get(Apis.URL_CHANGE_PASSWORD, params, completeListener);
    }

    public static void updateProfile(String firstName, String address, String email, String phone, String filePath, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        params.put(PARAM_NAME, firstName);
        params.put(PARAM_ADDRESS, address);
        params.put(PARAM_EMAIL, email);
        params.put(PARAM_PHONE, phone);
        HashMap<String, String> files = new HashMap<>();
        if (filePath != null) {
            files.put("avatar", filePath);
        }
        multipart(Apis.URL_UPDATE_PROFILE, params, files, true, completeListener);
    }

    public static void logOut(CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        get(Apis.URL_LOGOUT, params, completeListener);
    }

    // GET DATA
    public static void getListCategory(CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        get(Apis.URL_LIST_CATEGORY, params, false, completeListener);
    }

    public static void getListFoodByCategory(String id, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_CATEGORY_ID, id);
        get(Apis.URL_LIST_FOOD_BY_CATEGORY, params, false, completeListener);
    }

    public static void getListPromotions(CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        get(Apis.URL_LIST_PROMOTIONS, params, false, completeListener);
    }

    public static void getDetailPromotions(String id, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_OBJECTID, id);
        get(Apis.URL_LIST_PROMOTIONS, params, false, completeListener);
    }

    public static void searchFoods(String keyword, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_WORD, keyword);
        get(Apis.URL_SEARCH_FOOD, params, false, completeListener);
    }

    public static void addComment(String idPromo, String comment, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_OBJECT_ID, idPromo);
        params.put(PARAM_OBJECT_TYPE, "promotion");
        params.put(PARAM_USER_ID, DataStoreManager.getUser().id);
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        params.put(PARAM_COMMENT, comment);
        params.put(PARAM_CREATED_USER, DataStoreManager.getUser().getFirstName() + " " + DataStoreManager.getUser().getLastName());
        post(Apis.URL_ADD_COMMENT, params, true, completeListener);
    }

    public static void getListCommentByPromotion(String id, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_OBJECTID, id);
        get(Apis.URL_LIST_COMMENT, params, true, completeListener);
    }

    public static void favouritePromotion(String id, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_OBJECT_ID, id);
        params.put(PARAM_OBJECT_TYPE, "promotion");
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        get(Apis.URL_FAVOURITE, params, true, completeListener);
    }

    public static void addPromoCode(String promocode, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_PROMO_CODE, promocode);
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        get(Apis.URL_ADD_PROMO_CODE, params, true, completeListener);
    }


    public static void getListPromoCode(String type, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        params.put(PARAM_TYPE, type);
        if (type.equals(Constant.ALL_PROMO_CODE_ACTIVE))
            params.put("is_active", "1");
        get(Apis.URL_LIST_PROMO_CODE, params, false, completeListener);
    }

    public static void deletePromoCode(String idPromoCode, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "delete");
        params.put(PARAM_OBJECTID, idPromoCode);
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        get(Apis.URL_LIST_PROMO_CODE, params, true, completeListener);
    }

    public static void checkPromoCode(String promoCode, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("code", promoCode);
        params.put("date", String.valueOf(System.currentTimeMillis() / 1000));
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        get(Apis.URL_CHECK_PROMO_CODE, params, true, completeListener);
    }

    public static void getListAddress(CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        get(Apis.URL_LIST_ADDRESS, params, false, completeListener);
    }

    public static void addNewAddress(String address, String lat, String longitude, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_ADDRESS, address);
        params.put(PARAM_LATITUDE, lat);
        params.put(PARAM_LONGITUDE, longitude);
        params.put(PARAM_PHONE, DataStoreManager.getUser().getPhone());
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        get(Apis.URL_ADD_ADDRESS, params, true, completeListener);
    }

    public static void deleteAddress(String idAddress, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "delete");
        params.put(PARAM_OBJECTID, idAddress);
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        get(Apis.URL_LIST_ADDRESS, params, false, completeListener);
    }

    public static void getOpenHours(CompleteListener completeListener) {
        getOpenHours(false, completeListener);
    }

    public static void getOpenHours(boolean isAll, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        if (!isAll) {
            params.put("today", String.valueOf(System.currentTimeMillis() / 1000));
        }
        get(Apis.URL_OPEN_HOURS, params, true, completeListener);
    }

    public static void order(Context context, String address, String lat, String longitude, String promoCode, String riderTip, String deliveryFee, String timeDelivery, String paymentType, String price_total, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        User user = DataStoreManager.getUser();
        if (user != null) {
            params.put(PARAM_TOKEN, DataStoreManager.getToken());
            params.put(PARAM_TEL, user.getPhone());
            params.put(PARAM_EMAIL, user.getEmail());
            params.put(PARAM_ADDRESS, address);
            params.put(PARAM_LATITUDE, lat);
            params.put(PARAM_LONGITUDE, longitude);
            params.put(PARAM_NAME, user.getName());
            params.put(PARAM_LAST_NAME, user.getLastName());
            params.put(PARAM_DELIVERY_PRICE, deliveryFee);
            params.put(PARAM_DATA, gson.toJson(AppController.getInstance().cartManager.getCart()));
            if (promoCode != null && !promoCode.isEmpty()) {
                params.put(PARAM_PROMOTION_CODE, promoCode);
            }
            params.put(PARAM_RIDER_TIP, riderTip);
            params.put(PARAM_SHIPPING_TIME, timeDelivery);
            params.put(PARAM_PAYMENT_TYPE, paymentType);
            params.put(PARAM_ORDER_DATE, String.valueOf(System.currentTimeMillis() / 1000));
            params.put(PARAM_PRICE_TOTAL, price_total);

            post(Apis.URL_ORDER, params, false, completeListener);
        } else {
            AppUtil.showToast(context, R.string.msg_have_some_error);
        }

    }

    public static void order(Context context, String name, String email, String address, String lat, String longitude, String promoCode, String riderTip, String deliveryFee, String timeDelivery, String paymentType, String price_total, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        User user = DataStoreManager.getUser();
        if (user != null) {
            params.put(PARAM_TOKEN, DataStoreManager.getToken());
            params.put(PARAM_TEL, "0969824911");
            params.put(PARAM_EMAIL, email);
            params.put(PARAM_ADDRESS, address);
            params.put(PARAM_LATITUDE, lat);
            params.put(PARAM_LONGITUDE, longitude);
            params.put(PARAM_NAME, name);
            params.put(PARAM_LAST_NAME, "Suusoft");
            params.put(PARAM_DELIVERY_PRICE, deliveryFee);
            params.put(PARAM_DATA, gson.toJson(AppController.getInstance().cartManager.getCart()));
            if (promoCode != null && !promoCode.isEmpty()) {
                params.put(PARAM_PROMOTION_CODE, promoCode);
            }
            params.put(PARAM_RIDER_TIP, riderTip);
            params.put(PARAM_SHIPPING_TIME, timeDelivery);
            params.put(PARAM_PAYMENT_TYPE, paymentType);
            params.put(PARAM_ORDER_DATE, String.valueOf(System.currentTimeMillis() / 1000));
            params.put(PARAM_PRICE_TOTAL, price_total);

            post(Apis.URL_ORDER, params, false, completeListener);
        } else {
            AppUtil.showToast(context, R.string.msg_have_some_error);
        }

    }

    public static void getListOrder(CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        get(Apis.URL_LIST_ORDER, params, false, completeListener);
    }

    public static void getDetailOrder(String id, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        params.put(PARAM_OBJECTID, id);
        get(Apis.URL_LIST_ORDER, params, false, completeListener);
    }

    public static void getListPaymentMolllie(CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        get(Apis.URL_LIST_PAYMENT_MOLLIE, params, false, completeListener);
    }

    public static void getPaymentMolllie(String id, String amount, String methodPayment, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, DataStoreManager.getToken());
        params.put(PARAM_OBJECTID, id);
        params.put(PARAM_AMOUNT, amount);
        params.put(PARAM_METHOD, methodPayment);
        params.put("description", "TEst");
        post(Apis.URL_PAYMENT_MOLLIE, params, false, completeListener);
    }

    public static void comfirmPayment(String url, CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        get(url, params, true, completeListener);
    }

    public static void getListProduct(Context context, String type, String page, final CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TYPE, type);
        params.put(PARAM_PAGE, page);

        get(Apis.URL_GET_ALL, params, true, false, completeListener);
    }

    public static void getDetail(Context context, String id, final CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TASK_ID, id);
        get(Apis.URL_TASK_DETAIL, params, completeListener);
    }

    public static void postFile(Context context, String filePath, String extention, final CompleteListener completeListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "abczzz1");
        params.put("password", "abczzz1");
        params.put("email", "abczzz1@gmail.com");
        params.put("full_name", "DC Suusoft");
        params.put("extention", extention);
        HashMap<String, String> files = new HashMap<>();
        files.put("image", filePath);
        multipart("http://27.72.88.241:8888/mp3online-shiva/index.php/api/register", params, files, true, completeListener);
    }

}
