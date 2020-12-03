package com.suusoft.restaurantuser.main.myaccount.addpromocode;

import android.content.Context;

import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.PromoCode;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;

import java.util.ArrayList;

/**
 * Created by Suusoft on 22/08/2017.
 */

public class ListPromoCodeVM extends BaseViewModelList {

    public ListPromoCodeVM(Context context) {
        super(context);
    }

    @Override
    public void getData(int page) {
        setRefreshing(true);
        RequestManager.getListPromoCode(Constant.ALL_PROMO_CODE,new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    ArrayList<PromoCode> arrPromoCode = (ArrayList<PromoCode>) response.getDataList(PromoCode.class);
                    if (arrPromoCode != null && !arrPromoCode.isEmpty()) {
                        addListData(arrPromoCode);
                    }
                    checkLoadingMoreComplete(response.getRoot().optInt(Constant.KEY_TOTAL_PAGE));
                    setRefreshing(false);
                }
            }

            @Override
            public void onError(String message) {
                setRefreshing(false);
            }
        });
    }
}
