package com.suusoft.restaurantuser.main.myaccount.deliveryaddress;

import android.content.Context;

import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.DeliveryAddress;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;

import java.util.ArrayList;

/**
 * Created by Suusoft on 15/08/2017.
 */

public class ListAddressVM extends BaseViewModelList {


    /**
     * Constructor with fragment
     *
     * @param context context
     */
    public ListAddressVM(Context context) {
        super(context);

    }

    @Override
    public void getData(int page) {
        setRefreshing(true);
        RequestManager.getListAddress(new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    ArrayList<DeliveryAddress> arrAddress = (ArrayList<DeliveryAddress>) response.getDataList(DeliveryAddress.class);
                    if (arrAddress != null && !arrAddress.isEmpty()) {
                        addListData(arrAddress);

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
