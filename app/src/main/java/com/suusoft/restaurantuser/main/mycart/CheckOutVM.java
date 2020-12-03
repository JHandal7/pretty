package com.suusoft.restaurantuser.main.mycart;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.datastore.DataStoreManager;
import com.suusoft.restaurantuser.main.myaccount.deliveryaddress.DeliveryAddressActivity;
import com.suusoft.restaurantuser.model.MethodPayment;
import com.suusoft.restaurantuser.util.AppUtil;

/**
 * Created by Suusoft on 17/08/2017.
 */

public class CheckOutVM extends BaseViewModel {
    private double totalFinal;
    private boolean isHasDeliveryFee;
    public String methodPayment;
    private String phone;
    private String image;
    public MethodPayment method;
    public ObservableBoolean isChoseAddressDelivery;
    public ObservableBoolean isRestaurant;

    public CheckOutVM(Context self, double total, boolean isHasDeliveryFee) {
        super(self);
        totalFinal = total;
        this.isHasDeliveryFee = isHasDeliveryFee;
        methodPayment = self.getString(R.string.none);
        phone = DataStoreManager.getUser().getPhone();
        isChoseAddressDelivery = new ObservableBoolean(false);
        isRestaurant = new ObservableBoolean(true);
    }


    public String getTotal() {
        return self.getString(R.string.currency) + AppUtil.formatCurrency(totalFinal);
    }

    public void setIsRestaurant(boolean isRestaurant) {
        this.isRestaurant.set(isRestaurant);
        if (isHasDeliveryFee) {
            if (isRestaurant) {
                totalFinal = totalFinal - AppController.getInstance().cartManager.getDeliveryFee();
            } else {
                totalFinal = totalFinal + AppController.getInstance().cartManager.getDeliveryFee();
            }
        }
        notifyChange();
    }

    public boolean enable() {
        return AppController.getInstance().isChoseAddressDelivery();
    }

    public int getTextColor() {
        return AppController.getInstance().isChoseAddressDelivery() ? AppUtil.getColor(self, R.color.textColorPrimary) : AppUtil.getColor(self, R.color.textColorSecondary);
    }

    public String getMethodPayment() {
        return methodPayment;
    }

    public String getImage() {
        return image;
    }

    public boolean isSelected() {
        return true;
    }

    @SerializedName("setSelected")
    public static void setSelected(TextView view, boolean b) {
        view.setSelected(b);
    }

    public void addNewAddress(View view) {
        ((AppCompatActivity) self).startActivityForResult(new Intent(self, DeliveryAddressActivity.class), 1);
    }

    public void placeOrder(View view) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RQ_METHOD_PAYMENT && resultCode == Constant.RC_METHOD_PAYMENT) {
            method = data.getParcelableExtra(Constant.METHOD_PAYMENT);
            this.methodPayment = method.getDescription();
            image = method.getImage().getNormal();
            notifyChange();
        }

    }

    public void setMethodPayment(String name, String image) {
        this.methodPayment = name;
        this.image = image;
        notifyChange();
    }

    public void showListMethodPayment(View view) {
        Intent intent = new Intent(self, MethodPaymentsActivity.class);
        ((AppCompatActivity) self).startActivityForResult(intent, Constant.RQ_METHOD_PAYMENT);
    }

    private void showPopupPayment(View view) {
        PopupMenu popupMenu = new PopupMenu(self, view);
        popupMenu.inflate(R.menu.menu_method_payment);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.action_paypal:
                        methodPayment = self.getString(R.string.paypal);
                        notifyChange();
                        break;
                    case R.id.action_mollie:
                        methodPayment = self.getString(R.string.mollie);
                        notifyChange();
                        break;
                }
                return false;
            }

        });
        popupMenu.show();
    }
}
