package com.suusoft.restaurantuser.main.myaccount.addpromocode;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.PromoCode;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DialogUtil;

/**
 * Created by Suusoft on 22/08/2017.
 */

public class ItemPromoCodeVM extends BaseAdapterVM {
    private PromoCode model;

    public ItemPromoCodeVM(Context self, Object o) {
        super(self);
        model = (PromoCode) o;
    }

    @Override
    public void setData(Object object) {
        model = (PromoCode) object;
        notifyChange();
    }

    public String getCode() {
        return model.getCode();
    }

    public String getSale() {
        return model.getDetailPromoCode();
    }

    public String getStatus() {
        if (model.getIsActive()) {
            return "Valid";
        } else {
            return "Invalid";
        }

    }

    public int getTextColor() {
        if (model.getIsActive()) {
            return AppUtil.getColor(self, R.color.textColorSecondary);
        } else {
            return AppUtil.getColor(self, R.color.color_red_bold);
        }
    }

    public void deletePromoCode(View view) {
        DialogUtil.showAlertDialog(self, R.string.msg_delete_promo_code, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                RequestManager.deletePromoCode(model.getId(), new BaseRequest.CompleteListener() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        if (!response.isError()) {
                            ((AppCompatActivity) self).setResult(Constant.RC_PROMO_CODE);
                            LocalBroadcastManager.getInstance(self).sendBroadcast(new Intent(Constant.MSG_DEL_PROMO_CODE));
                            AppUtil.showToast(self, R.string.msg_delete_promo_code_success);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        AppUtil.showToast(self, message);
                    }
                });
            }
        });
    }
}
