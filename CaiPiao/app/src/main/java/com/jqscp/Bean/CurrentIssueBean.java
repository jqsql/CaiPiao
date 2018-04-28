package com.jqscp.Bean;

/**
 * 当前期号
 */

public class CurrentIssueBean {
    private String qihao;//当前期号
    private String resulttime;//开奖时间
    private int counttime;//倒计时

    public String getQihao() {
        return qihao;
    }

    public void setQihao(String qihao) {
        this.qihao = qihao;
    }

    public String getResulttime() {
        return resulttime;
    }

    public void setResulttime(String resulttime) {
        this.resulttime = resulttime;
    }

    public int getCounttime() {
        return counttime;
    }

    public void setCounttime(int counttime) {
        this.counttime = counttime;
    }
}
