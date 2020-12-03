package com.suusoft.restaurantuser.main.myaccount.myorder;

import android.support.v7.widget.LinearLayoutManager;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivityBinding;
import com.suusoft.restaurantuser.base.view.SingleAdapter;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.databinding.ActivityOrderDetailsBinding;
import com.suusoft.restaurantuser.model.Order;

public class OrderDetailsActivity extends BaseActivityBinding {
    ActivityOrderDetailsBinding binding;
    private OrderDetailsVM viewModel;
    private Order model;

    @Override
    protected void initView() {
        super.initView();
        model = getIntent().getExtras().getParcelable(Constant.KEY_ORDER);
    }

    @Override
    protected BaseViewModel getViewModel() {
        viewModel = new OrderDetailsVM(this, model);
        return viewModel;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void onViewCreated() {
        setToolbarTitle(R.string.order_details);
        binding = (ActivityOrderDetailsBinding) super.binding;
        setUpRecyclerView();
        setResult(Constant.RC_END_PAYMENT);
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {
    }

    private void setUpRecyclerView() {
        binding.rcvItems.setFocusable(false);
        binding.rcvItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvItems.setAdapter(new SingleAdapter(this, R.layout.item_food_order_details, viewModel.listFoods, ItemFoodOrderDetailsVM.class));
        binding.rcvItems.setNestedScrollingEnabled(false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
