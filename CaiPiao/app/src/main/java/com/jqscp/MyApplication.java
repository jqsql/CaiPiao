package com.jqscp;

import android.app.Application;
import android.content.Context;

import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;

/**
 *
 */

public class MyApplication extends Application{
    private static Context mContext;
    public static boolean isLogin;//本地存放是否登陆;
    public static String ServerToken;//用户Token

    //服务器地址
    public static String ServerPath="http://code.310liao.com/";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        isLogin= Sharedpreferences_Utils.getInstance(mContext).getBoolean("isUserLogin");
        ServerToken=Sharedpreferences_Utils.getInstance(mContext).getString("ServerToken");
    }

    public static Context getMContext() {
        return mContext;
    }
}
