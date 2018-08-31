package com.jqscp.Fragment.Main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jqscp.Activity.Lotterys.HistoryLotteryActivity;
import com.jqscp.Activity.MainActivity;
import com.jqscp.Activity.Plays.PlayStateManger;
import com.jqscp.Bean.CQHistoryBean;
import com.jqscp.Bean.LotteryBean;
import com.jqscp.Dao.BusinessDao;
import com.jqscp.Dao.OnResultListClick;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.DisplayUtil;
import com.jqscp.Util.BaseActivityUtils.BaseFragment;
import com.jqscp.Util.BaseRecyclerAdapterUtils.BaseRecyclerAdapter;
import com.jqscp.Util.BaseRecyclerAdapterUtils.BaseViewHolder;
import com.jqscp.Util.BaseRecyclerAdapterUtils.interfaces.OnRecycleItemClickListener;
import com.jqscp.View.FullyLinearLayoutManager;
import com.jqscp.View.MyGrideview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/12.
 */

public class LotteryFragment extends BaseFragment {
    private View mView;
    private MainActivity mMainActivity;
    private RelativeLayout mTopBarLayout;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private BaseRecyclerAdapter<LotteryBean> mAdapter;
    private CountDownTimer mCountDownTimer;

    private List<LotteryBean> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_lottery_view, null);
        mMainActivity = (MainActivity) getActivity();
        initView();
        initData();
        initListen();
        getLotteryList();
        return mView;
    }

    /**
     * 初始化
     */
    private void initView() {
        mTopBarLayout = mView.findViewById(R.id.Main_Lottery_TopLayout);
        mRecyclerView = mView.findViewById(R.id.Main_Lottery_RecyclerView);
        mRefreshLayout = mView.findViewById(R.id.Main_Lottery_ParentsLayout);

        if (mMainActivity.isSuccessStatusBar) {
            int pad = DisplayUtil.dip2px(_this, 16);
            mTopBarLayout.setPadding(pad, pad + _this.mStatusBarHeight, pad, pad);
        }

        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(_this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new BaseRecyclerAdapter<LotteryBean>(_this, R.layout.item_main_lottery, mList, false) {
            @Override
            protected void convert(BaseViewHolder holder, LotteryBean cqHistoryBean, int position) {
                if (cqHistoryBean == null)
                    return;
                holder.setText(R.id.Lottery_Item_Name, cqHistoryBean.getName())
                        .setText(R.id.Lottery_Item_QI, cqHistoryBean.getS_time()==null ? "暂无开奖信息" : "第"+cqHistoryBean.getS_time()+"期");
                LinearLayout layout01=holder.getView(R.id.Lottery_Item_NumberLayout);
                LinearLayout layout02=holder.getView(R.id.Lottery_Item_Number2Layout);
                LinearLayout layout03=holder.getView(R.id.Lottery_Item_NumberBottomLayout);
                if(cqHistoryBean.getS_bill()!=null) {
                    String[] strs = cqHistoryBean.getS_bill().split(",");
                    if (cqHistoryBean.getS_type()==PlayStateManger.SZ_K3 || cqHistoryBean.getS_type()==PlayStateManger.GX_K3
                            || cqHistoryBean.getS_type()==PlayStateManger.AH_K3) {
                        layout02.setVisibility(View.VISIBLE);
                        layout01.setVisibility(View.GONE);
                        layout03.setVisibility(View.GONE);
                        if(strs.length==3) {
                            holder.setImageResource(R.id.MainLottery_Item_Item_Icon01, setInitResource(strs[0]))
                                    .setImageResource(R.id.MainLottery_Item_Item_Icon02, setInitResource(strs[1]))
                                    .setImageResource(R.id.MainLottery_Item_Item_Icon03, setInitResource(strs[2]));
                        }
                    }else if(cqHistoryBean.getS_type()==PlayStateManger.PK10){
                        layout01.setVisibility(View.VISIBLE);
                        layout02.setVisibility(View.GONE);
                        layout03.setVisibility(View.VISIBLE);
                        if(strs.length==10) {
                            holder.setText(R.id.MainLottery_Item_Item_Number01, strs[0])
                                    .setText(R.id.MainLottery_Item_Item_Number02, strs[1])
                                    .setText(R.id.MainLottery_Item_Item_Number03, strs[2])
                                    .setText(R.id.MainLottery_Item_Item_Number04, strs[3])
                                    .setText(R.id.MainLottery_Item_Item_Number05, strs[4])
                                    .setText(R.id.MainLottery_Item_Item_Number06, strs[5])
                                    .setText(R.id.MainLottery_Item_Item_Number07, strs[6])
                                    .setText(R.id.MainLottery_Item_Item_Number08, strs[7])
                                    .setText(R.id.MainLottery_Item_Item_Number09, strs[8])
                                    .setText(R.id.MainLottery_Item_Item_Number10, strs[9]);
                        }
                    }else {
                        layout01.setVisibility(View.VISIBLE);
                        layout02.setVisibility(View.GONE);
                        layout03.setVisibility(View.GONE);
                        if(strs.length==5) {
                            holder.setText(R.id.MainLottery_Item_Item_Number01, strs[0])
                                    .setText(R.id.MainLottery_Item_Item_Number02, strs[1])
                                    .setText(R.id.MainLottery_Item_Item_Number03, strs[2])
                                    .setText(R.id.MainLottery_Item_Item_Number04, strs[3])
                                    .setText(R.id.MainLottery_Item_Item_Number05, strs[4]);
                        }
                    }
                }else {
                    layout01.setVisibility(View.GONE);
                    layout02.setVisibility(View.GONE);
                    layout03.setVisibility(View.GONE);
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initData() {

    }


    private void initListen() {
        mAdapter.setItemClickListener(new OnRecycleItemClickListener<LotteryBean>() {
            @Override
            public void onClick(BaseViewHolder holder, LotteryBean cqHistoryBean, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("DataType", cqHistoryBean.getS_type());
                bundle.putString("DataTypeName", cqHistoryBean.getName());
                _this.startActivityAndBundle(HistoryLotteryActivity.class, bundle);
            }
        });

        //刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLotteryList();
            }
        });
    }

    private int setInitResource(String str){
        int res=0;
        switch (str) {
            case "1":
                res=R.drawable.k3_1;
                break;
            case "2":
                res=R.drawable.k3_2;
                break;
            case "3":
                res=R.drawable.k3_3;
                break;
            case "4":
                res=R.drawable.k3_4;
                break;
            case "5":
                res=R.drawable.k3_5;
                break;
            case "6":
                res=R.drawable.k3_6;
                break;
            default:
                break;
        }
        return res;
    }

    /**
     * 计时器
     */
    private void initTime() {
        //每10分钟请求一次
        if(mCountDownTimer!=null)
            mCountDownTimer.cancel();
        mCountDownTimer=new CountDownTimer(10 * 60 * 1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                getLotteryList();
            }
        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mCountDownTimer!=null)
            mCountDownTimer.cancel();
    }

    private void getLotteryList() {
        BusinessDao.GetAllLottery(new OnResultListClick<LotteryBean>() {
            @Override
            public void success(List<LotteryBean> list) {
                mRefreshLayout.setRefreshing(false);
                if (list == null) {
                    initTime();
                    return;
                }

                mList.clear();
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
                initTime();
            }

            @Override
            public void fail(Throwable throwable) {
                mRefreshLayout.setRefreshing(false);
                initTime();
            }
        });
    }

}
