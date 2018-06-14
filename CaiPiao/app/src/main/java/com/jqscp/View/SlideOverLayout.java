package com.jqscp.View;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * 滑动覆盖布局
 * jqs
 * 2018.5.3
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class SlideOverLayout extends ViewGroup implements ScrollView.OnScrollChangeListener{
    private ScrollView mBottomScroll;//底部的滑动布局
    private ScrollView mTopScroll;//顶部的滑动布局

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
    }

    interface BottomScrollListener{
        void onSrcollBottom();//滑动到底部
        void onSrcollTop();//滑动到顶部
    }

    public SlideOverLayout(Context context) {
        this(context,null);
    }

    public SlideOverLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideOverLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量子控件布局宽高
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int mViewGroupWidth  = getMeasuredWidth();  //获取当前ViewGroup的总宽度
        //绘画光标位置坐标
        int mPainterPosX=left;
        int mPainterPosY=top;

        //该布局只能有两个子布局
        if(getChildCount()==2){
            mBottomScroll= (ScrollView) getChildAt(0);
            mTopScroll= (ScrollView) getChildAt(1);

            int mBottomWidth=mBottomScroll.getMeasuredWidth();
            int mBottomHeight=mBottomScroll.getMeasuredHeight();
            int mTopWidth=mTopScroll.getMeasuredWidth();
            int mTopHeight=mTopScroll.getMeasuredHeight();


            mBottomScroll.layout(mPainterPosX,mPainterPosY,mPainterPosX+mBottomWidth,mPainterPosY+mBottomHeight);

            mTopScroll.layout(mPainterPosX,mPainterPosY+mBottomHeight,mPainterPosX+mTopWidth,mPainterPosY+mBottomHeight+mTopHeight);

            mBottomScroll.setOnScrollChangeListener(this);
            mBottomScroll.setOnTouchListener(new TouchListenerImpl());
            mTopScroll.setTouchscreenBlocksFocus(true);
        }
    }


    private class TouchListenerImpl implements OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollY=view.getScrollY();
                    int height=view.getHeight();
                    int scrollViewMeasuredHeight=mBottomScroll.getChildAt(0).getMeasuredHeight();
                    if(scrollY==0){
                        System.out.println("滑动到了顶端 view.getScrollY()="+scrollY);
                    }
                    if((scrollY+height)==scrollViewMeasuredHeight){
                        System.out.println("滑动到了底部 scrollY="+scrollY);
                        System.out.println("滑动到了底部 height="+height);
                        System.out.println("滑动到了底部 scrollViewMeasuredHeight="+scrollViewMeasuredHeight);
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    break;

                default:
                    break;
            }
            return false;
        }

    };
}
