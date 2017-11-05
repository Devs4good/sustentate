package com.sustentate.app.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AspectImageView extends ImageView {
    public AspectImageView(Context context) {
        super(context);
    }

    public AspectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AspectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
