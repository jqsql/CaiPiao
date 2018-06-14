package com.jqscp.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class AlipayNotificationListenerService extends NotificationListenerService {
    public AlipayNotificationListenerService() {
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // 这里可以拿到包名，可以按照需要判断。
        String packageName = sbn.getPackageName();
        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("监听微信","in 1");
            Bundle extras = notification.extras;

            if (extras != null) {
                // 这里是具体的title和content，可以从中提取金额
                String title = extras.getString(Notification.EXTRA_TITLE, "");
                String content = extras.getString(Notification.EXTRA_TEXT, "");
                Log.d("监听微信", "title:" + title + "   content:" + content);
            }
        }
    }

    @Override
    public void onListenerConnected()
    {
        Log.e("监听微信","connected");
    }


}
