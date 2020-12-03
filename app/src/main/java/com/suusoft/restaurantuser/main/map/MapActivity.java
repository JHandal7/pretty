package com.suusoft.restaurantuser.main.map;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivityBinding;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;

public class MapActivity extends BaseActivityBinding {
    private MapVM viewModel;

    @Override
    protected BaseViewModel getViewModel() {
        viewModel = new MapVM(this);
        return viewModel;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_map;
    }

    @Override
    protected void onViewCreated() {

    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {

    }


}
