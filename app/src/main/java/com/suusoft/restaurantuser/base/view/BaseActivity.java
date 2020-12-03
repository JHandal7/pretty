package com.suusoft.restaurantuser.base.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Suusoft on 6/17/2016.
 */
public abstract class BaseActivity extends AbstractActivity {


    protected abstract int getLayoutInflate();
    protected abstract void initView();
    protected abstract void onViewCreated();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeChildView();
        inflateChildView();
        initView();
        onViewCreated();
    }

    /**
     * inflate child view. get from getLayoutInflate()
     */
    private void inflateChildView() {
        getLayoutInflater().inflate(getLayoutInflate(), contentLayout);
    }

    protected void initBeforeChildView(){};

}
