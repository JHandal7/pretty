package com.suusoft.restaurantuser;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.suusoft.restaurantuser.cart.CartManager;
import com.suusoft.restaurantuser.cart.CartUtils;
import com.suusoft.restaurantuser.datastore.BaseDataStore;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.network.VolleyRequestManager;
import com.suusoft.restaurantuser.widgets.textview.TypeFaceUtil;


/**
 * Created by Suusoft on 1/8/2016.
 */
public class AppController extends Application {

    // TODO: 7/11/2016 Need comment
    private static AppController instance;
    private String token;

    private int global;

    public static AppController getInstance() {
        return instance;
    }

    public CartManager cartManager;
    public double delivery = 5.00;
    public double amount_lowest;
    public double amount_promtion_lowest;
    public int isChoseAddressDelivery;
    public IForceLogin forceLoginListener;

    public interface IForceLogin {
        void cleanSystem();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BaseDataStore.init(this);
        VolleyRequestManager.init(this);
        BaseRequest.getInstance();
        TypeFaceUtil.getInstant().initTypeFace(this);
        cartManager = new CartUtils(this);
    }

    public String getToken() {
        if (token == null)
            token = DataStoreManager.getToken();
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getGlobal() {
        return global;
    }

    public void setGlobal(int global) {
        this.global = global;
    }

    public boolean isChoseAddressDelivery() {
        return isChoseAddressDelivery == 1 ? true : false;
    }

    public static void sendBroadcast(String action) {
        LocalBroadcastManager.getInstance(instance).sendBroadcast(new Intent(action));
    }

    public static void registerBroadcast(BroadcastReceiver broadcastReceiver, String action) {
        LocalBroadcastManager.getInstance(instance).registerReceiver(broadcastReceiver, new IntentFilter(action));
    }

    public static void unregisterBroadCast(BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager.getInstance(instance).unregisterReceiver(broadcastReceiver);
    }
    public IForceLogin getForceLoginListener() {
        return forceLoginListener;
    }

    public void setForceLoginListener(IForceLogin forceLoginListener) {
        this.forceLoginListener = forceLoginListener;
    }
}
