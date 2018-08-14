package com.jqscp.Activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jqscp.Activity.MySelf.SettingActivity;
import com.jqscp.Adapter.MyPagerAdapter;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.RequestMapPlayBean;
import com.jqscp.Bean.RxBusBean;
import com.jqscp.Bean.SystemVersion;
import com.jqscp.ConfigConsts;
import com.jqscp.Dao.InfoDao;
import com.jqscp.Dao.OnResultClick;
import com.jqscp.Fragment.Main.FindFragment;
import com.jqscp.Fragment.Main.LotteryFragment;
import com.jqscp.Fragment.Main.HomeFragment;
import com.jqscp.Fragment.Main.MeFragment;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;
import com.jqscp.Util.APPUtils.ToastUtils;
import com.jqscp.Util.AppUpgradeUtils.AppUpgradeManager;
import com.jqscp.Util.BaseActivityUtils.BaseActivity;
import com.jqscp.Util.PermissionUtils.RxPermissionUtil;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;
import com.jqscp.View.AskDialogFragment;
import com.jqscp.View.ProgressDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {
    public static MainActivity _This;
    private List<Fragment> mFragmentList;
    private long firstTime = 0;
    private RadioGroup mRadioGroup;
    private RadioButton mHome;
    private RadioButton mLottery;
    private RadioButton mFind;
    private RadioButton mMy;
    private ViewPager mViewPager;
    private MyPagerAdapter mPagerAdapter;
    private FragmentManager mFragmentManager;
    public boolean isSuccessStatusBar;//沉浸式是否成功
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _This=this;

        initView();
        initData();
        initListen();
        getVersion();
    }

    /**
     * 初始化
     */
    private void initView() {
        mRadioGroup=findViewById(R.id.Main_RadioGroup);
        mHome=findViewById(R.id.Main_Home);
        mLottery=findViewById(R.id.Main_Lottery);
        mFind=findViewById(R.id.Main_Find);
        mMy=findViewById(R.id.Main_My);
        mViewPager=findViewById(R.id.Main_ViewPager);
        mFragmentManager=getSupportFragmentManager();
    }
    /**
     * 数据处理
     */
    private void initData() {
        mFragmentList=new ArrayList<>();
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new LotteryFragment());
        mFragmentList.add(new FindFragment());
        mFragmentList.add(new MeFragment());
        mPagerAdapter=new MyPagerAdapter(mFragmentManager,mFragmentList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
    }

    /**
     * 事件监听
     */
    private void initListen() {
        RxBus.getDefault().doSubscribeMain(MainActivity.this, RxBusBean.class, new Consumer<RxBusBean>() {
            @Override
            public void accept(RxBusBean rxBusBean) throws Exception {
                if(rxBusBean.getType()== RxBusType.ToMainHome){
                    mViewPager.setCurrentItem(rxBusBean.getIntType(),false);
                    switch (rxBusBean.getIntType()){
                        case 0:
                            mHome.setChecked(true);
                            break;
                        case 1:
                            mLottery.setChecked(true);
                            break;
                        case 2:
                            mFind.setChecked(true);
                            break;
                        case 3:
                            mMy.setChecked(true);
                            break;
                    }
                }
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.Main_Home:
                        mViewPager.setCurrentItem(0,false);
                        break;
                    case R.id.Main_Lottery:
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.Main_Find:
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.Main_My:
                        mViewPager.setCurrentItem(3,false);
                        break;
                }
            }
        });
    }

    @Override
    protected void setStatusBarColor() {
        isSuccessStatusBar=setFullScreen(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _This=null;
        RxBus.getDefault().unDisposable(this);
    }

    // 返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;// 更新firstTime
                    return true;
                } else { // 两次按键小于2秒时，退出应用
                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);
                    System.exit(0);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void getVersion(){
        InfoDao.GetAppVersion(new OnResultClick<SystemVersion>() {
            @Override
            public void success(BaseHttpBean<SystemVersion> bean) {
                if(bean.getCode()==0){
                    SystemVersion version=bean.getData();
                    if(version!=null){
                        // 拿到版本号后后纯在share中逻辑放在获取网络数据
                        String appVersion = version.getVersionNnumber();
                        try{
                            if(isNeedDownLoadVersion(appVersion)){
                                updateApp(version);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void fail(Throwable throwable) {

            }
        });
    }

    /**
     * 将版本数字化比较,是否进行更新
     */
    private boolean isNeedDownLoadVersion(String onLineStr) {
        String localStr=getPackageInfo().versionName;
        String[] str_OnLine = onLineStr.split("\\.");
        String[] str_Local = localStr.split("\\.");
        if (str_OnLine != null && str_Local != null) {
            if (str_OnLine.length >= 2 && str_Local.length >= 2) {
                if (Integer.parseInt(str_OnLine[0]) > Integer.parseInt(str_Local[0]))
                    return true;
                else if (Integer.parseInt(str_OnLine[1]) > Integer.parseInt(str_Local[1]))
                    return true;
                /*else if (Integer.parseInt(str_OnLine[2]) > Integer.parseInt(str_Local[2]))
                    return true;*/
            }
        }
        return false;
    }

    public void updateApp(final SystemVersion mVersion) {
        if(mVersion.getForce()==1){
            //强制更新
            AskDialogFragment.Instance("发现新版本" + mVersion.getVersionNnumber() + ",请立即更新!",false).setListen(new AskDialogFragment.AskDialogListen() {
                @Override
                public void onOk() {
                    RxPermissionUtil.getInstance(MainActivity.this).getWriteReadPermiss(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if(aBoolean){
                                AppUpgradeManager.getInstance(MainActivity.this, mVersion).startDown();
                                ProgressDialogFragment.Instance("正在下载，请稍后...").show(getFragmentManager());
                            }else {
                                ToastUtils.showShort(MainActivity.this,"请允许应用对手机的读写权限！");
                            }
                        }
                    });
                }
                @Override
                public void onCancel() {
                }
            }).show(getFragmentManager());
        }else {
            AskDialogFragment.Instance("发现新版本" + mVersion.getVersionNnumber() + ",是否立即更新!").setListen(new AskDialogFragment.AskDialogListen() {
                @Override
                public void onOk() {
                    RxPermissionUtil.getInstance(MainActivity.this).getWriteReadPermiss(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if(aBoolean){
                                AppUpgradeManager.getInstance(MainActivity.this, mVersion).startDown();
                            }else {
                                ToastUtils.showShort(MainActivity.this,"请允许应用对手机的读写权限！");
                            }
                        }
                    });
                }
                @Override
                public void onCancel() {
                }
            }).show(getFragmentManager());
        }
    }
}
