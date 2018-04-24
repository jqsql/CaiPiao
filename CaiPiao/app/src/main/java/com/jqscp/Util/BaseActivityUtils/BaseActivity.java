package com.jqscp.Util.BaseActivityUtils;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.jqscp.R;
import com.jqscp.Util.StatusBarUtils.StatusBarColor;


/**
 * 通用activity
 */

public class BaseActivity extends AppCompatActivity {
    public static String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TAG=getClass().getSimpleName();//获取Activity名称
        BaseAppManager.getInstance().addActivity(this);//将当前activity添加到队列里面
        if(isFullScreen())
            StatusBarColor.setStatusBar(this);
        else
            StatusBarColor.setStatusBarColor(this, R.color.colorAccent);//修改状态栏颜色
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseAppManager.getInstance().removeActivity(this);//移出当前activity
    }

    /**
     * 是否需要全屏
     * @return
     */
    protected boolean isFullScreen(){
        return false;
    }
}
