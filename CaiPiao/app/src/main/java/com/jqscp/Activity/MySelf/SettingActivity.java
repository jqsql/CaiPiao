package com.jqscp.Activity.MySelf;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jqscp.Activity.MainActivity;
import com.jqscp.Activity.Setting.AboutUsActivity;
import com.jqscp.Activity.Setting.HelpCenterActivity;
import com.jqscp.Activity.Setting.ShareToActivity;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.RxBusBean;
import com.jqscp.Bean.SystemVersion;
import com.jqscp.ConfigConsts;
import com.jqscp.Dao.InfoDao;
import com.jqscp.Dao.OnResultClick;
import com.jqscp.MyApplication;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.DataCleanManager;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;
import com.jqscp.Util.APPUtils.ToastUtils;
import com.jqscp.Util.AppUpgradeUtils.AppUpgradeManager;
import com.jqscp.Util.BaseActivityUtils.BaseActivity;
import com.jqscp.Util.PermissionUtils.RxPermissionUtil;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;
import com.jqscp.View.AskDialogFragment;
import com.jqscp.View.PersonalItemView;
import com.jqscp.View.ProgressDialogFragment;

import java.io.File;

import io.reactivex.functions.Consumer;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mReturn;
    private TextView mTitle;
    private Button mQuit;
    private PersonalItemView mCache;
    private PersonalItemView mShare;
    private PersonalItemView mInspect;
    private PersonalItemView mAboutUs;
    private PersonalItemView mHelp;

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
        mReturn = findViewById(R.id.App_TopBar_Return);
        mTitle = findViewById(R.id.App_TopBar_Title);
        mQuit = findViewById(R.id.Main_Setting_Quit);
        mCache = findViewById(R.id.Main_Setting_Clear);
        mShare = findViewById(R.id.Main_Setting_Share);
        mInspect = findViewById(R.id.Main_Setting_Inspect);
        mAboutUs = findViewById(R.id.Main_Setting_AboutUs);
        mHelp = findViewById(R.id.Main_Setting_Help);

        mReturn.setOnClickListener(this);
        mQuit.setOnClickListener(this);
        mCache.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mInspect.setOnClickListener(this);
        mAboutUs.setOnClickListener(this);
        mHelp.setOnClickListener(this);
    }

    /**
     * 数据处理
     */
    private void initData() {
        mTitle.setText("设置");

        //获得应用内部缓存(/data/data/com.example.androidclearcache/cache)
        File file = new File(this.getCacheDir().getPath());
        try {
            mCache.setInfoValue(DataCleanManager.getCacheSize(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取当前版本号
        String localStr=getPackageInfo().versionName;
        mInspect.setInfoValue("当前版本"+localStr);
    }

    /**
     * 事件监听
     */
    private void initListen() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.App_TopBar_Return:
                //返回
                finish();
                break;
            case R.id.Main_Setting_Quit:
                //退出登录
                if (isUserLogin()) {
                    AskDialogFragment.Instance("确定退出登录么？").setListen(new AskDialogFragment.AskDialogListen() {
                        @Override
                        public void onOk() {
                            ConfigConsts.isLogin = false;
                            ConfigConsts.ServerToken = "";
                            Sharedpreferences_Utils.getInstance(SettingActivity.this).setBoolean("isUserLogin", false);
                            Sharedpreferences_Utils.getInstance(SettingActivity.this).setString("ServerToken", "");
                            RxBus.getDefault().post(new RxBusBean(RxBusType.JustLoginOut));
                            finish();
                        }
                        @Override
                        public void onCancel() {
                        }
                    }).show(getFragmentManager());

                } else {
                    ToastUtils.showShort(SettingActivity.this, "您并未登录！");
                }
                break;
            case R.id.Main_Setting_Clear:
                //清理缓存
                DataCleanManager.cleanInternalCache(getApplicationContext());
                mCache.setInfoValue("0KB");
                break;
            case R.id.Main_Setting_Share:
                //分享给好友
                startActivity(ShareToActivity.class);
                break;
            case R.id.Main_Setting_Inspect:
                //检查版本更新
                getVersion();
                break;
            case R.id.Main_Setting_AboutUs:
                //关于我们
                startActivity(AboutUsActivity.class);
                break;
            case R.id.Main_Setting_Help:
                //帮助中心
                startActivity(HelpCenterActivity.class);
                break;
        }
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
                            }else {
                                ToastUtils.showShort(SettingActivity.this,"当前已是最新版本");
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
            if (str_OnLine.length >= 3 && str_Local.length >= 3) {
                if (Integer.parseInt(str_OnLine[0]) > Integer.parseInt(str_Local[0]))
                    return true;
                else if (Integer.parseInt(str_OnLine[1]) > Integer.parseInt(str_Local[1]))
                    return true;
                else if (Integer.parseInt(str_OnLine[2]) > Integer.parseInt(str_Local[2]))
                    return true;
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
                    RxPermissionUtil.getInstance(SettingActivity.this).getWriteReadPermiss(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if(aBoolean){
                                AppUpgradeManager.getInstance(SettingActivity.this, mVersion).startDown();
                                ProgressDialogFragment.Instance("正在下载，请稍后...").show(getFragmentManager());
                            }else {
                                ToastUtils.showShort(SettingActivity.this,"请允许应用对手机的读写权限！");
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
                    RxPermissionUtil.getInstance(SettingActivity.this).getWriteReadPermiss(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if(aBoolean){
                                AppUpgradeManager.getInstance(SettingActivity.this, mVersion).startDown();
                            }else {
                                ToastUtils.showShort(SettingActivity.this,"请允许应用对手机的读写权限！");
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
