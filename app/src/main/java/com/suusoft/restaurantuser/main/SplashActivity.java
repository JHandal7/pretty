package com.suusoft.restaurantuser.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.main.user.LoginActivity;
import com.suusoft.restaurantuser.main.user.SignUpActivity;
import com.suusoft.restaurantuser.util.AppUtil;


public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    private Bundle args;
    private TextView tvSignIn, tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppUtil.getFacebookKeyHash(this);

        // check app is installed
//        if (!DataStoreManager.isInstalled()) {
//            AppUtil.addShortcut(this, SplashActivity.class);
//        }

        args = getIntent().getExtras();
        initUI();
        initControl();
        switchSreen();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        args = intent.getExtras();
        switchSreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                    AppUtil.startActivity(SplashActivity.this, MainCustomizeActivity.class, args);
                    finish();
                }
            }
        }, 2000);
    }

    private void initUI() {
        tvSignIn = (TextView) findViewById(R.id.tv_sign_in);
        tvSignUp = (TextView) findViewById(R.id.tv_signup);
    }

    private void initControl() {
        tvSignUp.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == tvSignIn) {
            switchToSignIn();
        } else if (view == tvSignUp) {
            switchToSignUp();
        }
    }

    private void switchToSignIn() {
        if (args == null) {
            args = new Bundle();
        }
        AppUtil.startActivity(SplashActivity.this, LoginActivity.class, args);
        finish();
    }

    private void switchToSignUp() {
        AppUtil.startActivity(SplashActivity.this, SignUpActivity.class);
    }
}
