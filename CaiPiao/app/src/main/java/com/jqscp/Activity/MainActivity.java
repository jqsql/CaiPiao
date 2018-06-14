package com.jqscp.Activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.jqscp.Fragment.Main.FindFragment;
import com.jqscp.Fragment.Main.LotteryFragment;
import com.jqscp.Fragment.Main.HomeFragment;
import com.jqscp.Fragment.Main.MeFragment;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;
import com.jqscp.Util.BaseActivityUtils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private List<Fragment> mFragmentList;
    private long firstTime = 0;
    private RadioGroup mRadioGroup;
    private ViewPager mViewPager;
    private MyPagerAdapter mPagerAdapter;
    private FragmentManager mFragmentManager;
    public boolean isSuccessStatusBar;//沉浸式是否成功
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isSuccessStatusBar=setFullScreen(true);
        initView();
        initData();
        initListen();
    }

    /**
     * 初始化
     */
    private void initView() {
        mRadioGroup=findViewById(R.id.Main_RadioGroup);
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
        mPagerAdapter=new MyPagerAdapter(mFragmentManager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
    }

    /**
     * 事件监听
     */
    private void initListen() {
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


    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int arg0) {
            return mFragmentList.get(arg0);
        }

        public int getCount() {
            return mFragmentList.size();
        }
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
}
