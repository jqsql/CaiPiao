package com.jqscp.Util.RxHttp;


import com.jqscp.MyApplication;
import com.jqscp.Util.APPUtils.ALog;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求初始化操作类
 */

public class RxRetrofit {
    private static RxRetrofit mRxRetrofit;
    private Retrofit mRetrofit;
    //服务器地址
    private  static  String path= "http://code.310liao.com/";

    /**
     * 网络请求初始化配置
     */
    private RxRetrofit() {
        // log拦截器  打印所有的log
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                ALog.e("HTTP:"+message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // 创建 OKHttpClient
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//连接超时时间(s)
                .writeTimeout(10,TimeUnit.SECONDS)//写操作超时时间(s)
                .readTimeout(10,TimeUnit.SECONDS)//读操作超时时间(s)
                .addInterceptor(new HttpInterceptor())
                .addInterceptor(logInterceptor)
                .build();
        // 创建 Retrofit
        mRetrofit=new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(path)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 初始化
     */
    public static RxRetrofit getInstance(){
        if(mRxRetrofit==null){
            synchronized (RxRetrofit.class){
                if(mRxRetrofit==null){
                    mRxRetrofit=new RxRetrofit();
                }
            }
        }
        return mRxRetrofit;
    }

    public <T> T create(Class<T> tClass){
        return mRetrofit.create(tClass);
    }
}
