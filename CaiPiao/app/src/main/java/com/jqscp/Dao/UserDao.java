package com.jqscp.Dao;

import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;
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
     * 获取余额或者判断余额
     * @param resultClick
     */
    public static void GetOrCheckMoney(double money,final OnResultClick<BaseHttpBean> resultClick){
        UserServer mUserServer = RxRetrofit.getInstance().create(UserServer.class);
        RxHttp.sendNoRequest(mUserServer.GetOrCheckMoney(money), new Consumer<BaseHttpBean>() {
            @Override
            public void accept(BaseHttpBean currentIssueBeanBaseHttpBean) throws Exception {
                resultClick.success(currentIssueBeanBaseHttpBean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }

}
