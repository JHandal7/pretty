package com.suusoft.restaurantuser.base.view;

import android.databinding.ViewDataBinding;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.suusoft.restaurantuser.BR;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.listener.IDataChangedListener;
import com.suusoft.restaurantuser.listener.IOnRefreshing;
import com.suusoft.restaurantuser.util.AppUtil;

import java.util.List;

/**
 * Created by Suusoft on 7/11/2016.
 */
public abstract class BaseListFragmentBinding extends BaseFragmentBinding implements IDataChangedListener, IOnRefreshing, SwipeRefreshLayout.OnRefreshListener {

    private BaseViewModelList mViewModel;
    public RecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;

    protected abstract void setUpRecyclerView(RecyclerView recyclerView);

    @Override
    protected int getLayoutInflate() {
        return R.layout._base_list;
    }

    @Override
    protected void setViewDataBinding(ViewDataBinding binding) {
        mViewModel = (BaseViewModelList) viewModel;
        // set listener data changed from viewmodel
        mViewModel.setDataListener(this);
        mViewModel.setOnRefreshing(this);
        // get binding
        this.binding = binding;
        // set view model
        this.binding.setVariable(BR.viewModel, mViewModel);
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_data);
        recyclerView.setLayoutManager(mViewModel.getLayoutManager());
        setUpRecyclerView(recyclerView);

        // init swipe refresh

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(AppUtil.getColor(self, R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(isEnableRefresh());

        viewModel.getData(1);
    }

    @Override
    public void onListDataChanged(List<?> data) {
        ((BaseAdapterBinding) recyclerView.getAdapter()).setItems(data);
    }

    public boolean isEnableRefresh() {
        return false;
    }

    private void setRefresh(final boolean isRefresh) {
        if (isEnableRefresh())
            swipeRefreshLayout.setRefreshing(isRefresh);

    }

    @Override
    public void onRefresh() {
        ((BaseAdapterBinding) recyclerView.getAdapter()).clear();
        viewModel.getData(1);
    }

    @Override
    public void setRefreshing(boolean isShow) {
        setRefresh(isShow);
    }
}
