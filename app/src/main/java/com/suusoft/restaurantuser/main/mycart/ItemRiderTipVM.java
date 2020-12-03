package com.suusoft.restaurantuser.main.mycart;

import android.content.Context;
import android.view.View;

import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.util.AppUtil;

/**
 * Created by Suusoft on 24/08/2017.
 */

public class ItemRiderTipVM extends BaseAdapterVM implements IOnItemClickedListener {
    private double riderTip;

    public ItemRiderTipVM(Context self, Object o) {
        super(self);
        riderTip = (double) o;
    }

    @Override
    public void setData(Object object) {
        riderTip = (double) object;
        notifyChange();
    }

    public String getRiderTip() {
        return AppUtil.formatCurrency((double) riderTip);
    }

    @Override
    public void onItemClicked(View view) {
        view.setTag(riderTip);
        if (listener != null) {
            listener.onItemClicked(view);
        }
    }
}
