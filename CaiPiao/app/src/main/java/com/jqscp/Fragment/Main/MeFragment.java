package com.jqscp.Fragment.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jqscp.Activity.Login.LoginActivity;
import com.jqscp.Activity.MainActivity;
import com.jqscp.Activity.MySelf.SettingActivity;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.RxBusBean;
import com.jqscp.Dao.OnResultClick;
import com.jqscp.Dao.UserDao;
import com.jqscp.MyApplication;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.DisplayUtil;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;
import com.jqscp.Util.BaseActivityUtils.BaseFragment;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;
import com.jqscp.View.PersonalItemView;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/6/12.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private MainActivity mMainActivity;
    private RelativeLayout mTopBarLayout;
    private TextView mNickName;//昵称

    private PersonalItemView mSetting;//设置

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
        mSetting = mView.findViewById(R.id.Main_Me_Setting);
        mNickName = mView.findViewById(R.id.Main_Me_NickName);

        mTopBarLayout = mView.findViewById(R.id.Main_Me_TopLayout);
        if (mMainActivity.isSuccessStatusBar) {
            int pad = DisplayUtil.dip2px(_this, 16);
            mTopBarLayout.setPadding(pad, pad + _this.mStatusBarHeight, pad, pad);
        }

        mSetting.setOnClickListener(this);
        mNickName.setOnClickListener(this);
    }

    /**
     * 数据处理
     */
    private void initData() {
        if (_this.isUserLogin()) {
            mNickName.setText("-昵称-");
        } else {
            mNickName.setText("未登录");
        }
        UserDao.GetOrCheckMoney(10, new OnResultClick<BaseHttpBean>() {
            @Override
            public void success(BaseHttpBean<BaseHttpBean> bean) {
                ALog.e("" + bean.getMessage());
            }

            @Override
            public void fail(Throwable throwable) {

            }
        });
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
                    mNickName.setText("未登录");
                }else if (rxBusBean.getType() == RxBusType.JustLoginIn) {
                    //登录成功回调
                    mNickName.setText("-昵称-");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Main_Me_NickName:
                if (!_this.isUserLogin())
                    _this.startActivity(LoginActivity.class);
                break;
            case R.id.Main_Me_Setting:
                _this.startActivity(SettingActivity.class);
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //需要放在onResume的方法放在该处执行
            //initData();
        } else {
            //界面不可见的时候执行的方法
        }
    }
}
