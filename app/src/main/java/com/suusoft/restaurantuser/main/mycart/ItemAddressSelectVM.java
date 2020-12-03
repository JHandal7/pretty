package com.suusoft.restaurantuser.main.mycart;

import android.content.Context;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.model.DeliveryAddress;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DialogUtil;

/**
 * Created by Suusoft on 25/08/2017.
 */

public class ItemAddressSelectVM extends BaseAdapterVM implements IOnItemClickedListener {
    private DeliveryAddress model;

    public ItemAddressSelectVM(Context self, Object o) {
        super(self);
        model = (DeliveryAddress) o;
    }

    @Override
    public void setData(Object object) {
        model = (DeliveryAddress) object;
        notifyChange();
    }

    @Override
    public void onItemClicked(View view) {
        view.setTag(model);
        if (listener != null) {
            listener.onItemClicked(view);
        }
    }

    public String getAddress() {
        return model.getAddress();
    }

    public boolean isSelected() {
        return model.isSelected();
    }

    public void deleteAddress(View view) {
        DialogUtil.showAlertDialog(self, R.string.msg_delete_address, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                RequestManager.deleteAddress(model.getId(), new BaseRequest.CompleteListener() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        if (!response.isError()) {
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
