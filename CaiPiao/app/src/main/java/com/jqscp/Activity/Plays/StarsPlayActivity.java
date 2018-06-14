package com.jqscp.Activity.Plays;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.Bean.RxBusBean;
import com.jqscp.Dao.BusinessDao;
import com.jqscp.Dao.OnNoResultClick;
import com.jqscp.Dao.OnResultClick;
import com.jqscp.Fragment.Plays.FiveStarPlayFragment;
import com.jqscp.Fragment.Plays.OneStarPlayFragment;
import com.jqscp.Fragment.Plays.ThreeStarPlayFragment;
import com.jqscp.Fragment.Plays.TwoStarPlayFragment;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.TimeUtils;
import com.jqscp.Util.BaseActivityUtils.BaseActivity;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;
import com.jqscp.View.CQPlay_PopupWindow;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StarsPlayActivity extends BaseActivity implements SensorEventListener {
    private static final int BeginShake = 1000;
    //控件部分
    private TextView mTitle;//标题
    private ImageView mReturn;//返回
    private TextView mThisIssue, mCountDown;//当前期号、倒计时
    //工具&数据
    private FragmentManager fManager;
    private CountDownTimer mCountDownTimer;//倒计时工具
    private OneStarPlayFragment mOneStarPlayFragment;
    private TwoStarPlayFragment mTwoStarPlayFragment;
    private ThreeStarPlayFragment mThreeStarPlayFragment;
    private FiveStarPlayFragment mFiveStarPlayFragment;
    //开启摇一摇
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private boolean isShake;//是否正在摇
    private MyHandler mHandler;
    private SparseArray<List<Integer>> mSparseArray = new SparseArray<>();
    private int mType = 1;//玩法类型 (1:一星;2:二星直选;3:二星组选;4:三星直选;5:三星组三;6:三星组六;7:五星直选;8:五星通选;9:;)
    private int mGroup;//组的个数
    private int mNumber;//组每个至少选的个数

    private CQPlay_PopupWindow mCQPlayPopupWindow;
    private Bundle savedInstanceState;

    private String mThisIssueContent;//当前期号
    private int mCountDownContent;//倒计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_stars_play);
        initView();
        initData();
        initListen();
        getThisIssueData();
    }

    private void initView() {
        mReturn = findViewById(R.id.App_TopBar_Return);
        mTitle = findViewById(R.id.App_TopBar_Title);
        mThisIssue = findViewById(R.id.Stars_Play_ThisIssue);
        mCountDown = findViewById(R.id.Stars_Play_CountDown);
        fManager = getSupportFragmentManager();
        mHandler = new MyHandler(this);
    }

    private void initData() {
        //初始赋值
        mTitle.setText("一星直选");

        initInterface();

        /*BusinessDao.putCurrentIssue("1", 1, 1, 2, "20180503-118", new OnNoResultClick() {
            @Override
            public void success() {

            }

            @Override
            public void fail(Throwable throwable) {

            }
        });*/

    }

    private void initListen() {

        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCQPlayPopupWindow = new CQPlay_PopupWindow(StarsPlayActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.CQPlay_1:
                                //一星直选
                                mType = 1;
                                mTitle.setText("一星直选");
                                break;
                            case R.id.CQPlay_21:
                                //二星直选
                                mType = 2;
                                mTitle.setText("二星直选");
                                break;
                            case R.id.CQPlay_22:
                                //二星组选
                                mType = 3;
                                mTitle.setText("二星组选");
                                break;
                            case R.id.CQPlay_31:
                                //三星直选
                                mType = 4;
                                mTitle.setText("三星直选");
                                break;
                            case R.id.CQPlay_32:
                                //三星组三单式
                                mType = 0;
                                mTitle.setText("三星组三单式");
                                break;
                            case R.id.CQPlay_33:
                                //三星组三复式
                                mType = 0;
                                mTitle.setText("三星组三复式");
                                break;
                            case R.id.CQPlay_34:
                                //三星组六
                                mType = 6;
                                mTitle.setText("三星组六");
                                break;
                            case R.id.CQPlay_51:
                                //五星直选
                                mType = 7;
                                mTitle.setText("五星直选");
                                break;
                            case R.id.CQPlay_52:
                                //五星通选
                                mType = 8;
                                mTitle.setText("五星通选");
                                break;
                            case R.id.CQPlay_big:
                                //大小单双
                                mType = 2;
                                mTitle.setText("大小单双");
                                break;
                        }
                        initInterface();
                    }
                });
                mCQPlayPopupWindow.showAsDropDown(findViewById(R.id.App_TopBar_Title), 0, 0);
            }
        });
    }

    /**
     * 界面更新
     */
    private void initInterface() {
        FragmentTransaction transaction = fManager.beginTransaction();
        hideFragment(transaction);
        switch (mType) {
            case 1:
                mGroup = 1;
                mNumber = 1;
                if (mOneStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mOneStarPlayFragment = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment");
                        mOneStarPlayFragment = new OneStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment, "OneStarPlayFragment");
                    } else {
                        mOneStarPlayFragment = new OneStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment, "OneStarPlayFragment");
                    }
                } else {
                    transaction.show(mOneStarPlayFragment);
                }
                break;
            case 2:
                mGroup = 2;
                mNumber = 1;
                if (mTwoStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mTwoStarPlayFragment = (TwoStarPlayFragment) fManager.findFragmentByTag("TwoStarPlayFragment");
                        mTwoStarPlayFragment = new TwoStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mTwoStarPlayFragment, "TwoStarPlayFragment");
                    } else {
                        mTwoStarPlayFragment = new TwoStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mTwoStarPlayFragment, "TwoStarPlayFragment");
                    }
                } else {
                    transaction.show(mTwoStarPlayFragment);
                }
                break;
            case 3:
                mGroup = 1;
                mNumber = 2;
                Bundle mBundle0 = new Bundle();
                mBundle0.putInt("Types", 3);
                if (mOneStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mOneStarPlayFragment = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment");
                        mOneStarPlayFragment = new OneStarPlayFragment();
                        mOneStarPlayFragment.setArguments(mBundle0);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment, "OneStarPlayFragment");
                    } else {
                        mOneStarPlayFragment = new OneStarPlayFragment();
                        mOneStarPlayFragment.setArguments(mBundle0);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment, "OneStarPlayFragment");
                    }
                } else {
                    mOneStarPlayFragment.setArguments(mBundle0);
                    transaction.show(mOneStarPlayFragment);
                }
                break;
            case 4:
                mGroup = 3;
                mNumber = 1;
                if (mThreeStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mThreeStarPlayFragment = (ThreeStarPlayFragment) fManager.findFragmentByTag("ThreeStarPlayFragment");
                        mThreeStarPlayFragment = new ThreeStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mThreeStarPlayFragment, "ThreeStarPlayFragment");
                    } else {
                        mThreeStarPlayFragment = new ThreeStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mThreeStarPlayFragment, "ThreeStarPlayFragment");
                    }
                } else {
                    transaction.show(mThreeStarPlayFragment);
                }
                break;
            case 5:
                mGroup = 1;
                mNumber = 2;
                Bundle mBundle1 = new Bundle();
                mBundle1.putInt("Types", 5);
                if (mOneStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mOneStarPlayFragment = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment");
                        mOneStarPlayFragment = new OneStarPlayFragment();
                        mOneStarPlayFragment.setArguments(mBundle1);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment, "OneStarPlayFragment");
                    } else {
                        mOneStarPlayFragment = new OneStarPlayFragment();
                        mOneStarPlayFragment.setArguments(mBundle1);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment, "OneStarPlayFragment");
                    }
                } else {
                    mOneStarPlayFragment.setArguments(mBundle1);
                    transaction.show(mOneStarPlayFragment);
                }
                break;
            case 6:
                mGroup = 1;
                mNumber = 3;
                Bundle mBundle2 = new Bundle();
                mBundle2.putInt("Types", 6);
                if (mOneStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mOneStarPlayFragment = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment");
                        mOneStarPlayFragment = new OneStarPlayFragment();
                        mOneStarPlayFragment.setArguments(mBundle2);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment, "OneStarPlayFragment");
                    } else {
                        mOneStarPlayFragment = new OneStarPlayFragment();
                        mOneStarPlayFragment.setArguments(mBundle2);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment, "OneStarPlayFragment");
                    }
                } else {
                    mOneStarPlayFragment.setArguments(mBundle2);
                    transaction.show(mOneStarPlayFragment);
                }
                break;
            case 7:
                mGroup = 5;
                mNumber = 1;
                if (mFiveStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mFiveStarPlayFragment = (FiveStarPlayFragment) fManager.findFragmentByTag("FiveStarPlayFragment");
                        mFiveStarPlayFragment = new FiveStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mFiveStarPlayFragment, "FiveStarPlayFragment");
                    } else {
                        mFiveStarPlayFragment = new FiveStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mFiveStarPlayFragment, "FiveStarPlayFragment");
                    }
                } else {
                    transaction.show(mFiveStarPlayFragment);
                }
                break;
            case 8:
                mGroup = 5;
                mNumber = 1;
                if (mFiveStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mFiveStarPlayFragment = (FiveStarPlayFragment) fManager.findFragmentByTag("FiveStarPlayFragment");
                        mFiveStarPlayFragment = new FiveStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mFiveStarPlayFragment, "FiveStarPlayFragment");
                    } else {
                        mFiveStarPlayFragment = new FiveStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mFiveStarPlayFragment, "FiveStarPlayFragment");
                    }
                } else {
                    transaction.show(mFiveStarPlayFragment);
                }
                break;
            case 9:
                mGroup = 5;
                mNumber = 1;
                if (mFiveStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mFiveStarPlayFragment = (FiveStarPlayFragment) fManager.findFragmentByTag("FiveStarPlayFragment");
                        mFiveStarPlayFragment = new FiveStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mFiveStarPlayFragment, "FiveStarPlayFragment");
                    } else {
                        mFiveStarPlayFragment = new FiveStarPlayFragment();
                        transaction.add(R.id.Stars_Play_FrameLayout, mFiveStarPlayFragment, "FiveStarPlayFragment");
                    }
                } else {
                    transaction.show(mFiveStarPlayFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        // TODO Auto-generated method stub
        if (mOneStarPlayFragment != null) {
            transaction.hide(mOneStarPlayFragment);
        } else {
            if (savedInstanceState != null) {
                mOneStarPlayFragment = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment");
                transaction.hide(mOneStarPlayFragment);
            }
        }
        if (mTwoStarPlayFragment != null) {
            transaction.hide(mTwoStarPlayFragment);
        } else {
            if (savedInstanceState != null) {
                mTwoStarPlayFragment = (TwoStarPlayFragment) fManager.findFragmentByTag("TwoStarPlayFragment");
                transaction.hide(mTwoStarPlayFragment);
            }
        }
        if (mThreeStarPlayFragment != null) {
            transaction.hide(mThreeStarPlayFragment);
        } else {
            if (savedInstanceState != null) {
                mThreeStarPlayFragment = (ThreeStarPlayFragment) fManager.findFragmentByTag("ThreeStarPlayFragment");
                transaction.hide(mThreeStarPlayFragment);
            }
        }
        if (mFiveStarPlayFragment != null) {
            transaction.hide(mFiveStarPlayFragment);
        } else {
            if (savedInstanceState != null) {
                mFiveStarPlayFragment = (FiveStarPlayFragment) fManager.findFragmentByTag("FiveStarPlayFragment");
                transaction.hide(mFiveStarPlayFragment);
            }
        }
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
                mHandler.sendEmptyMessageDelayed(BeginShake, 500);
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
            switch (msg.what) {
                case BeginShake:
                    //开始震动一下
                    Vibrator vibrator = (Vibrator) mActivity.getSystemService(mActivity.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                    RandomNumber(mActivity.mGroup, mActivity.mNumber);
                    RxBus.getDefault().post(new RxBusBean(RxBusType.Shake_Play, mActivity.mSparseArray));
                    mActivity.isShake = false;
                    break;
            }
        }

        /**
         * 获取0-9随机数
         *
         * @param n 多少组
         * @param m 每一组多少个
         */
        private void RandomNumber(int n, int m) {
            mActivity.mSparseArray.clear();
            for (int i = 0; i < n; i++) {
                List<Integer> list = Arrays.asList(randomArray(0, 9, m));
                mActivity.mSparseArray.put(i + 1, new ArrayList<>(list));
            }


        }

        /**
         * 随机指定范围内N个不重复的数
         * 在初始化的无重复待选数组中随机产生一个数放入结果中，
         * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换
         * 然后从len-2里随机产生下一个随机数，如此类推
         *
         * @param max 指定范围最大值
         * @param min 指定范围最小值
         * @param n   随机数个数
         * @return int[] 随机数结果集
         */
        private Integer[] randomArray(int min, int max, int n) {
            int len = max - min + 1;

            if (max < min || n > len) {
                return null;
            }

            //初始化给定范围的待选数组
            Integer[] source = new Integer[len];
            for (int i = min; i < min + len; i++) {
                source[i - min] = i;
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (mOneStarPlayFragment != null
                && mOneStarPlayFragment.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "OneStarPlayFragment", mOneStarPlayFragment);
        }
        if (mTwoStarPlayFragment != null
                && mTwoStarPlayFragment.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "TwoStarPlayFragment", mTwoStarPlayFragment);
        }

        if (mThreeStarPlayFragment != null
                && mThreeStarPlayFragment.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "ThreeStarPlayFragment", mThreeStarPlayFragment);
        }
        if (mFiveStarPlayFragment != null
                && mFiveStarPlayFragment.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "FiveStarPlayFragment", mFiveStarPlayFragment);
        }
    }

    /**
     * 界面赋值
     */
    private void initIssueData() {
        mThisIssue.setText("距" + mThisIssueContent + "期投注截止");
        mCountDown.setText(TimeUtils.setSecond2Minute(mCountDownContent));
        mCountDownTimer = new CountDownTimer(mCountDownContent * 1000, 1000) {
            @Override
            public void onTick(long l) {
                mCountDown.setText(TimeUtils.setSecond2Minute((int) (l / 1000)));
            }

            @Override
            public void onFinish() {
                //倒计时结束重新请求接口获取数据
                getThisIssueData();
            }
        }.start();
    }


    /**-------------网络接口部分-----------------*/
    /**
     * 获取当前期号、倒计时
     */
    private void getThisIssueData() {
        BusinessDao.GetCurrentIssue(new OnResultClick<CurrentIssueBean>() {
            @Override
            public void success(BaseHttpBean<CurrentIssueBean> bean) {
                if (bean.getData() != null) {
                    mThisIssueContent = bean.getData().getQihao();
                    mCountDownContent = bean.getData().getCounttime();
                    if (mCountDownContent >= 0)
                        initIssueData();
                }
            }

            @Override
            public void fail(Throwable throwable) {

            }
        });
    }


}
