package com.sustentate.app.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sustentate.app.R;

/*
 * Created by mzorilla on 11/8/17.
 */

public class Utils {

    public static View getViewIndicator(Context context) {
        float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        float margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());

        View indicator = new View(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = (int) dimension;
        params.height = (int) dimension;
        params.setMargins((int) margin, (int) margin * 2, (int) margin, (int) margin * 2);
        indicator.setLayoutParams(params);
        indicator.setBackground(context.getDrawable(R.drawable.circle_indicator_empty));
        return indicator;
    }
}
