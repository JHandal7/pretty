package com.suusoft.restaurantuser.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.widget.RatingBar;

/**
 * Created by Suusoft on 11/25/2016.
 */

public class RatingBarUtil {

    /**
     * fill a color to ratingBar
     * @param color your color
     * @param ratingBar your rating bar
     *
     */
    public static void drawColor(RatingBar ratingBar, int color ){
        ((LayerDrawable)ratingBar.getProgressDrawable()).getDrawable(2).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

    }
}
