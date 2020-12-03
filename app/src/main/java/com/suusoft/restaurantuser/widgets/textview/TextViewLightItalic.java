package com.suusoft.restaurantuser.widgets.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewLightItalic extends TextView {

    public TextViewLightItalic(Context context) {
        super(context);

        this.setTypeface(TypeFaceUtil.getInstant().lightItalic);
    }

    public TextViewLightItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(TypeFaceUtil.getInstant().lightItalic);
    }

    public TextViewLightItalic(Context context, AttributeSet attrs,
                               int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(TypeFaceUtil.getInstant().lightItalic);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
