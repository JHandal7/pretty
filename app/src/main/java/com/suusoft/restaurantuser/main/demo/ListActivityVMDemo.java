package com.suusoft.restaurantuser.main.demo;

import com.suusoft.restaurantuser.base.view.BaseListActivityBinding;
import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.Product;
import com.suusoft.restaurantuser.model.Task;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;

/**
 * Created by Suusoft on 7/16/2016.
 */
public class ListActivityVMDemo extends BaseViewModelList {

    public ListActivityVMDemo(BaseListActivityBinding listBinding) {
        super(listBinding);
        getData(1);
    }

    @Override
    public void getData(int page) {
        RequestManager.getListProduct(self, Task.ALL, page+"", new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse object) {
                addListData(object.getDataList(Product.class));
                checkLoadingMoreComplete(Integer.parseInt(object.getValueFromRoot(Constant.KEY_TOTAL_PAGE)));

            }

            @Override
            public void onError(String message) {
                AppUtil.showToast(self, message);
            }

        });
    }
}
