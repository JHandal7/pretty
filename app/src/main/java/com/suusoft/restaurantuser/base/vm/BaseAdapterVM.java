package com.suusoft.restaurantuser.base.vm;

import android.content.Context;
import android.databinding.BaseObservable;

import com.suusoft.restaurantuser.listener.IOnItemClickedListener;

/**
 * Created by Suusoft on 7/18/2016.
 */
public abstract class BaseAdapterVM extends BaseObservable {

    public Context self;

    public abstract void setData(Object object);

    public IOnItemClickedListener listener;


    public BaseAdapterVM(Context self) {
        this.self = self;
    }

    public IOnItemClickedListener getListener() {
        return listener;
    }

    public void setListener(IOnItemClickedListener listener) {
        this.listener = listener;
    }
}
