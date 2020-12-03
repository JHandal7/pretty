package com.suusoft.restaurantuser.base.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.suusoft.restaurantuser.base.vm.BaseViewModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Suusoft on 3/6/2017.
 */

public class SingleFragment extends BaseFragmentBinding {

    private static final String KEY_LAYOUT_INT = "LAYOUT_INT";
    private static final String KEY_CLASS_VM = "CLASS_VM";
    private static final String KEY_DATA = "KEY_DATA";

    public int layout;
    private Bundle mBundle;
    public BaseViewModel viewModel;
    private Constructor<?> cons;

    public static SingleFragment newInstance(int layout, Class viewModelClass) {
        return newInstance(layout,viewModelClass,null);
    }

    public static SingleFragment newInstance(int layout, Class viewModelClass, Bundle data) {

        Bundle bundle = new Bundle();
        SingleFragment fragment = new SingleFragment();
        bundle.putInt(KEY_LAYOUT_INT,layout);
        bundle.putString(KEY_CLASS_VM,viewModelClass.getName());
        if (data != null)
            bundle.putBundle(KEY_DATA,data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return layout;
    }

    @Override
    protected void initialize() {
        Bundle data = getArguments();
        try {
            Class viewModelClass = Class.forName(data.getString(KEY_CLASS_VM));
            cons = viewModelClass.getConstructor(Context.class);
            if (data.containsKey(KEY_DATA)) {
                mBundle = data.getBundle(KEY_DATA);
                cons = viewModelClass.getConstructor(Context.class, Bundle.class);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        layout = data.getInt(KEY_LAYOUT_INT);
    }

    @Override
    protected BaseViewModel getViewModel() {
        try {
            if (mBundle == null) {
                viewModel = (BaseViewModel) cons.newInstance(self);
            }else{
                viewModel = (BaseViewModel) cons.newInstance(self, mBundle);
            }
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return viewModel;
    }

    @Override
    protected void initView(View view) {

    }
}
