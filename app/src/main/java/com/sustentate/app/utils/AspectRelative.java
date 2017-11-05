package com.sustentate.app.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/*
 * Created by mzorilla on 11/4/17.
 */

public class AspectRelative extends RelativeLayout {
    public AspectRelative(Context context) {
        super(context);
    }

    public AspectRelative(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRelative(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AspectRelative(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        double height = width * 1.2;
        setMeasuredDimension(width, (int) height);
    }
}
