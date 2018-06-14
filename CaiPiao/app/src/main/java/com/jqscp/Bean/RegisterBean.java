package com.jqscp.Bean;

/**
 * 注册返回实体
 */

public class RegisterBean {
    private int id;//用户的 id
    private String token;//用户的token

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
