package com.suusoft.restaurantuser.main.detail;

import android.content.Context;
import android.view.View;

import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.model.User;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;

/**
 * Created by Suusoft on 7/26/2016.
 */
public class ItemUserOfferVM extends BaseAdapterVM implements IOnItemClickedListener {


    private User item;

    public String getId() {
        return item.id;
    }

    public String getRate() {
        return item.rate;
    }

    public String getImage() {
        return item.image;
    }

    public String getName() {
        return item.name;
    }

    public String getEmail() {
        return item.email;
    }


    public ItemUserOfferVM(Context self, User item) {
        super(self);
        this.item = item;
    }


    @Override
    public void setData(Object object) {
        item = (User) object;
        notifyChange();
    }

    @Override
    public void onItemClicked(View view) {
        AppUtil.showToast(self,"clicked");
    }


}
