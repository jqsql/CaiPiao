package com.jqscp;

import android.app.Application;
import android.content.Context;

/**
 *
 */

public class MyApplication extends Application{
    private static Context mContext;

    //服务器地址
    public static String ServerPath="http://code.310liao.com/";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
    }

    public static Context getMContext() {
        return mContext;
    }
}
