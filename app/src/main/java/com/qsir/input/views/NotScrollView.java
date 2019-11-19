package com.qsir.input.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class NotScrollView extends ScrollView {
    public NotScrollView(Context context) {
        super(context);
    }

    public NotScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NotScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
