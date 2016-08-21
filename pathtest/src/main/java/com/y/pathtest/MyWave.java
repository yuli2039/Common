package com.y.pathtest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 波浪view
 */
public class MyWave extends View {

    private Paint mFrontWavePaint;
    private Paint mBackWavePaint;
    private Path mFrontWavePath;
    private Path mBackWavePath;
    private Path mClipPath;

    private int mWidth;
    private int mHeight;
    private int mWaveLength;// 波长
    private int mWaveCount;
    private int mCurrentY;
    private int mOffset1 = 0;// 移动波浪用的偏移
    private int mOffset2 = 0;// 移动波浪用的偏移

    private int amplify = 50;// 贝塞尔曲线的控制点y坐标偏移，可以调整振幅
    private float wavelengthRatio = 1.0f;// 波长和view宽度的比例，可以调整波长大小
    private float ratio = 0.0f;// 波浪占整个view的高度比例0~1

    public MyWave(Context context) {
        super(context);
    }

    public MyWave(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        mClipPath = new Path();
        mFrontWavePath = new Path();
        mBackWavePath = new Path();

        mFrontWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFrontWavePaint.setColor(Color.BLUE);
        mFrontWavePaint.setAlpha(50);
        mFrontWavePaint.setStyle(Paint.Style.FILL);

        mBackWavePaint = new Paint(mFrontWavePaint);
        mBackWavePaint.setAlpha(100);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
        mWaveLength = (int) (w * wavelengthRatio);
        mWaveCount = (int) Math.round(mWidth / mWaveLength + 1.5);
        getCurrentY(h, ratio);


        // 新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，
        // 玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        Shader mShader = new LinearGradient(mWidth / 2, 0, mWidth / 2, mHeight, new int[]{Color.BLUE, Color.RED}, null, Shader.TileMode.CLAMP);
        mFrontWavePaint.setShader(mShader);
        mBackWavePaint.setShader(mShader);

        startAnim();
    }

    /**
     * 根据ratio和view的高度获取当前波浪的高度
     *
     * @param h view高度
     * @param r 波浪高度的比例
     */
    private void getCurrentY(int h, float r) {
        if (r == 0f) {
            mCurrentY = h - 2 * amplify;
        } else if (r == 1f) {
            mCurrentY = amplify;
        } else {
            mCurrentY = (int) ((h - 2 * amplify) * (1f - r));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        clipCanvas(canvas);
        drawFrontWave(canvas);
        drawBackWave(canvas);
    }

    private void drawBackWave(Canvas canvas) {
        mBackWavePath.reset();
        mBackWavePath.moveTo(-mWaveLength + mOffset2, mCurrentY);
        for (int i = 0; i < mWaveCount; i++) {
            //  二次贝塞尔方程 quadTo函数：第一个点为控制点，第二个点为终点
            mBackWavePath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset2,
                    mCurrentY - amplify,
                    (-mWaveLength / 2) + (i * mWaveLength) + mOffset2,
                    mCurrentY);
            mBackWavePath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset2,
                    mCurrentY + amplify,
                    i * mWaveLength + mOffset2,
                    mCurrentY);
        }
        mBackWavePath.lineTo(mWidth, mHeight);
        mBackWavePath.lineTo(0, mHeight);
        mBackWavePath.close();
        canvas.drawPath(mBackWavePath, mBackWavePaint);
    }

    private void drawFrontWave(Canvas canvas) {
        mFrontWavePath.reset();
        mFrontWavePath.moveTo(-mWaveLength + mOffset1, mCurrentY);
        for (int i = 0; i < mWaveCount; i++) {
            mFrontWavePath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset1,
                    mCurrentY + amplify,
                    (-mWaveLength / 2) + (i * mWaveLength) + mOffset1,
                    mCurrentY);
            mFrontWavePath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset1,
                    mCurrentY - amplify,
                    i * mWaveLength + mOffset1,
                    mCurrentY);
        }
        mFrontWavePath.lineTo(mWidth, mHeight);
        mFrontWavePath.lineTo(0, mHeight);
        mFrontWavePath.close();
        canvas.drawPath(mFrontWavePath, mFrontWavePaint);
    }

    /**
     * 裁剪画布，把view下边裁剪成波浪形
     */
    private void clipCanvas(Canvas canvas) {
        mClipPath.reset();
        mClipPath.lineTo(0, mHeight - amplify);

        mClipPath.quadTo(mWaveLength / 4, mHeight - 2 * amplify,
                mWaveLength / 2, mHeight - amplify);
        mClipPath.quadTo(mWaveLength * 3 / 4, mHeight,
                mWaveLength, mHeight - amplify);

        mClipPath.lineTo(mWidth, 0);
        mClipPath.lineTo(0, 0);
        canvas.clipPath(mClipPath);
    }

    /**
     * 控制两个波浪移动
     */
    private void startAnim() {
        //  两个波移动的速度不一样
        ValueAnimator animator = ValueAnimator.ofInt(0, mWaveLength);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset1 = (int) animation.getAnimatedValue();
                //   postInvalidate();
            }
        });
        animator.start();

        ValueAnimator animator2 = ValueAnimator.ofInt(0, mWaveLength);
        animator2.setDuration(1500);
        animator2.setRepeatCount(ValueAnimator.INFINITE);
        animator2.setInterpolator(new LinearInterpolator());
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset2 = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator2.start();
    }

    public float getRatio() {
        return ratio;
    }

    /**
     * 设置高度
     *
     * @param mRatio 0~1f
     */
    public void setRatio(float mRatio) {
        if (mRatio < 0f || mRatio > 1f) return;

        ValueAnimator animator = ValueAnimator.ofFloat(ratio, mRatio);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                getCurrentY(mHeight, value);
                postInvalidate();
            }
        });
        animator.start();
        this.ratio = mRatio;
    }
}
