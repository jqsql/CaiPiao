package com.jqscp.Fragment.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jqscp.Activity.MainActivity;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.DisplayUtil;
import com.jqscp.Util.BaseActivityUtils.BaseFragment;

/**
 * Created by Administrator on 2018/6/12.
 */

public class FindFragment extends BaseFragment{
    private View mView;
    private MainActivity mMainActivity;
    private RelativeLayout mTopBarLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_find_view,null);
        mMainActivity= (MainActivity) getActivity();
        initView();
        return mView;
    }
    /**
     * 初始化
     */
    private void initView() {
        mTopBarLayout=mView.findViewById(R.id.Main_Find_TopLayout);

        if(mMainActivity.isSuccessStatusBar) {
            int pad = DisplayUtil.dip2px(_this, 16);
            mTopBarLayout.setPadding(pad, pad + _this.mStatusBarHeight, pad, pad);
        }
    }

}
