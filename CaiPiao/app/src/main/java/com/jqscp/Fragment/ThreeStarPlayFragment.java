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
import com.jqscp.Util.APPUtils.ToastUtils;
import com.jqscp.Util.BaseActivityUtils.BaseFragment;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;

import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 三星玩法(三星直选、三星组六、三星组三)
 */

public class ThreeStarPlayFragment extends BaseFragment{
    private View mView;
    private SparseArray<List<Integer>> mList= new SparseArray<>();
    private SparseArray<List<Integer>> mCheckList= new SparseArray<>();
    private GridView m01;
    private GridView m02;
    private GridView m03;
    private StarPlayAdapter mPlayAdapter01;
    private StarPlayAdapter mPlayAdapter02;
    private StarPlayAdapter mPlayAdapter03;

    private TextView mCountText;
    private int count;//注数
    private int money;//多少钱


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.three_star_play_view,null);
        initView();
        initData();
        initListen();
        return mView;
    }

    private void initView() {
        mCountText=mView.findViewById(R.id.Five_Star_View_BottomText);
        m01=mView.findViewById(R.id.GridView01);
        m02=mView.findViewById(R.id.GridView02);
        m03=mView.findViewById(R.id.GridView03);
    }

    private void initData() {
        List<Integer> list= Arrays.asList(0,1,2,3,4,5,6,7,8,9);
        mList.put(1,list);
        mList.put(2,list);
        mList.put(3,list);
        mPlayAdapter01=new StarPlayAdapter(getActivity(),mList,3);
        mPlayAdapter02=new StarPlayAdapter(getActivity(),mList,2);
        mPlayAdapter03=new StarPlayAdapter(getActivity(),mList,1);
        m01.setAdapter(mPlayAdapter01);
        m02.setAdapter(mPlayAdapter02);
        m03.setAdapter(mPlayAdapter03);
    }

    private void initListen() {
        mPlayAdapter01.setOnChangeDataListen(new StarPlayAdapter.OnChangeDataListen() {
            @Override
            public void onChange(SparseArray<List<Integer>> checkedList) {
                mCheckList=checkedList;
                Calculate(1);
                ToastUtils.showLong(getActivity(),checkedList.toString());
            }
        });
        mPlayAdapter02.setOnChangeDataListen(new StarPlayAdapter.OnChangeDataListen() {
            @Override
            public void onChange(SparseArray<List<Integer>> checkedList) {
                mCheckList=checkedList;
                Calculate(1);
                ToastUtils.showLong(getActivity(),checkedList.toString());
            }
        });
        mPlayAdapter03.setOnChangeDataListen(new StarPlayAdapter.OnChangeDataListen() {
            @Override
            public void onChange(SparseArray<List<Integer>> checkedList) {
                mCheckList=checkedList;
                Calculate(1);
                ToastUtils.showLong(getActivity(),checkedList.toString());
            }
        });



        RxBus.getDefault().doSubscribeMain(getActivity(), RxBusBean.class, new Consumer<RxBusBean>() {
            @Override
            public void accept(RxBusBean rxBusBean) throws Exception {
                if(rxBusBean.getType()== RxBusType.Shake_Play){
                    mCheckList= (SparseArray<List<Integer>>) rxBusBean.getObject();
                    mPlayAdapter01.setCheckedList(mCheckList);
                    mPlayAdapter02.setCheckedList(mCheckList);
                    mPlayAdapter03.setCheckedList(mCheckList);
                    Calculate(1);
                }
            }
        });
    }

    /**
     * 计算有多少注
     */
    private synchronized void Calculate(int num){
        count=1;
        for (int i = 0; i < mCheckList.size(); i++) {
            int key=mCheckList.keyAt(i);
            if(mCheckList.get(key)==null || mCheckList.get(key).size()<num){
                count=0;
                money=count*2;
                mCountText.setText("共 "+count+" 注, "+money+" 元");
                return;
            }
            count= count*mCheckList.get(key).size();
        }
        money=count*2;
        mCountText.setText("共 "+count+" 注, "+money+" 元");
    }

}
