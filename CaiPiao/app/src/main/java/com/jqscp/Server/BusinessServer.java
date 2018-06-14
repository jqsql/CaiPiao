package com.jqscp.Server;

import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.MyApplication;

import io.reactivex.Flowable;
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
    //获取当前期号
    @GET("api/cqssc/v1/show")
    Flowable<BaseHttpBean<CurrentIssueBean>> GetCurrentIssue();
    //投注接口
    @FormUrlEncoded
    @POST("api/cqssc/v1/makebill")
    Flowable<BaseHttpBean> putCurrentIssue(@Field("bill") String bill, @Field("multiple") int multiple
            , @Field("ptype") int ptype, @Field("flag") int flag, @Field("sbill") String sbill);
}
