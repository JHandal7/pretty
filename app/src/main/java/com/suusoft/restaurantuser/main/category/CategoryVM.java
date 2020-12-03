package com.suusoft.restaurantuser.main.category;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.Category;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;

import java.util.ArrayList;

/**
 * Created by Suusoft on 17/07/2017.
 */

public class CategoryVM extends BaseViewModelList {


    /**
     * Constructor with fragment
     *
     * @param context context
     */
    public CategoryVM(Context context) {
        super(context);
//        getData(1);
    }

    public CategoryVM(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager manager  = new GridLayoutManager(self, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        return manager;
    }


    @Override
    public void getData(int page) {
        setRefreshing(true);
        RequestManager.getListCategory(new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    setRefreshing(false);
                    ArrayList<Category> listCategories = (ArrayList<Category>) response.getDataList(Category.class);
                    if (listCategories != null && !listCategories.isEmpty()) {
                        addListData(listCategories);
                    }
                    checkLoadingMoreComplete(response.getRoot().optInt(Constant.KEY_TOTAL_PAGE));
                }
            }

            @Override
            public void onError(String message) {
                setRefreshing(false);
            }
        });

    }
}
