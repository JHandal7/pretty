package com.suusoft.restaurantuser.main.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseFragment;
import com.suusoft.restaurantuser.base.view.SingleListFragment;


/**
 * Created by Suusoft on 6/29/2016.
 *
 */
public class HomeFragment extends BaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pagger);

        viewPager.setAdapter(new TabViewPaggerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void getData() {

    }

    private class TabViewPaggerAdapter extends FragmentPagerAdapter {

        String[] titles = getResources().getStringArray(R.array.tabs);

        public TabViewPaggerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return SingleListFragment.newInstance(CustomListVM.class, ItemProduct.class);
                case 1:
                    return SingleListFragment.newInstance(NewProductVM.class, R.layout.item_product, ItemProductVM.class);
                case 2:
                    return SingleListFragment.newInstance(CustomListVM.class, ItemProduct.class);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
