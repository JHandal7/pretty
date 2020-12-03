package com.suusoft.restaurantuser.main.listFoods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.base.vm.BaseViewModelList;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.Food;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;

import java.util.ArrayList;

/**
 * Created by Suusoft on 21/07/2017.
 */

public class ListFoodsVM extends BaseViewModelList {
    /**
     * Constructor with fragment
     *
     * @param context context
     */
    private String idCategory;
    private Bundle bundle;
    private int typeList;

    public ListFoodsVM(Context context) {
        super(context);


    }

    public ListFoodsVM(Context context, Bundle bundle) {
        super(context, bundle);
        this.bundle = bundle;
        idCategory = bundle.getString(Constant.KEY_ID_CATE);
        typeList = bundle.getInt(Constant.KEY_TYPE_LIST);
//        getData(1);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        switch (typeList) {
            case Constant.GRID_ITEMS:
                return new GridLayoutManager(self, 2);
            case Constant.LIST_ITEMS:
                return new LinearLayoutManager(self);
            default:
                return new LinearLayoutManager(self);
        }

    }

    @Override
    public void getData(int page) {
        setRefreshing(true);
        RequestManager.getListFoodByCategory(idCategory, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    ArrayList<Food> listFoods = (ArrayList<Food>) response.getDataList(Food.class);
                    if (listFoods != null && !listFoods.isEmpty()) {
                        ArrayList<Food> items = AppController.getInstance().cartManager.getCart();
                        for (Food food : listFoods) {
                            for (Food item : items) {
                                if (!item.isHasVersion())
                                    if (food.getId().equals(item.getId())) {
                                        int index = items.indexOf(item);
                                        food.setSelected(true);
                                        food.setQuantity(item.getQuantity());
                                        items.add(index, food);
                                        items.remove(item);
                                        break;
                                    }
                            }
                        }

                        addListData(listFoods);

                    }
                    Intent intent = new Intent(Constant.ACTION_LOAD_DATA_FOOD);
                    intent.putExtra(Constant.KEY_SIZE_DATA_FOOD, getListData().size());
                    LocalBroadcastManager.getInstance(self).sendBroadcast(intent);
                    setRefreshing(false);
                }
                checkLoadingMoreComplete(response.getRoot().optInt(Constant.KEY_TOTAL_PAGE));
            }

            @Override
            public void onError(String message) {
                setRefreshing(false);
            }
        });


    }

    public void setTypeList(int typeList) {
        this.typeList = typeList;
    }

    public void reloadDataWith(String id) {
        idCategory = id;
        getData(1);
    }
}
