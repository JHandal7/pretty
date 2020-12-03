package com.suusoft.restaurantuser.base.vm;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;

/**
 * Created by Suusoft on 7/3/2016.
 */
public abstract class BaseViewModel extends BaseObservable {

    public Context self;
    protected Bundle bundle;

    public BaseViewModel(Context self) {
        this.self = self;
    }

    public BaseViewModel(Context self, Bundle bundle) {
        this(self);
        this.bundle = bundle;
    }

    public void destroy(){}
    public void getData(int page){}
    public void onActivityResult(int requestCode, int resultCode, Intent data) {}

}
