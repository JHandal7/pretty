package com.suusoft.restaurantuser.animation;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * Created by Suusoft on 11/20/2017.
 */

public class AniChangeSizeView extends Animation {
    private int initialHeight, actualHeight;
    private ViewGroup viewGroup;
    private ImageView btnDeal, btnNoDeal;

    public AniChangeSizeView(ViewGroup viewGroup, int actualHeight){
        this.viewGroup = viewGroup;
        this.actualHeight = actualHeight;
        this.btnDeal = btnDeal;
        this.btnNoDeal = btnNoDeal;
    }

    public void setChildView(ImageView btnDeal, ImageView btnNoDeal){

    }

    @Override
    public void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;

        newHeight = (int)(initialHeight * interpolatedTime);

//        viewGroup.removeAllViews();
        viewGroup.getLayoutParams().height = newHeight;
        viewGroup.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        initialHeight = actualHeight;
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
