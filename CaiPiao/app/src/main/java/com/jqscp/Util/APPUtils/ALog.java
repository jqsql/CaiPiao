package com.jqscp.Util.APPUtils;

import android.util.Log;

import com.jqscp.Util.BaseActivityUtils.BaseActivity;

/**
 * 自定义log工具类
 */

public class ALog {
    //log打印开关
    private final static boolean isOpen = true;

    public static void v(String content) {
        if (content == null)
            content = "";
        if (isOpen)
            Log.v(BaseActivity.TAG, content);
    }

    public static void d(String content) {
        if (content == null)
            content = "";
        if (isOpen)
            Log.d(BaseActivity.TAG, content);
    }

    public static void i(String content) {
        if (content == null)
            content = "";
        if (isOpen)
            Log.i(BaseActivity.TAG, content);
    }

    public static void w(String content) {
        if (content == null)
            content = "";
        if (isOpen)
            Log.w(BaseActivity.TAG, content);
    }

    public static void e(String content) {
        if (content == null)
            content = "";
        if (isOpen) {
            int strLength = content.length();
            int start = 0;
            int end = 300;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.e(BaseActivity.TAG, content.substring(start, end));
                    start = end;
                    end = end + 300;
                } else {
                    Log.e(BaseActivity.TAG, content.substring(start, strLength));
                    break;
                }
            }
        }
    }
}
