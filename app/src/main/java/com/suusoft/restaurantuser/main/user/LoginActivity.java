package com.suusoft.restaurantuser.main.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivityBinding;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.main.MainCustomizeActivity;
import com.suusoft.restaurantuser.social.facebook.FaceBookManager;
import com.suusoft.restaurantuser.social.googleplus.GooglePlusManager;
import com.suusoft.restaurantuser.util.AppUtil;

public class LoginActivity extends BaseActivityBinding implements LoginVM.ISignInListener {

    private LoginVM viewModel;
    private Bundle args;
    private ViewGroup layoutScreen;

    @Override
    protected BaseViewModel getViewModel() {
        viewModel = new LoginVM(this, this);
        return viewModel;
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NONE;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_login;
    }

    @Override
    protected void onViewCreated() {
        layoutScreen = (ViewGroup) findViewById(R.id.rlt_root_function);
        layoutScreen.getLayoutParams().height = AppUtil.getScreenHeight(this) - AppUtil.getStatusBarHeight(this);
        args = getIntent().getExtras();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        args = intent.getExtras();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GooglePlusManager.RC_SIGN_IN) {
            viewModel.googlePlusManager.onActivityResult(requestCode, data);
        } else {
            FaceBookManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onSignInComplete() {
        if (args == null) {
            args = new Bundle();
        }
        startActivity(MainCustomizeActivity.class, args);
        finish();
    }

    @Override
    public void onSignInError(int message) {
        showSnackBar(message);
    }

    @Override
    public void onClickForgotpass() {

    }

    @Override
    public void onClickSignUp() {
        startActivity(SignUpActivity.class);
    }
}
