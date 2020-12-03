package com.suusoft.restaurantuser.main.mycart;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.MethodPayment;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;

import java.util.ArrayList;

/**
 * Created by Suusoft on 12/09/2017.
 */

public class MethodPaymentsVM extends BaseViewModelList {
    public MethodPaymentsVM(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void getData(int page) {
        setRefreshing(true);
        RequestManager.getListPaymentMolllie(new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                setRefreshing(false);
                ArrayList<MethodPayment> arrMethodPayment = (ArrayList<MethodPayment>) response.getDataList(MethodPayment.class);
                if (arrMethodPayment != null && !arrMethodPayment.isEmpty()) {
                    addListData(arrMethodPayment);
                }
                checkLoadingMoreComplete(response.getRoot().optInt(Constant.KEY_TOTAL_PAGE));
            }

            @Override
            public void onError(String message) {
                setRefreshing(false);
            }
        });
    }

}
