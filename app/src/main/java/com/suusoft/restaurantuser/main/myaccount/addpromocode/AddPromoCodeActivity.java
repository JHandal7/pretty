package com.suusoft.restaurantuser.main.myaccount.addpromocode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivityBinding;
import com.suusoft.restaurantuser.base.view.SingleListFragment;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.databinding.ActivityAddPromoCodeBinding;
import com.suusoft.restaurantuser.model.PromoCode;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.StringUtil;

public class AddPromoCodeActivity extends BaseActivityBinding {
    private AddPromoCodeVM viewModel;
    private SingleListFragment listPromoCodesFragment;
    private ActivityAddPromoCodeBinding addPromoCodeBinding;
    private BroadcastReceiver delPromoCodeBroadcast;


    @Override
    protected BaseViewModel getViewModel() {
        viewModel = new AddPromoCodeVM(this);
        return viewModel;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_add_promo_code;
    }

    @Override
    protected void onViewCreated() {
        setToolbarTitle(R.string.add_promo_code);

//        showListPromoCode();
        addPromoCodeBinding = (ActivityAddPromoCodeBinding) binding;
        addPromoCodeBinding.edtPromoCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String code = addPromoCodeBinding.edtPromoCode.getText().toString().trim();
                    if (StringUtil.isEmpty(code)) {
                        AppUtil.showToast(getApplicationContext(), R.string.msg_promo_code_required);
                        return true;
                    }
                    checkPromoCode(code);
                }
                return false;
            }
        });
        delPromoCodeBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                listPromoCodesFragment.adapter.clear();
                listPromoCodesFragment.viewModel.getData(1);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(delPromoCodeBroadcast, new IntentFilter(Constant.MSG_DEL_PROMO_CODE));
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(delPromoCodeBroadcast);
    }

    private void addPromoCode(String code) {
        RequestManager.addPromoCode(code, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    listPromoCodesFragment.adapter.clear();
                    listPromoCodesFragment.viewModel.getData(1);
                    setResult(Constant.RC_PROMO_CODE);
                }
                AppUtil.showToast(getApplicationContext(), response.getMessage());
            }

            @Override
            public void onError(String message) {
                AppUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    private void checkPromoCode(String code) {
        RequestManager.checkPromoCode(code, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                PromoCode promoCode = response.getDataObject(PromoCode.class);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_PROMO_CODE, promoCode);
                setResult(Constant.RC_PROMO_CODE, intent);
                finish();
            }

            @Override
            public void onError(String message) {
                AppUtil.showToast(self, message);
            }
        });
    }

    private void showListPromoCode() {
        listPromoCodesFragment = SingleListFragment.newInstance(ListPromoCodeVM.class, R.layout.item_promo_code, ItemPromoCodeVM.class);
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_root_content, listPromoCodesFragment).commit();
    }

}
