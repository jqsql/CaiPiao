package com.jqscp.Util.BaseActivityUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Fragment通用
 */

public class BaseFragment extends Fragment {
    protected BaseActivity _this;
    private boolean isFirst=true;//第一次加载

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BaseActivity) {
            _this = (BaseActivity) getActivity();
        }else {
            _this=null;
        }
    }
    /*
      懒加载数据
     */
    protected void lazyData(){}
    /*
     可见时获取数据
    */
    protected void showAndData(){}
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //需要放在onResume的方法放在该处执行
            showAndData();
            if(isFirst) {
                lazyData();
                isFirst=false;
            }
        } else {
            //界面不可见的时候执行的方法
        }
    }
}
