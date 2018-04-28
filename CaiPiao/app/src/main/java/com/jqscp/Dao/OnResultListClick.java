package com.jqscp.Dao;


import java.util.List;

/**
 * 有返回值的
 */

public interface OnResultListClick<T> {
    void success(List<T> list);
    void fail(Throwable throwable);
}
