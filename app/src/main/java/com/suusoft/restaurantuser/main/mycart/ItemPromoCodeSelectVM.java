package com.suusoft.restaurantuser.main.mycart;

import android.content.Context;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.model.PromoCode;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DialogUtil;

/**
 * Created by Suusoft on 22/08/2017.
 */

public class ItemPromoCodeSelectVM extends BaseAdapterVM implements IOnItemClickedListener {
    private PromoCode model;

    public ItemPromoCodeSelectVM(Context self, Object o) {
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

    public boolean isChecked() {
        return model.isSelected();
    }

    public int colorText() {
        if (model.getIsActive()) {
            return AppUtil.getColor(self,R.color.textColorPrimary);
        } else {
            return AppUtil.getColor(self,R.color.color_red_bold);
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
    @Override
    public void onItemClicked(View view) {
        view.setTag(model);
        if (listener != null)
            listener.onItemClicked(view);
    }
}
