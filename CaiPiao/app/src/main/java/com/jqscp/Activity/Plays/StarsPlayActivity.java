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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jqscp.Activity.WebShowActivity;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.Bean.LotteryBean;
import com.jqscp.Bean.RxBusBean;
import com.jqscp.Dao.BusinessDao;
import com.jqscp.Dao.OnResultClick;
import com.jqscp.Dao.OnResultListClick;
import com.jqscp.Fragment.Plays.BigStarPlayFragment;
import com.jqscp.Fragment.Plays.FiveStarPlayFragment;
import com.jqscp.Fragment.Plays.OneStarPlayFragment;
import com.jqscp.Fragment.Plays.ThreeStarPlayFragment;
import com.jqscp.Fragment.Plays.TwoStarPlayFragment;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.TimeUtils;
import com.jqscp.Util.BaseActivityUtils.BaseActivity;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;
import com.jqscp.View.CQPlay_PopupWindow;
import com.jqscp.View.CircleBGTextView;
import com.jqscp.View.HistoryPopupWindow;
import com.jqscp.View.SerializableSparseArray;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.reactivex.functions.Consumer;

public class StarsPlayActivity extends BaseActivity implements SensorEventListener {
    private static final int BeginShake = 1000;
    //控件部分
    private TextView mTitle;//标题
    private TextView mHintTitle;//右侧隐藏标题
    private LinearLayout mTitleLayout;//
    private ImageView mReturn;//返回
    private ImageView mPlayIcon;//玩法界面才有
    private TextView mStopIssue, mThisIssue, mCountDown;//暂停投注、当前期号、倒计时
    private RelativeLayout mHistoryLottery;//历史开奖结果


    //工具&数据
    private FragmentManager fManager;
    private CountDownTimer mCountDownTimer;//倒计时工具
    private OneStarPlayFragment mOneStarPlayFragment1, mOneStarPlayFragment3, mOneStarPlayFragment5, mOneStarPlayFragment6;
    private TwoStarPlayFragment mTwoStarPlayFragment;
    private ThreeStarPlayFragment mThreeStarPlayFragment;
    private FiveStarPlayFragment mFiveStarPlayFragment7, mFiveStarPlayFragment8;
    private BigStarPlayFragment mBigStarPlayFragment;

    //开奖号部分
    private LinearLayout mLayout01;
    private CircleBGTextView mCircle01;
    private CircleBGTextView mCircle02;
    private CircleBGTextView mCircle03;
    private CircleBGTextView mCircle04;
    private CircleBGTextView mCircle05;
    private LotteryBean mLotteryBean;

    private CQPlay_PopupWindow mCQPlayPopupWindow;
    //开启摇一摇
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private boolean isShake;//是否正在摇
    private MyHandler mHandler;

    private Bundle savedInstanceState;
    private Bundle bundles;

    //Flag
    private int mType = 1;//玩法类型 (1:一星;2:二星直选;3:二星组选;4:三星直选;5:三星组三;6:三星组六;7:五星直选;8:五星通选;9:大小;)
    private int mMakeType = 1;//玩法类型 (1:一星;2:二星直选;3:二星组选;4:三星直选;5:三星组三;6:三星组六;7:五星直选;8:五星通选;9:大小;)
    private int mGroup;//组的个数
    private int mNumber;//组每个至少选的个数

    private SerializableSparseArray<List<Integer>> mSparseArray = new SerializableSparseArray<>();//选中的号码集合
    private List<List<String>> mLostList = new ArrayList<>();//遗漏号码集合
    private String mThisIssueContent;//当前期号
    private String mSubmitContentStr;//选中号码内容
    private int mCountDownContent;//倒计时

    private int PlayFlag;//0：重庆时时彩、1：新疆时时彩、2：天津时时彩
    private boolean IsMake;//是否是修改


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_stars_play);
        initView();
        initData();
        initListen();
        getThisIssueData();
        getLostNumber();
        getLotteryList();
    }

    private void initView() {
        mReturn = findViewById(R.id.App_TopBar_Return);
        mPlayIcon = findViewById(R.id.App_TopBar_PlayIcon);
        mTitle = findViewById(R.id.App_TopBar_Title);
        mTitleLayout = findViewById(R.id.App_TopBar_TitleLayout);
        mHintTitle = findViewById(R.id.App_TopBar_HintText);
        mStopIssue = findViewById(R.id.Stars_Play_StopIssue);
        mThisIssue = findViewById(R.id.Stars_Play_ThisIssue);
        mCountDown = findViewById(R.id.Stars_Play_CountDown);
        mHistoryLottery = findViewById(R.id.Stars_Play_HistoryLottery);

        mLayout01 = findViewById(R.id.Lottery_Item_NumberLayout);
        mCircle01 = findViewById(R.id.MainLottery_Item_Item_Number01);
        mCircle02 = findViewById(R.id.MainLottery_Item_Item_Number02);
        mCircle03 = findViewById(R.id.MainLottery_Item_Item_Number03);
        mCircle04 = findViewById(R.id.MainLottery_Item_Item_Number04);
        mCircle05 = findViewById(R.id.MainLottery_Item_Item_Number05);

        fManager = getSupportFragmentManager();
        mHandler = new MyHandler(this);
    }

    private void initData() {
        bundles = getIntent().getExtras();
        if (bundles != null) {
            PlayFlag = bundles.getInt("PlayType");
            //初始赋值
            mMakeType = bundles.getInt("Type",1);
            mType=mMakeType;
            //用来判断是否是修改界面
            IsMake = bundles.getBoolean("IsMake",false);
            boolean IsAgain = bundles.getBoolean("IsAgain", false);
            if (IsMake) {
                mTitle.setText(bundles.getString("TypeStr"));
                mSubmitContentStr = bundles.getString("Number");
            }else if(IsAgain){//再来一注
                mTitle.setText(bundles.getString("TypeStr"));
                mSubmitContentStr = bundles.getString("Number");
            }else {
                if(mType!=1){
                    mTitle.setText(bundles.getString("TypeStr"));
                }else {
                    //初始赋值
                    mTitle.setText("一星复式");
                    mType = 1;
                }
            }
        } else {
            //初始赋值
            mTitle.setText("一星复式");
            mType = 1;
        }
        mHintTitle.setVisibility(View.VISIBLE);
        mHintTitle.setText("玩法说明");

        initInterface();

        if (!TextUtils.isEmpty(mSubmitContentStr)) {
            String[] strings = mSubmitContentStr.split(",");
            for (int i = 0; i < strings.length; i++) {
                List<Integer> list = new ArrayList<>();
                String[] strs = strings[i].split("");
                for (int j = 0; j < strs.length; j++) {
                    if (!TextUtils.isEmpty(strs[j]))
                        list.add(Integer.parseInt(strs[j]));
                }
                mSparseArray.put(i+1, list);
            }
        }
    }

    private void initListen() {
        //返回
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //机选
        RxBus.getDefault().doSubscribeMain(this, RxBusBean.class, new Consumer<RxBusBean>() {
            @Override
            public void accept(RxBusBean rxBusBean) throws Exception {
                if(rxBusBean.getType()==RxBusType.ToShake){
                    isShake = true;
                    //开始震动 发出提示音 展示动画效果(延迟500ms)
                    mHandler.sendEmptyMessageDelayed(BeginShake, 500);
                }
            }
        });
        //玩法说明
        mHintTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                switch (PlayFlag){
                    case PlayStateManger.CQSSC:
                        bundle.putString("Url", "http://code.310liao.com/static/html/cqsscplay.html");
                        bundle.putString("Title", "玩法说明");
                        startActivityAndBundle(WebShowActivity.class,bundle);
                        break;
                    case PlayStateManger.XJSSC:
                        bundle.putString("Url", "http://code.310liao.com/static/html/xjsscplay.html");
                        bundle.putString("Title", "玩法说明");
                        startActivityAndBundle(WebShowActivity.class,bundle);
                        break;
                    case PlayStateManger.TJSSC:
                        bundle.putString("Url", "http://code.310liao.com/static/html/tjsscplay.html");
                        bundle.putString("Title", "玩法说明");
                        startActivityAndBundle(WebShowActivity.class,bundle);
                        break;
                }
            }
        });
        //历史开奖结果
        mHistoryLottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryPopupWindow historyPopupWindow = new HistoryPopupWindow(StarsPlayActivity.this,PlayFlag);
                historyPopupWindow.showAtLocation(findViewById(R.id.Stars_Play_ParentsLayout), Gravity.BOTTOM, 0, 0);
            }
        });
        //玩法选择
        mTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayIcon.setImageResource(R.drawable.go_up);
                mCQPlayPopupWindow = new CQPlay_PopupWindow(StarsPlayActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSparseArray.clear();
                        switch (view.getId()) {
                            case R.id.CQPlay_1:
                                //一星直选
                                mType = 1;
                                mTitle.setText("一星复式");
                                break;
                            case R.id.CQPlay_21:
                                //二星直选
                                mType = 2;
                                mTitle.setText("二星复式");
                                break;
                            case R.id.CQPlay_22:
                                //二星组选
                                mType = 3;
                                mTitle.setText("二星组选");
                                break;
                            case R.id.CQPlay_31:
                                //三星直选
                                mType = 4;
                                mTitle.setText("三星复式");
                                break;
                            case R.id.CQPlay_32:
                                //三星组三
                                mType = 5;
                                mTitle.setText("三星组三");
                                break;
                            case R.id.CQPlay_33:
                                //三星组六
                                mType = 6;
                                mTitle.setText("三星组六");
                                break;
                            case R.id.CQPlay_51:
                                //五星直选
                                mType = 7;
                                mTitle.setText("五星复式");
                                break;
                            case R.id.CQPlay_52:
                                //五星通选
                                mType = 8;
                                mTitle.setText("五星通选");
                                break;
                            case R.id.CQPlay_big:
                                //大小单双
                                mType = 9;
                                mTitle.setText("大小单双");
                                break;
                        }
                        initInterface();
                    }
                }, new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        //popWindow消失监听
                        mPlayIcon.setImageResource(R.drawable.go_down);
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
        Bundle mBundle = new Bundle();
        mBundle.putInt("PlayType", PlayFlag);
        mBundle.putSerializable("LostNumber", (Serializable) mLostList);
        if (bundles != null && !TextUtils.isEmpty(mSubmitContentStr)) {
            if(mType==mMakeType) {
                mBundle.putSerializable("Content", mSparseArray);
            }
            if(IsMake) {
                mBundle.putBoolean("isModification", true);
                mBundle.putLong("ID", bundles.getLong("ID"));
            }
        }
        FragmentTransaction transaction = fManager.beginTransaction();
        hideFragment(transaction);
        switch (mType) {
            case 1:
                mGroup = 1;
                mNumber = 1;
                mBundle.putInt("Types", 1);
                if (mOneStarPlayFragment1 == null) {
                    if (savedInstanceState != null) {
                        mOneStarPlayFragment1 = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment1");
                        if (mOneStarPlayFragment1 == null)
                            mOneStarPlayFragment1 = new OneStarPlayFragment();
                        mOneStarPlayFragment1.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment1, "OneStarPlayFragment1");
                    } else {
                        mOneStarPlayFragment1 = new OneStarPlayFragment();
                        mOneStarPlayFragment1.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment1, "OneStarPlayFragment1");
                    }
                } else {
                    mOneStarPlayFragment1.setArguments(mBundle);
                    transaction.show(mOneStarPlayFragment1);
                }
                break;
            case 2:
                mGroup = 2;
                mNumber = 1;
                if (mTwoStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mTwoStarPlayFragment = (TwoStarPlayFragment) fManager.findFragmentByTag("TwoStarPlayFragment");
                        if (mTwoStarPlayFragment == null)
                            mTwoStarPlayFragment = new TwoStarPlayFragment();
                        mTwoStarPlayFragment.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mTwoStarPlayFragment, "TwoStarPlayFragment");
                    } else {
                        mTwoStarPlayFragment = new TwoStarPlayFragment();
                        mTwoStarPlayFragment.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mTwoStarPlayFragment, "TwoStarPlayFragment");
                    }
                } else {
                    mTwoStarPlayFragment.setArguments(mBundle);
                    transaction.show(mTwoStarPlayFragment);
                }
                break;
            case 3:
                mGroup = 1;
                mNumber = 2;
                mBundle.putInt("Types", 3);
                if (mOneStarPlayFragment3 == null) {
                    if (savedInstanceState != null) {
                        mOneStarPlayFragment3 = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment3");
                        if (mOneStarPlayFragment3 == null)
                            mOneStarPlayFragment3 = new OneStarPlayFragment();
                        mOneStarPlayFragment3.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment3, "OneStarPlayFragment3");
                    } else {
                        mOneStarPlayFragment3 = new OneStarPlayFragment();
                        mOneStarPlayFragment3.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment3, "OneStarPlayFragment3");
                    }
                } else {
                    mOneStarPlayFragment3.setArguments(mBundle);
                    transaction.show(mOneStarPlayFragment3);
                }
                break;
            case 4:
                mGroup = 3;
                mNumber = 1;
                if (mThreeStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mThreeStarPlayFragment = (ThreeStarPlayFragment) fManager.findFragmentByTag("ThreeStarPlayFragment");
                        if (mThreeStarPlayFragment == null)
                            mThreeStarPlayFragment = new ThreeStarPlayFragment();
                        mThreeStarPlayFragment.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mThreeStarPlayFragment, "ThreeStarPlayFragment");
                    } else {
                        mThreeStarPlayFragment = new ThreeStarPlayFragment();
                        mThreeStarPlayFragment.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mThreeStarPlayFragment, "ThreeStarPlayFragment");
                    }
                } else {
                    mThreeStarPlayFragment.setArguments(mBundle);
                    transaction.show(mThreeStarPlayFragment);
                }
                break;
            case 5:
                mGroup = 1;
                mNumber = 2;
                mBundle.putInt("Types", 5);
                if (mOneStarPlayFragment5 == null) {
                    if (savedInstanceState != null) {
                        mOneStarPlayFragment5 = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment5");
                        if (mOneStarPlayFragment5 == null)
                            mOneStarPlayFragment5 = new OneStarPlayFragment();
                        mOneStarPlayFragment5.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment5, "OneStarPlayFragment5");
                    } else {
                        mOneStarPlayFragment5 = new OneStarPlayFragment();
                        mOneStarPlayFragment5.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment5, "OneStarPlayFragment5");
                    }
                } else {
                    mOneStarPlayFragment5.setArguments(mBundle);
                    transaction.show(mOneStarPlayFragment5);
                }
                break;
            case 6:
                mGroup = 1;
                mNumber = 3;
                mBundle.putInt("Types", 6);
                if (mOneStarPlayFragment6 == null) {
                    if (savedInstanceState != null) {
                        mOneStarPlayFragment6 = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment6");
                        if (mOneStarPlayFragment6 == null)
                            mOneStarPlayFragment6 = new OneStarPlayFragment();
                        mOneStarPlayFragment6.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment6, "OneStarPlayFragment6");
                    } else {
                        mOneStarPlayFragment6 = new OneStarPlayFragment();
                        mOneStarPlayFragment6.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mOneStarPlayFragment6, "OneStarPlayFragment6");
                    }
                } else {
                    mOneStarPlayFragment6.setArguments(mBundle);
                    transaction.show(mOneStarPlayFragment6);
                }
                break;
            case 7:
                mGroup = 5;
                mNumber = 1;
                mBundle.putInt("Types", 7);
                if (mFiveStarPlayFragment7 == null) {
                    if (savedInstanceState != null) {
                        mFiveStarPlayFragment7 = (FiveStarPlayFragment) fManager.findFragmentByTag("FiveStarPlayFragment7");
                        if (mFiveStarPlayFragment7 == null)
                            mFiveStarPlayFragment7 = new FiveStarPlayFragment();
                        mFiveStarPlayFragment7.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mFiveStarPlayFragment7, "FiveStarPlayFragment7");
                    } else {
                        mFiveStarPlayFragment7 = new FiveStarPlayFragment();
                        mFiveStarPlayFragment7.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mFiveStarPlayFragment7, "FiveStarPlayFragment7");
                    }
                } else {
                    mFiveStarPlayFragment7.setArguments(mBundle);
                    transaction.show(mFiveStarPlayFragment7);
                }
                break;
            case 8:
                mGroup = 5;
                mNumber = 1;
                mBundle.putInt("Types", 8);
                if (mFiveStarPlayFragment8 == null) {
                    if (savedInstanceState != null) {
                        mFiveStarPlayFragment8 = (FiveStarPlayFragment) fManager.findFragmentByTag("FiveStarPlayFragment8");
                        if (mFiveStarPlayFragment8 == null)
                            mFiveStarPlayFragment8 = new FiveStarPlayFragment();
                        mFiveStarPlayFragment8.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mFiveStarPlayFragment8, "FiveStarPlayFragment8");
                    } else {
                        mFiveStarPlayFragment8 = new FiveStarPlayFragment();
                        mFiveStarPlayFragment8.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mFiveStarPlayFragment8, "FiveStarPlayFragment8");
                    }
                } else {
                    mFiveStarPlayFragment8.setArguments(mBundle);
                    transaction.show(mFiveStarPlayFragment8);
                }
                break;
            case 9:
                mGroup = 2;
                mNumber = 1;
                if (mBigStarPlayFragment == null) {
                    if (savedInstanceState != null) {
                        mBigStarPlayFragment = (BigStarPlayFragment) fManager.findFragmentByTag("BigStarPlayFragment");
                        if (mBigStarPlayFragment == null)
                            mBigStarPlayFragment = new BigStarPlayFragment();
                        mBigStarPlayFragment.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mBigStarPlayFragment, "BigStarPlayFragment");
                    } else {
                        mBigStarPlayFragment = new BigStarPlayFragment();
                        mBigStarPlayFragment.setArguments(mBundle);
                        transaction.add(R.id.Stars_Play_FrameLayout, mBigStarPlayFragment, "BigStarPlayFragment");
                    }
                } else {
                    mBigStarPlayFragment.setArguments(mBundle);
                    transaction.show(mBigStarPlayFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        // TODO Auto-generated method stub
        if (mOneStarPlayFragment1 != null) {
            transaction.hide(mOneStarPlayFragment1);
        } else {
            if (savedInstanceState != null) {
                mOneStarPlayFragment1 = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment1");
                transaction.hide(mOneStarPlayFragment1);
            }
        }
        if (mOneStarPlayFragment3 != null) {
            transaction.hide(mOneStarPlayFragment3);
        } else {
            if (savedInstanceState != null) {
                mOneStarPlayFragment3 = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment3");
                transaction.hide(mOneStarPlayFragment3);
            }
        }
        if (mOneStarPlayFragment5 != null) {
            transaction.hide(mOneStarPlayFragment5);
        } else {
            if (savedInstanceState != null) {
                mOneStarPlayFragment5 = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment5");
                transaction.hide(mOneStarPlayFragment5);
            }
        }
        if (mOneStarPlayFragment6 != null) {
            transaction.hide(mOneStarPlayFragment6);
        } else {
            if (savedInstanceState != null) {
                mOneStarPlayFragment6 = (OneStarPlayFragment) fManager.findFragmentByTag("OneStarPlayFragment6");
                transaction.hide(mOneStarPlayFragment6);
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
        if (mFiveStarPlayFragment7 != null) {
            transaction.hide(mFiveStarPlayFragment7);
        } else {
            if (savedInstanceState != null) {
                mFiveStarPlayFragment7 = (FiveStarPlayFragment) fManager.findFragmentByTag("FiveStarPlayFragment7");
                transaction.hide(mFiveStarPlayFragment7);
            }
        }
        if (mFiveStarPlayFragment8 != null) {
            transaction.hide(mFiveStarPlayFragment8);
        } else {
            if (savedInstanceState != null) {
                mFiveStarPlayFragment8 = (FiveStarPlayFragment) fManager.findFragmentByTag("FiveStarPlayFragment8");
                transaction.hide(mFiveStarPlayFragment8);
            }
        }
        if (mBigStarPlayFragment != null) {
            transaction.hide(mBigStarPlayFragment);
        } else {
            if (savedInstanceState != null) {
                mBigStarPlayFragment = (BigStarPlayFragment) fManager.findFragmentByTag("BigStarPlayFragment");
                transaction.hide(mBigStarPlayFragment);
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
        if(mCountDownTimer!=null)
            mCountDownTimer.cancel();
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
                    RxBus.getDefault().post(new RxBusBean(RxBusType.Shake_Play, mActivity.mType, mActivity.mSparseArray));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.isShake = false;
                        }
                    },1000);
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
                if (mActivity.mType == 9) {
                    List<Integer> list = Arrays.asList(randomArray(0, 3, m));
                    mActivity.mSparseArray.put(i + 1, new ArrayList<>(list));
                } else {
                    List<Integer> list = Arrays.asList(randomArray(0, 9, m));
                    mActivity.mSparseArray.put(i + 1, new ArrayList<>(list));
                }
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
        if (mOneStarPlayFragment1 != null
                && mOneStarPlayFragment1.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "OneStarPlayFragment1", mOneStarPlayFragment1);
        }
        if (mOneStarPlayFragment3 != null
                && mOneStarPlayFragment3.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "OneStarPlayFragment3", mOneStarPlayFragment3);
        }
        if (mOneStarPlayFragment5 != null
                && mOneStarPlayFragment5.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "OneStarPlayFragment5", mOneStarPlayFragment5);
        }
        if (mOneStarPlayFragment6 != null
                && mOneStarPlayFragment6.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "OneStarPlayFragment6", mOneStarPlayFragment6);
        }
        if (mTwoStarPlayFragment != null
                && mTwoStarPlayFragment.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "TwoStarPlayFragment", mTwoStarPlayFragment);
        }

        if (mThreeStarPlayFragment != null
                && mThreeStarPlayFragment.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "ThreeStarPlayFragment", mThreeStarPlayFragment);
        }
        if (mFiveStarPlayFragment7 != null
                && mFiveStarPlayFragment7.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "FiveStarPlayFragment7", mFiveStarPlayFragment7);
        }
        if (mFiveStarPlayFragment8 != null
                && mFiveStarPlayFragment8.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "FiveStarPlayFragment8", mFiveStarPlayFragment8);
        }
        if (mBigStarPlayFragment != null
                && mBigStarPlayFragment.isVisible()) {
            getSupportFragmentManager().putFragment(savedInstanceState, "BigStarPlayFragment", mBigStarPlayFragment);
        }
    }

    /**
     * 界面赋值
     */
    private void initIssueData() {
        if (TextUtils.isEmpty(mThisIssueContent)) {
            mStopIssue.setVisibility(View.VISIBLE);
            mThisIssue.setVisibility(View.GONE);
            mCountDown.setVisibility(View.GONE);
            return;
        }

        String[] itemC = mThisIssueContent.split("-");
        if (itemC.length >= 2) {
            mThisIssue.setText("距" + itemC[1] + "期投注截止");
        } else {
            mThisIssue.setText("距000期投注截止");
        }
        mCountDown.setText(TimeUtils.setSecond2Minute(mCountDownContent));
        if(mCountDownTimer!=null)
            mCountDownTimer.cancel();
        mCountDownTimer = new CountDownTimer(mCountDownContent * 1000, 1000) {
            @Override
            public void onTick(long l) {
                mCountDown.setText(TimeUtils.setSecond2Minute((int) (l / 1000)));
            }

            @Override
            public void onFinish() {
                //倒计时结束重新请求接口获取数据
                getThisIssueData();
                getLotteryList();
            }
        }.start();
    }
    /**-------------网络接口部分-----------------*/
    /**
     * 获取当前期号、倒计时
     */
    private void getThisIssueData() {
        switch (PlayFlag) {
            case PlayStateManger.CQSSC:
                //重庆时时彩
                BusinessDao.GetCurrentIssue(new OnResultClick<CurrentIssueBean>() {
                    @Override
                    public void success(BaseHttpBean<CurrentIssueBean> bean) {
                        if (bean.getCode() == 0) {
                            if (bean.getData() != null) {
                                mThisIssueContent = bean.getData().getQihao();
                                mCountDownContent = bean.getData().getCounttime();
                                if (mCountDownContent >= 0)
                                    initIssueData();
                            }else {
                                mThisIssueContent = "";
                                mThisIssue.setText("获取期号失败");
                            }
                        } else {
                            mThisIssueContent = "";
                            initIssueData();

                        }

                    }

                    @Override
                    public void fail(Throwable throwable) {
                        mThisIssue.setText("获取期号失败");
                    }
                });
                break;
            case PlayStateManger.XJSSC:
                //新疆时时彩
                BusinessDao.GetXJCurrentIssue(new OnResultClick<CurrentIssueBean>() {
                    @Override
                    public void success(BaseHttpBean<CurrentIssueBean> bean) {
                        if (bean.getCode() == 0) {
                            if (bean.getData() != null) {
                                mThisIssueContent = bean.getData().getQihao();
                                mCountDownContent = bean.getData().getCounttime();
                                if (mCountDownContent >= 0)
                                    initIssueData();
                            }else {
                                mThisIssueContent="";
                                mThisIssue.setText("获取期号失败");
                            }
                        } else {
                            mThisIssueContent = "";
                            initIssueData();

                        }
                    }

                    @Override
                    public void fail(Throwable throwable) {
                        mThisIssue.setText("获取期号失败");
                    }
                });
                break;
            case PlayStateManger.TJSSC:
                //天津时时彩
                BusinessDao.GetTJCurrentIssue(new OnResultClick<CurrentIssueBean>() {
                    @Override
                    public void success(BaseHttpBean<CurrentIssueBean> bean) {
                        if (bean.getCode() == 0) {
                            if (bean.getData() != null) {
                                mThisIssueContent = bean.getData().getQihao();
                                mCountDownContent = bean.getData().getCounttime();
                                if (mCountDownContent >= 0)
                                    initIssueData();
                            }else {
                                mThisIssueContent = "";
                                mThisIssue.setText("获取期号失败");
                            }
                        } else {
                            mThisIssueContent = "";
                            initIssueData();

                        }
                    }

                    @Override
                    public void fail(Throwable throwable) {
                        mThisIssue.setText("获取期号失败");
                    }
                });
                break;
        }

    }

    /**
     * 获取遗漏号码
     */
    private void getLostNumber(){
        BusinessDao.GetLostNumber(PlayFlag, new OnResultListClick<List<String>>() {
            @Override
            public void success(List<List<String>> list) {
                mLostList=list;
                RxBus.getDefault().post(new RxBusBean(RxBusType.LostNumber_Notice,list));
            }

            @Override
            public void fail(Throwable throwable) {

            }
        });
    }

    /**\
     * 获取开奖号码
     */
    private void getLotteryList() {
        BusinessDao.GetAllLottery(new OnResultListClick<LotteryBean>() {
            @Override
            public void success(List<LotteryBean> list) {
                for (LotteryBean bean:list) {
                    if(bean.getS_type()==PlayFlag) {
                        mLotteryBean = bean;
                        break;
                    }
                }
                if(mLotteryBean!=null && mLotteryBean.getS_bill()!=null) {
                    mLayout01.setVisibility(View.VISIBLE);
                    String[] strs = mLotteryBean.getS_bill().split(",");
                    if(strs.length==5) {
                        mCircle01.setText(strs[0]);
                        mCircle02.setText(strs[1]);
                        mCircle03.setText(strs[2]);
                        mCircle04.setText(strs[3]);
                        mCircle05.setText(strs[4]);
                    }
                }else {
                    mLayout01.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void fail(Throwable throwable) {
                mLayout01.setVisibility(View.INVISIBLE);
            }
        });
    }

}
