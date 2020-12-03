package com.suusoft.restaurantuser.main.myaccount.myorder;

import android.content.Context;
import android.databinding.ObservableInt;
import android.view.View;

import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.model.Food;
import com.suusoft.restaurantuser.model.ProductOption;
import com.suusoft.restaurantuser.model.ProductVersion;
import com.suusoft.restaurantuser.util.AppUtil;

import java.util.ArrayList;

/**
 * Created by Suusoft on 16/08/2017.
 */

public class ItemFoodOrderDetailsVM extends BaseAdapterVM {
    private Food model;
    public ObservableInt isShowOptions;

    public ItemFoodOrderDetailsVM(Context self, Object o) {
        super(self);
        model = (Food) o;
        isShowOptions = new ObservableInt(View.GONE);
    }

    @Override
    public void setData(Object object) {
        model = (Food) object;
        notifyChange();
    }

    public String getName() {
        return model.getName();
    }

    public String getOptions() {
        String options = "";
        ArrayList<ProductVersion> listProductVersion = model.getProduct_version();
        if (!listProductVersion.isEmpty())
            for (int i = 0; i < listProductVersion.size(); i++) {
                ProductVersion productVersion = listProductVersion.get(i);
                if (productVersion.isSelected()) {
                    ArrayList<ProductOption> listProductOptions = productVersion.getProduct_option();
                    if (!listProductOptions.isEmpty()) {
                        for (ProductOption productOption : listProductOptions) {
                            if (productOption.isSelected()) {
                                options += productOption.getName() + "\n";
                            }
                        }
                    }
                }

            }
        if (options.isEmpty()) {
            isShowOptions.set(View.GONE);
        } else {
            isShowOptions.set(View.VISIBLE);
        }
        notifyChange();
        return options.trim();
    }

    public String getQuantity() {
        return String.valueOf(model.getQuantity());
    }

    public String getPrice() {
        return AppUtil.formatCurrency(model.getPrice_total());
    }

    public int isVisiblePriceSale() {
        return model.getDiscountPromoCode() == 0 ? View.GONE : View.VISIBLE;
    }

    public String getPriceDiscount() {
        return AppUtil.formatCurrency(model.getPrice_discount());
    }
}
