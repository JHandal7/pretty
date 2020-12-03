package com.suusoft.restaurantuser.main;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivity;
import com.suusoft.restaurantuser.base.view.SingleListFragment;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.main.listFoods.ItemFoodVM;
import com.suusoft.restaurantuser.main.listFoods.SearchFoodsVM;
import com.suusoft.restaurantuser.main.category.CategoryVM;
import com.suusoft.restaurantuser.main.category.ItemCategoryVM;
import com.suusoft.restaurantuser.main.myaccount.RootMyAccountFragment;
import com.suusoft.restaurantuser.main.myaccount.myorder.OrderDetailsActivity;
import com.suusoft.restaurantuser.main.mycart.MyCartActivity;
import com.suusoft.restaurantuser.main.mycart.MyCartFragment;
import com.suusoft.restaurantuser.main.promotion.ItemPromotionVM;
import com.suusoft.restaurantuser.main.promotion.PromotionDetailActivity;
import com.suusoft.restaurantuser.main.promotion.PromotionVM;
import com.suusoft.restaurantuser.main.search.SearchFragment;
import com.suusoft.restaurantuser.main.user.LoginActivity;
import com.suusoft.restaurantuser.model.OpenHour;
import com.suusoft.restaurantuser.model.Order;
import com.suusoft.restaurantuser.model.Promotion;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DialogUtil;
import com.suusoft.restaurantuser.util.StringUtil;
import com.suusoft.restaurantuser.widgets.dialog.MyProgressDialog;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Suusoft on 14/07/2017.
 */

public class MainCustomizeActivity extends BaseActivity implements View.OnClickListener ,AppController.IForceLogin{
    private static final String TAG_MENU = "TAG_MENU";
    private static final String TAG_PROMOTION = "TAG_PROMOTION";
    private static final String TAG_SEARCH = "TAG_SEARCH";
    private static final String TAG_CART = "TAG_CART";
    private static final String TAG_MY_ACCOUNT = "TAG_MY_ACCOUNT";

    private SingleListFragment categoryFragment, promotionFragment;
    private SingleListFragment listFoodsFragment;
    private RootMyAccountFragment rootMyAccountFragment;
    private SearchFragment searchFragment;
    private MyCartFragment myCartFragment;
    private ImageView imgMenu, imgPromotion, imgSearch, imgCart, imgAccount, imgBack;
    private TextView tvTitle;
    private TextView tvNotifiCart;
    private BroadcastReceiver broadcastCartChange;
    private BroadcastReceiver broadcastRemoveItem;

    private Bundle bundleKeyword;
    private MenuPosition currentMenuPosition = MenuPosition.MENU;

    private ViewGroup rootBottomBar ;
    private ViewGroup viewBottom;
    private Animation animUptoBottomBar;



    private enum MenuPosition {
        MENU, PROMOTION, SEARCH, CART, ACCOUNT
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_main_reskin;
    }

    @Override
    protected void initView() {
        AppUtil.logSizeMultiScreen(this);
        bundleKeyword = new Bundle();
        imgMenu = (ImageView) findViewById(R.id.img_menu);
        imgPromotion = (ImageView) findViewById(R.id.img_promotion);
        imgSearch = (ImageView) findViewById(R.id.img_search);
        imgCart = (ImageView) findViewById(R.id.img_cart);
        imgAccount = (ImageView) findViewById(R.id.img_account);
        imgBack = (ImageView) findViewById(R.id.img_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvNotifiCart = (TextView) findViewById(R.id.tv_notifi_cart);
        rootBottomBar = (ViewGroup) findViewById(R.id.root_bottom_bar);
        viewBottom = (ViewGroup) findViewById(R.id.view_bottom);
        showMenu();
        handleHightLightButtonMenu(imgMenu);

        broadcastCartChange = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showNotifiCart();
                if (listFoodsFragment != null && currentMenuPosition == MenuPosition.CART) {
                    listFoodsFragment.adapter.notifyDataSetChanged();
                }
            }
        };

        broadcastRemoveItem = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showNotifiCart();
            }
        };
        AppController.getInstance().cartManager.registerReceiverCartChange(broadcastCartChange);
        AppController.getInstance().cartManager.registerReceiverRemoveItem(broadcastRemoveItem);
        AppController.getInstance().setForceLoginListener(this);
    }

    @Override
    protected void onViewCreated() {
        imgMenu.setOnClickListener(this);
        imgPromotion.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgCart.setOnClickListener(this);
        imgAccount.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        handleTapOnNotification(getIntent().getExtras());
    }

    @Override
    protected void onResume() {
        super.onResume();

        setAnimationShowBottomBar();
    }

    private void setAnimationShowBottomBar() {
        Log.e("anim", "bottombar" );
        viewBottom.setVisibility(View.GONE);
        animUptoBottomBar = AnimationUtils.loadAnimation(self, R.anim.slide_in_up);
        animUptoBottomBar.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e("anim", "onAnimationStart" );
                rootBottomBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("anim", "onAnimationEnd" );
                viewBottom.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rootBottomBar.setAnimation(animUptoBottomBar);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean isForceLogin = handleForceLogin(intent);
        if (isForceLogin) {
            return;
        }
        handleTapOnNotification(intent.getExtras());
    }
    @Override
    public void cleanSystem() {
        Intent intent = new Intent(this, MainCustomizeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Constant.KEY_FORCE_LOGIN, Constant.FORCE_LOGIN);
        startActivity(intent);
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
        AppUtil.hideSoftKeyboard(this);
        handleHightLightButtonMenu(view);
        if (view == imgBack) {
            handleBackPressed();
        } else if (view == imgMenu) {
            if (currentMenuPosition != MenuPosition.MENU) {
                currentMenuPosition = MenuPosition.MENU;
                showMenu();
            }
        } else if (view == imgSearch) {
            if (currentMenuPosition != MenuPosition.SEARCH) {
                currentMenuPosition = MenuPosition.SEARCH;
                showSearch();
            }

        } else if (view == imgPromotion) {
            if (currentMenuPosition != MenuPosition.PROMOTION) {
                currentMenuPosition = MenuPosition.PROMOTION;
                showPromotion();
            }
        } else if (view == imgCart) {
            if (currentMenuPosition != MenuPosition.CART) {
                currentMenuPosition = MenuPosition.CART;
                showMyCart();
            }

        } else if (view == imgAccount) {
            if (currentMenuPosition != MenuPosition.ACCOUNT) {
                currentMenuPosition = MenuPosition.ACCOUNT;
                showMyAccount();
            }

        }
    }


    @Override
    public void onBackPressed() {
        handleBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getInstance().cartManager.unregisterReceiverCartChange(broadcastCartChange);
        AppController.getInstance().cartManager.unregisterReceiverCartChange(broadcastRemoveItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RQ_EXIT_APP && resultCode == Constant.RC_EXIT_APP) {
            AppUtil.startActivity(this, LoginActivity.class);
            finish();
        }
    }

    private void showMenu() {
        Log.e("show", "showMenu");
        setTitle(R.string.menu);
        categoryFragment = (SingleListFragment) getSupportFragmentManager().findFragmentByTag(TAG_MENU);
        if (categoryFragment == null) {
            categoryFragment = SingleListFragment.newInstance(CategoryVM.class, R.layout.item_category_v2, ItemCategoryVM.class);
        }
//        categoryFragment = SingleListFragment.newInstance(CategoryVM.class, R.layout.item_category_reskin, ItemCategoryVM.class);

        replaceFragment(categoryFragment, TAG_MENU);
    }

    private void showPromotion() {
        setTitle(R.string.promotion);
        promotionFragment = (SingleListFragment) getSupportFragmentManager().findFragmentByTag(TAG_PROMOTION);
        if (promotionFragment == null) {
            promotionFragment = SingleListFragment.newInstance(PromotionVM.class, R.layout.item_promotion_v2, ItemPromotionVM.class);
        }
        replaceFragment(promotionFragment, TAG_PROMOTION);
    }

    private void showMyAccount() {
        setTitle(R.string.my_account);
        rootMyAccountFragment = (RootMyAccountFragment) getSupportFragmentManager().findFragmentByTag(TAG_MY_ACCOUNT);
        if (rootMyAccountFragment == null) {
            rootMyAccountFragment = RootMyAccountFragment.newInstance();
        }
        replaceFragment(rootMyAccountFragment, TAG_MY_ACCOUNT);
    }

    private void showSearch() {
        setTitle(R.string.search);
        searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag(TAG_SEARCH);
        if (searchFragment == null) {
            searchFragment = SearchFragment.newInstance();
        }
        replaceFragment(searchFragment, TAG_SEARCH);
    }

    private void showListFoodSearch(String keyword) {
        listFoodsFragment = (SingleListFragment) getSupportFragmentManager().findFragmentByTag(TAG_SEARCH);
        bundleKeyword.putString(Constant.KEY_KEYWORD, keyword);
        if (listFoodsFragment == null) {
            listFoodsFragment = SingleListFragment.newInstance(SearchFoodsVM.class, R.layout.item_food, ItemFoodVM.class, bundleKeyword);
        }

        replaceFragment(listFoodsFragment, TAG_SEARCH);
    }

    //
//    private void showMyCart() {
//        if (NetworkUtility.isNetworkAvailable()) {
//            if (AppController.getInstance().cartManager.countItems() == 0) {
//                AppUtil.showToast(this, R.string.msg_cart_empty);
//            } else {
//                getOpenHours();
//            }
//        } else {
//            AppUtil.showToast(this, R.string.msg_connection_network_error);
//        }
//    }

    private void showMyCart() {
        setTitle(R.string.order);
        myCartFragment = (MyCartFragment) getSupportFragmentManager().findFragmentByTag(TAG_CART);
        if (myCartFragment == null) {
            myCartFragment = MyCartFragment.newInstance();
        }
        replaceFragment(myCartFragment, TAG_CART);
    }

    private void handleHightLightButtonMenu(View view) {
        if (view != imgBack) {
            imgMenu.setColorFilter(AppUtil.getColor(this, R.color.color_nornal_bottom_bar));
            imgPromotion.setColorFilter(AppUtil.getColor(this, R.color.color_nornal_bottom_bar));
            imgSearch.setColorFilter(AppUtil.getColor(this, R.color.color_nornal_bottom_bar));
            imgCart.setColorFilter(AppUtil.getColor(this, R.color.color_nornal_bottom_bar));
            imgAccount.setColorFilter(AppUtil.getColor(this, R.color.color_nornal_bottom_bar));
        }
        if (view == imgMenu) {
            imgMenu.setColorFilter(AppUtil.getColor(this, R.color.color_hight_light_bottom_bar));
        } else if (view == imgPromotion) {
            imgPromotion.setColorFilter(AppUtil.getColor(this, R.color.color_hight_light_bottom_bar));
        } else if (view == imgSearch) {
            imgSearch.setColorFilter(AppUtil.getColor(this, R.color.color_hight_light_bottom_bar));
        } else if (view == imgCart) {
            imgCart.setColorFilter(AppUtil.getColor(this, R.color.color_hight_light_bottom_bar));
        } else if (view == imgAccount) {
            imgAccount.setColorFilter(AppUtil.getColor(this, R.color.color_hight_light_bottom_bar));
        }
    }

    private void handleBackPressed() {
        DialogUtil.showAlertDialog(this, R.string.msg_confirm_exit, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                finish();
            }
        });
    }

    public void replaceFragment(Fragment fragment, String tag, int enter, int out, boolean isAddBackstack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager
                .beginTransaction()
                .setCustomAnimations(enter, out)
                .replace(R.id.fr_root_content, fragment);
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

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(int idRes) {
        setTitle(getString(idRes));
    }

    private void showDialogSearch() {
        setTitle(R.string.search);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_search);

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        final EditText edtSearch = (EditText) dialog.findViewById(R.id.edt_search);
        ImageView imgClose = (ImageView) dialog.findViewById(R.id.img_close);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = edtSearch.getText().toString().trim();
                    if (StringUtil.isEmpty(keyword)) {
                        AppUtil.showToast(getApplicationContext(), R.string.msg_keyword_required);
                        return true;
                    } else if (keyword.length() < 3) {
                        AppUtil.showToast(getApplicationContext(), R.string.msg_keyword_lenght_required);
                        return true;
                    }
                    showListFoodSearch(keyword);
                    dialog.dismiss();
                }
                return false;
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void showNotifiCart() {
        int count = AppController.getInstance().cartManager.countItems();
        if (count > 0) {
            tvNotifiCart.setVisibility(View.VISIBLE);
            tvNotifiCart.setText(String.valueOf(AppController.getInstance().cartManager.countItems()));
        } else {
            tvNotifiCart.setVisibility(View.GONE);
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
                        AppUtil.startActivity(MainCustomizeActivity.this, MyCartActivity.class, bundle);
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

    private void handleTapOnNotification(Bundle bundle) {
        if (bundle != null && bundle.containsKey(Constant.KEY_ORDER_ID)) {
            String id = bundle.getString(Constant.KEY_ORDER_ID);
            if (id != null && !id.isEmpty()) {
                getDetailOrder(id);
            }

        }
        if (bundle != null && bundle.containsKey(Constant.KEY_PROMOTION_ID)) {
            String id = bundle.getString(Constant.KEY_PROMOTION_ID);
            if (id != null && !id.isEmpty()) {
                getDetailPromotion(id);
            }
        }
    }

    private void getDetailOrder(String id) {
        final MyProgressDialog progressDialog = new MyProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestManager.getDetailOrder(id, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                Order order = response.getDataObject(Order.class);
                if (order != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constant.KEY_ORDER, order);
                    AppUtil.startActivity(MainCustomizeActivity.this, OrderDetailsActivity.class, bundle);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                AppUtil.showToast(getApplicationContext(), message);
            }
        });
    }
    private void getDetailPromotion(String id) {
        final MyProgressDialog progressDialog = new MyProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestManager.getDetailPromotions(id, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                Promotion promotion = response.getDataObject(Promotion.class);
                if (promotion != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constant.KEY_PROMOTION, promotion);
                    AppUtil.startActivity(self, PromotionDetailActivity.class, bundle);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
            }
        });
    }
    private boolean handleForceLogin(Intent intent) {
        if (intent.hasExtra(Constant.KEY_FORCE_LOGIN)) {
            int force_login = intent.getIntExtra(Constant.KEY_FORCE_LOGIN, 0);
            if (force_login == Constant.FORCE_LOGIN) {
                AppUtil.startActivity(this, LoginActivity.class);
                finish();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
