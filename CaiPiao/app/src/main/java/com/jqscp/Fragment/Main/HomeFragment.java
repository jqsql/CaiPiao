package com.jqscp.Fragment.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jqscp.Activity.MainActivity;
import com.jqscp.Activity.Plays.StarsPlayActivity;
import com.jqscp.R;
import com.jqscp.Util.APPUtils.DisplayUtil;
import com.jqscp.Util.APPUtils.MathUtils;
import com.jqscp.Util.APPUtils.UiUtils;
import com.jqscp.Util.BaseActivityUtils.BaseFragment;
import com.jqscp.View.ImageCycleView;
import com.jqscp.View.LooperTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/12.
 */

public class HomeFragment extends BaseFragment {
    private View mView;
    private MainActivity mMainActivity;
    private Button mBtn;
    private RelativeLayout mTopBarLayout;
    private ImageCycleView mImageCycleView;//轮播图控件
    private LooperTextView mLooperTextView;//公告控件

    private List<String> mLunBoList;//轮播图数据
    private List<String> mLooperList;//公告数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_view, null);
        mMainActivity= (MainActivity) getActivity();
        initView();
        initData();
        initListen();
        return mView;
    }

    /**
     * 初始化
     */
    private void initView() {
        mBtn = mView.findViewById(R.id.GoToPlay);
        mTopBarLayout=mView.findViewById(R.id.Main_Home_TopLayout);
        mImageCycleView = mView.findViewById(R.id.Main_Home_ImageCycleView);
        mLooperTextView = mView.findViewById(R.id.Main_Home_LooperTextView);

        if(mMainActivity.isSuccessStatusBar) {
            int pad = DisplayUtil.dip2px(_this, 16);
            mTopBarLayout.setPadding(pad, pad + _this.mStatusBarHeight, pad, pad);
        }
    }

    /**
     * 数据处理
     */
    private void initData() {
        mLunBoList = new ArrayList<>();
        mLooperList = new ArrayList<>();
        mLunBoList.add("http://b.hiphotos.baidu.com/image/h%3D300/sign=27b13845316d55fbdac670265d234f40/96dda144ad345982b391b10900f431adcbef8415.jpg");
        mLunBoList.add("http://pic.5442.com/2014/0910/05/03.jpg");
        mLunBoList.add("http://c.hiphotos.baidu.com/image/h%3D300/sign=e677945ef7edab646b724bc0c737af81/8b13632762d0f703d3f77f4904fa513d2797c5da.jpg");
        mLunBoList.add("http://a.hiphotos.baidu.com/image/h%3D300/sign=1c5b5e74232eb938f36d7cf2e56385fe/d0c8a786c9177f3e2fb5a1987ccf3bc79e3d56a5.jpg");
        setLunBoData();

        mLooperList.add("第11111111111111条");
        mLooperList.add("第22222222222条");
        mLooperList.add("第333333333333333333333333333333333333333333333333333333333333333333333333333条");
        setNoticeData();
    }

    /**
     * 事件监听
     */
    private void initListen() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _this.startActivity(StarsPlayActivity.class);
            }
        });
    }

    /**
     * 轮播图数据处理展示
     */
    public void setLunBoData() {
        if (mLunBoList != null && mLunBoList.size() != 0) {
            mImageCycleView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mImageCycleView.getLayoutParams();
            params.width = UiUtils.getWindowsWidth(_this);
            params.height = (int) MathUtils.div(UiUtils.getWindowsWidth(_this), 2);
            mImageCycleView.setLayoutParams(params);
            ArrayList<String> mImageUrl = new ArrayList<String>();
            ArrayList<String> mImageTitle = new ArrayList<String>();
            for (int i = 0; i < mLunBoList.size(); i++) {
                mImageUrl.add(mLunBoList.get(i));
                mImageTitle.add("");
            }
            mImageCycleView.setImageResources(mImageUrl, mImageTitle,
                    mAdCycleViewListener, 2, false);
        } else {
            mImageCycleView.setVisibility(View.GONE);
        }

    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void onImageClick(final int position, View imageView) {

        }
    };

    /**
     * 公告数据处理展示
     */
    private void setNoticeData() {
        if (mLooperList != null && mLooperList.size() != 0) {// 显示公告
            mLooperTextView.setVisibility(View.VISIBLE);
            ArrayList<String> tipList = new ArrayList<String>();
            for (int i = 0; i < mLooperList.size(); i++) {
                tipList.add(mLooperList.get(i));
            }
            mLooperTextView.setTipList(tipList);
            mLooperTextView.tv_tip_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < mLooperList.size(); i++) {
                        if (mLooperTextView.tv_tip_out.getText().toString()
                                .equals(mLooperList.get(i))) {
                            //点击事件

                            break;
                        }
                    }
                }
            });
            mLooperTextView.tv_tip_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < mLooperList.size(); i++) {
                        if (mLooperTextView.tv_tip_in.getText().toString()
                                .equals(mLooperList.get(i))) {
                            //点击事件

                            break;
                        }

                    }
                }
            });
        } else {
            mLooperTextView.setVisibility(View.GONE);
        }
    }


}
