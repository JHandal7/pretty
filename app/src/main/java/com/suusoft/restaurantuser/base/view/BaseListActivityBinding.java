package com.suusoft.restaurantuser.base.view;

import android.databinding.ViewDataBinding;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.suusoft.restaurantuser.BR;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.listener.IDataChangedListener;

import java.util.List;

/**
 * Created by Suusoft on 7/15/2016.
 */
public abstract class BaseListActivityBinding extends BaseActivityBinding implements IDataChangedListener {

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
        // set viewmodel
        mViewModel = (BaseViewModelList) viewModel;
        mViewModel.setDataListener(this);

        // set bindding
        this.binding =  binding;
        this.binding.setVariable(BR.viewModel,viewModel);

        // get recyclerView and set
        recyclerView = (RecyclerView) binding.getRoot().findViewById(R.id.rcv_data);
        recyclerView.setLayoutManager(mViewModel.getLayoutManager());
        setUpRecyclerView(recyclerView);

        //init swipe to refresh layout
        if (isEnableRefresh()) {
            swipeRefreshLayout = (SwipeRefreshLayout) binding.getRoot().findViewById(R.id.swipe_container);
            swipeRefreshLayout.setEnabled(isEnableRefresh());
        }
    }

    /**
     * listener #BaseViewmodel.notifyDataChanged()
     *  and refresh adapter
     *
     */
    @Override
    public void onListDataChanged(List<?> data) {
        BaseAdapterBinding adapter = (BaseAdapterBinding) recyclerView.getAdapter();
        adapter.setItems(data);
    }

    public boolean isEnableRefresh() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.destroy();
    }
}
