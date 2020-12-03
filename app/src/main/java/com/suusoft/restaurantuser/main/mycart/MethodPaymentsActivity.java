package com.suusoft.restaurantuser.main.mycart;


import android.support.v4.app.Fragment;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivity;
import com.suusoft.restaurantuser.base.view.SingleListFragment;

public class MethodPaymentsActivity extends BaseActivity {

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_method_payments;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onViewCreated() {
        setToolbarTitle(R.string.method_payment);
        replaceFragment(SingleListFragment.newInstance(MethodPaymentsVM.class, R.layout.item_method_payment, ItemMethodPaymentVM.class));
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_root, fragment).commit();
    }

}
