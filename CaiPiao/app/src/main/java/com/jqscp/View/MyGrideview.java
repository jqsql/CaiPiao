package com.jqscp.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 防嵌套滑动问题
 */
public class MyGrideview extends GridView {

    public MyGrideview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO 自动生成的构造函数存根
    }

    public MyGrideview(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO 自动生成的构造函数存根
    }

    public MyGrideview(Context context) {
        super(context);
        // TODO 自动生成的构造函数存根
    }


    //重新测量其大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO 自动生成的方法存根
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO 自动生成的方法存根
        return super.onTouchEvent(ev);
    }


}
