package com.suusoft.restaurantuser.main.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.SingleAdapter;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.base.view.BaseListFragmentBinding;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;

/**
 * Created by Suusoft on 6/29/2016.
 */
public class NewProductFragment extends BaseListFragmentBinding implements IOnItemClickedListener {

    private SingleAdapter mAdapter;
    private NewProductVM viewModel;

    public static NewProductFragment newInstance() {

        Bundle args = new Bundle();
        NewProductFragment fragment = new NewProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setUpRecyclerView(RecyclerView recyclerView) {
        mAdapter = new SingleAdapter(self, R.layout.item_product, viewModel.getListData() , ItemProductVM.class, this);
        recyclerView.setAdapter(mAdapter);
       // recyclerView.addOnScrollListener(viewModel.getOnScrollListener());
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected BaseViewModel getViewModel() {
        viewModel = new NewProductVM(self);
        return viewModel;
    }

    @Override
    public void onItemClicked(View view) {
        AppUtil.showToast(self,"hiha");
    }


}
