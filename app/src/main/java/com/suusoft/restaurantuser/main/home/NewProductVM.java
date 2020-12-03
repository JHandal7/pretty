package com.suusoft.restaurantuser.main.home;

import android.content.Context;

import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.Product;
import com.suusoft.restaurantuser.model.Task;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.base.vm.BaseViewModelList;

/**
 * Created by Suusoft on 7/3/2016.
 */
public class NewProductVM extends BaseViewModelList {

    public NewProductVM(Context context) {
        super(context);
        getData(1);
    }

    @Override
    public void getData(int page) {
        RequestManager.getListProduct(self, Task.ALL, page+"", new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse object) {
                addListData(object.getDataList(Product.class));
                // for loading more feature
                checkLoadingMoreComplete(Integer.parseInt(object.getValueFromRoot(Constant.KEY_TOTAL_PAGE)));

            }

            @Override
            public void onError(String message) {
                AppUtil.showToast(self, message);
            }

        });
    }

}
