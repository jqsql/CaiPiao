package com.jqscp.Util.RxHttp;

import android.content.Intent;

import com.jqscp.ConfigConsts;
import com.jqscp.MyApplication;
import com.jqscp.Service.MyTokenService;
import com.jqscp.Util.APPUtils.ALog;
import com.jqscp.Util.APPUtils.Sharedpreferences_Utils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络拦截器
 */

public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //添加消息头
        Request request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .build();

       /* if (MyApplication.ServerToken != null)
            request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    //.addHeader("token", MyApplication.ServerToken)
                    //.addHeader("HG_Device", MyApplication.DeviceId)
                    //.addHeader("HG_Type", "" + MyApplication.UserType)
                    .build();
        else
            request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    //.addHeader("HG_Device", MyApplication.DeviceId)
                    .build();*/

        RequestBody body = request.body();
        HttpUrl url = request.url();
        //if(body!=null) {
        //RequestBody newBody = null;
        if(url.url().toString().contains("?")) {
            url = HttpUrl.parse(url.url().toString() + "&token=" + ConfigConsts.ServerToken);
        }else {
            url = HttpUrl.parse(url.url().toString() + "?token=" + ConfigConsts.ServerToken);
        }
           /* if (body instanceof FormBody) {
                url=HttpUrl.parse(url.url().toString()+"?token="+MyApplication.ServerToken);
                //newBody = addParamsToFormBody((FormBody) body);
                newBody = body;
            } else if (body instanceof MultipartBody) {
                newBody = addParamsToMultipartBody((MultipartBody) body);
            }*/

        //if (newBody!=null) {
        Request newRequest = request.newBuilder()
                .url(url)
                .method(request.method(), body)
                .build();
        //return chain.proceed(newRequest);
        // }
        //}
        // try the request
        Response originalResponse = chain.proceed(newRequest);
        int code = originalResponse.code();
        if (code == 999 && ConfigConsts.isLogin && Sharedpreferences_Utils.getInstance(MyApplication.getMContext()).getBoolean("isUserLogin")) {
            //根据和服务端的约定判断token过期
            MyApplication.getMContext().startService(
                    new Intent(MyApplication.getMContext(), MyTokenService.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        return originalResponse;
    }

    /**
     * 为MultipartBody类型请求体添加参数
     *
     * @param body
     * @return
     */
    private MultipartBody addParamsToMultipartBody(MultipartBody body) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        //添加自定义携带参数
        String data = ConfigConsts.ServerToken;
        builder.addFormDataPart("token", data);
        //添加原请求体
        for (int i = 0; i < body.size(); i++) {
            builder.addPart(body.part(i));
        }
        return builder.build();
    }

    /**
     * 为FormBody类型请求体添加参数
     *
     * @param body
     * @return
     */
    private FormBody addParamsToFormBody(FormBody body) {
        FormBody.Builder builder = new FormBody.Builder();
        //添加自定义携带参数
        String data = ConfigConsts.ServerToken;
        builder.add("token", data);
        //添加原请求体
        for (int i = 0; i < body.size(); i++) {
            builder.addEncoded(body.encodedName(i), body.encodedValue(i));
        }

        return builder.build();
    }
}
