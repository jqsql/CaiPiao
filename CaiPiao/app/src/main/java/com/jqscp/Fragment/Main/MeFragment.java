package com.jqscp.Fragment.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jqscp.Activity.Login.LoginActivity;
import com.jqscp.Activity.MainActivity;
import com.jqscp.Activity.MySelf.AccountDetailsActivity;
import com.jqscp.Activity.MySelf.BettingRecordActivity;
import com.jqscp.Activity.MySelf.NoticeMessageActivity;
import com.jqscp.Activity.MySelf.SafetyCenterActivity;
import com.jqscp.Activity.MySelf.SettingActivity;
import com.jqscp.Activity.MySelf.WithdrawActivity;
import com.jqscp.Activity.Setting.ChangeNameActivity;
import com.jqscp.Activity.WebShowActivity;
import com.jqscp.Bean.AccountMoneyBean;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.RxBusBean;
import com.jqscp.Bean.UserInfo;
import com.jqscp.ConfigConsts;
import com.jqscp.Dao.InfoDao;
import com.jqscp.Dao.OnResultClick;
import com.jqscp.Dao.UserDao;
import com.jqscp.MyApplication;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.DisplayUtil;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;
import com.jqscp.Util.APPUtils.ToastUtils;
import com.jqscp.Util.BaseActivityUtils.BaseFragment;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;
import com.jqscp.View.PersonalItemView;
import com.makeramen.roundedimageview.RoundedImageView;

import io.reactivex.functions.Consumer;

/**
 * 我的模块
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private MainActivity mMainActivity;
    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mTopBarLayout;
    private TextView mNickName;//昵称
    private RoundedImageView mHeader;//头像
    private TextView mAccountMoney;//账户余额
    private TextView mCanUseMoney;//可用余额
    private ImageView mNotice;//通知
    private ImageView mKeFu;//客服
    private Button mOutMoney;//提款
    private Button mInMoney;//充值

    private PersonalItemView mSetting;//设置
    private PersonalItemView mRecord;//投注记录
    private PersonalItemView mAccount;//账户明细
    private PersonalItemView mSafety;//安全中心
    private UserInfo user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_me_view, null);
        mMainActivity = (MainActivity) getActivity();
        initView();
        initData();
        initListen();
        return mView;
    }

    /**
     * 初始化
     */
    private void initView() {
        mRefreshLayout = mView.findViewById(R.id.Main_Me_ParentsLayout);
        mHeader = mView.findViewById(R.id.Main_Me_Header);
        mSetting = mView.findViewById(R.id.Main_Me_Setting);
        mRecord = mView.findViewById(R.id.Main_Me_Record);
        mAccount = mView.findViewById(R.id.Main_Me_Account);
        mSafety = mView.findViewById(R.id.Main_Me_Safety);
        mNickName = mView.findViewById(R.id.Main_Me_NickName);
        mOutMoney = mView.findViewById(R.id.Main_Me_OutMoney);
        mInMoney = mView.findViewById(R.id.Main_Me_InMoney);
        mAccountMoney = mView.findViewById(R.id.Main_Me_HaveAccount);
        mCanUseMoney = mView.findViewById(R.id.Main_Me_CanUseMoney);
        mNotice = mView.findViewById(R.id.Main_Me_News);
        mKeFu = mView.findViewById(R.id.Main_Me_Service);

        mTopBarLayout = mView.findViewById(R.id.Main_Me_TopLayout);
        if (mMainActivity.isSuccessStatusBar) {
            int pad = DisplayUtil.dip2px(_this, 16);
            mTopBarLayout.setPadding(pad, pad + _this.mStatusBarHeight, pad, pad);
        }

        mSetting.setOnClickListener(this);
        mRecord.setOnClickListener(this);
        mAccount.setOnClickListener(this);
        mSafety.setOnClickListener(this);
        mNickName.setOnClickListener(this);
        mNotice.setOnClickListener(this);
        mOutMoney.setOnClickListener(this);
        mInMoney.setOnClickListener(this);
    }

    /**
     * 数据处理
     */
    private void initData() {
        if (!_this.isUserLogin()) {
            mNickName.setText("未登录");
            mAccountMoney.setText("0");
            mCanUseMoney.setText("0");
            mNotice.setImageResource(R.drawable.news);
        } else {
            getUserMessage();
        }
    }

    /**
     * 事件监听
     */
    private void initListen() {
        RxBus.getDefault().doSubscribeMain(getActivity(), RxBusBean.class, new Consumer<RxBusBean>() {
            @Override
            public void accept(RxBusBean rxBusBean) throws Exception {
                if (rxBusBean != null)
                    if (rxBusBean.getType() == RxBusType.JustLoginOut) {
                        //退出登录回调
                        initData();
                    } else if (rxBusBean.getType() == RxBusType.JustLoginIn) {
                        //登录成功回调
                        initData();
                    } else if (rxBusBean.getType() == RxBusType.JustMoney) {
                        //金钱发生变化
                        getMoneyMessage();
                    } else if (rxBusBean.getType() == RxBusType.UserInfo) {
                        //用户基本信息变化
                        initData();
                    }
            }
        });
        //刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserMessage();
            }
        });

        //客服
        mKeFu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //防止连续点击
                mKeFu.setEnabled(false);
                getKeFu();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Main_Me_NickName:
                if (!_this.isUserLogin())
                    _this.startActivity(LoginActivity.class);
                else {
                    Bundle bundle = new Bundle();
                    if (user != null)
                        bundle.putString("name", user.getNickname());
                    _this.startActivityAndBundle(ChangeNameActivity.class, bundle);
                }
                break;
            case R.id.Main_Me_Setting:
                //设置
                _this.startActivity(SettingActivity.class);
                break;
            case R.id.Main_Me_OutMoney:
                //提款
                if (_this.isUserLogin()) {
                    _this.startActivity(WithdrawActivity.class);
                }else {
                    _this.startActivity(LoginActivity.class);
                }
                break;
            case R.id.Main_Me_InMoney:
                //充值
                if (_this.isUserLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Url", "http://code.310liao.com/api/appuser/v1/index?token=" + ConfigConsts.ServerToken);
                    bundle.putString("Title", "充值");
                    _this.startActivityAndBundle(WebShowActivity.class, bundle);
                } else {
                    _this.startActivity(LoginActivity.class);
                }
                break;
            case R.id.Main_Me_Record:
                //投注记录
                if (_this.isUserLogin()) {
                    _this.startActivity(BettingRecordActivity.class);
                } else {
                    _this.startActivity(LoginActivity.class);
                }
                break;
            case R.id.Main_Me_Account:
                //账户明细
                if (_this.isUserLogin()) {
                    _this.startActivity(AccountDetailsActivity.class);
                } else {
                    _this.startActivity(LoginActivity.class);
                }
                break;
            case R.id.Main_Me_Safety:
                //安全中心
                if (_this.isUserLogin()) {
                    Bundle bundle = new Bundle();
                    if (user != null) {
                        bundle.putBoolean("isSetBank", user.isIds());
                        bundle.putBoolean("isSetPwd", user.isWithdraw());
                    }
                    _this.startActivityAndBundle(SafetyCenterActivity.class,bundle);
                } else {
                    _this.startActivity(LoginActivity.class);
                }
                break;
            case R.id.Main_Me_News:
                //消息通知
                _this.startActivity(NoticeMessageActivity.class);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unDisposable(this);
    }

    @Override
    protected void showAndData() {
        super.showAndData();
        //可见时调用
        getUserMessage();
    }

    /**
     * 获取用户信息
     */
    private void getUserMessage() {
        UserDao.GetUserInfo(new OnResultClick<UserInfo>() {
            @Override
            public void success(BaseHttpBean<UserInfo> bean) {
                mRefreshLayout.setRefreshing(false);
                if (bean.getCode() == 0) {
                    user = bean.getData();
                    if (user != null) {
                        mAccountMoney.setText(user.getTotalmoney() + "");
                        mCanUseMoney.setText(user.getMoney() + "");
                        mNickName.setText(user.getNickname());
                        if (user.isNewmsg()) {
                            mNotice.setImageResource(R.drawable.gp_person_xinxiaoxi);
                        } else {
                            mNotice.setImageResource(R.drawable.news);
                        }
                    }
                } else {
                    mAccountMoney.setText("0");
                    mCanUseMoney.setText("0");
                    mNickName.setText("未登录");
                    mNotice.setImageResource(R.drawable.news);
                }
            }

            @Override
            public void fail(Throwable throwable) {
                mRefreshLayout.setRefreshing(false);
                mAccountMoney.setText("0");
                mCanUseMoney.setText("0");
                mNickName.setText("未登录");
                mNotice.setImageResource(R.drawable.news);
            }
        });
    }

    /**
     * 获取金钱信息
     */
    private void getMoneyMessage() {
        UserDao.GetOrCheckMoney(0, new OnResultClick<AccountMoneyBean>() {
            @Override
            public void success(BaseHttpBean<AccountMoneyBean> bean) {
                if (bean.getCode() == 0) {
                    AccountMoneyBean moneyBean = bean.getData();
                    if (moneyBean != null) {
                        mAccountMoney.setText(moneyBean.getTotal() + "");
                        mCanUseMoney.setText(moneyBean.getMoney() + "");
                    }
                }
            }

            @Override
            public void fail(Throwable throwable) {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getKeFu(){
        InfoDao.GetKeFu(new OnResultClick<String>() {
            @Override
            public void success(BaseHttpBean<String> bean) {
                mKeFu.setEnabled(true);
                if(bean!=null) {
                    if (bean.getCode() == 0) {
                        String url = bean.getData();
                        Bundle bundle = new Bundle();
                        bundle.putString("Url", url);
                        bundle.putString("Title", "客服");
                        _this.startActivityAndBundle(WebShowActivity.class, bundle);
                    } else {
                        ToastUtils.showShort(_this, bean.getMessage());
                    }
                }
            }

            @Override
            public void fail(Throwable throwable) {
                mKeFu.setEnabled(true);
                ToastUtils.showShort(_this, "网络异常");
            }
        });
    }

}
