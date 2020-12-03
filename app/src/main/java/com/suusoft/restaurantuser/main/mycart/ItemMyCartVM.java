package com.suusoft.restaurantuser.main.mycart;

import android.content.Context;
import android.view.View;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.model.Food;
import com.suusoft.restaurantuser.model.ProductOption;
import com.suusoft.restaurantuser.model.ProductVersion;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DialogUtil;

import java.util.ArrayList;

/**
 * Created by Suusoft on 01/08/2017.
 */

public class ItemMyCartVM extends BaseAdapterVM implements IOnItemClickedListener {
    private Food model;

    public ItemMyCartVM(Context self, Object o) {
        super(self);
        model = (Food) o;
    }

    @Override
    public void setData(Object object) {
        model = (Food) object;
        notifyChange();
    }

    public String getName() {
        return model.getName();
    }

    public String getQuantity() {
        return String.valueOf(model.getQuantity());
    }

    public String getPrice() {

        double price = AppController.getInstance().cartManager.calculatePriceItem(model) * model.getQuantity();
        return AppUtil.formatCurrency(price);
    }

    public String getPriceSale() {
        double price = AppController.getInstance().cartManager.calculatePriceItemSale(model) * model.getQuantity();
        return AppUtil.formatCurrency(price);
    }

    public int isVisiblePriceSale() {
        return model.getDiscountPromoCode() == 0 ? View.GONE : View.VISIBLE;
    }

    public boolean isSelected() {
        return true;
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
                                options += "+ " + productOption.getName() + " ";
                            }
                        }
                    }
                }

            }
        return options.trim();
    }

    public String getVersion() {
        ArrayList<ProductVersion> listProductVersion = model.getProduct_version();
        String name = "";
        if (!listProductVersion.isEmpty()) {
            for (ProductVersion productVersion : listProductVersion) {
                if (productVersion.isSelected()) {
                    name = productVersion.getName();
                    break;
                }
            }
        }
        return "Size: " + name;
    }

    public int isShowOption() {
        if (getOptions().isEmpty()) {
            return View.GONE;
        } else {
            return View.VISIBLE;
        }
    }

    public int isShowVersion() {
        ArrayList<ProductVersion> listProductVersion = model.getProduct_version();
        if (listProductVersion.isEmpty()) {
            return View.GONE;
        } else {
            return View.VISIBLE;
        }
    }

    public void removeItem(View view) {
        DialogUtil.showAlertDialog(self, R.string.msg_remove_item_cart, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                AppController.getInstance().cartManager.removeFromCart(model);
            }
        });
    }

    public void addItem(View view) {
        int quantity = model.getQuantity();
        if (quantity < 99) {
            quantity++;
            model.setQuantity(quantity);
        }
    }

    public void subItem(View view) {
        int quantity = model.getQuantity();
        if (quantity >= 1) {
            quantity--;
            model.setQuantity(quantity);
            if (quantity==0)
                AppUtil.showToast(view.getContext(),
                        String.format(view.getContext().getString(R.string.remove_product_from_cart), model.getName()));
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
}
