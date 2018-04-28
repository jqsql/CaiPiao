package com.jqscp.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import com.jqscp.R;

/**
 * 圆形背景的自定义view
 */

public class CircleBGTextView extends AppCompatTextView{
    private Context mContext;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 圆的边框颜色
     */
    private int mBorderColor;
    /**
     * 圆的边框宽度
     */
    private int mBorderSize;
    /**
     * 圆的填充颜色
     */
    private int mFillColor;
    /**
     * 是否填充颜色
     */
    private boolean isFillColor;

    private OnViewClickListen mViewClickListen;
    /**
     * 回调
     */
    public interface OnViewClickListen{
        void onClick(boolean isFillColor);
    }

    public void setOnViewClickListen(OnViewClickListen viewClickListen) {
        mViewClickListen = viewClickListen;
    }

    public CircleBGTextView(Context context) {
        this(context,null);
    }

    public CircleBGTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleBGTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        this.setGravity(Gravity.CENTER);
        mPaint = new Paint();
        //获取自定义属性值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleBGTextStyle);
        mBorderColor = mTypedArray.getColor(R.styleable.CircleBGTextStyle_borderColor, getResources().getColor(R.color.Grey));
        mFillColor = mTypedArray.getColor(R.styleable.CircleBGTextStyle_fillColor, getResources().getColor(R.color.Red));
        mBorderSize = mTypedArray.getDimensionPixelSize(R.styleable.CircleBGTextStyle_borderColor, 2);
        isFillColor = mTypedArray.getBoolean(R.styleable.CircleBGTextStyle_isFillColor, false);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //开始绘制 内部实心圆
        mPaint.setAntiAlias(true);//消除锯齿
        if (isFillColor) {//判断是否绘画内部实心圆
            this.setTextColor(Color.WHITE);
            mPaint.setColor(mFillColor);//画笔颜色
            mPaint.setStyle(Paint.Style.FILL);//实心
        }else {
            this.setTextColor(Color.RED);
            mPaint.setColor(mBorderColor);//画笔颜色
            mPaint.setStyle(Paint.Style.STROKE);//空心
            mPaint.setStrokeWidth(mBorderSize);
        }
        //创建一个区域,限制圆弧范围
        RectF rectF = new RectF();
        //设置半径,比较长宽,取最大值
        int radius = getWidth() > getHeight() ? getWidth() : getHeight();
        ViewGroup.LayoutParams params= CircleBGTextView.this.getLayoutParams();
        params.width=radius;
        params.height=radius;
        CircleBGTextView.this.setLayoutParams(params);
        //设置Padding 不一致,绘制出的是椭圆;一致的是圆形
        rectF.set(0,0,radius,radius);
        //绘制圆弧
        canvas.drawArc(rectF,0,360,false,mPaint);
        super.onDraw(canvas);
    }

    public boolean isFillColor() {
        return isFillColor;
    }

    public synchronized void setFillColor(boolean fillColor) {
        isFillColor = fillColor;
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (x + getLeft() < getRight() && y + getTop() < getBottom()) {
                    if(isFillColor){
                        setFillColor(false);
                    }else {
                        setFillColor(true);
                    }
                    mViewClickListen.onClick(isFillColor);
                }
                break;
        }
        return true;
    }


}
