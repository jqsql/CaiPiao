package com.jqscp.Util.RxHttp;
import com.jqscp.Bean.BaseHttpBean;

import org.reactivestreams.Publisher;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 网络封装请求工具类
 */

public class RxHttp {
    /**
     * 基本封装（io线程-----APP程序主线程）
     * @param flowables
     * @param response
     */
    public  static void sendNoRequest(Flowable<BaseHttpBean> flowables, Consumer<BaseHttpBean> response, Consumer<Throwable> fail){
        flowables.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response,fail);
    }
    /**
     * 基本封装（io线程-----APP程序主线程）
     * @param flowables
     * @param response
     */
    public  static <T> void sendRequest(Flowable<BaseHttpBean<T>> flowables, Consumer<BaseHttpBean<T>> response, Consumer<Throwable> fail){
        flowables.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response,fail);
    }
    /**
     * 基本封装获取list（io线程-----APP程序主线程）
     * @param flowables
     * @param response
     */
    public static <T> void sendRequestList(Flowable<BaseHttpBean<T>> flowables, Consumer<T> response, Consumer<Throwable> fail){
        flowables.filter(new Predicate<BaseHttpBean<T>>() {
                    @Override
                    public boolean test(BaseHttpBean bean) throws Exception {
                        return bean.getCode()==0;
                    }
                })
                .flatMap(new Function<BaseHttpBean, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(BaseHttpBean bean) throws Exception {
                        return (Publisher<T>) Flowable.fromArray(bean.getData());
                    }
                 }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response,fail);
    }
}
