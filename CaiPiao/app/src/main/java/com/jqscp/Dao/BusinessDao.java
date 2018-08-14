package com.jqscp.Dao;

import com.jqscp.Bean.AccountDetailsBean;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.BettingRecordBean;
import com.jqscp.Bean.CQHistoryBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.Bean.LotteryBean;
import com.jqscp.Bean.OrderDetailsBean;
import com.jqscp.MyApplication;
import com.jqscp.Server.BusinessServer;
import com.jqscp.Server.UserServer;
import com.jqscp.Util.RxHttp.RxHttp;
import com.jqscp.Util.RxHttp.RxRetrofit;

import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.RequestBody;

/**
 * 业务逻辑
 */

public class BusinessDao {
    /**
     * 获取当前期号（重庆时时彩）
     *
     * @param resultClick
     */
    public static void GetCurrentIssue(final OnResultClick<CurrentIssueBean> resultClick) {
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
     * 获取当前期号（新疆时时彩）
     *
     * @param resultClick
     */
    public static void GetXJCurrentIssue(final OnResultClick<CurrentIssueBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequest(mBusinessServer.GetXJCurrentIssue(), new Consumer<BaseHttpBean<CurrentIssueBean>>() {
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
     * 获取当前期号（山东11选5时时彩）
     *
     * @param resultClick
     */
    public static void GetSD11_5CurrentIssue(final OnResultClick<CurrentIssueBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequest(mBusinessServer.GetSD11_5CurrentIssue(), new Consumer<BaseHttpBean<CurrentIssueBean>>() {
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
     * 获取当前期号（天津11选5时时彩）
     *
     * @param resultClick
     */
    public static void GetTJ11_5CurrentIssue(final OnResultClick<CurrentIssueBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequest(mBusinessServer.GetTJ11_5CurrentIssue(), new Consumer<BaseHttpBean<CurrentIssueBean>>() {
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
     * 获取当前期号（江苏快3）
     *
     * @param resultClick
     */
    public static void GetJSK3CurrentIssue(final OnResultClick<CurrentIssueBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequest(mBusinessServer.GetJSK3CurrentIssue(), new Consumer<BaseHttpBean<CurrentIssueBean>>() {
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
     * 获取当前期号（天津时时彩）
     *
     * @param resultClick
     */
    public static void GetTJCurrentIssue(final OnResultClick<CurrentIssueBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequest(mBusinessServer.GetTJCurrentIssue(), new Consumer<BaseHttpBean<CurrentIssueBean>>() {
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
     * 投注接口(重庆时时彩)
     * <p>
     * bill 投注号码
     * multiple 倍数
     * ptype 玩法id 1-9 一复，二复，二组，三复，三组三，三组六，五复，五通，大小单双
     * flag 追号期数1-99 1指当前期
     * sbill 彩票期号
     */
    public static void putCurrentIssue(String data, final OnResultClick<BaseHttpBean> resultClick) {
        RequestBody bady=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data);
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendNoRequest(mBusinessServer.putCurrentIssue(bady), new Consumer<BaseHttpBean>() {
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
    /**
     * 投注接口(新疆时时彩)
     * <p>
     * bill 投注号码
     * multiple 倍数
     * ptype 玩法id 1-9 一复，二复，二组，三复，三组三，三组六，五复，五通，大小单双
     * flag 追号期数1-99 1指当前期
     * sbill 彩票期号
     */
    public static void putXJCurrentIssue(String data, final OnResultClick<BaseHttpBean> resultClick) {
        RequestBody bady=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data);
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendNoRequest(mBusinessServer.putXJCurrentIssue(bady), new Consumer<BaseHttpBean>() {
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
    /**
     * 投注接口(天津时时彩)
     * <p>
     * bill 投注号码
     * multiple 倍数
     * ptype 玩法id 1-9 一复，二复，二组，三复，三组三，三组六，五复，五通，大小单双
     * flag 追号期数1-99 1指当前期
     * sbill 彩票期号
     */
    public static void putTJCurrentIssue(String data, final OnResultClick<BaseHttpBean> resultClick) {
        RequestBody bady=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data);
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendNoRequest(mBusinessServer.putTJCurrentIssue(bady), new Consumer<BaseHttpBean>() {
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

    /**
     * pk10投注接口
     *
     * @param resultClick 结果回调
     */
    public static void Pk10CurrentIssue(String data, final OnResultClick<BaseHttpBean> resultClick) {
        RequestBody bady=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data);
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendNoRequest(mBusinessServer.Pk10CurrentIssue(bady), new Consumer<BaseHttpBean>() {
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
    /**
     * 山东11选5投注接口
     *
     * @param resultClick 结果回调
     */
    public static void SD11_5CurrentIssue(String data, final OnResultClick<BaseHttpBean> resultClick) {
        RequestBody bady=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data);
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendNoRequest(mBusinessServer.SD11_5CurrentIssue(bady), new Consumer<BaseHttpBean>() {
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
    /**
     * 天津11选5投注接口
     *
     * @param resultClick 结果回调
     */
    public static void TJ11_5CurrentIssue(String data, final OnResultClick<BaseHttpBean> resultClick) {
        RequestBody bady=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data);
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendNoRequest(mBusinessServer.TJ11_5CurrentIssue(bady), new Consumer<BaseHttpBean>() {
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
    /**
     * 江苏快3投注接口
     *
     * @param resultClick 结果回调
     */
    public static void JSK3CurrentIssue(String data, final OnResultClick<BaseHttpBean> resultClick) {
        RequestBody bady=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data);
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendNoRequest(mBusinessServer.JSK3CurrentIssue(bady), new Consumer<BaseHttpBean>() {
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

    /**
     * 投注记录
     *
     * @param type        1 全部，2 中奖，3待开奖，4 追号
     * @param p           页码
     * @param resultClick
     */
    public static void getBettingRecord(String type, int p, final OnResultListClick<BettingRecordBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequestList(mBusinessServer.GetBettingRecord(type, p), new Consumer<List<BettingRecordBean>>() {
            @Override
            public void accept(List<BettingRecordBean> bettingRecordBeen) throws Exception {
                resultClick.success(bettingRecordBeen);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }

    /**
     * 账户明细
     *
     * @param type        1全部，2投注，3充值，4提现，5派奖
     * @param p           页码
     * @param resultClick
     */
    public static void getAccountDetails(String type, int p, final OnResultListClick<AccountDetailsBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequestList(mBusinessServer.GetAccountDetails(type, p), new Consumer<List<AccountDetailsBean>>() {
            @Override
            public void accept(List<AccountDetailsBean> bettingRecordBeen) throws Exception {
                resultClick.success(bettingRecordBeen);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }

    /**
     * 订单详情
     *
     * @param oid         订单id
     * @param flag        追号期数
     * @param msg        如果是中奖信息查看详情，该值传1
     * @param resultClick
     */
    public static void getOrderDetails(String oid, String flag,int msg, final OnResultClick<OrderDetailsBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequest(mBusinessServer.GetOrderDetails(oid, flag,msg), new Consumer<BaseHttpBean<OrderDetailsBean>>() {
            @Override
            public void accept(BaseHttpBean<OrderDetailsBean> bean) throws Exception {
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
     * 取消追号(重庆时时彩)
     *
     * @param oid         订单id
     * @param type  1 重庆时时彩；2 山东11选5；3 PK10；4 新疆时时彩；5天津时时彩
     * @param resultClick
     */
    public static void cancelBills(String oid,String type, final OnResultClick resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendNoRequest(mBusinessServer.CancelBills(oid,type), new Consumer<BaseHttpBean>() {
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
     * 获取往期开奖号列表(重庆)
     *
     * @param resultClick
     */
    public static void GetCQBillHistory(final OnResultListClick<CQHistoryBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequestList(mBusinessServer.GetCQBillHistory(), new Consumer<List<CQHistoryBean>>() {
            @Override
            public void accept(List<CQHistoryBean> list) throws Exception {
                resultClick.success(list);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }
    /**
     * 获取往期开奖号列表（新疆）
     *
     * @param resultClick
     */
    public static void GetXJBillHistory(final OnResultListClick<CQHistoryBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequestList(mBusinessServer.GetXJBillHistory(), new Consumer<List<CQHistoryBean>>() {
            @Override
            public void accept(List<CQHistoryBean> list) throws Exception {
                resultClick.success(list);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }
    /**
     * 获取往期开奖号列表（天津）
     *
     * @param resultClick
     */
    public static void GetTJBillHistory(final OnResultListClick<CQHistoryBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequestList(mBusinessServer.GetTJBillHistory(), new Consumer<List<CQHistoryBean>>() {
            @Override
            public void accept(List<CQHistoryBean> list) throws Exception {
                resultClick.success(list);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }
    /**
     * 获取往期开奖号列表（山东11选5）
     *
     * @param resultClick
     */
    public static void GetSD11_5BillHistory(final OnResultListClick<CQHistoryBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequestList(mBusinessServer.GetSD11_5BillHistory(), new Consumer<List<CQHistoryBean>>() {
            @Override
            public void accept(List<CQHistoryBean> list) throws Exception {
                resultClick.success(list);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }
    /**
     * 获取往期开奖号列表（天津11选5）
     *
     * @param resultClick
     */
    public static void GetTJ11_5BillHistory(final OnResultListClick<CQHistoryBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequestList(mBusinessServer.GetTJ11_5BillHistory(), new Consumer<List<CQHistoryBean>>() {
            @Override
            public void accept(List<CQHistoryBean> list) throws Exception {
                resultClick.success(list);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }
    /**
     * 获取往期开奖号列表（江苏快3）
     *
     * @param resultClick
     */
    public static void GetJSK3BillHistory(final OnResultListClick<CQHistoryBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequestList(mBusinessServer.GetJSK3BillHistory(), new Consumer<List<CQHistoryBean>>() {
            @Override
            public void accept(List<CQHistoryBean> list) throws Exception {
                resultClick.success(list);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }
    /**
     * 16获取全部彩种开奖号
     *
     * @param resultClick
     */
    public static void GetAllLottery(final OnResultListClick<LotteryBean> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequestList(mBusinessServer.GetAllLottery(), new Consumer<List<LotteryBean>>() {
            @Override
            public void accept(List<LotteryBean> list) throws Exception {
                resultClick.success(list);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }

    /**
     * 遗漏号码接口
     *
     * @param ctype  彩种id
     * @param resultClick 从高位到低位
     */
    public static void GetLostNumber(int ctype, final OnResultListClick<List<String>> resultClick) {
        BusinessServer mBusinessServer = RxRetrofit.getInstance().create(BusinessServer.class);
        RxHttp.sendRequestList(mBusinessServer.GetLostNumber(ctype), new Consumer<List<List<String>>>() {
            @Override
            public void accept(List<List<String>> bettingRecordBeen) throws Exception {
                resultClick.success(bettingRecordBeen);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                resultClick.fail(throwable);
            }
        });
    }

}
