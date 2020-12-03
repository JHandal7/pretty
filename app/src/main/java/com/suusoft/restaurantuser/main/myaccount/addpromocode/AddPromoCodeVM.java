package com.suusoft.restaurantuser.main.myaccount.addpromocode;

import android.content.Context;

import com.suusoft.restaurantuser.base.vm.BaseViewModel;

/**
 * Created by Suusoft on 15/08/2017.
 */

public class AddPromoCodeVM extends BaseViewModel {
    private String  promoCode;

    public AddPromoCodeVM(Context self) {
        super(self);
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}
