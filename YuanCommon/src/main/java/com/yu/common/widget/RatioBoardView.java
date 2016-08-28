package com.yu.common.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 仪表盘view
 * @author yu
 *         Create on 16/8/19.
 */
public class RatioBoardView extends View {

    private Paint mPaint;
    private Paint mPaint2;
    private float mAnimatorValue;

    public RatioBoardView(Context context) {
        super(context);
    }

    public RatioBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(20);
        mPaint.setColor(Color.parseColor("#ff0000"));
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint2 = new Paint(mPaint);
        mPaint2.setColor(Color.parseColor("#ffff00"));

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.start();
    }

    public RatioBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(new RectF(20, 20, getWidth() - 20, getWidth() - 20), 135, 270, false, mPaint);
        canvas.save();
        canvas.drawArc(new RectF(20, 20, getWidth() - 20, getWidth() - 20), 135, 225 * mAnimatorValue, false, mPaint2);
    }
}
