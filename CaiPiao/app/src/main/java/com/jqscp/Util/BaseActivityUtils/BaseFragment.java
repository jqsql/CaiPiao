package com.jqscp.Util.BaseActivityUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Fragment通用
 */

public class BaseFragment extends Fragment {
    protected BaseActivity _this;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BaseActivity) {
            _this = (BaseActivity) getActivity();
        }else {
            _this=null;
        }
    }

}
