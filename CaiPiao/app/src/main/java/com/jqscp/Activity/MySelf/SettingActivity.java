package com.jqscp.Activity.MySelf;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jqscp.Bean.RxBusBean;
import com.jqscp.MyApplication;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;
import com.jqscp.Util.APPUtils.ToastUtils;
import com.jqscp.Util.BaseActivityUtils.BaseActivity;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mReturn;
    private TextView mTitle;
    private Button mQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
        initListen();
    }
    /**
     * 初始化
     */
    private void initView() {
        mReturn=findViewById(R.id.App_TopBar_Return);
        mTitle=findViewById(R.id.App_TopBar_Title);
        mQuit=findViewById(R.id.Main_Setting_Quit);

        mReturn.setOnClickListener(this);
        mQuit.setOnClickListener(this);
    }
    /**
     * 数据处理
     */
    private void initData() {
        mTitle.setText("设置");
    }
    /**
     * 事件监听
     */
    private void initListen() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.App_TopBar_Return:
                //返回
                finish();
                break;
            case R.id.Main_Setting_Quit:
                //退出登录
                if(isUserLogin()) {
                    MyApplication.isLogin = false;
                    MyApplication.ServerToken = "";
                    Sharedpreferences_Utils.getInstance(SettingActivity.this).setBoolean("isUserLogin", false);
                    Sharedpreferences_Utils.getInstance(SettingActivity.this).setString("ServerToken", "");
                    RxBus.getDefault().post(new RxBusBean(RxBusType.JustLoginOut));
                    finish();
                }else {
                    ToastUtils.showShort(SettingActivity.this,"您并未登录！");
                }
                break;
        }
    }
}
