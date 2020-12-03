package com.suusoft.restaurantuser.main.demo;

import android.support.v7.widget.RecyclerView;

import com.suusoft.restaurantuser.main.home.ItemProduct;
import com.suusoft.restaurantuser.base.view.BaseListActivityBinding;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;

/**
 * Created by Suusoft on 7/16/2016.
 */
public class ListActivityDemo extends BaseListActivityBinding {

    private ItemProduct mAdapter;
    private ListActivityVMDemo viewModel;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NONE;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    protected BaseViewModel getViewModel() {
        viewModel = new ListActivityVMDemo(this);
        return viewModel;
    }

    @Override
    protected void onViewCreated() {

    }


    @Override
    protected void setUpRecyclerView(RecyclerView recyclerView) {
        mAdapter = new ItemProduct(self,viewModel.getListData());
        recyclerView.setLayoutManager(viewModel.getLayoutManager());
        recyclerView.setAdapter(mAdapter);
        // for loading more feature
        recyclerView.addOnScrollListener(viewModel.getOnScrollListener());
    }
}
