package com.suusoft.restaurantuser.widgets.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewRegular extends TextView {

    public TextViewRegular(Context context) {
        super(context);

        this.setTypeface(TypeFaceUtil.getInstant().normal);
    }

    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(TypeFaceUtil.getInstant().normal);
    }

    public TextViewRegular(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(TypeFaceUtil.getInstant().normal);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
