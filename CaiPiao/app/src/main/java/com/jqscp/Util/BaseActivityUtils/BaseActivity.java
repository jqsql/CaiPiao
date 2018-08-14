package com.jqscp.Util.BaseActivityUtils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jqscp.ConfigConsts;
import com.jqscp.MyApplication;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.OSUtils;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;
import com.jqscp.View.Dialog_Hint;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * 通用activity
 * jqs
 */

public class BaseActivity extends AppCompatActivity {
    public static String TAG;
    //----------------沉浸式Begin-----------------------
    private LinearLayout mLinearLayout;//最外层布局
    private FrameLayout mFrameLayout;//用户显示布局
    private View mViewStatusBarPlace;//状态栏等高度布局
    private boolean isFullScreen = false;//是否全屏显示
    private boolean fontIconDark = false;//状态栏字体和图标颜色是否为深色//true会改变状态栏文字为黑色
    private int mStatusBarColor = R.color.APPStartBarColor;//状态栏颜色
    public int mStatusBarHeight = 0;//状态栏高度

    //----------------沉浸式End-----------------------
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//禁止弹出键盘
        TAG = getClass().getSimpleName();//获取Activity名称
        BaseAppManager.getInstance().addActivity(this);//将当前activity添加到队列里面

        creatLayout();
        setStatusBarColor();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (mFrameLayout != null) {
            mFrameLayout.addView(LayoutInflater.from(this).inflate(layoutResID, null));
        } else {
            super.setContentView(layoutResID);
        }
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

    /**
     * 判断当前用户是否登陆
     *
     * @return
     */
    public boolean isUserLogin() {
        return ConfigConsts.isLogin && Sharedpreferences_Utils.getInstance(this).getBoolean("isUserLogin");
    }


    /**
     * 数据正在加载的展示
     */
    public void LoaderDialog() {

    }

    /**
     * 结果提示框
     */
    public void hintDialogShort(String hint) {
        if (hint == null)
            return;
        Dialog_Hint.Instance(this, hint, null).dismissDelayed();
    }

    /**
     * 结果提示框
     */
    public void showHintDialog(String hint) {
        if (hint == null)
            return;
        Dialog_Hint.Instance(this, hint, null).show();
    }

    /**
     * 结果提示框
     */
    public void dismissHintDialog() {
        Dialog_Hint.Instance(this, "", null).dismiss();
    }



    /**----------------------------沉浸式分界线---------------------------*/
    protected void setStatusBarColor(){
        setFontIconDark(false, mStatusBarColor);//修改状态栏颜色
    }


    /**
     * 设置是否为深色，true则将状态栏字体颜色变为黑色
     *
     * @param fontIconDark
     */
    protected boolean setFontIconDark(boolean fontIconDark, int statusBarColor) {
        this.fontIconDark = fontIconDark;
        if (statusBarColor != 0)
            this.mStatusBarColor = statusBarColor;
        return setImmersiveStatusBar(mStatusBarColor);
    }

    /**
     * 设置是否为全屏模式
     *
     * @param fullScreen
     */
    protected boolean setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
        return setImmersiveStatusBar(mStatusBarColor);//修改状态栏颜色
    }

    /**
     * 创建布局
     */
    private void creatLayout() {
        mLinearLayout = new LinearLayout(this);
        mFrameLayout = new FrameLayout(this);
        mViewStatusBarPlace = new View(this);
        mStatusBarHeight = getStatusBarHeight();
        ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mStatusBarHeight);
        mViewStatusBarPlace.setLayoutParams(params2);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setLayoutParams(params);
        mFrameLayout.setLayoutParams(params);
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.addView(mLinearLayout);
        mLinearLayout.addView(mViewStatusBarPlace, 0);
        mLinearLayout.addView(mFrameLayout, 1);
    }

    /**
     * 设置沉浸式状态栏 * * @param fontIconDark 状态栏字体和图标颜色是否为深色
     */
    protected boolean setImmersiveStatusBar(int statusBarColor) {
        if (setTranslucentStatus()) {
            if (fontIconDark) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || OSUtils.isMiui() || OSUtils.isFlyme()) {
                    setStatusBarFontIconDark(true);
                } else {
                    if (statusBarColor == Color.WHITE) {
                        //如果状态栏字体为白色，则改变背景色为灰色
                        statusBarColor = 0xffcccccc;
                    }
                }
            } else {
                statusBarColor = mStatusBarColor;
            }
            setStatusBarPlaceColor(statusBarColor);
            return true;
        } else {
            return false;
        }
    }

    private void setStatusBarPlaceColor(int statusColor) {
        if (mLinearLayout == null || mViewStatusBarPlace == null)
            return;
        if (!isFullScreen) {
            mViewStatusBarPlace.setBackgroundColor(getResources().getColor(statusColor));
            mViewStatusBarPlace.setVisibility(View.VISIBLE);
        } else {
            mViewStatusBarPlace.setVisibility(View.GONE);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    protected int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 设置状态栏透明
     */
    private boolean setTranslucentStatus() {
        //5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            return false;
        }
        return true;
    }


    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param dark 状态栏字体是否为深色
     */
    private void setStatusBarFontIconDark(boolean dark) {
        // 小米MIUI
        try {
            Window window = getWindow();
            Class clazz = getWindow().getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {
                //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {
                //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 魅族FlymeUI
        try {
            Window window = getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // android6.0+系统
        // 这个设置和在xml的style文件中用这个<item name="android:windowLightStatusBar">true</item>属性是一样的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }
    /**-------------------------------------END---------------------------------------------**/
    public static PackageInfo getPackageInfo() {
        PackageManager manager = MyApplication.getMContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(MyApplication.getMContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return info;
    }
    /**
     * 返回键拦截监听
     *
     * @return
     */
    public boolean onKeyBackClick() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (onKeyBackClick())
                    return true;
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
