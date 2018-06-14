package com.jqscp.Activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jqscp.Activity.MainActivity;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.RegisterBean;
import com.jqscp.Bean.RxBusBean;
import com.jqscp.Dao.LoginDao;
import com.jqscp.Dao.OnResultClick;
import com.jqscp.MyApplication;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;
import com.jqscp.Util.APPUtils.ToastUtils;
import com.jqscp.Util.APPUtils.VerdictUtil;
import com.jqscp.Util.BaseActivityUtils.BaseActivity;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mReturn;
    private TextView mTitle;
    private EditText mUserPhone;//账号
    private EditText mUserPassword;//密码
    private Button mToRegister;//立即注册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();
        initListen();
    }

    /**
     * 初始化
     */
    private void initView() {
        mReturn = findViewById(R.id.App_TopBar_Return);
        mTitle = findViewById(R.id.App_TopBar_Title);
        mUserPhone = findViewById(R.id.Register_UserPhone);
        mUserPassword = findViewById(R.id.Register_UserPassword);
        mToRegister = findViewById(R.id.Register_toRegister);
        mToRegister.setOnClickListener(this);
    }

    /**
     * 数据处理
     */
    private void initData() {
        mTitle.setText("注  册");
    }

    /**
     * 事件监听
     */
    private void initListen() {
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 注册
     */
    public void GoToRegister() {
        String name = mUserPhone.getText().toString();
        String pwd = mUserPassword.getText().toString();
        if (name.trim().length() != 11) {
            ToastUtils.showShort(this, "账号必须为手机号");
        }
        if (!VerdictUtil.isPasswordValid(pwd)) {
            ToastUtils.showShort(this, "密码必须由6-15位字母、数字或下划线组成");
        }
        LoginDao.Register(name, pwd, new OnResultClick<RegisterBean>() {
            @Override
            public void success(BaseHttpBean<RegisterBean> bean) {
                ALog.e("" + bean.getMessage());
                if (bean.getCode() == 0) {
                    MyApplication.isLogin = true;
                    Sharedpreferences_Utils.getInstance(RegisterActivity.this).setBoolean("isUserLogin", true);
                    if (bean.getData() != null) {
                        MyApplication.ServerToken = bean.getData().getToken();
                        Sharedpreferences_Utils.getInstance(RegisterActivity.this).setString("ServerToken",bean.getData().getToken());
                    }
                    RxBus.getDefault().post(new RxBusBean(RxBusType.JustLoginIn));
                    //跳转并清空MainActivity之上的界面
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void fail(Throwable throwable) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Register_toRegister:
                //立即注册
                GoToRegister();
                break;
        }
    }
}
