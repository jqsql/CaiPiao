package com.jqscp.Dao;

import com.jqscp.Bean.AccountMoneyBean;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.Bean.UserInfo;
import com.jqscp.MyApplication;
import com.jqscp.Server.UserServer;
import com.jqscp.Util.RxHttp.RxHttp;
import com.jqscp.Util.RxHttp.RxRetrofit;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/5/11.
 */

public class UserDao {
    /**
     * 获取用户信息
     * @param resultClick
     */
    public static void GetUserInfo(final OnResultClick<UserInfo> resultClick){
        UserServer mUserServer = RxRetrofit.getInstance().create(UserServer.class);
        RxHttp.sendRequest(mUserServer.GetUserInfo(), new Consumer<BaseHttpBean<UserInfo>>() {
            @Override
            public void accept(BaseHttpBean<UserInfo> bean) throws Exception {
                resultClick.success(bean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }
    /**
     * 获取余额或者判断余额
     * @param resultClick
     */
    public static void GetOrCheckMoney(double money,final OnResultClick<AccountMoneyBean> resultClick){
        UserServer mUserServer = RxRetrofit.getInstance().create(UserServer.class);
        RxHttp.sendRequest(mUserServer.GetOrCheckMoney(money), new Consumer<BaseHttpBean<AccountMoneyBean>>() {
            @Override
            public void accept(BaseHttpBean<AccountMoneyBean> currentIssueBeanBaseHttpBean) throws Exception {
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
     * 修改昵称
     * @param resultClick
     */
    public static void SetUserName(String name ,final OnResultClick resultClick){
        UserServer mUserServer = RxRetrofit.getInstance().create(UserServer.class);
        RxHttp.sendNoRequest(mUserServer.SetUserName(name), new Consumer<BaseHttpBean>() {
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

    /**
     * 设置提款密码
     * @param pwd 提款密码
     * @param code 验证码
     * @param resultClick
     */
    public static void SetWithDrawPwd(String pwd,String code,final OnResultClick resultClick){
        UserServer mUserServer = RxRetrofit.getInstance().create(UserServer.class);
        RxHttp.sendNoRequest(mUserServer.SetWithDrawPwd(pwd,code), new Consumer<BaseHttpBean>() {
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
    /**
     * 修改登录密码
     * @param old 旧密码
     * @param news 新证码
     * @param resultClick
     */
    public static void EditPassword(String old,String news,final OnResultClick resultClick){
        UserServer mUserServer = RxRetrofit.getInstance().create(UserServer.class);
        RxHttp.sendNoRequest(mUserServer.EditPassword(old,news), new Consumer<BaseHttpBean>() {
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
