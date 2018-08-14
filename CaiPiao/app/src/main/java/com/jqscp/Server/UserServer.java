package com.jqscp.Server;

import com.jqscp.Bean.AccountMoneyBean;
import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;
import com.jqscp.Bean.UserInfo;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 用户信息接口
 */

public interface UserServer {
    //获取个人信息
    @GET("api/appuser/v1/selfcenter")
    Flowable<BaseHttpBean<UserInfo>> GetUserInfo();
    //获取用户余额/验证余额是否充足
    @GET("api/appuser/v1/checkmoney")
    Flowable<BaseHttpBean<AccountMoneyBean>> GetOrCheckMoney(@Query("money") double money);
    //更改昵称
    @FormUrlEncoded
    @POST("api/appuser/v1/editnickname")
    Flowable<BaseHttpBean> SetUserName(@Field("nickname") String name);
    //设置提款密码
    @FormUrlEncoded
    @POST("api/appuser/v1/withdrawpwd")
    Flowable<BaseHttpBean> SetWithDrawPwd(@Field("withdrawpwd") String withdrawpwd,@Field("code") String code);
    //修改登录密码
    @FormUrlEncoded
    @POST("api/appuser/v1/editpassword")
    Flowable<BaseHttpBean> EditPassword(@Field("oldpwd") String oldpwd,@Field("newpwd") String newpwd);


}
