package com.jqscp.Activity.Login;

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

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mReturn;
    private TextView mTitle;
    private EditText mUserPhone;//账号
    private EditText mUserPassword;//密码
    private Button mLogin,mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        mUserPhone = findViewById(R.id.Login_UserPhone);
        mUserPassword = findViewById(R.id.Login_UserPassword);
        mLogin = findViewById(R.id.Login_toLogin);
        mRegister = findViewById(R.id.Login_toRegister);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    /**
     * 数据处理
     */
    private void initData() {
        mTitle.setText("登  陆");

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
     * 跳转登陆
     */
    public void GoToLogin() {
        String name = mUserPhone.getText().toString();
        String pwd = mUserPassword.getText().toString();
        if (name.trim().length() != 11) {
            ToastUtils.showShort(this, "账号必须为手机号");
        }
        if (!VerdictUtil.isPasswordValid(pwd)) {
            ToastUtils.showShort(this, "密码必须由6-15位字母、数字或下划线组成");
        }
        LoginDao.Login(name, pwd, new OnResultClick<RegisterBean>() {
            @Override
            public void success(BaseHttpBean<RegisterBean> bean) {
                ALog.e("" + bean.getMessage());
                if (bean.getCode() == 0) {
                    MyApplication.isLogin=true;
                    Sharedpreferences_Utils.getInstance(LoginActivity.this).setBoolean("isUserLogin",true);
                    if (bean.getData() != null) {
                        MyApplication.ServerToken = bean.getData().getToken();
                        Sharedpreferences_Utils.getInstance(LoginActivity.this).setString("ServerToken",bean.getData().getToken());
                    }
                    RxBus.getDefault().post(new RxBusBean(RxBusType.JustLoginIn));
                    startActivityAndFinishSelf(MainActivity.class, null);
                }
            }

            @Override
            public void fail(Throwable throwable) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Login_toLogin:
                //登陆
                GoToLogin();
                break;
            case R.id.Login_toRegister:
                //注册
                startActivity(RegisterActivity.class);
                break;
        }
    }
}
