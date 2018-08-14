package com.jqscp.Server;

import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.Bean.RegisterBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/5/11.
 */

public interface LoginServer {
    //用户注册接口
    @FormUrlEncoded
    @POST("api/appuser/v1/register")
    Flowable<BaseHttpBean<RegisterBean>> Register(@Field("mobile") String mobile, @Field("password") String password);
    //用户注册接口
    @FormUrlEncoded
    @POST("api/appuser/v1/login")
    Flowable<BaseHttpBean<RegisterBean>> Login(@Field("mobile") String mobile, @Field("password") String password);
    //忘记密码
    @FormUrlEncoded
    @POST("api/appuser/v1/forgetpasswd")
    Flowable<BaseHttpBean> ForgetPwd(@Field("account") String account, @Field("password") String password, @Field("code") String code);
}
