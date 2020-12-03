package com.suusoft.restaurantuser.main.mycart;

import android.content.Context;
import android.view.View;

import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;

/**
 * Created by Suusoft on 21/08/2017.
 */

public class ItemTimDeliveryVM extends BaseAdapterVM implements IOnItemClickedListener {
    private String timeDelivery;

    public ItemTimDeliveryVM(Context self, Object o) {
        super(self);
        timeDelivery = (String) o;
    }

    @Override
    public void setData(Object object) {
        timeDelivery = (String) object;
        notifyChange();
    }

    public String getTimeDelivery() {
        return timeDelivery;
    }

    @Override
    public void onItemClicked(View view) {
        view.setTag(timeDelivery);
        listener.onItemClicked(view);
    }
}
