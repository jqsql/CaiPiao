package com.jqscp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.jqscp.Activity.StarsPlayActivity;
import com.jqscp.Dao.LoginDao;
import com.jqscp.Dao.OnNoResultClick;
import com.jqscp.Util.BaseActivityUtils.BaseActivity;
import com.jqscp.View.CircleBGTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    Button b1,b2,b3,b4,b5,b6,b7,b8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.a1);
        b2=findViewById(R.id.a2);
        b3=findViewById(R.id.a3);
        b4=findViewById(R.id.a4);
        b5=findViewById(R.id.a5);
        b6=findViewById(R.id.a6);
        b7=findViewById(R.id.a7);
        b8=findViewById(R.id.a8);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);

        LoginDao mLoginDao=new LoginDao();
        mLoginDao.putCurrentIssue("12345", 1, 6, 1, "123", new OnNoResultClick() {
            @Override
            public void success() {

            }

            @Override
            public void fail(Throwable throwable) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.a1:
                Bundle bundle=new Bundle();
                bundle.putInt("Types",1);
                startActivityAndBundle(StarsPlayActivity.class,bundle);
                break;
            case R.id.a2:
                Bundle bundle2=new Bundle();
                bundle2.putInt("Types",2);
                startActivityAndBundle(StarsPlayActivity.class,bundle2);
                break;
            case R.id.a3:
                Bundle bundle3=new Bundle();
                bundle3.putInt("Types",3);
                startActivityAndBundle(StarsPlayActivity.class,bundle3);
                break;
            case R.id.a4:
                Bundle bundle4=new Bundle();
                bundle4.putInt("Types",4);
                startActivityAndBundle(StarsPlayActivity.class,bundle4);
                break;
            case R.id.a5:
                Bundle bundle5=new Bundle();
                bundle5.putInt("Types",5);
                startActivityAndBundle(StarsPlayActivity.class,bundle5);
                break;
            case R.id.a6:
                Bundle bundle6=new Bundle();
                bundle6.putInt("Types",6);
                startActivityAndBundle(StarsPlayActivity.class,bundle6);
                break;
            case R.id.a7:
                Bundle bundle7=new Bundle();
                bundle7.putInt("Types",7);
                startActivityAndBundle(StarsPlayActivity.class,bundle7);
                break;
            case R.id.a8:
                Bundle bundle8=new Bundle();
                bundle8.putInt("Types",8);
                startActivityAndBundle(StarsPlayActivity.class,bundle8);
                break;
        }
    }


}
