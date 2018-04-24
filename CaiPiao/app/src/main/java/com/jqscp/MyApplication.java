package com.jqscp;

import android.app.Application;
import android.content.Context;

/**
 *
 */

public class MyApplication extends Application{
    private static Context mContext;
    public static String ServerPath="";




    public static Context getMContext() {
        return mContext;
    }
}
