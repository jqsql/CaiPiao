package com.jqscp.Server;

import com.jqscp.Bean.BaseHttpBean;
import com.jqscp.Bean.CurrentIssueBean;

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

    //获取用户余额/验证余额是否充足
    @GET("api/appuser/v1/checkmoney")
    Flowable<BaseHttpBean> GetOrCheckMoney(@Query("money") double money);

}
