package com.jqscp.Util.PermissionUtils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * android6.0以上动态申请权限类
 */

public class RxPermissionUtil {
    private RxPermissions rxPermissions;//权限申请
    private static RxPermissionUtil sPermissionUtil;

    public RxPermissionUtil(Activity activity) {
        rxPermissions=new RxPermissions(activity);
    }

    public static RxPermissionUtil getInstance(Activity activity){
        sPermissionUtil=new RxPermissionUtil(activity);
        return sPermissionUtil;
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

    /**
     * 获取8.0安装权限
     */
    public void getInstallApkPermiss(Consumer<Boolean> consumer){
        rxPermissions.request(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                .subscribe(consumer);
    }
    /**
     * 获取读取手机信息权限
     */
    public void getReadPhonePermiss(Consumer<Boolean> consumer){
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(consumer);
    }
    /**
     * 获取手机读写权限
     */
    public void getWritePermiss(Consumer<Boolean> consumer){
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(consumer);
    }
    /**
     * 获取手机相机权限
     */
    public void getCameraPermiss(Consumer<Boolean> consumer){
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(consumer);
    }
    /**
     * 获取手机短信权限
     */
    public void getMmsPermiss(Consumer<Boolean> consumer){
        rxPermissions.request(Manifest.permission.RECEIVE_MMS,
                Manifest.permission.READ_CALL_LOG)
                .subscribe(consumer);
    }
}
