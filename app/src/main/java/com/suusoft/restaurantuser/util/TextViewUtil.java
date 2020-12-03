package com.suusoft.restaurantuser.util;

import android.graphics.Paint;
import android.widget.TextView;

/**
 * Created by Suusoft on 11/25/2016.
 */

public class TextViewUtil {

    /**
     * draw a center line on textview
     * @param textView TextView is need to draw
     *
     */
    public static void drawCenterLine(TextView textView){
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * draw a under line on textview
     * @param textView TextView is need to draw
     *
     */
    public static void drawUnderLine(TextView textView){
        textView.setPaintFlags(textView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
    }

}
