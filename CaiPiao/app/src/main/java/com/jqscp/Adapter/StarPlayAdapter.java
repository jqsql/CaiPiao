package com.jqscp.Adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jqscp.Fragment.OneStarPlayFragment;
import com.jqscp.MainActivity;
import com.jqscp.R;
import com.jqscp.View.CircleBGTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 选彩适配器
 */

public class StarPlayAdapter extends BaseAdapter{
    private StarPlayAdapter mAdapter;
    private Context mContext;
    private List<Integer> mList;
    private SparseArray<List<Integer>> mCheckedList=new SparseArray<>();//选中的集合
    private LayoutInflater mInflater;
    private OnChangeDataListen mChangeDataListen;
    private int type=1;//万位：5，千位：4，百位：3，十位：2，个位：1
    public interface OnChangeDataListen{
        void onChange(SparseArray<List<Integer>> checkedList);
    }

    public void setOnChangeDataListen(OnChangeDataListen changeDataListen) {
        mChangeDataListen = changeDataListen;
    }

    public StarPlayAdapter(Context context, SparseArray<List<Integer>> list,int type) {
        mContext = context;
        mList = list.get(type);
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
            view.setTag(mViewHolder);
        }else {
            mViewHolder= (viewHolder) view.getTag();
        }
        final int bean=mList.get(i);
        mViewHolder.mTextView.setText(bean+"");
        if(mCheckedList.get(type)!=null && mCheckedList.get(type).contains(bean)){
            mViewHolder.mTextView.setFillColor(true);
        }else {
            mViewHolder.mTextView.setFillColor(false);
        }
        mViewHolder.mTextView.setOnViewClickListen(new CircleBGTextView.OnViewClickListen() {
            @Override
            public void onClick(boolean isFillColor) {
                if(isFillColor){
                    if(mCheckedList.get(type)==null)
                        mCheckedList.put(type,new ArrayList<Integer>());
                    mCheckedList.get(type).add(bean);
                }else {
                    if(mCheckedList.get(type)==null)
                        mCheckedList.put(type,new ArrayList<Integer>());
                    mCheckedList.get(type).remove((Object)bean);
                }
                mChangeDataListen.onChange(mCheckedList);
            }
        });
        return view;
    }
    class viewHolder{
        CircleBGTextView mTextView;
    }


    public SparseArray<List<Integer>> getCheckedList() {
        return mCheckedList;
    }

    public void setCheckedList(SparseArray<List<Integer>> checkedList) {
        mCheckedList= checkedList==null ? new SparseArray<List<Integer>>() : checkedList;
        notifyDataSetChanged();
    }
}
