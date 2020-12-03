package com.suusoft.restaurantuser.main.promotion;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseAdapterVM;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.listener.IOnItemClickedListener;
import com.suusoft.restaurantuser.model.Promotion;
import com.suusoft.restaurantuser.util.AppUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by Suusoft on 18/07/2017.
 */

public class ItemPromotionVM extends BaseAdapterVM implements IOnItemClickedListener {
    private Promotion model;

    public ItemPromotionVM(Context self, Object model) {
        super(self);
        this.model = (Promotion) model;
    }

    @Override
    public void setData(Object object) {
        model = (Promotion) object;
    }

    public String getDescription() {
        return model.getDescription();
    }

    public String getDiscount() {
        return model.getDetailSale();
    }

    public String getName() {
        return model.getName();
    }

    public String getFavourite() {
        return String.valueOf(model.getFavourite());
    }

    public String getTime() {
        return model.getTimeComment();
    }

    public String getNumComment() {
        return String.valueOf(model.getNumComment());
    }

    public String getImage() {
        return model.getImage();
    }

    public String getPromoCode() {
        if (model.getPromoCode() == null || (model.getPromoCode() != null && model.getPromoCode().isEmpty())) {
            return "";
        }
        return "Promo Code: " + model.getPromoCode();
    }


    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(android.R.drawable.stat_notify_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(view.getContext())
                .load(imageUrl)
                .apply(options)
                .into(view);
        Log.e("imageUrl", "loadImage: "+ imageUrl);
//        Picasso.with(view.getContext())
//                .load(imageUrl)
//                .into(view);

    }

    public Drawable getIcon() {
        if (model.getIs_favourite()) {
            return ContextCompat.getDrawable(self, R.drawable.ic_like);
        } else {
            return ContextCompat.getDrawable(self, R.drawable.ic_dislike);
        }
    }

    @Override
    public void onItemClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.KEY_PROMOTION, model);
        AppUtil.startActivity(self, PromotionDetailActivity.class, bundle);
    }

    public int getHeightImage() {

        return AppUtil.getScreenHeight((AppCompatActivity) self) / 7;
    }
}
