package com.suusoft.restaurantuser.widgets.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewLight extends TextView {

    public TextViewLight(Context context) {
        super(context);

        this.setTypeface(TypeFaceUtil.getInstant().light);
    }

    public TextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(TypeFaceUtil.getInstant().light);
    }

    public TextViewLight(Context context, AttributeSet attrs,
                         int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(TypeFaceUtil.getInstant().light);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
