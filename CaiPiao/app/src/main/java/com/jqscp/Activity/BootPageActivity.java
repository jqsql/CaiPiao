package com.jqscp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jqscp.Util.BaseActivityUtils.BaseActivity;

/**
 * 启动页
 */
public class BootPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setFullScreen(true);

        ToPage();
    }

    private void ToPage(){
        //延时3秒
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(BootPageActivity.this, MainActivity.class));
                finish();
            }
        },3000);
    }

}
