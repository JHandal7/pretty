package com.suusoft.restaurantuser.main.detail;

import android.support.v7.widget.RecyclerView;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseListActivityBinding;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.listener.ISetDataListener;
import com.suusoft.restaurantuser.model.Product;

public class DetailActivity extends BaseListActivityBinding implements ISetDataListener {

    private DetailVM viewModel;
    private String mId;

    private OfferUserAdapter mAdapter;

    @Override
    protected void setUpRecyclerView(RecyclerView recyclerView) {
        mAdapter = new OfferUserAdapter(self);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_detail;
    }

    @Override
    protected BaseViewModel getViewModel() {

        viewModel = new DetailVM(this, mId, this);
        return viewModel;
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {
        if (getIntent().hasExtra(Constant.PREF_KEY_ID)) {
            mId = getIntent().getExtras().getString(Constant.PREF_KEY_ID);
        }

        if (mId == null) {
            finish();
        }
    }

    @Override
    protected void onViewCreated() {
    }

    @Override
    public void setListData(Object object) {
        mAdapter.setItems(((Product) object).getListOffers());
    }
}
