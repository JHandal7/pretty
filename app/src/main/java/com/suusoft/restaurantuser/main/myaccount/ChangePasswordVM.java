package com.suusoft.restaurantuser.main.myaccount;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.databinding.LayoutDialogChangePasswordBinding;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.StringUtil;


/**
 * Created by Suusoft on 11/22/2016.
 */
public class ChangePasswordVM extends BaseViewModel {
    private LayoutDialogChangePasswordBinding binding;


    private String oldPassWord;
    private String newPassword;
    private String confirmNewPassword;
    private AlertDialog dialog;

    public ChangePasswordVM(Context self) {
        super(self);
    }

    public void setBinding(LayoutDialogChangePasswordBinding binding) {
        this.binding = binding;
    }

    public void onClickUpdatePassword(View view) {
        if (StringUtil.isEmpty(oldPassWord)) {
            binding.edtOldPassWord.requestFocus();
            AppUtil.showToast(self, String.format(self.getString(R.string.msg_error_empty), self.getString(R.string.password_old)));
            return;
        }

        if (StringUtil.isEmpty(newPassword)) {
            binding.edtNewPassword.requestFocus();

            AppUtil.showToast(self, String.format(self.getString(R.string.msg_error_empty), self.getString(R.string.password_new)));
            return;
        }
        if (StringUtil.isEmpty(confirmNewPassword)) {
            binding.edtConfirmNewPassword.requestFocus();

            AppUtil.showToast(self, String.format(self.getString(R.string.msg_error_empty), self.getString(R.string.password_confirm)));
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            binding.edtNewPassword.requestFocus();
            AppUtil.showToast(self, self.getString(R.string.msg_error_math));
            return;
        }
        if (oldPassWord.equals(newPassword)) {
            binding.edtNewPassword.requestFocus();

            AppUtil.showToast(self, self.getString(R.string.msg_error_math_old));
            return;
        }
        if (DataStoreManager.getToken() == null) {

            AppUtil.showToast(self, self.getString(R.string.error));
            return;
        }

        RequestManager.changePassword(oldPassWord, newPassword, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse object) {
                AppUtil.showToast(self, self.getString(R.string.msg_change_pass_success));
                oldPassWord = "";
                newPassword = "";
                confirmNewPassword = "";
                notifyChange();
                dialog.dismiss();
            }

            @Override
            public void onError(String message) {
                if (self == null || ((Activity) self).isFinishing()) {
                    return;
                }
                AppUtil.showToast(self, message);
                dialog.dismiss();

            }

        });

    }

    public String getOldPassWord() {
        return oldPassWord;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setOldPassWord(String oldPassWord) {
        this.oldPassWord = oldPassWord;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    @Override
    public void destroy() {
        self = null;
    }

    @Override
    public void getData(int page) {

    }

    public ChangePasswordVM setAlertDialog(AlertDialog dialog) {
        this.dialog = dialog;
        return this;
    }

}
