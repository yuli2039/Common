package com.yu.common.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 仪表盘view
 *
 * @author yu
 *         Create on 16/8/19.
 */
public class ArcView extends View {

    private Context context;
    private Paint mPaint;
    private Paint mPaint2;
    private float mAnimatorValue = 0;
    private float mRatio = 0f;// 当前进度

    public ArcView(Context context) {
        super(context);
    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(24);
        mPaint.setColor(Color.parseColor("#e3e3e3"));
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint2 = new Paint(mPaint);
//        mPaint2.setColor(context.getResources().getColor(R.color.main_color));
        mPaint2.setColor(Color.BLUE);
        mPaint2.setStrokeWidth(26);
    }

    public ArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFrontColor(int colorId){
        mPaint2.setColor(context.getResources().getColor(colorId));
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 背景弧线 135°~270°
        canvas.drawArc(new RectF(20, 20, getWidth() - 20, getWidth() - 20), 135, 270, false, mPaint);
        if (mRatio > 0) {
            // 前景进度弧线
            canvas.drawArc(new RectF(20, 20, getWidth() - 20, getWidth() - 20), 135, mAnimatorValue * 270, false, mPaint2);
        }
    }

    /**
     * 设置进度
     *
     * @param ratio 0~1之间
     */
    public void setRatio(float ratio) {
        if (ratio > 1 || ratio < 0) return;
        if (ratio == mRatio) return;

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(mRatio, ratio);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(1400);
        valueAnimator.start();

        this.mRatio = ratio;
    }
}
