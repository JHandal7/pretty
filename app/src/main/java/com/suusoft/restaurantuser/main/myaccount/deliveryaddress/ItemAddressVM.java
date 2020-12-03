package com.suusoft.restaurantuser.main.myaccount.deliveryaddress;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.DeliveryAddress;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DialogUtil;

/**
 * Created by Suusoft on 15/08/2017.
 */

public class ItemAddressVM extends BaseAdapterVM {
    private DeliveryAddress model;

    public ItemAddressVM(Context self, Object o) {
        super(self);
        model = (DeliveryAddress) o;
    }

    @Override
    public void setData(Object object) {
        model = (DeliveryAddress) object;
        notifyChange();
    }

    public String getAddress() {
        return model.getAddress().toString();
    }

    public void deleteAddress(View view) {
        DialogUtil.showAlertDialog(self, R.string.msg_delete_address, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                RequestManager.deleteAddress(model.getId(), new BaseRequest.CompleteListener() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        if (!response.isError()) {
                            LocalBroadcastManager.getInstance(self).sendBroadcast(new Intent(Constant.MSG_DEL_ADDRESS));
                            ((AppCompatActivity) self).setResult(Constant.RESULT_ADDRESS);
                            AppUtil.showToast(self, R.string.msg_delete_address_success);
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
