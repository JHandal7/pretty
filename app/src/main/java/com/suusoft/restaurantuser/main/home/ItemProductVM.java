package com.suusoft.restaurantuser.main.home;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.model.Product;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.StringUtil;
import com.suusoft.restaurantuser.main.demo.CustomListActivity;
import com.suusoft.restaurantuser.main.detail.DetailActivity;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;

/**
 * Created by Suusoft on 7/3/2016.
 */
public class ItemProductVM extends BaseAdapterVM implements IOnItemClickedListener{

    private Product product;

    public ItemProductVM(Context context, Object data) {
        super(context);
        this.product = (Product) data;
    }


    public String getTitle(){
        return product.title;
    }

    public String getImage(){
        return product.image;
    }

    public String getType(){
        return product.type;
    }

    public String getBudget(){
        return StringUtil.getPrice(product.budget);
    }

    @Override
    public void setData(Object object) {
        this.product = (Product) object;
        notifyChange();
    }

    @Override
    public void onItemClicked(View view) {
        if (listener != null)
            listener.onItemClicked(view);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.PREF_KEY_ID,product.id);
        AppUtil.startActivity(self, DetailActivity.class, bundle);
    }

    public void onClickAction(View view){
        AppUtil.startActivity(self, CustomListActivity.class);
    }

}
