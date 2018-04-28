package com.jqscp.Util.BaseActivityUtils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.jqscp.R;
import com.jqscp.Util.APPUtils.OSUtils;
import com.jqscp.Util.StatusBarUtils.StatusBarColor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * 通用activity
 */

public class BaseActivity extends AppCompatActivity {
    public static String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//禁止弹出键盘
        TAG = getClass().getSimpleName();//获取Activity名称
        BaseAppManager.getInstance().addActivity(this);//将当前activity添加到队列里面
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseAppManager.getInstance().removeActivity(this);//移出当前activity
    }


    /**
     * 开启新的activity并且关闭自己
     *
     * @param cls 新的activity的字节码
     */
    public void startActivityAndFinishSelf(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    /**
     * 开启新的activity不关闭自己
     *
     * @param cls 新的activity的字节码
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * 开启新的activity不关闭自己
     *
     * @param cls 新的activity的字节码
     */
    public void startActivityAndBundle(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
