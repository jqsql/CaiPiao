package com.jqscp.Dao;


/**
 * 有返回值的
 */

public interface OnNoResultClick {
    void success();
    void fail(Throwable throwable);
}
