package com.jqscp.View;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jqscp.R;
import com.jqscp.Util.BaseView.BasePopupWindow;

/**
 * Created by Administrator on 2018/6/12.
 */

public class CQPlay_PopupWindow extends BasePopupWindow implements View.OnClickListener {
    private View mView;
    private View.OnClickListener mListener;
    private TextView CQPlay_1, CQPlay_21, CQPlay_22, CQPlay_31, CQPlay_32, CQPlay_33, CQPlay_51,
            CQPlay_52, CQPlay_big;

    public CQPlay_PopupWindow(Context context, View.OnClickListener listener) {
        this(context, listener, null);
    }

    public CQPlay_PopupWindow(Context context, View.OnClickListener listener, OnDismissListener dismissListener) {
        super(context, R.layout.cq_play_popwindow);
        this.setAnimationStyle(R.style.Scale_Animal_PopupWindow);
        mView = getContentView();
        mListener = listener;
        setOnDismissListener(dismissListener);
        initView();
    }

    private void initView() {
        CQPlay_1 = mView.findViewById(R.id.CQPlay_1);
        CQPlay_21 = mView.findViewById(R.id.CQPlay_21);
        CQPlay_22 = mView.findViewById(R.id.CQPlay_22);
        CQPlay_31 = mView.findViewById(R.id.CQPlay_31);
        CQPlay_32 = mView.findViewById(R.id.CQPlay_32);
        CQPlay_33 = mView.findViewById(R.id.CQPlay_33);
        CQPlay_51 = mView.findViewById(R.id.CQPlay_51);
        CQPlay_52 = mView.findViewById(R.id.CQPlay_52);
        CQPlay_big = mView.findViewById(R.id.CQPlay_big);
        CQPlay_1.setOnClickListener(this);
        CQPlay_21.setOnClickListener(this);
        CQPlay_22.setOnClickListener(this);
        CQPlay_31.setOnClickListener(this);
        CQPlay_32.setOnClickListener(this);
        CQPlay_33.setOnClickListener(this);
        CQPlay_51.setOnClickListener(this);
        CQPlay_52.setOnClickListener(this);
        CQPlay_big.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onClick(view);
            dismiss();
        }
    }
}
