package com.jqscp.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jqscp.Bean.NumberPlayBean;
import com.jqscp.R;
import com.jqscp.View.CircleBGTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 选彩适配器
 */

public class StarPlayAdapter extends BaseAdapter{
    private Context mContext;
    private List<NumberPlayBean> mList;
    private List<Integer> mCheckedList=new ArrayList<>();//选中的集合
    private LayoutInflater mInflater;
    private OnChangeDataListen mChangeDataListen;
    private int type=1;//万位：5，千位：4，百位：3，十位：2，个位：1
    public interface OnChangeDataListen{
        void onChange(List<Integer> checkedList,int types);
    }

    public void setOnChangeDataListen(OnChangeDataListen changeDataListen) {
        mChangeDataListen = changeDataListen;
    }

    public StarPlayAdapter(Context context, List<NumberPlayBean> list, int type) {
        mContext = context;
        mList = list;
        this.type = type;
        mInflater=LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        viewHolder mViewHolder;
        if(view==null){
            mViewHolder=new viewHolder();
            view= mInflater.inflate(R.layout.circle_bg_item,null);
            mViewHolder.mTextView=view.findViewById(R.id.Select_Number);
            mViewHolder.mLostText=view.findViewById(R.id.Forget_Number);
            view.setTag(mViewHolder);
        }else {
            mViewHolder= (viewHolder) view.getTag();
        }
        NumberPlayBean bean=mList.get(i);
        final int number=bean.getNumber();
        String lostNumber=bean.getLostNumber();
        mViewHolder.mTextView.setText(number+"");
        if(TextUtils.isEmpty(lostNumber)){
            mViewHolder.mLostText.setText("-");
        }else {
            mViewHolder.mLostText.setText(lostNumber);
        }
        if(mCheckedList!=null && mCheckedList.contains(number)){
            mViewHolder.mTextView.setFillColor(true);
        }else {
            mViewHolder.mTextView.setFillColor(false);
        }
        mViewHolder.mTextView.setOnViewClickListen(new CircleBGTextView.OnViewClickListen() {
            @Override
            public void onClick(boolean isFillColor) {
                if(mCheckedList==null)
                    return;
                if(isFillColor){
                    mCheckedList.add(number);
                }else {
                    mCheckedList.remove((Object)number);
                }
                mChangeDataListen.onChange(mCheckedList,type);
            }
        });
        return view;
    }
    class viewHolder{
        CircleBGTextView mTextView;
        TextView mLostText;
    }


    public void setCheckedList(List<Integer> checkedList) {
        mCheckedList= checkedList==null ? new ArrayList<Integer>() : checkedList;
        //mChangeDataListen.onChange(mCheckedList,type);
        notifyDataSetChanged();
    }
}
