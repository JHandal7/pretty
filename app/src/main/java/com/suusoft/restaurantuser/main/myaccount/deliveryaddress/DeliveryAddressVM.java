package com.suusoft.restaurantuser.main.myaccount.deliveryaddress;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.util.AppUtil;

/**
 * Created by Suusoft on 15/08/2017.
 */

public class DeliveryAddressVM extends BaseViewModel {
    public DeliveryAddressVM(Context self) {
        super(self);
    }

    public void searchAddress(View view) {
        AppUtil.searchAddress((AppCompatActivity) self);
    }


}
