package com.suusoft.restaurantuser.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.SingleListFragment;
import com.suusoft.restaurantuser.main.category.CategoryVM;
import com.suusoft.restaurantuser.main.category.ItemCategoryVM;
import com.suusoft.restaurantuser.main.myaccount.RootMyAccountFragment;
import com.suusoft.restaurantuser.main.mycart.MyCartFragment;
import com.suusoft.restaurantuser.main.promotion.ItemPromotionVM;
import com.suusoft.restaurantuser.main.promotion.PromotionVM;
import com.suusoft.restaurantuser.main.search.SearchFragment;


/**
 * Created by Suusoft on 11/21/2017.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {


    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return  5;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return SingleListFragment.newInstance(PromotionVM.class, R.layout.item_promotion, ItemPromotionVM.class);
            case 1: return SearchFragment.newInstance();
            case 2: return SingleListFragment.newInstance(CategoryVM.class, R.layout.item_category, ItemCategoryVM.class);
            case 3: return MyCartFragment.newInstance();
            case 4: return RootMyAccountFragment.newInstance();
        }
        return null;
    }

}
