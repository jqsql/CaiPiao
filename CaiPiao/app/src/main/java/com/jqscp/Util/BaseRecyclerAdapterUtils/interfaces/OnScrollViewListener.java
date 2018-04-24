package com.jqscp.Util.BaseRecyclerAdapterUtils.interfaces;

/**
 * 只适应于货架的监听
 */

public interface OnScrollViewListener {
    void onSrcoll(int position, boolean isPeople);
    void onSrcollState(boolean isPeople);
}
