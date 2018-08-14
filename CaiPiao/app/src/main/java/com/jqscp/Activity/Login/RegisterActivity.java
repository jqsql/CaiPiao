package com.jqscp.Activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jqscp.Activity.MainActivity;
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
import com.jqscp.View.MyTextWatcher;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mReturn;
    private TextView mTitle;
    private EditText mUserPhone;//账号
    private EditText mUserPassword;//密码
    //private EditText mPhoneCard;//验证码
    private Button mToRegister;//立即注册
    private Button mGetPhoneCard;//获取手机验证码
    private TextView mTerms;//隐私条款
    private CheckBox mCheckBox;//是否同意隐私条款

    private boolean isOldEdit,isNewEdit;//,isNewEditAgain
    private MyTextWatcher.TextWatcherListen mWatcherListen;

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
        //mPhoneCard = findViewById(R.id.Register_PhoneCard);
        mToRegister = findViewById(R.id.Register_toRegister);
        mGetPhoneCard = findViewById(R.id.Register_PhoneCardBtn);
        mTerms = findViewById(R.id.Register_Terms);
        mCheckBox = findViewById(R.id.Register_CheckBox);
        mReturn.setOnClickListener(this);
        mToRegister.setOnClickListener(this);
        mTerms.setOnClickListener(this);


        mWatcherListen=new MyTextWatcher.TextWatcherListen() {
            @Override
            public void setChageData(int flag, String content, boolean isNotNull) {
                switch (flag){
                    case 1:
                        isOldEdit=isNotNull;
                        break;
                    case 2:
                        isNewEdit=isNotNull;
                        break;
                    /*case 3:
                        isNewEditAgain=isNotNull;
                        break;*/
                }
                if(isOldEdit && isNewEdit){// && isNewEditAgain
                    mToRegister.setEnabled(true);
                    mToRegister.setBackgroundResource(R.drawable.shape_white_4);
                }else {
                    mToRegister.setEnabled(false);
                    mToRegister.setBackgroundResource(R.drawable.shape_grey_4);
                }
            }
        };
        mUserPhone.addTextChangedListener(new MyTextWatcher(1,mWatcherListen));
        mUserPassword.addTextChangedListener(new MyTextWatcher(2,mWatcherListen));
        //mPhoneCard.addTextChangedListener(new MyTextWatcher(3,mWatcherListen));
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
        //获取验证码
        mGetPhoneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //防止重复点击
                String name = mUserPhone.getText().toString();
                if(!VerdictUtil.isPhone(name)) {
                    hintDialogShort("请输入正确的手机号码");
                    return;
                }
                mGetPhoneCard.setEnabled(false);
                new CountDownTimer(60 * 1000, 1000) {
                    @Override
                    public void onTick(long l) {
                        mGetPhoneCard.setText((int) (l / 1000) + "s");
                    }

                    @Override
                    public void onFinish() {
                        //倒计时结束重新请求接口获取数据
                        mGetPhoneCard.setText("重新获取验证码");
                        mGetPhoneCard.setEnabled(true);
                    }
                }.start();
            }
        });
    }

    @Override
    protected void setStatusBarColor() {
        setFontIconDark(false,R.color.AppColor);
    }

    /**
     * 注册
     */
    public void GoToRegister() {
        final String name = mUserPhone.getText().toString();
        String pwd = mUserPassword.getText().toString();
        //String card = mPhoneCard.getText().toString();
        if (name.trim().length() != 11) {
            ToastUtils.showShort(this, "请填写正确的手机账号");
            return;
        }
        if (!VerdictUtil.isPassword(pwd)) {
            ToastUtils.showShort(this, "密码必须同时包含字母和数字的6-15位组成");
            return;
        }
       /* if (TextUtils.isEmpty(card)) {
            ToastUtils.showShort(this, "请填写获取到的验证码");
            return;
        }*/
        if (!mCheckBox.isClickable()) {
            ToastUtils.showShort(this, "必须同意服务隐私条款");
            return;
        }
        LoginDao.Register(name, pwd, new OnResultClick<RegisterBean>() {
            @Override
            public void success(BaseHttpBean<RegisterBean> bean) {
                ALog.e("" + bean.getMessage());
                if (bean.getCode() == 0) {
                    showHintDialog("登陆成功");
                    ConfigConsts.isLogin = true;
                    Sharedpreferences_Utils.getInstance(RegisterActivity.this)
                            .setBoolean("isUserLogin", true)
                            .setString("UserPhone", name);
                    if (bean.getData() != null) {
                        ConfigConsts.ServerToken = bean.getData().getToken();
                        Sharedpreferences_Utils.getInstance(RegisterActivity.this).setString("ServerToken", bean.getData().getToken());
                    }
                    RxBus.getDefault().post(new RxBusBean(RxBusType.JustLoginIn));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismissHintDialog();
                            //跳转并清空MainActivity之上的界面
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
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
        switch (view.getId()) {
            case R.id.App_TopBar_Return:
                //返回
                finish();
                break;
            case R.id.Register_toRegister:
                //立即注册
                GoToRegister();
                break;
            case R.id.Register_Terms:
                //隐私条款

                break;
        }
    }
}
