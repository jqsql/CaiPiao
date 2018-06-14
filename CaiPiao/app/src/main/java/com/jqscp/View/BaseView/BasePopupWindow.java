package com.jqscp.View.BaseView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;


/**
 * 描述：通用popwindow
 * 作者：贾强胜
 * 时间：2018.6.12
 */
public class BasePopupWindow extends PopupWindow {
    protected View mMenuView;
    private boolean isCanEnable=true;//是否弹出窗体可点击

    public BasePopupWindow(Context context,int layoutID) {
        this(context,null,layoutID);
    }

    public BasePopupWindow(Context context, AttributeSet attrs,int layoutID) {
        this(context, attrs,0,layoutID);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr,int layoutID) {
        super(context, attrs, defStyleAttr);
        setMenuView(context,layoutID);
        init();
    }

    protected void setMenuView(Context context,int  layoutID){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(layoutID, null);
    }
    private void init(){
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(isCanEnable);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.getRootView().getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    protected void setCanEnable(boolean canEnable) {
        isCanEnable = canEnable;
    }
}
