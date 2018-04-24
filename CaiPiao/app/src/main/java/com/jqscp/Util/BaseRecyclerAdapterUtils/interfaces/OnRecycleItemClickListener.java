package com.jqscp.Util.BaseRecyclerAdapterUtils.interfaces;


import com.jqscp.Util.BaseRecyclerAdapterUtils.BaseViewHolder;

/**
 * recycleView  item点击事件
 */

public interface OnRecycleItemClickListener<T> {
    void onClick(BaseViewHolder holder, T t, int position);
}
