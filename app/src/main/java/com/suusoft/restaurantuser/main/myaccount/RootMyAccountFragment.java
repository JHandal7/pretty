package com.suusoft.restaurantuser.main.myaccount;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseFragment;
import com.suusoft.restaurantuser.base.view.SingleFragment;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.model.User;
import com.suusoft.restaurantuser.util.AppUtil;

/**
 * Created by Suusoft on 18/07/2017.
 */

public class RootMyAccountFragment extends BaseFragment {
    public static final String TAG = RootMyAccountFragment.class.getName();
    public static final String TAG_MY_ACCOUNT = RootMyAccountFragment.class.getName() + SingleFragment.class.getName();
    private AccountFragment myAccountFragment;

    public static RootMyAccountFragment newInstance() {

        Bundle args = new Bundle();

        RootMyAccountFragment fragment = new RootMyAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_root_my_account;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        showMyAccount();
        User user = DataStoreManager.getUser();
        if (user != null) {
            if (user.getPhone().isEmpty()) {
                AppUtil.showToast(getActivity(), R.string.msg_phone_update_phone);
            }
        }
    }

    @Override
    protected void getData() {

    }

    private void showMyAccount() {
        myAccountFragment = (AccountFragment) getChildFragmentManager().findFragmentByTag(TAG_MY_ACCOUNT);
        if (myAccountFragment == null) {
            myAccountFragment = AccountFragment.newInstance();
        }
        replaceFragment(myAccountFragment, TAG_MY_ACCOUNT);
    }

    public void replaceFragment(Fragment fragment, String tag, int enter, int out, boolean isAddBackstack) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction().replace(R.id.fr_root_content, fragment, tag).setCustomAnimations(enter, out);
        if (isAddBackstack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, String tag, boolean isAddBackstack) {
        replaceFragment(fragment, tag, R.anim.slide_in_right, R.anim.slide_out_left, isAddBackstack);
    }

    public void replaceFragment(Fragment fragment, String tag) {
        replaceFragment(fragment, tag, true);
    }
}
