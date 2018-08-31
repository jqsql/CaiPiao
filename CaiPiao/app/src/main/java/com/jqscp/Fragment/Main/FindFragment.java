package com.jqscp.Fragment.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jqscp.Activity.Finds.EventsActivity;
import com.jqscp.Activity.MainActivity;
import com.jqscp.Activity.WebShowActivity;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Dao.InfoDao;
import com.jqscp.Dao.OnResultClick;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.DisplayUtil;
import com.jqscp.Util.APPUtils.ToastUtils;
import com.jqscp.Util.BaseActivityUtils.BaseFragment;

/**
 * Created by Administrator on 2018/6/12.
 */

public class FindFragment extends BaseFragment{
    private View mView;
    private MainActivity mMainActivity;
    private RelativeLayout mTopBarLayout;
    private RelativeLayout mEvents;
    private RelativeLayout mKeFu;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_find_view,null);
        mEvents=mView.findViewById(R.id.Find_ActivityLayout);
        mKeFu=mView.findViewById(R.id.Find_KeFuLayout);
        mMainActivity= (MainActivity) getActivity();
        initView();
        initListen();
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
    private void initListen(){
        //活动
        mEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _this.startActivity(EventsActivity.class);
            }
        });
        //客服
        mKeFu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //防止连续点击
                mKeFu.setEnabled(false);
                getKeFu();
            }
        });
    }

    private void getKeFu(){
        InfoDao.GetKeFu(new OnResultClick<String>() {
            @Override
            public void success(BaseHttpBean<String> bean) {
                mKeFu.setEnabled(true);
                if(bean!=null) {
                    if (bean.getCode() == 0) {
                        String url = bean.getData();
                        Bundle bundle = new Bundle();
                        bundle.putString("Url", url);
                        bundle.putString("Title", "客服");
                        _this.startActivityAndBundle(WebShowActivity.class, bundle);
                    } else {
                        ToastUtils.showShort(_this, bean.getMessage());
                    }
                }
            }

            @Override
            public void fail(Throwable throwable) {
                mKeFu.setEnabled(true);
                ToastUtils.showShort(_this, "网络异常");
            }
        });
    }

}
