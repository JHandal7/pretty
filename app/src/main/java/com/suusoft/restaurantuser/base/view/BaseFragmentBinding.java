package com.suusoft.restaurantuser.base.view;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suusoft.restaurantuser.BR;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;

/**
 * Created by Suusoft on 6/29/2016.
 */
public abstract class BaseFragmentBinding extends Fragment {

    protected Context self;
    protected ViewDataBinding binding;
    protected BaseViewModel viewModel;

    protected abstract int getLayoutInflate();
    protected abstract void initialize();
    protected abstract BaseViewModel getViewModel();
    protected abstract void initView(View view);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        self = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        self = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,getLayoutInflate(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        viewModel = getViewModel();
        setViewDataBinding(binding);
        initView(view);
    }

    protected void setViewDataBinding(ViewDataBinding binding){
        this.binding = binding;
        this.binding.setVariable(BR.viewModel, viewModel);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.destroy();
        viewModel=null;
    }

}
