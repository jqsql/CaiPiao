package com.jqscp.Activity.Login;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jqscp.Activity.Setting.ForgetPwdActivity;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.RegisterBean;
import com.jqscp.Bean.RxBusBean;
import com.jqscp.ConfigConsts;
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
    private TextView mForgetPwd;
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
        mForgetPwd = findViewById(R.id.Login_ForgetPwd);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mForgetPwd.setOnClickListener(this);
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

    @Override
    protected void setStatusBarColor() {
        setFontIconDark(false,R.color.AppColor);
    }

    /**
     * 跳转登陆
     */
    public void GoToLogin() {
        final String name = mUserPhone.getText().toString();
        String pwd = mUserPassword.getText().toString();
        if (!VerdictUtil.isPhone(name)) {
            ToastUtils.showShort(this, "请填写正确的手机账号");
            return;
        }
        if (!VerdictUtil.isPassword(pwd)) {
            ToastUtils.showShort(this, "密码必须同时包含字母和数字的6-15位组成");
            return;
        }
        LoginDao.Login(name, pwd, new OnResultClick<RegisterBean>() {
            @Override
            public void success(BaseHttpBean<RegisterBean> bean) {
                if (bean.getCode() == 0) {
                    showHintDialog("登陆成功");
                    ConfigConsts.isLogin=true;
                    Sharedpreferences_Utils.getInstance(LoginActivity.this).setBoolean("isUserLogin",true)
                    .setString("UserPhone",name);
                    if (bean.getData() != null) {
                        ConfigConsts.ServerToken = bean.getData().getToken();
                        Sharedpreferences_Utils.getInstance(LoginActivity.this).setString("ServerToken",bean.getData().getToken());
                    }
                    RxBus.getDefault().post(new RxBusBean(RxBusType.JustLoginIn));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismissHintDialog();
                            finish();
                            //startActivityAndFinishSelf(MainActivity.class, null);
                        }
                    }, ConfigConsts.hintTime);

                }else {
                    hintDialogShort(bean.getMessage());
                }
            }

            @Override
            public void fail(Throwable throwable) {
                hintDialogShort("网络异常");
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
            case R.id.Login_ForgetPwd:
                //忘记密码
                Bundle bundle=new Bundle();
                bundle.putInt("Flag",0);
                startActivity(ForgetPwdActivity.class);
                break;
        }
    }
}
