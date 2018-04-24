package com.jqscp.Util.RxHttp;

import android.content.Intent;


import com.jqscp.MyApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络拦截器
 */

public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
       /* //添加消息头
        Request request;
        if (MyApplication.ServerToken != null)
            request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    *//*.addHeader("HG_Server", MyApplication.ServerToken)
                    .addHeader("HG_Device", MyApplication.DeviceId)
                    .addHeader("HG_Type", "" + MyApplication.UserType)*//*
                    .build();
        else
            request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .addHeader("HG_Device", MyApplication.DeviceId)
                    .build();
        // try the request
        Response originalResponse = chain.proceed(request);
        int code = originalResponse.code();
        if (code == -200) {//根据和服务端的约定判断token过期
            MyApplication.mContext.startService(
                    new Intent(MyApplication.mContext, MyTokenService.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        return originalResponse;*/
        return chain.proceed(chain.request());
    }
}
