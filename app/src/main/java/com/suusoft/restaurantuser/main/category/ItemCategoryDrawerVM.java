package com.suusoft.restaurantuser.main.category;

import android.content.Context;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.model.Category;


/**
 * Created by Suusoft on 17/07/2017.
 */

public class ItemCategoryDrawerVM extends BaseAdapterVM implements IOnItemClickedListener {
    private Category model;

    public ItemCategoryDrawerVM(Context self, Object model) {
        super(self);
        this.model = (Category) model;
    }

    @Override
    public void setData(Object object) {
        model = (Category) object;
        notifyChange();
    }

    public String getNameCategory() {
        return model.getName();
    }

    public String getNameTypeFood() {
        return model.getNameTypeFood();
    }

    public String getQuantityFoods() {
        return String.format(self.getString(R.string.fomat_food_items), String.valueOf(model.getQuantityFoods()));
    }

    public String getImage() {
        return model.getImage();
    }

    @Override
    public void onItemClicked(View view) {
        if (listener != null) {
            view.setTag(model);
            listener.onItemClicked(view);
        }
    }

    public boolean isSelected() {
        return true;
    }
}
