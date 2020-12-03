package com.suusoft.restaurantuser.configs;

import android.content.Context;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;

/**
 * Created by Suusoft
 */
public class Apis {

    //private static final String APP_DOMAIN = "http://projectemplate.info/_test/moza-fastfood-rex/backend/web/";
    //private static final String APP_DOMAIN = "http://fastfood.suusoft.com/backend/web/"; /*index.php/*/

    private static final String APP_DOMAIN = AppController.getInstance().getString(R.string.URL_API) + "backend/web/"; /*index.php/*/

    public static final String URL_IMAGE_DEFAUNT = "no_image.jpg";
    // Urls
    public static final String APP = "app/";
    public static final String API = "api/";
    public static final String ECOMMERCE = "ecommerce/";

    //User
    public static final String URL_REGISTER_DEVICE = APP_DOMAIN + APP + API + "device";
    public static final String URL_LOGIN = APP_DOMAIN + APP + API + "login";
    public static final String URL_LOGOUT = APP_DOMAIN + APP + API + "logout";
    public static final String URL_REGISTER = APP_DOMAIN + APP + API + "dang-ky";
    public static final String URL_RESET_PASSWORD = APP_DOMAIN + APP + API + "forget-password";
    public static final String URL_CHANGE_PASSWORD = APP_DOMAIN + APP + API + "change-password";
    public static final String URL_UPDATE_PROFILE = APP_DOMAIN + APP + API + "update-profile";


    //get data
    public static final String URL_LIST_CATEGORY = APP_DOMAIN + API + "category";
    public static final String URL_LIST_FOOD_BY_CATEGORY = APP_DOMAIN + ECOMMERCE + API + "list-by-category";
    public static final String URL_LIST_PROMOTIONS = APP_DOMAIN + ECOMMERCE + API + "list-promotion";
    public static final String URL_SEARCH_FOOD = APP_DOMAIN + ECOMMERCE + API + "list-food";

    //comment
    public static final String URL_ADD_COMMENT = APP_DOMAIN + APP + API + "add-comment";
    public static final String URL_LIST_COMMENT = APP_DOMAIN + APP + API + "list-comment";
    public static final String URL_FAVOURITE = APP_DOMAIN + API + "favourite";

    public static final String URL_OPEN_HOURS = APP_DOMAIN + ECOMMERCE + API + "open-hour";

    //promo code
    public static final String URL_ADD_PROMO_CODE = APP_DOMAIN + ECOMMERCE + API + "add-promotion-code";
    public static final String URL_LIST_PROMO_CODE = APP_DOMAIN + ECOMMERCE + API + "list-promotion-code";
    public static final String URL_DEL_PROMO_CODE = APP_DOMAIN + ECOMMERCE + API + "list-promotion-code";
    public static final String URL_CHECK_PROMO_CODE = APP_DOMAIN + ECOMMERCE + API + "check-promotion-code";


    //delivery address
    public static final String URL_ADD_ADDRESS = APP_DOMAIN + APP + API + "add-address-delivery";
    public static final String URL_LIST_ADDRESS = APP_DOMAIN + APP + API + "list-address-delivery";

    //order
    public static final String URL_ORDER = APP_DOMAIN + ECOMMERCE + API + "order";
    public static final String URL_LIST_ORDER = APP_DOMAIN + ECOMMERCE + API + "list-order";
    public static final String URL_LIST_PAYMENT_MOLLIE = APP_DOMAIN + ECOMMERCE + API + "list-payment-mollie";
    public static final String URL_PAYMENT_MOLLIE = APP_DOMAIN + ECOMMERCE + API + "payment-mollie";


    public static final String URL_GET_ALL = APP_DOMAIN + "browseTask";

    //temp
    public static final String API_BROWTASK = "browseTask";
    public static final String URL_TASK_DETAIL = APP_DOMAIN + "taskDetail";

}
