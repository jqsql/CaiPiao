package com.jqscp.Dao;
import android.graphics.Bitmap;
import android.util.Base64;

import com.google.gson.Gson;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.Bean.RegisterBean;
import com.jqscp.Server.LoginServer;
import com.jqscp.Server.UserServer;
import com.jqscp.Util.APPUtils.ALog;
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
     * 注册
     * @param resultClick
     */
    public static void Register(String mobile,String pwd,final OnResultClick<RegisterBean> resultClick){
        LoginServer mLoginServer = RxRetrofit.getInstance().create(LoginServer.class);
        RxHttp.sendRequest(mLoginServer.Register(mobile,pwd), new Consumer<BaseHttpBean<RegisterBean>>() {
            @Override
            public void accept(BaseHttpBean<RegisterBean> currentIssueBeanBaseHttpBean) throws Exception {
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
     * 登陆
     * @param resultClick
     */
    public static void Login(String username,String pwd,final OnResultClick<RegisterBean> resultClick){
        LoginServer mLoginServer = RxRetrofit.getInstance().create(LoginServer.class);
        RxHttp.sendRequest(mLoginServer.Login(username,pwd), new Consumer<BaseHttpBean<RegisterBean>>() {
            @Override
            public void accept(BaseHttpBean<RegisterBean> currentIssueBeanBaseHttpBean) throws Exception {
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
     * 忘记密码
     * @param account
     * @param pwd
     * @param code
     * @param resultClick
     */
    public static void ForgetPwd(String account,String pwd,String code,final OnResultClick resultClick){
        LoginServer mLoginServer = RxRetrofit.getInstance().create(LoginServer.class);
        RxHttp.sendNoRequest(mLoginServer.ForgetPwd(account,pwd,code), new Consumer<BaseHttpBean>() {
            @Override
            public void accept(BaseHttpBean bean) throws Exception {
                resultClick.success(bean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }


}
