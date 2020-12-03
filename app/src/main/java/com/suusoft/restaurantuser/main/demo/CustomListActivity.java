package com.suusoft.restaurantuser.main.demo;

import android.support.v7.widget.RecyclerView;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseListActivityBinding;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.main.home.ItemProduct;
import com.suusoft.restaurantuser.main.home.CustomListVM;

/**
 * Created by Suusoft on 2/20/2017.
 */

public class CustomListActivity extends BaseListActivityBinding {

    private CustomListVM viewModel;
    private ItemProduct mAdapter;

    @Override
    protected int getLayoutInflate() {
        return R.layout.layout_custom_list;
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NONE;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    protected BaseViewModel getViewModel() {
        viewModel = new CustomListVM(this);
        return viewModel;
    }

    @Override
    protected void onViewCreated() {

    }

    @Override
    protected void setUpRecyclerView(RecyclerView recyclerView) {
        mAdapter = new ItemProduct(self,viewModel.getListData());
        recyclerView.setAdapter(mAdapter);
    }
}
