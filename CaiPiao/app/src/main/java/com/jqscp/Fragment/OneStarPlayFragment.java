package com.jqscp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.jqscp.Adapter.StarPlayAdapter;
import com.jqscp.Bean.RxBusBean;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.ToastUtils;
import com.jqscp.Util.BaseActivityUtils.BaseFragment;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 一星玩法
 */

public class OneStarPlayFragment extends BaseFragment {
    private View mView;
    private SparseArray<List<Integer>> mList = new SparseArray<>();
    private SparseArray<List<Integer>> mCheckList = new SparseArray<>();
    private GridView mGridView;
    private StarPlayAdapter mPlayAdapter;

    private TextView mCountText;
    private int count;//注数
    private int money;//多少钱

    private int mType;//3:二星组选;5:三星组三;6:三星组六


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.one_star_play_view, null);
        initView();
        initData();
        initListen();
        return mView;
    }

    private void initView() {
        mCountText = mView.findViewById(R.id.Five_Star_View_BottomText);
        mGridView = mView.findViewById(R.id.GridView01);

    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null)
            mType = bundle.getInt("Types", 0);
        List<Integer> list = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        mList.put(1, list);
        mPlayAdapter = new StarPlayAdapter(getActivity(), mList, 1);
        mGridView.setAdapter(mPlayAdapter);
    }

    private void initListen() {
        mPlayAdapter.setOnChangeDataListen(new StarPlayAdapter.OnChangeDataListen() {
            @Override
            public void onChange(SparseArray<List<Integer>> checkedList) {
                mCheckList = checkedList;
                if (mType == 3) {
                    MathDo(2);
                } else if (mType == 5) {
                    Calculate(2);
                } else if (mType == 6) {
                    MathDo(3);
                }

                ToastUtils.showLong(getActivity(), checkedList.toString());
            }
        });


        RxBus.getDefault().doSubscribeMain(getActivity(), RxBusBean.class, new Consumer<RxBusBean>() {
            @Override
            public void accept(RxBusBean rxBusBean) throws Exception {
                if (rxBusBean.getType() == RxBusType.Shake_Play) {
                    mCheckList = (SparseArray<List<Integer>>) rxBusBean.getObject();
                    mPlayAdapter.setCheckedList(mCheckList);
                    if (mType == 3) {
                        MathDo(2);
                    } else if (mType == 5) {
                        Calculate(2);
                    } else if (mType == 6) {
                        MathDo(3);
                    }
                }
            }
        });
    }

    /**
     * 计算有多少注
     *
     * @param num 每一位至少选的个数
     */
    private synchronized void Calculate(int num) {
        List<Integer> list = mCheckList.get(1);
        if (list == null || list.size() < num) {
            count = 0;
            money = 0;
            mCountText.setText("共 " + count + " 注, " + money + " 元");
            return;
        }
        count = list.size() * (list.size() - 1);
        money = count * 2;
        mCountText.setText("共 " + count + " 注, " + money + " 元");
    }

    private void MathDo(int num) {
        List<Integer> list = mCheckList.get(1);
        if (list == null || list.size() < num) {
            count = 0;
            money = 0;
            mCountText.setText("共 " + count + " 注, " + money + " 元");
            return;
        }
        int s = 1;
        int m = 1;
        for (int i = 0; i < num; i++) {
            m = m * (list.size() - i);
            s = s * (i+1);
        }
        ALog.e("结果1=" + m + "," + s);
        count = m / s;
        money = count * 2;
        mCountText.setText("共 " + count + " 注, " + money + " 元");
        ALog.e("结果=" + count + "," + money);
    }

}
