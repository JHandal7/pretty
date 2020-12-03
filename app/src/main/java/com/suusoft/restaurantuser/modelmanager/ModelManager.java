package com.suusoft.restaurantuser.modelmanager;

import android.content.Context;
import android.net.Uri;

import com.android.volley.RequestQueue;
import com.suusoft.restaurantuser.listener.IResponse;
import com.suusoft.restaurantuser.listener.ModelManagerListener;
import com.suusoft.restaurantuser.network.VolleyRequestManager;
import com.suusoft.restaurantuser.retrofit.Param;

public class ModelManager {
    private static final String PARAM_FIELDS = "fields";
    private static final String PARAM_ACCESS_TOKEN = "access_token";

    public static void getFacebookInfo(Context ctx, RequestQueue volleyQueue, String accessToken,
                                       final ModelManagerListener listener) {

        String url = Param.URL_GET_FACEBOOK_INFO;

        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter(PARAM_FIELDS, "id,name,email,first_name,last_name,picture");
        builder.appendQueryParameter(PARAM_ACCESS_TOKEN, accessToken);

        new VolleyRequestManager(ctx, true, false).getJSONObject(builder, volleyQueue, new IResponse() {
            @Override
            public void onResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response);
                } else {
                    listener.onError();
                }
            }
        });
    }
}
