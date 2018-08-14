package com.jqscp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.FileUtils;
import com.jqscp.Util.APPUtils.ImgDonwload;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;
import com.jqscp.Util.BaseActivityUtils.BaseActivity;
import com.jqscp.Util.PermissionUtils.RxPermissionUtil;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

/**
 * 启动页
 */
public class BootPageActivity extends BaseActivity {
    private MyHandler handler;
    private String mBootImagePath = "CaiP_Boot.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        /**
         * 请求读写权限
         */
        RxPermissionUtil.getInstance(this).getWritePermiss(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                ALog.e("启动页图片权限----"+aBoolean);
            }
        });
        Bitmap bitmap=FileUtils.getInstance(this).readBootBitmap(mBootImagePath);
        if (bitmap != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                getWindow().getDecorView().setBackground(new BitmapDrawable(bitmap));
            }else {
                getWindow().getDecorView().setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        }else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.boot_page);
        }
        getBootPage();
        ToPage();
    }

    @Override
    protected void setStatusBarColor() {
        setFullScreen(true);
    }

    private void ToPage() {
        //延时3秒
        handler=new MyHandler(BootPageActivity.this);
        handler.sendEmptyMessageDelayed(1, 1500);
    }

    static class MyHandler extends Handler{
        WeakReference<BootPageActivity> mWeakReference;

        public MyHandler(BootPageActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mWeakReference.get();
            if(activity!=null) {
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeMessages(1);
    }

    /**
     * 获取启动页图片
     */
    private void getBootPage() {
       /* int id = Sharedpreferences_Utils.getInstance(this).getInt("BootPageIconID");
        if (id < 3) {
            ALog.e("启动页图片----");
            ImgDonwload.getInstance(this, "http://www.iwatch365.com/data/attachment/album/201405/21/164353j89htg9ywa9f9ag4.jpg", mBootImagePath);
            Sharedpreferences_Utils.getInstance(this).setInt("BootPageIconID", 3);
        } else {
            ALog.e("启动页图片----不下了");
            FileUtils.getInstance(this).deleteBootBitmap(mBootImagePath);
        }*/

    }
}
