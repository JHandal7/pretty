package com.suusoft.restaurantuser.main.myaccount;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.databinding.LayoutDialogChangePasswordBinding;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.main.myaccount.about.AboutActivity;
import com.suusoft.restaurantuser.main.myaccount.addpromocode.AddPromoCodeActivity;
import com.suusoft.restaurantuser.main.myaccount.deliveryaddress.DeliveryAddressActivity;
import com.suusoft.restaurantuser.main.myaccount.mydetail.MyDetailsActivity;
import com.suusoft.restaurantuser.main.myaccount.myorder.MyOrdersActivity;
import com.suusoft.restaurantuser.main.user.LoginActivity;
import com.suusoft.restaurantuser.model.User;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DialogUtil;

/**
 * Created by Suusoft on 18/07/2017.
 */

public class MyAccountVM extends BaseViewModel {
    private User user;

    public MyAccountVM(Context self) {
        super(self);
        user = DataStoreManager.getUser();
    }

    public void myOrders(View view) {
        AppUtil.startActivity(self, MyOrdersActivity.class);
    }

    public void myDetails(View view) {
        Intent intent = new Intent(self, MyDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ((AppCompatActivity) self).startActivityForResult(intent, Constant.RQ_EXIT_APP);
//        AppUtil.startActivity(self, MyDetailsActivity.class);
    }

    public void addPromoCode(View view) {
        AppUtil.startActivity(self, AddPromoCodeActivity.class);
    }

    public void addDeliveryAddress(View view) {
        AppUtil.startActivity(self, DeliveryAddressActivity.class);
    }

    public void about(View view) {
        AppUtil.startActivity(self, AboutActivity.class);
    }

    public void changePassWord(View view) {
        showDialogChangePass();
    }

    public String getNotificationLogIn() {
        return "Loggged in as " + user.getEmail();
    }

    public void logOut(View view) {
        DialogUtil.showAlertDialog(self, R.string.msg_confirm_log_out, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                RequestManager.logOut(new BaseRequest.CompleteListener() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        if (!response.isError()) {
                            DataStoreManager.saveUser(null);
                            AppUtil.startActivity(self, LoginActivity.class);
                            ((AppCompatActivity) self).finish();
                        }
                    }

                    @Override
                    public void onError(String message) {

                    }
                });

            }
        });
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
