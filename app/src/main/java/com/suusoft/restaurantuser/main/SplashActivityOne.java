package com.suusoft.restaurantuser.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.main.user.LoginActivity;
import com.suusoft.restaurantuser.util.AppUtil;

/**
 * Created by Suusoft on 12/01/2017.
 */

public class SplashActivityOne extends AppCompatActivity {
    private Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_one);
        AppUtil.getFacebookKeyHash(this);

        // check app is installed
//        if (!DataStoreManager.isInstalled()) {
//            AppUtil.addShortcut(this, SplashActivity.class);
//        }
        args = getIntent().getExtras();
        switchSreen();


    }

    private void switchSreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (DataStoreManager.getUser() != null) {
                    if (args == null) {
                        args = new Bundle();
                    }
                    AppController.getInstance().setToken(DataStoreManager.getToken());
                    AppUtil.startActivity(SplashActivityOne.this, MainCustomizeActivity.class, args);
                    finish();
                } else {
                    AppUtil.startActivity(SplashActivityOne.this, LoginActivity.class);
                    finish();
                }

            }
        }, 2000);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        args = intent.getExtras();
        switchSreen();
    }
}
