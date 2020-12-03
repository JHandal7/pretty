package com.suusoft.restaurantuser.widgets.textview;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Suusoft on 14/07/2017.
 */

public class TypeFaceUtil {

    private static TypeFaceUtil instance;

    public Typeface bold, normal, italic, boldItalic, light, lightItalic;
    public Typeface lato_bold, lato_black, lato_light, lato_medium, lato_regular, lato_thin,lato_heavy;

    public static TypeFaceUtil getInstant() {
        if (instance == null) {
            instance = new TypeFaceUtil();
        }

        return instance;
    }

    public void initTypeFace(Context context) {
        instance.bold = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_BOLT);
        instance.normal = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_REGULAR);
        instance.italic = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_ITALIC);
        instance.boldItalic = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_BOLT_ITALIC);
        instance.light = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_LIGHT);
        instance.lightItalic = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_LIGHT_ITALIC);

        instance.lato_black = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_LATO_BLACK);
        instance.lato_bold = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_LATO_BOLD);
        instance.lato_light = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_LATO_LIGHT);
        instance.lato_medium = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_LATO_MEDIUM);
        instance.lato_regular = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_LATO_REGULAR);
        instance.lato_thin = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_LATO_THIN);
        instance.lato_heavy = Typeface.createFromAsset(context.getAssets(), TextFontConfig.FONT_LATO_HEAVY);
    }

}
