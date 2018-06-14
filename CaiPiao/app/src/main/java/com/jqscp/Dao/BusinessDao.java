package com.jqscp.Dao;

import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.MyApplication;
import com.jqscp.Server.BusinessServer;
import com.jqscp.Server.UserServer;
import com.jqscp.Util.RxHttp.RxHttp;
import com.jqscp.Util.RxHttp.RxRetrofit;

import io.reactivex.functions.Consumer;

/**
 * 业务逻辑
 */

public class BusinessDao {
    /**
     * 获取当前期号
     * @param resultClick
     */
    public static void GetCurrentIssue(final OnResultClick<CurrentIssueBean> resultClick){
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequest(mBusinessServer.GetCurrentIssue(), new Consumer<BaseHttpBean<CurrentIssueBean>>() {
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
     * 投注接口
     * @param bill 投注号码
     * @param multiple 倍数
     * @param ptype 玩法id 1-9 一复，二复，二组，三复，三组三，三组六，五复，五通，大小单双
     * @param flag 追号期数1-99 1指当前期
     * @param sbill 彩票期号
     * @param resultClick 结果回调
     */
    public static void putCurrentIssue(String bill, int multiple
            , int ptype, int flag, String sbill, final OnNoResultClick resultClick){
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendNoRequest(mBusinessServer.putCurrentIssue(bill,multiple,ptype,flag,sbill), new Consumer<BaseHttpBean>() {
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
