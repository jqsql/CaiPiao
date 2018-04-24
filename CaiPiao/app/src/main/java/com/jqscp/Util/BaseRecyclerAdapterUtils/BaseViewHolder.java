package com.jqscp.Util.BaseRecyclerAdapterUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jqscp.Util.APPUtils.ImageLoader;
import com.makeramen.roundedimageview.RoundedImageView;


/**
 * BaseViewHolder
 */

public class BaseViewHolder extends RecyclerView.ViewHolder{
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;
    private int mPosition;

    public BaseViewHolder(Context context, View itemView, ViewGroup parent)
    {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }


    public static BaseViewHolder get(Context context, ViewGroup parent, int layoutId)
    {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        BaseViewHolder holder = new BaseViewHolder(context, itemView, parent);
        return holder;
    }


    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId)
    {
        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


    public int getViewPosition(){
        return mPosition;
    }

    public void setViewPosition(int position) {
        mPosition = position;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * TextView.setText()
     * @param viewId
     * @param adapter
     * @return
     */
    public BaseViewHolder setRecyclerAdapter(int viewId,RecyclerView.Adapter adapter){
        RecyclerView rv= getView(viewId);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        return this;
    }
    /**
     * TextView.setText()
     * @param viewId
     * @param text
     * @return
     */
    public BaseViewHolder setText(int viewId,String text){
        TextView tv= getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     *ImageView.setImageResource()
     * @param viewId
     * @param resId
     * @return
     */
    public BaseViewHolder setImageResource(int viewId,int resId){
        ImageView imageView=getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    /**
     * ImageView.setImageBitmap()
     * @param viewId
     * @param bm
     * @return
     */
    public BaseViewHolder setImageBitmap(int viewId,Bitmap bm){
        ImageView imageView=getView(viewId);
        imageView.setImageBitmap(bm);
        return this;
    }


    /**
     * Button.setText()
     * @param viewId
     * @param text
     * @return
     */
    public BaseViewHolder setBtnText(int viewId,String text){
        Button button=getView(viewId);
        button.setText(text);
        return this;
    }

    /**
     * 网络图片加载
     * @param viewId
     * @param path
     * @return
     */
    public BaseViewHolder setImage(int viewId,String path){
        ImageView imageView=getView(viewId);
        ImageLoader.getInstance(mContext).setImages(imageView,path);
        return this;
    }

    /**
     * 网络圆形图片加载
     * @param viewId
     * @param path
     * @return
     */
    public BaseViewHolder setCircleImage(int viewId,String path){
        RoundedImageView imageView=getView(viewId);
        ImageLoader.getInstance(mContext).setImages(imageView,path);
        return this;
    }
    /**
     * 设置可见不可见
     * @param viewId
     * @param isShow
     * @return
     */
    public BaseViewHolder setVisibility(int viewId,int isShow){
        View view=getView(viewId);
        view.setVisibility(isShow);
        return this;
    }

    /**
     * view点击事件
     * @param viewId
     * @param click
     * @return
     */
    public BaseViewHolder setOnClickListener(int viewId,View.OnClickListener click){
        View view=getView(viewId);
        synchronized (view) {
            view.setOnClickListener(click);
        }
        return this;
    }
}
