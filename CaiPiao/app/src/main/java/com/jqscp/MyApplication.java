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
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        ConfigConsts.isLogin= Sharedpreferences_Utils.getInstance(mContext).getBoolean("isUserLogin");
        ConfigConsts.ServerToken=Sharedpreferences_Utils.getInstance(mContext).getString("ServerToken");
    }

    public static Context getMContext() {
        return mContext;
    }
}
