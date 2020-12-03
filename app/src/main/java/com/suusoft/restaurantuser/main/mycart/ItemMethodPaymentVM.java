package com.suusoft.restaurantuser.main.mycart;

import android.content.Context;
import android.view.View;

import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.model.MethodPayment;

/**
 * Created by Suusoft on 12/09/2017.
 */

public class ItemMethodPaymentVM extends BaseAdapterVM implements IOnItemClickedListener {
    private MethodPayment model;

    public ItemMethodPaymentVM(Context self, Object o) {
        super(self);
        model = (MethodPayment) o;
    }

    @Override
    public void setData(Object object) {
        model = (MethodPayment) object;
        notifyChange();
    }

    public String getName() {
        return model.getDescription();
    }

    public String getImage() {
        return model.getImage().getBigger();
    }

    @Override
    public void onItemClicked(View view) {
        view.setTag(model);
        if (listener != null) {
            listener.onItemClicked(view);
        }
//        Intent intent = new Intent();
//        intent.putExtra(Constant.METHOD_PAYMENT, model);
//        ((AppCompatActivity) self).setResult(Constant.RC_METHOD_PAYMENT, intent);
//        ((AppCompatActivity) self).finish();
    }
}
