package com.jqscp.Fragment.Plays;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jqscp.Activity.Orders.CQ_SSC_OrderActivity;
import com.jqscp.Adapter.StarPlayAdapter;
import com.jqscp.Bean.NumberPlayBean;
import com.jqscp.Bean.RxBusBean;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.ToastUtils;
import com.jqscp.Util.APPUtils.ToolUtils;
import com.jqscp.Util.BaseActivityUtils.BaseFragment;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 三星玩法(三星直选、三星组六、三星组三)
 */

public class ThreeStarPlayFragment extends BaseFragment{
    private View mView;
    private List<NumberPlayBean> mList1 = new ArrayList<>();
    private List<NumberPlayBean> mList2 = new ArrayList<>();
    private List<NumberPlayBean> mList3 = new ArrayList<>();
    private SparseArray<List<Integer>> mCheckList= new SparseArray<>();
    private GridView m01;
    private GridView m02;
    private GridView m03;
    private StarPlayAdapter mPlayAdapter01;
    private StarPlayAdapter mPlayAdapter02;
    private StarPlayAdapter mPlayAdapter03;
    private LinearLayout mDelete;//清空
    private LinearLayout mSharkIt;//机选
    private TextView mSubmitContent;//选中内容显示控件
    private Button mSubmitBtn;//提交

    private boolean isModification;//是否是修改操作
    private Long ID;//本地数据库存储id
    private int count;//注数
    private int money;//多少钱

    private int PlayFlag;//0：重庆时时彩、1：新疆时时彩、2：天津时时彩


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
        m01=mView.findViewById(R.id.GridView01);
        m02=mView.findViewById(R.id.GridView02);
        m03=mView.findViewById(R.id.GridView03);
        mDelete = mView.findViewById(R.id.Stars_Play_Delete);
        mSharkIt = mView.findViewById(R.id.Stars_Play_SharkIt);
        mSubmitContent = mView.findViewById(R.id.Stars_Play_SubmitContent);
        mSubmitBtn = mView.findViewById(R.id.Stars_Play_SubmitBtn);
    }

    private void initData() {
        mList1.add(new NumberPlayBean(0));
        mList1.add(new NumberPlayBean(1));
        mList1.add(new NumberPlayBean(2));
        mList1.add(new NumberPlayBean(3));
        mList1.add(new NumberPlayBean(4));
        mList1.add(new NumberPlayBean(5));
        mList1.add(new NumberPlayBean(6));
        mList1.add(new NumberPlayBean(7));
        mList1.add(new NumberPlayBean(8));
        mList1.add(new NumberPlayBean(9));
        mList2= ToolUtils.depCopy(mList1);
        mList3=ToolUtils.depCopy(mList1);

        mPlayAdapter01=new StarPlayAdapter(getActivity(),mList3,3);
        mPlayAdapter02=new StarPlayAdapter(getActivity(),mList2,2);
        mPlayAdapter03=new StarPlayAdapter(getActivity(),mList1,1);
        m01.setAdapter(mPlayAdapter01);
        m02.setAdapter(mPlayAdapter02);
        m03.setAdapter(mPlayAdapter03);

        Bundle bundle = getArguments();
        if (bundle != null) {
            PlayFlag=bundle.getInt("PlayType");
            setLostNumber((List<List<String>>) bundle.getSerializable("LostNumber"));
            mCheckList = (SparseArray<List<Integer>>) bundle.getSerializable("Content");
            isModification=bundle.getBoolean("isModification");
            ID=bundle.getLong("ID");
            if (mCheckList != null) {
                mPlayAdapter01.setCheckedList(mCheckList.get(3));
                mPlayAdapter02.setCheckedList(mCheckList.get(2));
                mPlayAdapter03.setCheckedList(mCheckList.get(1));
                setData();
            }else {
                mCheckList=new SparseArray<>();
            }
        }

    }

    private void initListen() {
        mPlayAdapter01.setOnChangeDataListen(new StarPlayAdapter.OnChangeDataListen() {
            @Override
            public void onChange(List<Integer> checkedList,int types) {
                mCheckList.put(types,checkedList);
                setData();
            }
        });
        mPlayAdapter02.setOnChangeDataListen(new StarPlayAdapter.OnChangeDataListen() {
            @Override
            public void onChange(List<Integer> checkedList,int types) {
                mCheckList.put(types,checkedList);
                setData();
            }
        });
        mPlayAdapter03.setOnChangeDataListen(new StarPlayAdapter.OnChangeDataListen() {
            @Override
            public void onChange(List<Integer> checkedList,int types) {
                mCheckList.put(types,checkedList);
                setData();
            }
        });
        //机选
        mSharkIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.getDefault().post(new RxBusBean(RxBusType.ToShake));
            }
        });
        //清空
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });
        //提交
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==0){
                    ToastUtils.showShort(_this,"请至少选择一注");
                    return;
                }
                if (mCheckList == null)
                    return;
                StringBuilder number = new StringBuilder("");
                for (int i = 1; i <= 3; i++) {
                    List<Integer> list = mCheckList.get(i);
                    //升序排列
                    Collections.sort(list);
                    if (list != null && list.size() != 0) {
                        for (Integer data : list) {
                            number.append(data);
                        }
                        number.append(",");
                    }
                }
                String data = number.toString();
                if (data.contains(",")) {
                    data = data.substring(0, data.length() - 1);
                }
                Bundle bundle=new Bundle();
                bundle.putString("Data",data);
                bundle.putInt("Type",4);
                bundle.putInt("Count",count);
                bundle.putInt("Money",money);

                bundle.putInt("PlayType",PlayFlag);
                if(isModification){
                    Intent intent=_this.getIntent();
                    bundle.putLong("ID",ID);
                    intent.putExtras(bundle);
                    _this.setResult(1,intent);
                    _this.finish();
                }else {
                    _this.startActivityAndFinishSelf(CQ_SSC_OrderActivity.class, bundle);
                }
            }
        });


        RxBus.getDefault().doSubscribeMain(getActivity(), RxBusBean.class, new Consumer<RxBusBean>() {
            @Override
            public void accept(RxBusBean rxBusBean) throws Exception {
                if(rxBusBean.getType()== RxBusType.Shake_Play && rxBusBean.getIntType()==4){
                    mCheckList= (SparseArray<List<Integer>>) rxBusBean.getObject();
                    if(mCheckList==null)
                        return;
                    mPlayAdapter01.setCheckedList(mCheckList.get(3));
                    mPlayAdapter02.setCheckedList(mCheckList.get(2));
                    mPlayAdapter03.setCheckedList(mCheckList.get(1));
                    setData();
                }else if(rxBusBean.getType() == RxBusType.LostNumber_Notice){
                    List<List<String>> mLostList= (List<List<String>>) rxBusBean.getObject();
                    setLostNumber(mLostList);
                }
            }
        });
    }
    private void setLostNumber(List<List<String>> mLostList){
        if(mLostList!=null && mLostList.size()>=5) {
            List<String> list1 = mLostList.get(4);
            if(list1!=null && list1.size()==mList1.size()) {
                for (int i = 0; i < mList1.size(); i++) {
                    mList1.get(i).setLostNumber(list1.get(i));
                }
                mPlayAdapter03.notifyDataSetChanged();
            }
            List<String> list2 = mLostList.get(3);
            if(list2!=null && list2.size()==mList2.size()) {
                for (int i = 0; i < mList2.size(); i++) {
                    mList2.get(i).setLostNumber(list2.get(i));
                }
                mPlayAdapter02.notifyDataSetChanged();
            }
            List<String> list3 = mLostList.get(2);
            if(list3!=null && list3.size()==mList3.size()) {
                for (int i = 0; i < mList3.size(); i++) {
                    mList3.get(i).setLostNumber(list3.get(i));
                }
                mPlayAdapter01.notifyDataSetChanged();
            }
        }
    }

    private void setData(){
        Calculate2(3);
        mSubmitContent.setText("共 " + count + " 注, " + money + " 元");
    }

    /**
     * 二星复式、三星复式计算方法
     */
    private synchronized void Calculate2(int num) {
        if (mCheckList == null || mCheckList.size()==0)
            return;
        if (mCheckList.size() < num) {
            count = 0;
            money = 0;
            return;
        }
        count = 1;
        for (int i = 0; i < mCheckList.size(); i++) {
            int key = mCheckList.keyAt(i);
            if (mCheckList.get(key) == null || mCheckList.get(key).size() < 1) {
                count = 0;
                break;
            }
            count = count * mCheckList.get(key).size();
        }
        money = count * 2;
    }

    /**
     * 清空号码
     */
    private void clear(){
        mCheckList.clear();
        setData();
        mPlayAdapter01.setCheckedList(null);
        mPlayAdapter02.setCheckedList(null);
        mPlayAdapter03.setCheckedList(null);
    }
}
