package com.jqscp.Util.PermissionUtils;

import android.app.Activity;
import android.content.Context;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * android6.0动态申请权限类
 */

public class RxPermissionUtil {
    private RxPermissions rxPermissions;//权限申请
    private static RxPermissionUtil sPermissionUtil;

    public RxPermissionUtil(Activity activity) {
        rxPermissions=new RxPermissions(activity);
    }

    /**
     * 申请多个权限
     * @param permission
     * @param consumer
     */
    public void getEachPermissions(Consumer<Permission> consumer, String... permission){
        rxPermissions.requestEach(permission)
                .subscribe(consumer);
    }
    public void DrawOverlays(Context context){

    }

}
