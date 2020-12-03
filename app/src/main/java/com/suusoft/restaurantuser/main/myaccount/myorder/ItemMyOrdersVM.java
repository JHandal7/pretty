package com.suusoft.restaurantuser.main.myaccount.myorder;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.model.Order;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DateTimeUtility;

/**
 * Created by Suusoft on 14/08/2017.
 */

public class ItemMyOrdersVM extends BaseAdapterVM implements IOnItemClickedListener {
    private Order model;

    public ItemMyOrdersVM(Context self, Object o) {
        super(self);
        model = (Order) o;
    }

    @Override
    public void setData(Object object) {
        model = (Order) object;
        notifyChange();
    }

    public String getStatus() {
        return model.getStatus();
    }

    public String getName() {
        return model.getName();
    }

    public String getPhone() {
        return model.getPhone();
    }

    public String getTotalPrice() {
        return self.getString(R.string.currency) + String.valueOf(model.getTotalPrice());
    }

    public String getDate() {
        return (model.getCreated_date());
    }

    @Override
    public void onItemClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.KEY_ORDER, model);
        AppUtil.startActivity(self, OrderDetailsActivity.class, bundle);
    }

    public String getOrderNumber() {
        switch (model.getId().length()) {
            case 1:
                return "000" + model.getId();
            case 2:
                return "00" + model.getId();
            case 3:
                return "0" + model.getId();
            default:
                return model.getId();
        }
    }

    public boolean isSelected() {
        return true;
    }
}
