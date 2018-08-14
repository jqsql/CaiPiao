package com.jqscp.Server;

import com.jqscp.Bean.AccountDetailsBean;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.BettingRecordBean;
import com.jqscp.Bean.CQHistoryBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.Bean.LotteryBean;
import com.jqscp.Bean.OrderDetailsBean;
import com.jqscp.MyApplication;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 业务逻辑
 */

public interface BusinessServer {
    //重庆时时彩获取当前期号
    @GET("api/cqssc/v1/show")
    Flowable<BaseHttpBean<CurrentIssueBean>> GetCurrentIssue();

    //新疆时时彩获取当前期号
    @GET("api/xjssc/v1/show")
    Flowable<BaseHttpBean<CurrentIssueBean>> GetXJCurrentIssue();

    //天津时时彩获取当前期号
    @GET("api/tjssc/v1/show")
    Flowable<BaseHttpBean<CurrentIssueBean>> GetTJCurrentIssue();

    //山东11选5获取当前期号
    @GET("api/sd11x5/v1/show")
    Flowable<BaseHttpBean<CurrentIssueBean>> GetSD11_5CurrentIssue();

    //山东11选5获取当前期号
    @GET("api/tj11x5/v1/show")
    Flowable<BaseHttpBean<CurrentIssueBean>> GetTJ11_5CurrentIssue();

    //江苏快3获取当前期号
    @GET("api/jsksan/v1/ksshow")
    Flowable<BaseHttpBean<CurrentIssueBean>> GetJSK3CurrentIssue();

    //重庆时时彩投注接口
    @POST("api/cqssc/v1/makebill")
    Flowable<BaseHttpBean> putCurrentIssue(@Body RequestBody data);

    //新疆时时彩投注接口
    @POST("api/xjssc/v1/makebill")
    Flowable<BaseHttpBean> putXJCurrentIssue(@Body RequestBody data);

    //天津时时彩投注接口
    @POST("api/tjssc/v1/makebill")
    Flowable<BaseHttpBean> putTJCurrentIssue(@Body RequestBody data);

    //PK10彩投注接口
    @POST("api/pkten/v1/pkten")
    Flowable<BaseHttpBean> Pk10CurrentIssue(@Body RequestBody data);

    //山东11选5彩投注接口
    @POST("api/sd11x5/v1/makebill")
    Flowable<BaseHttpBean> SD11_5CurrentIssue(@Body RequestBody data);

    //天津11选5彩投注接口
    @POST("api/tj11x5/v1/makebill")
    Flowable<BaseHttpBean> TJ11_5CurrentIssue(@Body RequestBody data);

    //江苏快3投注接口
    @POST("api/jsksan/v1/ksten")
    Flowable<BaseHttpBean> JSK3CurrentIssue(@Body RequestBody data);

    //投注记录
    @GET("api/appuser/v1/makebillrecord")
    Flowable<BaseHttpBean<List<BettingRecordBean>>> GetBettingRecord(@Query("type") String type, @Query("p") int p);

    //账户明细
    @GET("api/appuser/v1/capitaldetails")
    Flowable<BaseHttpBean<List<AccountDetailsBean>>> GetAccountDetails(@Query("type") String type, @Query("p") int p);

    //订单详情
    @GET("api/appuser/v1/orderdetail")
    Flowable<BaseHttpBean<OrderDetailsBean>> GetOrderDetails(@Query("oid") String oid, @Query("flag") String flag, @Query("msg") int msg);

    //取消追号
    @GET("api/appuser/v1/cancelbills")
    Flowable<BaseHttpBean> CancelBills(@Query("oid") String oid,@Query("ctype") String ctype);


    //获取往期开奖号列表
    @GET("api/cqssc/v1/oldbills")
    Flowable<BaseHttpBean<List<CQHistoryBean>>> GetCQBillHistory();
    //获取新疆往期开奖号列表
    @GET("api/xjssc/v1/oldbills")
    Flowable<BaseHttpBean<List<CQHistoryBean>>> GetXJBillHistory();
    //获取天津往期开奖号列表
    @GET("api/xjssc/v1/oldbills")
    Flowable<BaseHttpBean<List<CQHistoryBean>>> GetTJBillHistory();
    //获取天津往期开奖号列表
    @GET("api/jsksan/v1/oldbills")
    Flowable<BaseHttpBean<List<CQHistoryBean>>> GetJSK3BillHistory();
    //获取山东11选5开奖号列表
    @GET("api/sd11x5/v1/oldbills")
    Flowable<BaseHttpBean<List<CQHistoryBean>>> GetSD11_5BillHistory();
    //获取天津往期开奖号列表
    @GET("api/tj11x5/v1/oldbills")
    Flowable<BaseHttpBean<List<CQHistoryBean>>> GetTJ11_5BillHistory();

    //16获取全部彩种开奖号
    @GET("api/appuser/v1/billcodelists")
    Flowable<BaseHttpBean<List<LotteryBean>>> GetAllLottery();

    //遗漏号码接口
    @GET("api/appuser/v1/yilou")
    Flowable<BaseHttpBean<List<List<String>>>> GetLostNumber(@Query("ctype") int ctype);
}
