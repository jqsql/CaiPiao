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
import com.jqscp.Util.BaseActivityUtils.BaseFragment;
import com.jqscp.Util.RxJavaUtils.RxBus;
import com.jqscp.Util.RxJavaUtils.RxBusType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 一星玩法
 */

public class OneStarPlayFragment extends BaseFragment {
    private View mView;
    private List<NumberPlayBean> mList = new ArrayList<>();
    private SparseArray<List<Integer>> mCheckList = new SparseArray<>();
    private GridView mGridView;
    private TextView mHintText;
    private StarPlayAdapter mPlayAdapter;

    private int mType;//1.一星复式 3:二星组选;5:三星组三;6:三星组六
    private LinearLayout mDelete;//清空
    private LinearLayout mSharkIt;//机选
    private TextView mSubmitContent;//选中内容显示控件
    private Button mSubmitBtn;//提交

    private boolean isModification=false;//是否是修改操作
    private Long ID;//本地数据库存储id
    private int count;//注数
    private int money;//多少钱

    private int PlayFlag;//0：重庆时时彩、1：新疆时时彩、2：天津时时彩

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
        mGridView = mView.findViewById(R.id.GridView01);
        mDelete = mView.findViewById(R.id.Stars_Play_Delete);
        mSharkIt = mView.findViewById(R.id.Stars_Play_SharkIt);
        mSubmitContent = mView.findViewById(R.id.Stars_Play_SubmitContent);
        mSubmitBtn = mView.findViewById(R.id.Stars_Play_SubmitBtn);
        mHintText = mView.findViewById(R.id.OneStar_1);
    }

    private void initData() {
        mList.add(new NumberPlayBean(0));
        mList.add(new NumberPlayBean(1));
        mList.add(new NumberPlayBean(2));
        mList.add(new NumberPlayBean(3));
        mList.add(new NumberPlayBean(4));
        mList.add(new NumberPlayBean(5));
        mList.add(new NumberPlayBean(6));
        mList.add(new NumberPlayBean(7));
        mList.add(new NumberPlayBean(8));
        mList.add(new NumberPlayBean(9));
        mPlayAdapter = new StarPlayAdapter(getActivity(), mList, 1);
        mGridView.setAdapter(mPlayAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            PlayFlag=bundle.getInt("PlayType");
            mType = bundle.getInt("Types", 0);
            setLostNumber((List<List<String>>) bundle.getSerializable("LostNumber"));
            mCheckList = (SparseArray<List<Integer>>) bundle.getSerializable("Content");
            isModification=bundle.getBoolean("isModification",false);
            ID=bundle.getLong("ID");
            if (mCheckList != null) {
                mPlayAdapter.setCheckedList(mCheckList.get(1));
                setData();
            }else {
                mCheckList=new SparseArray<>();
            }
            switch (mType){
                case 1:
                    mHintText.setText("个位");
                    break;
                case 3:
                    mHintText.setText("二星组选");
                    break;
                case 5:
                    mHintText.setText("三星组三");
                    break;
                case 6:
                    mHintText.setText("三星组六");
                    break;
                default:
                    break;
            }
        }

    }

    private void initListen() {
        mPlayAdapter.setOnChangeDataListen(new StarPlayAdapter.OnChangeDataListen() {
            @Override
            public void onChange(List<Integer> checkedList, int types) {
                mCheckList.put(types, checkedList);
                //RxBus.getDefault().post(new RxBusBean(RxBusType.CQPlay_Notice, mCheckList));
                setData();
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
                for (int i = 1; i <= 1; i++) {
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
                bundle.putInt("Type",mType);
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
                if (rxBusBean.getType() == RxBusType.Shake_Play && rxBusBean.getIntType()==mType) {
                    mCheckList = (SparseArray<List<Integer>>) rxBusBean.getObject();
                    if (mCheckList == null)
                        return;
                    mPlayAdapter.setCheckedList(mCheckList.get(1));
                    setData();
                }else if(rxBusBean.getType() == RxBusType.LostNumber_Notice){
                    List<List<String>> mLostList = (List<List<String>>) rxBusBean.getObject();
                    setLostNumber(mLostList);
                }
            }
        });
    }

    private void setLostNumber(List<List<String>> mLostList){
        if(mType==1) {
            if (mLostList != null && mLostList.size() >= 5) {
                List<String> list = mLostList.get(4);
                if (list != null && list.size() == mList.size()) {
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setLostNumber(list.get(i));
                    }
                    mPlayAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**----------------------算法部分--------------------------*/

    private void setData(){
        switch (mType) {
            case 1:
                OneMathDo();
                break;
            case 3:
                MathDo(2);
                break;
            case 5:
                Calculate(2);
                break;
            case 6:
                MathDo(3);
                break;
        }
        mSubmitContent.setText("共 " + count + " 注, " + money + " 元");
    }
    /**
     * 计算有多少注
     *
     * @param num 每一位至少选的个数
     */
    private synchronized void Calculate(int num) {
        if (mCheckList == null || mCheckList.size()==0)
            return;
        List<Integer> list = mCheckList.get(1);
        if (list == null || list.size() < num) {
            count = 0;
            money = 0;
            return;
        }
        count = list.size() * (list.size() - 1);
        money = count * 2;
    }

    private void MathDo(int num) {
        if (mCheckList == null || mCheckList.size()==0)
            return;
        List<Integer> list = mCheckList.get(1);
        if (list == null || list.size() < num) {
            count = 0;
            money = 0;
            return;
        }
        int s = 1;
        int m = 1;
        for (int i = 0; i < num; i++) {
            m = m * (list.size() - i);
            s = s * (i + 1);
        }
        count = m / s;
        money = count * 2;
    }

    /**
     * 一星算法
     */
    private void OneMathDo() {
        if (mCheckList == null || mCheckList.size()==0)
            return;
        List<Integer> list = mCheckList.get(1);
        count = list.size();
        money = count * 2;
    }

    /**
     * 清空号码
     */
    private void clear(){
        mCheckList.clear();
        setData();
        mPlayAdapter.setCheckedList(null);
    }
}
