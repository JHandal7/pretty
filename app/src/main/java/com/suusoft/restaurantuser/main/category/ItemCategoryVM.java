package com.suusoft.restaurantuser.main.category;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.main.ListFoodsActivity;
import com.suusoft.restaurantuser.model.Category;
import com.suusoft.restaurantuser.util.AppUtil;


/**
 * Created by Suusoft on 17/07/2017.
 */

public class ItemCategoryVM extends BaseAdapterVM implements IOnItemClickedListener {
    private Category model;
    private int w, h;

    public ItemCategoryVM(Context self, Object model) {
        super(self);
        this.model = (Category) model;
    }

    @Override
    public void setData(Object object) {
        model = (Category) object;
        notifyChange();
    }
//    @BindingAdapter("android:layout_height")
//
////    public void setHeight(View view) {
////        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
////        layoutParams.height = (int) w;
////        view.setLayoutParams(layoutParams);
////    }
//
//    public int getHeight(){
//        return AppUtil.convertPixelsToDp(self,w);
//    }
//
//    @BindingAdapter("android:layout_width")
//
////    public void setWidth(View view) {
////        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
////        layoutParams.height = (int) w;
////        view.setLayoutParams(layoutParams);
////    }
//
//    public int getWidth(){
//        return AppUtil.convertPixelsToDp(self,w);
//    }

    public String getNameCategory() {
        return model.getName();
    }

    public String getNameTypeFood() {
        return model.getNameTypeFood();
    }

    public String getQuantityFoods() {
//        return String.format(self.getString(R.string.fomat_food_items), String.valueOf(model.getQuantityFoods()));
        return  String.valueOf(model.getQuantityFoods());
    }

    public String getImage() {
        return model.getImage();
    }

    @Override
    public void onItemClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.KEY_CATEGORY, model);
        AppUtil.startActivity(self, ListFoodsActivity.class, bundle);
    }
}
