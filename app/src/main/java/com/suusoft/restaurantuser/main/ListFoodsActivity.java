package com.suusoft.restaurantuser.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.animation.AniChangeSizeView;
import com.suusoft.restaurantuser.base.view.BaseActivity;
import com.suusoft.restaurantuser.base.view.SingleAdapter;
import com.suusoft.restaurantuser.base.view.SingleListFragment;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.main.listFoods.ItemFoodVM;
import com.suusoft.restaurantuser.main.listFoods.ListFoodsVM;
import com.suusoft.restaurantuser.main.category.ItemCategoryDrawerVM;
import com.suusoft.restaurantuser.main.mycart.MyCartActivity;
import com.suusoft.restaurantuser.model.Category;
import com.suusoft.restaurantuser.model.OpenHour;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.ImageUtil;
import com.suusoft.restaurantuser.util.NetworkUtility;

import org.json.JSONArray;

import java.util.ArrayList;

public class ListFoodsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgCategory;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private DrawerLayout drawerLayout;
    private RecyclerView rcvCategory;
    private SingleAdapter adapterCategory;
    private ArrayList<Category> listCategories;
    private FrameLayout rootBack, rootMenu;
    private TextView tvTitle;

    private TextView tvQuantityFoodsCart;
    private TextView tvTotalPriceCart;
    private RelativeLayout rootViewCart;
    private ImageView imgGrid, imgList;
    private FloatingActionButton btnSwitchList;
    private TextView tvCountItems;
    private ViewGroup rootToolSupport;

    private boolean isAnimOutDown = false, isAnimInUp = false;

    private SingleListFragment listFoodsFragment;
    private BroadcastReceiver broadcastCartChange;
    private BroadcastReceiver broadcastListData;

    Category category;

    private int typeList = Constant.LIST_ITEMS;
    private int layoutItem = R.layout.item_food;
    private boolean isZoomOut = false, isZoomIn = false;
    private Animation animZoomOut, animZoomIn , animInUp, animOutDown ;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_list_foods;
    }

    @Override
    protected void initView() {
        initUI();
        setAnimationDownsizeTool();
        getBundleData();

//        listenerHideAndShowView();

    }

    private void setAnimationUpsizeTool() {
        AniChangeSizeView aniUpSize = new AniChangeSizeView(rootToolSupport,
                140);
        aniUpSize.setDuration(1000);
        rootToolSupport.clearAnimation();
        rootToolSupport.setAnimation(aniUpSize);
//        rootToolSupport.setVisibility(View.VISIBLE);
    }

    private void setAnimationDownsizeTool() {
        AniChangeSizeView aniUpSize = new AniChangeSizeView(rootToolSupport,
                0);
        aniUpSize.setDuration(1000);
        rootToolSupport.clearAnimation();
        rootToolSupport.setAnimation(aniUpSize);
//        rootToolSupport.setVisibility(View.VISIBLE);
    }

//    private void listenerHideAndShowView() {
//        AppUtil.checkViewIsShow(btnSwitchList, getApplicationContext(), new IListenerVisibleView() {
//            @SuppressLint("WrongConstant")
//            @Override
//            public void onVisibleView() {
//                Log.e("visible", "rootToolSupport " + rootToolSupport.getVisibility());
//                if (rootToolSupport.getVisibility()==View.VISIBLE){
//                    Animation animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_zoom_out );
//                    rootToolSupport.setAnimation(animZoomOut);
//                    animZoomOut.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            rootToolSupport.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//                }
//            }
//
//            @SuppressLint("WrongConstant")
//            @Override
//            public void onGoneView() {
//                Log.e("visible", "rootToolSupport " + rootToolSupport.getVisibility());
//                if (rootToolSupport.getVisibility()==View.GONE){
//                    rootToolSupport.setVisibility(View.VISIBLE);
//                    Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_zoom_in );
//                    rootToolSupport.setAnimation(animZoomIn);
//                    animZoomIn.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//                }
//
//            }
//        });
//
//
//
//
//    }

    @Override
    protected void onViewCreated() {
        setUpToolBar();
        initControl();
        setUpDrawerLayout();
        bindData(category);
        showListFoods();
        showRootViewCart();
        setUpListCategory();
        getListCategory();

        broadcastCartChange = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showRootViewCart();
                listFoodsFragment.adapter.notifyDataSetChanged();
            }
        };
        AppController.getInstance().cartManager.registerReceiverCartChange(broadcastCartChange);

        broadcastListData = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int countItems = intent.getIntExtra(Constant.KEY_SIZE_DATA_FOOD, 0);
                if (countItems == 0) {
                    setAnimationDownsizeTool();
//                    rootToolSupport.setVisibility(View.GONE);

                } else {
                    if (countItems > 1) {
                        tvCountItems.setText(countItems + " " + getString(R.string.msg_items_ready_for_you));
                    } else {
                        tvCountItems.setText(countItems + " " + getString(R.string.msg_item_ready_for_you));
                    }

                    setAnimationUpsizeTool();
                }
            }
        };
        AppController.registerBroadcast(broadcastListData, Constant.ACTION_LOAD_DATA_FOOD);

        handleHightLightButton(imgList, imgGrid);
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NONE;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    public void onClick(View view) {
        if (view == rootBack) {
            finish();
        } else if (view == rootMenu) {
            openMenu();
        } else if (view == rootViewCart) {
            showMyCart();
        } else if (view == imgList) {
            switchTypeViewList(Constant.LIST_ITEMS, imgList, imgGrid);
        } else if (view == imgGrid) {
            switchTypeViewList(Constant.GRID_ITEMS, imgGrid, imgList);
        } else if (view ==btnSwitchList){
            if (typeList==Constant.GRID_ITEMS){
                switchTypeViewList(Constant.LIST_ITEMS);
            }else {
                switchTypeViewList(Constant.GRID_ITEMS);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getInstance().cartManager.unregisterReceiverCartChange(broadcastCartChange);
        AppController.unregisterBroadCast(broadcastListData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RQ_REQUEST_REFRESH && resultCode == Constant.RC_REQUEST_REFRESH) {
            listFoodsFragment.adapter.clear();
            listFoodsFragment.viewModel.getData(1);
        }

    }

    private void initUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setSelected(true);
        imgCategory = (ImageView) findViewById(R.id.img_category);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        rootBack = (FrameLayout) findViewById(R.id.btn_back);
        rootMenu = (FrameLayout) findViewById(R.id.btn_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        rcvCategory = (RecyclerView) findViewById(R.id.rcv_category);

        tvQuantityFoodsCart = (TextView) findViewById(R.id.tv_quantity_foods);
        tvTotalPriceCart = (TextView) findViewById(R.id.tv_total_price);
        rootViewCart = (RelativeLayout) findViewById(R.id.root_view_cart);

        btnSwitchList = (FloatingActionButton) findViewById(R.id.btn_switch_show_list);
        imgList = (ImageView) findViewById(R.id.img_list);
        imgGrid = (ImageView) findViewById(R.id.img_grid);
        rootToolSupport = (ViewGroup) findViewById(R.id.root_tool_support);
        tvCountItems = (TextView) findViewById(R.id.tv_quantity_items);
    }

    private void initControl() {
        rootBack.setOnClickListener(this);
        rootMenu.setOnClickListener(this);
//        rootViewCart.setOnClickListener(this);
        imgGrid.setOnClickListener(this);
        imgList.setOnClickListener(this);
        btnSwitchList.setOnClickListener(this);
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        category = bundle.getParcelable(Constant.KEY_CATEGORY);
    }

    private void bindData(Category category) {
        ImageUtil.setImage(imgCategory, category.getImage());
        setTitleCustomize(category.getName());
    }

    private void setTitleCustomize(String title) {
        tvTitle.setText(title);
    }

    private void setTitleCustomize(int idRes) {
        setTitleCustomize(getString(idRes));
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
    }

    private void setUpDrawerLayout() {
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {

            }
        });
    }

    private void openMenu() {
        if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    private void closeMenu() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    private void showListFoods() {
        Bundle bundleCate = new Bundle();
        bundleCate.putString(Constant.KEY_ID_CATE, category.getId());
        bundleCate.putInt(Constant.KEY_TYPE_LIST, typeList);
        listFoodsFragment = SingleListFragment.newInstance(ListFoodsVM.class, R.layout.item_food, ItemFoodVM.class, bundleCate);
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_root_content, listFoodsFragment).commit();
    }

    private void showRootViewCart() {
        if (AppController.getInstance().cartManager.countItems() > 0) {
            setAnimationInUp();
//            rootViewCart.setVisibility(View.VISIBLE);
        } else {
            setAnimationOutDown();
//            rootViewCart.setVisibility(View.GONE);
        }

        tvQuantityFoodsCart.setText(String.valueOf(AppController.getInstance().cartManager.countItems()));
        tvTotalPriceCart.setText(AppUtil.formatCurrency(AppController.getInstance().cartManager.getTotal()));
    }

    private void setAnimationOutDown() {
        if (!isAnimOutDown){
            Log.e("anim", "setAnimationOutDown isAnimOutDown fale" );
            animOutDown = AnimationUtils.loadAnimation(self, R.anim.slide_out_down);
            animOutDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    rootViewCart.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            rootViewCart.setAnimation(animOutDown);
            isAnimOutDown = true;
            isAnimInUp = false;
        } else {
            Log.e("anim", "isAnimOutDown true " );

        }

    }

    private void setAnimationInUp() {
        if (!isAnimInUp){
            Log.e("anim", "setAnimationInUp isAnimInUp false" );
            animInUp = AnimationUtils.loadAnimation(self,R.anim.slide_in_up);
            animInUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    rootViewCart.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            rootViewCart.setAnimation(animInUp);
            isAnimOutDown = false;
            isAnimInUp = true;
        } else {
            Log.e("anim", "isAnimInUp true" );

        }

    }

    private void showMyCart() {
        if (NetworkUtility.isNetworkAvailable()) {
            if (AppController.getInstance().cartManager.countItems() == 0) {
                AppUtil.showToast(this, R.string.msg_cart_empty);
            } else {
                getOpenHours();
            }
        } else {
            AppUtil.showToast(this, R.string.msg_connection_network_error);
        }
    }

    private void getOpenHours() {
        RequestManager.getOpenHours(new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    ArrayList<OpenHour> arrOpenHours = (ArrayList<OpenHour>) response.getDataList(OpenHour.class);
                    if (arrOpenHours != null && !arrOpenHours.isEmpty()) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(Constant.KEY_LIST_OPEN_HOUR, arrOpenHours);
                        JSONArray jsonArray = response.getRoot().optJSONArray(Constant.KEY_RIDER_TIP);
                        double[] listRiderTips = new double[jsonArray.length()];
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                listRiderTips[i] = jsonArray.optDouble(i);
                            }
                        }
                        bundle.putDoubleArray(Constant.KEY_RIDER_TIP, listRiderTips);
                        Intent intent = new Intent(ListFoodsActivity.this, MyCartActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, Constant.RQ_REQUEST_REFRESH);
                        AppController.getInstance().cartManager.setDeliveryFee(response.getRoot().optDouble(Constant.KEY_DELIVERY_FEE));
                        AppController.getInstance().amount_lowest = response.getRoot().optDouble(Constant.KEY_AMOUNT_LOWEST);
                        AppController.getInstance().amount_promtion_lowest = response.getRoot().optDouble(Constant.KEY_AMOUNT_PROMOTION_LOWEST);
                        AppController.getInstance().isChoseAddressDelivery = response.getRoot().optInt(Constant.KEY_CHOSE_ADDRESS_DELIVERY);
                    }
                }
            }

            @Override
            public void onError(String message) {
                AppUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    private void setUpListCategory() {
        listCategories = new ArrayList<>();
        rcvCategory.setLayoutManager(new LinearLayoutManager(this));
        adapterCategory = new SingleAdapter(this, R.layout.item_category_drawer, listCategories, ItemCategoryDrawerVM.class, new IOnItemClickedListener() {
            @Override
            public void onItemClicked(View view) {
                Category category = (Category) view.getTag();
                bindData(category);
                listFoodsFragment.adapter.clear();
                ((ListFoodsVM) listFoodsFragment.viewModel).reloadDataWith(category.getId());
                closeMenu();
            }
        });
        rcvCategory.setAdapter(adapterCategory);

    }

    private void getListCategory() {
        RequestManager.getListCategory(new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    listCategories.clear();
                    listCategories.addAll(response.getDataList(Category.class));
                    adapterCategory.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

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
            ((ListFoodsVM) listFoodsFragment.viewModel).setTypeList(typeList);
            handleHightLightButton(imgSelected, imgUnSelected);
            listFoodsFragment.handleChangeTypeListItems();
        }

    }

    private void switchTypeViewList(int typeList) {
        if (this.typeList != typeList) {
            this.typeList = typeList;

            if (this.typeList == Constant.LIST_ITEMS) {
                listFoodsFragment.layoutItem = R.layout.item_food;
                layoutItem = R.layout.item_food;
                ImageUtil.setImage(btnSwitchList, R.drawable.ic_view_list);
            } else if (this.typeList == Constant.GRID_ITEMS) {
                ImageUtil.setImage(btnSwitchList, R.drawable.ic_view_grid);
                listFoodsFragment.layoutItem = R.layout.item_food_grid;
                layoutItem = R.layout.item_food_grid;
            }
            ((ListFoodsVM) listFoodsFragment.viewModel).setTypeList(typeList);
            handleHightLightButton();
            listFoodsFragment.handleChangeTypeListItems();
        }
    }


    private void handleHightLightButton(ImageView imgSelected, ImageView imgUnSelected) {
        imgUnSelected.setColorFilter(null);
        imgSelected.setColorFilter(AppUtil.getColor(this, R.color.colorPrimary));
    }


    private void handleHightLightButton() {
        btnSwitchList.setColorFilter(AppUtil.getColor(this, R.color.colorPrimary));
    }

}
