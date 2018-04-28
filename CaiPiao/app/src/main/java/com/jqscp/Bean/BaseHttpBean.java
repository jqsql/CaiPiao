package com.jqscp.Bean;

/**
 * 网络通用返回字段
 *
 */

public class BaseHttpBean<T>{
    private int code;//0表示成功,非0表示失败
    private T data;//数据
    private String msg;//Code不等于0时才存在,失败信息

    public int getCode() {
        return code;
    }

    public void setCode(int codes) {
        code = codes;
    }

    public T getData() {
        return data;
    }

    public void setData(T datas) {
        data = datas;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        msg = message;
    }

    @Override
    public String toString() {
        return "BaseHttpBean{" +
                "Code=" + code +
                ", Data=" + data +
                ", Message='" + msg + '\'' +
                '}';
    }
}
