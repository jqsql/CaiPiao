package com.jqscp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.jqscp.View.CircleBGTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Integer> mList= new ArrayList<>();
    GridView m01;
    GridView m02;
    GridView m03;
    GridView m04;
    GridView m05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList= Arrays.asList(0,1,2,3,4,5,6,7,8,9);
        m01=findViewById(R.id.GridView01);
        m02=findViewById(R.id.GridView02);
        m03=findViewById(R.id.GridView03);
        m04=findViewById(R.id.GridView04);
        m05=findViewById(R.id.GridView05);
        m01.setAdapter(new GridViewAdapter());
        m02.setAdapter(new GridViewAdapter());
        m03.setAdapter(new GridViewAdapter());
        m04.setAdapter(new GridViewAdapter());
        m05.setAdapter(new GridViewAdapter());
    }



    class GridViewAdapter extends BaseAdapter{
        public GridViewAdapter() {

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
        public View getView(int i, View view, ViewGroup viewGroup) {
            viewHolder mViewHolder;
            if(view==null){
                mViewHolder=new viewHolder();
                view= LayoutInflater.from(MainActivity.this).inflate(R.layout.circle_bg_item,null);
                mViewHolder.mTextView=view.findViewById(R.id.text);
                view.setTag(mViewHolder);
            }else {
                mViewHolder= (viewHolder) view.getTag();
            }
            mViewHolder.mTextView.setText(mList.get(i)+"");
            return view;
        }
        class viewHolder{
            CircleBGTextView mTextView;
        }
    }
}
