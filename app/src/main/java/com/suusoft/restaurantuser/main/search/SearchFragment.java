package com.suusoft.restaurantuser.main.search;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.animation.AniChangeSizeView;
import com.suusoft.restaurantuser.base.view.BaseFragment;
import com.suusoft.restaurantuser.base.view.SingleListFragment;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.main.listFoods.ItemFoodVM;
import com.suusoft.restaurantuser.main.listFoods.SearchFoodsVM;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.StringUtil;

/**
 * Created by Suusoft on 05/10/2017.
 */

public class SearchFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG_SEARCH = SearchFragment.class.getName();
    private EditText edtSearch;
    private ImageView imgSearch;
    private SingleListFragment listFoodsFragment;
    private Bundle bundleKeyword;
    private ImageView imgGrid, imgList;
    private TextView tvAllItems;
    private TextView tvCountItems;
    private int typeList = Constant.LIST_ITEMS;
    private String keyword = "";
    private BroadcastReceiver broadcastData;
    private ViewGroup rootToolSupport;
    private int layoutItem = R.layout.item_food;

    public static SearchFragment newInstance() {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_search;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        bundleKeyword = new Bundle();
        edtSearch = (EditText) view.findViewById(R.id.edt_search);
        imgSearch = (ImageView) view.findViewById(R.id.img_search);
        imgList = (ImageView) view.findViewById(R.id.img_list);
        imgGrid = (ImageView) view.findViewById(R.id.img_grid);
        tvAllItems = (TextView) view.findViewById(R.id.tv_all_items);
        tvCountItems = (TextView) view.findViewById(R.id.tv_quantity_items);
        rootToolSupport = (ViewGroup) view.findViewById(R.id.root_tool_support);
        setViewAnimationDownsize();
        initControl();
        handleHightLightButton(imgList, imgGrid);
        broadcastData = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (listFoodsFragment!=null){
                    int countItems = listFoodsFragment.viewModel.getListData().size();
                    if (countItems > 0) {
//                    rootToolSupport.setVisibility(View.VISIBLE);
                        setViewAnimationUpsize();
                        if (countItems == 1) {
                            tvCountItems.setText(String.valueOf(countItems) + " ITEM FOUND...");
                        } else {
                            tvCountItems.setText(String.valueOf(countItems) + " ITEMS FOUND...");
                        }

                    } else {
                        setViewAnimationDownsize();
//                    rootToolSupport.setVisibility(View.GONE);
                    }
                }


            }
        };
        AppController.registerBroadcast(broadcastData, Constant.ACTION_LOAD_DATA_SEARCH);


    }

    private void setViewAnimationUpsize() {
        AniChangeSizeView aniUpSize = new AniChangeSizeView(rootToolSupport,
                AppUtil.getScreenWidth(getActivity())/7);
        aniUpSize.setDuration(1000);
        rootToolSupport.clearAnimation();
        rootToolSupport.setAnimation(aniUpSize);
    }

    private void setViewAnimationDownsize() {
        AniChangeSizeView aniUpSize = new AniChangeSizeView(rootToolSupport,
                0);
        aniUpSize.setDuration(1000);
        rootToolSupport.clearAnimation();
        rootToolSupport.setAnimation(aniUpSize);
    }

    @Override
    protected void getData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppController.unregisterBroadCast(broadcastData);
    }

    @Override
    public void onClick(View view) {
        if (view == imgSearch) {
            handleSearch();
        } else if (view == imgGrid) {
            switchTypeViewList(Constant.GRID_ITEMS, imgGrid, imgList);
        } else if (view == imgList) {
            switchTypeViewList(Constant.LIST_ITEMS, imgList, imgGrid);
        } else if (view == tvAllItems) {

        }
    }

    private void initControl() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    handleSearch();
                }
                return false;
            }
        });
        imgSearch.setOnClickListener(this);
        imgGrid.setOnClickListener(this);
        imgList.setOnClickListener(this);
        tvAllItems.setOnClickListener(this);
        tvAllItems.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    /***********
     * Part  handle switch Type view list items
     * @param typeList
     * @param imgSelected
     * @param imgUnSelected
     */
    private void switchTypeViewList(int typeList, ImageView imgSelected, ImageView imgUnSelected) {
        if (this.typeList != typeList) {
            this.typeList = typeList;

            if (this.typeList == Constant.LIST_ITEMS) {
                listFoodsFragment.layoutItem = R.layout.item_food;
                layoutItem = R.layout.item_food;
            } else if (this.typeList == Constant.GRID_ITEMS) {
                listFoodsFragment.layoutItem = R.layout.item_food_grid;
                layoutItem = R.layout.item_food_grid;
            }
            ((SearchFoodsVM) listFoodsFragment.viewModel).setTypeList(typeList);
            handleHightLightButton(imgSelected, imgUnSelected);
            listFoodsFragment.handleChangeTypeListItems();
        }

    }


    private void handleHightLightButton(ImageView imgSelected, ImageView imgUnSelected) {
        imgUnSelected.setColorFilter(null);
        imgSelected.setColorFilter(AppUtil.getColor(getActivity(), R.color.color_red));
    }

    /*****************************
     *
     * Part Handle search
     */
    private boolean handleSearch() {
        String keyword = edtSearch.getText().toString().trim();
        if (StringUtil.isEmpty(keyword)) {
            AppUtil.showToast(getActivity(), R.string.msg_keyword_required);
            return true;
        } else if (keyword.length() < 3) {
            AppUtil.showToast(getActivity(), R.string.msg_keyword_lenght_required);
            return true;
        } else
            AppUtil.hideSoftKeyboard(getActivity());

        if (!this.keyword.equals(keyword)) {
            this.keyword = keyword;
            showListFoodSearch(keyword);
        }

        return false;
    }

    private void showListFoodSearch(String keyword) {

        listFoodsFragment = (SingleListFragment) getChildFragmentManager().findFragmentByTag(TAG_SEARCH);
        bundleKeyword.putString(Constant.KEY_KEYWORD, keyword);
        bundleKeyword.putInt(Constant.KEY_TYPE_LIST, typeList);
        if (listFoodsFragment == null) {
            listFoodsFragment = SingleListFragment.newInstance(SearchFoodsVM.class, layoutItem, ItemFoodVM.class, bundleKeyword);
        }

        replaceFragment(listFoodsFragment, TAG_SEARCH);
    }

    public void replaceFragment(Fragment fragment, String tag, int enter, int out, boolean isAddBackstack) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager
                .beginTransaction()
                .setCustomAnimations(enter, out)
                .replace(R.id.content, fragment);
        if (isAddBackstack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, String tag, boolean isAddBackstack) {
        replaceFragment(fragment, tag, R.anim.slide_in_left, R.anim.slide_out_left, isAddBackstack);
    }

    public void replaceFragment(Fragment fragment, String tag) {
        replaceFragment(fragment, tag, true);
    }


}
