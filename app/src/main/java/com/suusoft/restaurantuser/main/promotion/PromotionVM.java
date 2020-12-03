package com.suusoft.restaurantuser.main.promotion;

import android.content.Context;

import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.Promotion;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;

import java.util.ArrayList;

/**
 * Created by Suusoft on 18/07/2017.
 */

public class PromotionVM extends BaseViewModelList {
    /**
     * Constructor with fragment
     *
     * @param context context
     */
    public PromotionVM(Context context) {
        super(context);
//        getData(1);
    }

    @Override
    public void getData(int page) {
       setRefreshing(true);
        RequestManager.getListPromotions(new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    ArrayList<Promotion> listPromotions = (ArrayList<Promotion>) response.getDataList(Promotion.class);
                    if (listPromotions != null && !listPromotions.isEmpty()) {
                        addListData(listPromotions);
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
