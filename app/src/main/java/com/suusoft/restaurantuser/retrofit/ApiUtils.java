package com.suusoft.restaurantuser.retrofit;


import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;

public class ApiUtils {

    public static final String BASE_URL = AppController.getInstance().getString(R.string.URL_API);

    public static final String TAG = ApiUtils.class.getSimpleName ();

    public static APIService getAPIService() {
        //Log.e (TAG, "getAPIService: " + RetrofitClient.getClient (BASE_URL).toString () );
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);

    }

}
