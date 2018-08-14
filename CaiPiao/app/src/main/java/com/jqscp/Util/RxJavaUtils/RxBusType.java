package com.jqscp.Util.RxJavaUtils;

/**
 * RxBus 通知消息类型
 */

public enum RxBusType {
    LostNumber_Notice,//通知遗漏号码
    Shake_Play,//通知摇一摇选取号码
    Shake_11_5Play,//通知摇一摇选取号码
    Shake_K3Play,//通知摇一摇选取号码
    JustLoginOut,//通知退出登录、用户信息有变化
    JustMoney,//通知金钱有变化
    JustLoginIn,//通知登录成功
    UserInfo,//用户基本信息变化
    ToMainHome,//通知移动到某一部分
}
