package com.suusoft.restaurantuser.main.myaccount;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseFragment;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.databinding.LayoutDialogChangePasswordBinding;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.main.myaccount.about.AboutActivity;
import com.suusoft.restaurantuser.main.myaccount.mydetail.MyDetailsActivity;
import com.suusoft.restaurantuser.main.myaccount.myorder.MyOrdersActivity;
import com.suusoft.restaurantuser.model.User;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.ImageUtil;

/**
 * Created by Suusoft on 13/10/2017.
 */

public class AccountFragment extends BaseFragment implements View.OnClickListener {
    private TextView tvProfile, tvChangePassword, tvMyOrders, tvAbout;
    private ImageView imgAvatar;

    public static AccountFragment newInstance() {

        Bundle args = new Bundle();

        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_account_v2;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {

        tvProfile = (TextView) view.findViewById(R.id.tv_profile);
        tvChangePassword = (TextView) view.findViewById(R.id.tv_change_password);
        tvMyOrders = (TextView) view.findViewById(R.id.tv_my_orders);
        tvAbout = (TextView) view.findViewById(R.id.tv_about);
        imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
        if (!DataStoreManager.isLoginSocial() ){
            tvChangePassword.setVisibility(View.VISIBLE);
        }else  tvChangePassword.setVisibility(View.GONE);

        initControl();
        User user = DataStoreManager.getUser();
        if (user != null) {
            ImageUtil.setImageOrginRotate(imgAvatar, user.getImage());
            Log.e("image", user.getImage());
        }
    }

    @Override
    protected void getData() {

    }

    @Override
    public void onClick(View view) {
        if (view == tvProfile) {
            switchToMyProfile();
        } else if (view == tvChangePassword) {
            showDialogChangePass();
        } else if (view == tvMyOrders) {
            AppUtil.startActivity(getActivity(), MyOrdersActivity.class);
        } else if (view == tvAbout) {
            AppUtil.startActivity(getActivity(), AboutActivity.class);
        }
    }

    private void initControl() {
        tvProfile.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvMyOrders.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
    }

    private void switchToMyProfile() {
        Intent intent = new Intent(self, MyDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ((AppCompatActivity) self).startActivityForResult(intent, Constant.RQ_EXIT_APP);
    }

    private void showDialogChangePass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(self);
        LayoutDialogChangePasswordBinding changePasswordBinding = DataBindingUtil.inflate(LayoutInflater.from(self), R.layout.layout_dialog_change_password, null, false);
        ChangePasswordVM changePasswordVM = new ChangePasswordVM(self);
        changePasswordBinding.setViewModel(changePasswordVM);
        builder.setView(changePasswordBinding.getRoot());

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        changePasswordVM.setAlertDialog(dialog);
        changePasswordVM.setBinding(changePasswordBinding);

        if (!dialog.isShowing()) {
            dialog.show();
        }

    }
}
