package com.jqscp.Server;

import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 用户信息接口
 */

public interface UserServer {
    //获取用户信息接口
    @GET("api/cqssc/v1/show")
    Flowable<BaseHttpBean<CurrentIssueBean>> GetCurrentIssue();
    //获取用户信息接口
    @FormUrlEncoded
    @POST("api/cqssc/v1/makebill")
    Flowable<BaseHttpBean> putCurrentIssue(@Field("bill") String bill, @Field("multiple") int multiple
            , @Field("ptype") int ptype, @Field("flag") int flag,@Field("sbill") String sbill);
}