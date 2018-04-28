package com.jqscp.Activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.Bean.RxBusBean;
import com.jqscp.Dao.LoginDao;
import com.jqscp.Dao.OnResultClick;
import com.jqscp.Fragment.FiveStarPlayFragment;
import com.jqscp.Fragment.OneStarPlayFragment;
import com.jqscp.Fragment.ThreeStarPlayFragment;
import com.jqscp.Fragment.TwoStarPlayFragment;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.BaseActivityUtils.BaseActivity;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StarsPlayActivity extends BaseActivity implements SensorEventListener{
    private static final int BeginShake=1000;
    private FragmentManager fManager;
    private FragmentTransaction fTransaction;
    private OneStarPlayFragment mOneStarPlayFragment;
    private TwoStarPlayFragment mTwoStarPlayFragment;
    private ThreeStarPlayFragment mThreeStarPlayFragment;
    private FiveStarPlayFragment mFiveStarPlayFragment;
    //开启摇一摇
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private boolean isShake;//是否正在摇
    private MyHandler mHandler;
    private SparseArray<List<Integer>> mSparseArray=new SparseArray<>();
    private int mType;//玩法类型 (1:一星;2:二星直选;3:二星组选;4:三星直选;5:三星组三;6:三星组六;7:五星直选;8:五星通选;9:;)
    private int mGroup;//组的个数
    private int mNumber;//组每个至少选的个数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars_play);
        initView();
        initData();
        initListen();
    }

    private void initView() {
        fManager = getSupportFragmentManager();
        mHandler=new MyHandler(this);

        fTransaction = fManager.beginTransaction();
        mOneStarPlayFragment=new OneStarPlayFragment();
        mTwoStarPlayFragment=new TwoStarPlayFragment();
        mThreeStarPlayFragment=new ThreeStarPlayFragment();
        mFiveStarPlayFragment=new FiveStarPlayFragment();
    }

    private void initData() {
        LoginDao mLoginDao=new LoginDao();
        mLoginDao.GetCurrentIssue(new OnResultClick<CurrentIssueBean>() {
            @Override
            public void success(BaseHttpBean<CurrentIssueBean> bean) {
                ALog.e("这里获得当前期号="+bean.toString());
            }

            @Override
            public void fail(Throwable throwable) {

            }
        });

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
            mType=bundle.getInt("Types",0);
        switch (mType){
            case 1:
                mGroup=1;
                mNumber=1;
                fTransaction.add(R.id.Stars_Play_FrameLayout,mOneStarPlayFragment);
                fTransaction.commit();
                break;
            case 2:
                mGroup=2;
                mNumber=1;
                fTransaction.add(R.id.Stars_Play_FrameLayout,mTwoStarPlayFragment);
                fTransaction.commit();
                break;
            case 3:
                mGroup=1;
                mNumber=2;
                Bundle mBundle0=new Bundle();
                mBundle0.putInt("Types",3);
                mOneStarPlayFragment.setArguments(mBundle0);
                fTransaction.add(R.id.Stars_Play_FrameLayout,mOneStarPlayFragment);
                fTransaction.commit();
                break;
            case 4:
                mGroup=3;
                mNumber=1;
                fTransaction.add(R.id.Stars_Play_FrameLayout,mThreeStarPlayFragment);
                fTransaction.commit();
                break;
            case 5:
                mGroup=1;
                mNumber=2;
                Bundle mBundle1=new Bundle();
                mBundle1.putInt("Types",5);
                mOneStarPlayFragment.setArguments(mBundle1);
                fTransaction.add(R.id.Stars_Play_FrameLayout,mOneStarPlayFragment);
                fTransaction.commit();
                break;
            case 6:
                mGroup=1;
                mNumber=3;
                Bundle mBundle2=new Bundle();
                mBundle2.putInt("Types",6);
                mOneStarPlayFragment.setArguments(mBundle2);
                fTransaction.add(R.id.Stars_Play_FrameLayout,mOneStarPlayFragment);
                fTransaction.commit();
                break;
            case 7:
                mGroup=5;
                mNumber=1;
                fTransaction.add(R.id.Stars_Play_FrameLayout,mFiveStarPlayFragment);
                fTransaction.commit();
                break;
            case 8:
                mGroup=5;
                mNumber=1;
                fTransaction.add(R.id.Stars_Play_FrameLayout,mFiveStarPlayFragment);
                fTransaction.commit();
                break;
            case 9:
                mGroup=5;
                mNumber=1;
                fTransaction.add(R.id.Stars_Play_FrameLayout,mFiveStarPlayFragment);
                fTransaction.commit();
                break;
        }

    }

    private void initListen() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }

    }

    @Override
    protected void onPause() {
        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unDisposable(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            //获取三个方向值
            float[] values = sensorEvent.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math
                    .abs(z) > 17) && !isShake) {
                isShake = true;
                //开始震动 发出提示音 展示动画效果(延迟500ms)
                mHandler.sendEmptyMessageDelayed(BeginShake,500);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private static class MyHandler extends Handler {
        private WeakReference<StarsPlayActivity> mReference;
        private StarsPlayActivity mActivity;
        public MyHandler(StarsPlayActivity activity) {
            mReference = new WeakReference<>(activity);
            if (mReference != null) {
                mActivity = mReference.get();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case BeginShake:
                    //开始震动一下
                    Vibrator vibrator = (Vibrator)mActivity.getSystemService(mActivity.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                    RandomNumber(mActivity.mGroup,mActivity.mNumber);
                    RxBus.getDefault().post(new RxBusBean(RxBusType.Shake_Play,mActivity.mSparseArray));
                    mActivity.isShake = false;
                    break;
            }
        }

        /**
         * 获取0-9随机数
         * @param n 多少组
         * @param m 每一组多少个
         */
        private void RandomNumber(int n,int m){
            mActivity.mSparseArray.clear();
            for (int i = 0; i < n; i++) {
                List<Integer> list= Arrays.asList(randomArray(0,9,m));
                mActivity.mSparseArray.put(i+1,new ArrayList<>(list));
            }



        }
        /**
         * 随机指定范围内N个不重复的数
         * 在初始化的无重复待选数组中随机产生一个数放入结果中，
         * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换
         * 然后从len-2里随机产生下一个随机数，如此类推
         * @param max  指定范围最大值
         * @param min  指定范围最小值
         * @param n  随机数个数
         * @return int[] 随机数结果集
         */
        private Integer[] randomArray(int min,int max,int n){
            int len = max-min+1;

            if(max < min || n > len){
                return null;
            }

            //初始化给定范围的待选数组
            Integer[] source = new Integer[len];
            for (int i = min; i < min+len; i++){
                source[i-min] = i;
            }

            Integer[] result = new Integer[n];
            Random rd = new Random();
            int index = 0;
            for (int i = 0; i < result.length; i++) {
                //待选数组0到(len-2)随机一个下标
                index = Math.abs(rd.nextInt() % len--);
                //将随机到的数放入结果集
                result[i] = source[index];
                //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
                source[index] = source[len];
            }
            return result;
        }
    }


}
