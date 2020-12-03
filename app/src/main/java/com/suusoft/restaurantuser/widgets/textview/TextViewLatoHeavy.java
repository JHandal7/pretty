package com.suusoft.restaurantuser.widgets.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewLatoHeavy extends TextView {

    public TextViewLatoHeavy(Context context) {
        super(context);
        this.setTypeface(TypeFaceUtil.getInstant().lato_heavy);
    }

    public TextViewLatoHeavy(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(TypeFaceUtil.getInstant().lato_heavy);
    }

    public TextViewLatoHeavy(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(TypeFaceUtil.getInstant().lato_heavy);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
