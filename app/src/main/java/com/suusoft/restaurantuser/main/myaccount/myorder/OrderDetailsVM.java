package com.suusoft.restaurantuser.main.myaccount.myorder;

import android.content.Context;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.Food;
import com.suusoft.restaurantuser.model.Order;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.DateTimeUtility;

import java.util.ArrayList;

/**
 * Created by Suusoft on 16/08/2017.
 */

public class OrderDetailsVM extends BaseViewModel {
    private Order model;
    public ArrayList<Food> listFoods;

    public OrderDetailsVM(Context self, Object o) {
        super(self);
        model = (Order) o;
        listFoods = model.getListFoods();
    }

    public String getTotalProduct(){
        return listFoods.size()+"";
    }

    public String getOrderNumber() {
        switch (model.getId().length()) {
            case 1:
                return "#000" + model.getId();
            case 2:
                return "#00" + model.getId();
            case 3:
                return "#0" + model.getId();
            default:
                return "#" + model.getId();
        }
    }

    public String getDeliveredFrom() {
        return model.getDelivery_from();
    }

    public String getDeliveredTo() {
        return model.getDelivery_address();
    }

    public String labelDeliveryTo() {
        if (model.getDelivery_address().equals(Constant.SEA_PALACE)) {
            return self.getString(R.string.receiver_order_at);
        } else {
            return self.getString(R.string.delivered_to);
        }
    }


    public String getStatus() {
        String time = "";
        if (model.getShipping_time() == 0) {
            time = "- ASAP";
        } else {
            time = DateTimeUtility.convertTimeStampToDate(model.getShipping_time());
        }
        return model.getStatus() + " " + time;
    }

    public String getDate() {
        return DateTimeUtility.convertTimeStampToDate(model.getShipping_time(), "EEE, yyyy-MM-dd");
    }

    public String getTime() {
        return DateTimeUtility.convertTimeStampToDate(model.getShipping_time(), "HH:mm aa");
    }

    public String getTotal() {
        return self.getString(R.string.currency) + AppUtil.formatCurrency(model.getTotalPrice());
    }

    public String getDeliveryFee() {
        return self.getString(R.string.currency) + AppUtil.formatCurrency(model.getPrice_shipping());
    }

    public String getRiderTip() {
        return self.getString(R.string.currency) + AppUtil.formatCurrency(model.getShipping_rider_tip());
    }

    public String getPaymentMethod() {
        return model.getMethod_payment();
    }

    public String getPromoCode() {
        return model.getPromotion_code();
    }

    public int isAvailablePromoCode() {
        return model.getPromotion_code().isEmpty() ? View.GONE : View.VISIBLE;
    }

    public String getSale() {
        if (model.getSale() == 0) {
            return model.getSale_text();
        } else {
            return "Sale " + model.getSale() + "%";
        }
    }
}
