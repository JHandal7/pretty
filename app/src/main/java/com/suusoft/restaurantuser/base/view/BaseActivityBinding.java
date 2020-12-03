package com.suusoft.restaurantuser.base.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.suusoft.restaurantuser.BR;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;

/**
 * Created by Suusoft on 7/6/2016.
 */
public abstract class BaseActivityBinding extends AbstractActivity {

    protected BaseViewModel viewModel;
    protected ViewDataBinding binding;

    protected abstract BaseViewModel getViewModel();
    protected abstract int getLayoutInflate();
    protected abstract void onViewCreated();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initChildView();
        onViewCreated();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (viewModel != null)
            viewModel.onActivityResult(requestCode,resultCode,data);
    }

    /**
     * inflate child view. get from getLayoutInflate()
     */
    private void initChildView(){
        viewModel = getViewModel();
        setViewDataBinding(inflateView());
    }

    protected void initView(){
        // overide for child class
    }

    /**
     * get ViewDatabinding is referenced to #getLayoutInflate()
     */
    public final ViewDataBinding inflateView(){
        return DataBindingUtil.inflate(getLayoutInflater(),getLayoutInflate(),super.contentLayout,true);
    }

    protected void setViewDataBinding(ViewDataBinding binding){
        this.binding = binding;
        this.binding.setVariable(BR.viewModel,viewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.destroy();
    }

}
