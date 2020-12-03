package com.suusoft.restaurantuser.cart;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.model.ProductVersion;

/**
 * Created by Suusoft on 20/10/2017.
 */

public class ItemVersionVM extends BaseAdapterVM implements IOnItemClickedListener {
    private ProductVersion model;
    private Drawable itemBackground;

    public ItemVersionVM(Context self, Object o) {
        super(self);
        model = (ProductVersion) o;
    }

    @Override
    public void setData(Object object) {
        model = (ProductVersion) object;
        notifyChange();
    }

    public String getNameVersion() {
        return "+ " + model.getName();
    }

    public Drawable getItemBackground() {
        if (model.isSelected()) {
            itemBackground = ContextCompat.getDrawable(self, R.drawable.bg_pressed_color_red);
            return itemBackground;
        } else {
            itemBackground = ContextCompat.getDrawable(self, R.drawable.bg_border_grey_radius_white);
            return itemBackground;
        }
    }

    public int getTextColor() {
        if (model.isSelected()) {

            return ContextCompat.getColor(self, R.color.white);
        } else {
            return ContextCompat.getColor(self, R.color.textColorSecondary);
        }
    }

    @Override
    public void onItemClicked(View view) {
        view.setTag(model);
        if (listener != null) {
            listener.onItemClicked(view);
        }
    }

    public boolean getSelected() {
        return true;
    }

    @BindingAdapter("setSelected")
    public static void setSelected(TextView tv, boolean selected) {
        tv.setSelected(selected);
    }
}
