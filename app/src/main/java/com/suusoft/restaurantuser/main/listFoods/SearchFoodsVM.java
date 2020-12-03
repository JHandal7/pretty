package com.suusoft.restaurantuser.main.listFoods;

import android.content.Context;
import android.os.Bundle;
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

public class SearchFoodsVM extends BaseViewModelList {

    private int typeList;
    /**
     * Constructor with fragment
     *
     * @param context context
     */
    private String keyword;
    private Bundle bundle;

    public SearchFoodsVM(Context context) {
        super(context);

    }

    public SearchFoodsVM(Context context, Bundle bundle) {
        super(context, bundle);
        this.bundle = bundle;
        keyword = bundle.getString(Constant.KEY_KEYWORD);
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

//        createDataVitural();

        getDataListFood();



    }

    private void getDataListFood() {

        RequestManager.searchFoods(keyword, new BaseRequest.CompleteListener() {
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
                    AppController.sendBroadcast(Constant.ACTION_LOAD_DATA_SEARCH);
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

    private void createDataVitural() {
        ArrayList<Food> listFoods = new ArrayList<>();
        Food f1 = new Food("1", "pizza","hông có đánh giá nào · Cửa hàng thực phẩm","30",
                "https://firebasestorage.googleapis.com/v0/b/store-manager-dcd49.appspot.com/o/product1499220806354.jpg?alt=media&token=a8b21cec-7a5f-452e-b36e-19bc3c22f897" );
        listFoods.add(f1);
        listFoods.add(f1);
        listFoods.add(f1);
        listFoods.add(f1);
        listFoods.add(f1);
        listFoods.add(f1);
        listFoods.add(f1);
        addListData(listFoods);
        AppController.sendBroadcast(Constant.ACTION_LOAD_DATA_SEARCH);
        setRefreshing(false);
    }

    public void setTypeList(int typeList) {
        this.typeList = typeList;
    }

    public void reloadDataWith(String keyword) {
        this.keyword = keyword;
        getData(1);
    }
}
