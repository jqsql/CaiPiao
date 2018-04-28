package com.jqscp.Dao;


import com.jqscp.Bean.BaseHttpBean;

/**
 * 有返回值的
 */

public interface OnResultClick<T> {
    void success(BaseHttpBean<T> bean);
    void fail(Throwable throwable);
}
