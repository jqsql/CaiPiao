package com.jqscp.Bean;

/**
 * 网络通用返回字段
 *
 */

public class BaseHttpBean<T>{
    private int Code;//0表示成功,非0表示失败
    private T Data;//数据
    private String Message;//Code不等于0时才存在,失败信息

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public String toString() {
        return "BaseHttpBean{" +
                "Code=" + Code +
                ", Data=" + Data +
                ", Message='" + Message + '\'' +
                '}';
    }
}
