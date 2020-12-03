package com.suusoft.restaurantuser.main.listFoods;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import com.suusoft.restaurantuser.AppController;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.model.Food;
import com.suusoft.restaurantuser.util.AppUtil;

/**
 * Created by Suusoft on 21/07/2017.
 */

public class ItemFoodVM extends BaseAdapterVM implements IOnItemClickedListener {
    private Food model;
    private Context context;
    private com.suusoft.restaurantuser.cart.Dialog dialog;
    final static String TAG = ItemFoodVM.class.getSimpleName();

    public ItemFoodVM(Context self, Object o) {
        super(self);
        context = self;
        model = (Food) o;

        dialog = new com.suusoft.restaurantuser.cart.Dialog.Builder()
                .setFood(model)
                .build(self);
    }

    @Override
    public void setData(Object object) {
        model = (Food) object;
        notifyChange();
    }

    public String getName() {
        return model.getName();
    }

    public String getDescription() {
        return model.getDescription();
    }

    public Spanned getContent() {
        Spanned spanned = Html.fromHtml(model.getContent());
        return spanned;
    }

    public void setImage(){

    }

    public String getImage() {
        return model.getImage();

    }




    public int isShowImage() {
//        if (model.getImage().endsWith(Apis.URL_IMAGE_DEFAUNT) || model.getImage().isEmpty()) {
//            return View.GONE;
//        } else {
//            return View.VISIBLE;
//        }
        return View.VISIBLE;
    }

    public String getPrice() {

        return String.format(self.getString(R.string.format_currency), model.getPrice());
    }

    public boolean isSelected() {
        return model.isSelected();
    }

    public String getQuantity() {
        if (model.getQuantity() < 10) {
            return "0.0" + model.getQuantity();
        } else {
            return String.valueOf(model.getQuantity());
        }

    }

    public int isPopular() {
        if (model.getIsTop()) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public int isSale() {
        if (model.getPrice_discount()> 0) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    @Override
    public void onItemClicked(View view) {
        Log.e(TAG, "onItemClicked: "+model.getImage() );
        if (!model.getProduct_version().isEmpty()) {
            dialog.showDialogChooseFood();
        } else {
            if (!model.isSelected()) {
                model.setSelected(true);
                notifyChange();
                AppController.getInstance().cartManager.addToCart(model);
                AppUtil.showToast(view.getContext(),
                        String.format(view.getContext().getString(R.string.add_product_to_cart), model.getName())  );
            }
        }

    }

    public void onAddFood(View view) {
        int quantity = model.getQuantity();
        if (quantity < 99) {
            quantity++;
            model.setQuantity(quantity);
            notifyChange();
        }
    }

    public void onSubFood(View view) {
        int quantity = model.getQuantity();
        if (quantity > 0) {
            quantity--;
            model.setQuantity(quantity);
            notifyChange();
            if (quantity==0){
                AppUtil.showToast(context, String.format(context.getString(R.string.remove_product_from_cart), model.getName()));
            }
        }
    }


}
