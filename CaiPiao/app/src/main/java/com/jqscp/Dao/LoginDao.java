package com.jqscp.Dao;
import android.graphics.Bitmap;
import android.util.Base64;

import com.google.gson.Gson;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.Server.UserServer;
import com.jqscp.Util.RxHttp.RxHttp;
import com.jqscp.Util.RxHttp.RxRetrofit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * 登录操作
 */

public class LoginDao {
    private static Gson mGson = new Gson();
    /**
     * 获取当前期号
     * @param resultClick
     */
    public void GetCurrentIssue(final OnResultClick<CurrentIssueBean> resultClick){
        UserServer mUserServer = RxRetrofit.getInstance().create(UserServer.class);
        RxHttp.sendRequest(mUserServer.GetCurrentIssue(), new Consumer<BaseHttpBean<CurrentIssueBean>>() {
            @Override
            public void accept(BaseHttpBean<CurrentIssueBean> currentIssueBeanBaseHttpBean) throws Exception {
                resultClick.success(currentIssueBeanBaseHttpBean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }
    /**
     * 上传
     * @param resultClick
     */
    public void putCurrentIssue(String bill, int multiple
            , int ptype, int flag, String sbill, final OnNoResultClick resultClick){
        UserServer mUserServer = RxRetrofit.getInstance().create(UserServer.class);
        RxHttp.sendNoRequest(mUserServer.putCurrentIssue(bill,multiple,ptype,flag,sbill), new Consumer<BaseHttpBean>() {
            @Override
            public void accept(BaseHttpBean currentIssueBeanBaseHttpBean) throws Exception {
                resultClick.success();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }
}
