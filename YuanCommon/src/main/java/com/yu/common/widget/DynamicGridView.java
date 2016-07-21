package com.yu.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 不能滚动，高度为wrapContent的GridView
 *
 * @author Yuli
 *         Create on 15/10/15.
 */
public class DynamicGridView extends GridView {

    public DynamicGridView(Context context) {
        super(context);
    }

    public DynamicGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
