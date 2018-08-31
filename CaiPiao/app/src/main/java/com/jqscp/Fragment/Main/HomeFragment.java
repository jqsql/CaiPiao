package com.jqscp.Fragment.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jqscp.Activity.MainActivity;
import com.jqscp.Activity.Orders.CQ_SSC_OrderActivity;
import com.jqscp.Activity.Orders.K3_OrderActivity;
import com.jqscp.Activity.Orders.PK10_OrderActivity;
import com.jqscp.Activity.Orders.SD11_5_OrderActivity;
import com.jqscp.Activity.Plays.K3PlayActivity;
import com.jqscp.Activity.Plays.PK10PlayActivity;
import com.jqscp.Activity.Plays.PlayStateManger;
import com.jqscp.Activity.Plays.SD11_5PlayActivity;
import com.jqscp.Activity.Plays.StarsPlayActivity;
import com.jqscp.Activity.WebShowActivity;
import com.jqscp.Adapter.HomeGridViewAdapter;
import com.jqscp.Bean.AppConfigBean;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.BroadCastBean;
import com.jqscp.Bean.HomePlayInfoBean;
import com.jqscp.Bean.LunBoBean;
import com.jqscp.Dao.InfoDao;
import com.jqscp.Dao.OnResultClick;
import com.jqscp.Dao.OnResultListClick;
import com.jqscp.GreenDao.DaoUtil.HomePlayDaoUtil;
import com.jqscp.GreenDao.DaoUtil.LunBoDaoUtil;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.DisplayUtil;
import com.jqscp.Util.APPUtils.MathUtils;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;
import com.jqscp.Util.APPUtils.ToastUtils;
import com.jqscp.Util.APPUtils.ToolUtils;
import com.jqscp.Util.APPUtils.UiUtils;
import com.jqscp.Util.BaseActivityUtils.BaseFragment;
import com.jqscp.View.ImageCycleView;
import com.jqscp.View.LooperTextView;
import com.jqscp.View.MyGrideview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/12.
 */

public class HomeFragment extends BaseFragment {
    private View mView;
    private MainActivity mMainActivity;
    private RelativeLayout mTopBarLayout;
    private TextView mTitle;//标题
    private ImageCycleView mImageCycleView;//轮播图控件
    private LooperTextView mLooperTextView;//公告控件
    private LinearLayout mLooperTextViewlayout;//公告控件
    private MyGrideview mGrideview;//
    private SwipeRefreshLayout mRefreshLayout;
    private HomeGridViewAdapter mGridAdapter;

    private List<LunBoBean> mLunBoList;//轮播图数据
    private List<String> mLooperList;//公告数据
    private List<HomePlayInfoBean> mPlayInfoList= new ArrayList<>();;
    private HomePlayDaoUtil mDaoUtil;
    private LunBoDaoUtil mLunBoDaoUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_view, null);
        mMainActivity = (MainActivity) getActivity();
        initView();
        initData();
        initListen();
        GetAppConfig();
        getLunBo();
        getBroadCast();
        getPlayList();
        return mView;
    }

    /**
     * 初始化
     */
    private void initView() {
        mTopBarLayout = mView.findViewById(R.id.Main_Home_TopLayout);
        mTitle = mView.findViewById(R.id.Main_Home_Title);
        mImageCycleView = mView.findViewById(R.id.Main_Home_ImageCycleView);
        mLooperTextView = mView.findViewById(R.id.Main_Home_LooperTextView);
        mLooperTextViewlayout = mView.findViewById(R.id.Main_Home_LooperTextViewLayout);
        mGrideview = mView.findViewById(R.id.MainHome_Grideview);
        mRefreshLayout = mView.findViewById(R.id.Main_Home_SwipeRefreshLayout);
        mRefreshLayout.setDistanceToTriggerSync(200);

        if (mMainActivity.isSuccessStatusBar) {
            int pad = DisplayUtil.dip2px(_this, 16);
            mTopBarLayout.setPadding(pad, pad + _this.mStatusBarHeight, pad, pad);
        }

        mGridAdapter = new HomeGridViewAdapter(mGrideview,_this, mPlayInfoList);
        mGrideview.setAdapter(mGridAdapter);
        mGrideview.setFocusable(false);

    }

    /**
     * 数据处理
     */
    private void initData() {
        mDaoUtil=new HomePlayDaoUtil(_this);
        mLunBoDaoUtil=new LunBoDaoUtil(_this);

        mLunBoList = mLunBoDaoUtil.queryAllOrder();
        mPlayInfoList=mDaoUtil.queryAllOrder();

        mGridAdapter.setDataNotify(mPlayInfoList);
        setLunBoData();

        String appName=Sharedpreferences_Utils.getInstance(_this).getString("AppName");
        if(TextUtils.isEmpty(appName)){
            mTitle.setText("高频彩");
        }else {
            mTitle.setText(appName);
        }

    }

    /**
     * 事件监听
     */
    private void initListen() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLunBo();
                getBroadCast();
                getPlayList();
            }
        });
        mGrideview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                int type=0;
                try {
                    type=Integer.parseInt(mPlayInfoList.get(i).getCtype());
                }catch (ClassCastException e){

                }
                switch (type) {
                    case PlayStateManger.CQSSC:
                        //重庆时时彩
                        bundle.putInt("PlayType", PlayStateManger.CQSSC);
                        if (Sharedpreferences_Utils.getInstance(_this).getBoolean("isCq_Have")) {
                            _this.startActivityAndBundle(CQ_SSC_OrderActivity.class, bundle);
                        } else {
                            _this.startActivityAndBundle(StarsPlayActivity.class, bundle);
                        }
                        break;
                    case PlayStateManger.XJSSC:
                        //新疆时时彩
                        bundle.putInt("PlayType", PlayStateManger.XJSSC);
                        if (Sharedpreferences_Utils.getInstance(_this).getBoolean("isXj_Have")) {
                            _this.startActivityAndBundle(CQ_SSC_OrderActivity.class, bundle);
                        } else {
                            _this.startActivityAndBundle(StarsPlayActivity.class, bundle);
                        }
                        break;
                    case PlayStateManger.TJSSC:
                        //天津时时彩
                        bundle.putInt("PlayType", PlayStateManger.TJSSC);
                        if (Sharedpreferences_Utils.getInstance(_this).getBoolean("isTj_Have")) {
                            _this.startActivityAndBundle(CQ_SSC_OrderActivity.class, bundle);
                        } else {
                            _this.startActivityAndBundle(StarsPlayActivity.class, bundle);
                        }
                        break;
                    case PlayStateManger.SD11_5:
                        //山东11选5
                        bundle.putInt("PlayType",PlayStateManger.SD11_5);
                        if (Sharedpreferences_Utils.getInstance(_this).getBoolean("isSD11_5Have")) {
                            _this.startActivityAndBundle(SD11_5_OrderActivity.class, bundle);
                        } else {
                            _this.startActivityAndBundle(SD11_5PlayActivity.class, bundle);
                        }
                        break;
                    case PlayStateManger.TJ11_5:
                        //山东11选5
                        bundle.putInt("PlayType",PlayStateManger.TJ11_5);
                        if (Sharedpreferences_Utils.getInstance(_this).getBoolean("isTJ11_5Have")) {
                            _this.startActivityAndBundle(SD11_5_OrderActivity.class, bundle);
                        } else {
                            _this.startActivityAndBundle(SD11_5PlayActivity.class, bundle);
                        }
                        break;
                    case PlayStateManger.JX11_5:
                        //江西11选5
                        bundle.putInt("PlayType",PlayStateManger.JX11_5);
                        if (Sharedpreferences_Utils.getInstance(_this).getBoolean("isJX11_5Have")) {
                            _this.startActivityAndBundle(SD11_5_OrderActivity.class, bundle);
                        } else {
                            _this.startActivityAndBundle(SD11_5PlayActivity.class, bundle);
                        }
                        break;
                    case PlayStateManger.SZ_K3:
                        //江苏快3
                        bundle.putInt("PlayType", PlayStateManger.SZ_K3);
                        if (Sharedpreferences_Utils.getInstance(_this).getBoolean("isSZ_K3Have")) {
                            _this.startActivityAndBundle(K3_OrderActivity.class, bundle);
                        } else {
                            _this.startActivityAndBundle(K3PlayActivity.class, bundle);
                        }
                        break;
                    case PlayStateManger.GX_K3:
                        //广西快3
                        bundle.putInt("PlayType", PlayStateManger.GX_K3);
                        if (Sharedpreferences_Utils.getInstance(_this).getBoolean("isGX_K3Have")) {
                            _this.startActivityAndBundle(K3_OrderActivity.class, bundle);
                        } else {
                            _this.startActivityAndBundle(K3PlayActivity.class, bundle);
                        }
                        break;
                    case PlayStateManger.AH_K3:
                        //安徽快3
                        bundle.putInt("PlayType", PlayStateManger.AH_K3);
                        if (Sharedpreferences_Utils.getInstance(_this).getBoolean("isAH_K3Have")) {
                            _this.startActivityAndBundle(K3_OrderActivity.class, bundle);
                        } else {
                            _this.startActivityAndBundle(K3PlayActivity.class, bundle);
                        }
                        break;
                    case PlayStateManger.PK10:
                        //PK10
                        bundle.putInt("PlayType", PlayStateManger.PK10);
                        if (Sharedpreferences_Utils.getInstance(_this).getBoolean("isCPK10Have")) {
                            _this.startActivityAndBundle(PK10_OrderActivity.class, bundle);
                        } else {
                            _this.startActivityAndBundle(PK10PlayActivity.class, bundle);
                        }
                        break;
                }
            }
        });
    }


    @Override
    protected void showAndData() {
        super.showAndData();
        //每次可见会一次接口
        //getBroadCast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGridAdapter != null) {
            mGridAdapter.cancelAllTimers();
        }
    }

    /**
     * 轮播图数据处理展示
     */
    public void setLunBoData() {
        if (mLunBoList != null && mLunBoList.size() != 0) {
            mImageCycleView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mImageCycleView.getLayoutParams();
            params.width = UiUtils.getWindowsWidth(_this);
            params.height = (int) MathUtils.div(UiUtils.getWindowsWidth(_this), MathUtils.div(1245,575));
            mImageCycleView.setLayoutParams(params);
            ArrayList<String> mImageUrl = new ArrayList<String>();
            ArrayList<String> mImageTitle = new ArrayList<String>();
            for (int i = 0; i < mLunBoList.size(); i++) {
                mImageUrl.add("http://code.310liao.com"+mLunBoList.get(i).getImg());
                mImageTitle.add("");
            }
            mImageCycleView.setImageResources(mImageUrl, mImageTitle,
                    mAdCycleViewListener, 2, false);
        } else {
            mImageCycleView.setVisibility(View.GONE);
        }

    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void onImageClick(final int position, View imageView) {
            if(mLunBoList!=null) {
                LunBoBean bean = mLunBoList.get(position);
                if (bean!=null){
                    if(bean.getType()==3) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Url", bean.getUrl());
                        bundle.putString("Title", bean.getTitle());
                        _this.startActivityAndBundle(WebShowActivity.class, bundle);
                    }
                }
            }
        }
    };

    /**
     * 公告数据处理展示
     */
    private void setNoticeData() {
        if (mLooperList != null && mLooperList.size() != 0) {// 显示公告
            mLooperTextViewlayout.setVisibility(View.VISIBLE);
            ArrayList<String> tipList = new ArrayList<String>();
            for (int i = 0; i < mLooperList.size(); i++) {
                tipList.add(mLooperList.get(i));
            }
            mLooperTextView.setTipList(tipList);
            mLooperTextView.tv_tip_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < mLooperList.size(); i++) {
                        if (mLooperTextView.tv_tip_out.getText().toString()
                                .equals(mLooperList.get(i))) {
                            //点击事件

                            break;
                        }
                    }
                }
            });
            mLooperTextView.tv_tip_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < mLooperList.size(); i++) {
                        if (mLooperTextView.tv_tip_in.getText().toString()
                                .equals(mLooperList.get(i))) {
                            //点击事件

                            break;
                        }

                    }
                }
            });
        } else {
            //mLooperTextViewlayout.setVisibility(View.GONE);
        }
    }


    /**
     * 获取广播列表
     */
    private void getBroadCast() {
        InfoDao.GetBroadCastList(new OnResultListClick<BroadCastBean>() {
            @Override
            public void success(List<BroadCastBean> list) {
                if (list == null || list.size()==0) {
                    mLooperTextViewlayout.setVisibility(View.GONE);
                    return;
                }

                if (mLooperList == null)
                    mLooperList = new ArrayList<>();
                else
                    mLooperList.clear();

                for (BroadCastBean bean : list) {
                    mLooperList.add(bean.getU_bonus() + "元" + bean.getName() + "奖金已被" + bean.getAccount() + "领取");
                }
                setNoticeData();
            }

            @Override
            public void fail(Throwable throwable) {
                setNoticeData();
            }
        });
    }

    /**
     * 获取首页玩法列表
     */
    private void getPlayList(){
        InfoDao.GetPlayList(new OnResultListClick<HomePlayInfoBean>() {
            @Override
            public void success(List<HomePlayInfoBean> list) {
                mRefreshLayout.setRefreshing(false);
                if(list!=null){
                    mPlayInfoList= ToolUtils.depCopy(list);
                    mGridAdapter.setDataNotify(mPlayInfoList);

                    for (HomePlayInfoBean b:list) {
                        b.setCounttime("-1");
                    }
                    //存储本地数据库（先清空再添加）
                    mDaoUtil.deleteAll();
                    mDaoUtil.insertMultOrder(list);

                }
            }

            @Override
            public void fail(Throwable throwable) {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 获取轮播图
     */
    private void getLunBo(){
        InfoDao.GetLunBoList(new OnResultListClick<LunBoBean>() {
            @Override
            public void success(List<LunBoBean> list) {
                if(list!=null){
                    mLunBoList=list;
                }else {
                    mLunBoList.clear();
                }
                //存储本地数据库（先清空再添加）
                mLunBoDaoUtil.deleteAll();
                mLunBoDaoUtil.insertMultOrder(list);
                setLunBoData();
            }

            @Override
            public void fail(Throwable throwable) {
                ToastUtils.showShort(_this,"网络异常");
            }
        });
    }

    /**
     * 获取AppConfig
     */
    private void GetAppConfig(){
        InfoDao.GetAppConfig(new OnResultClick<AppConfigBean>() {
            @Override
            public void success(BaseHttpBean<AppConfigBean> bean) {
                if(bean.getCode()==0){
                    AppConfigBean mConfig=bean.getData();
                    if(mConfig!=null){
                        mTitle.setText(mConfig.getAppname());
                        Sharedpreferences_Utils.getInstance(_this).setString("AppName",mConfig.getAppname());
                    }
                }
            }

            @Override
            public void fail(Throwable throwable) {

            }
        });
    }


}
