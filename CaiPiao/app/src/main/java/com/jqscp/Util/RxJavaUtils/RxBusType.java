package com.jqscp.Util.RxJavaUtils;

/**
 * RxBus 通知消息类型
 */

public enum RxBusType {
    Third_Login,//微信回调
    Goods_Shelf,//通知主页显示货架界面
    Pay_Util,//通知支付界面的切换
    Cart_Number,//通知购物车内容变化
    Cart_NumberShow,//通知购物车下面的数字变化
    Cart_Number_UpData,//通知购物车重新获取
    Shelf_Number_Zero,//通知货架在购物车内删除了某样商品
    Shelf_Number,//通知货架购物车内容发生了变化
    Home_ProductsClass,//通知主界面显示体验店商品分类界面
    Mine_Fragment,//通知主界面显示我的页面
    Home_GoShopping,//通知主界面显示继续购物界面
    Home_Ordering,//通知主界面显示订单界面
    Shopping_Suc,//通知支付成功
    Shopping_Suc_Flag,//通知支付成功,货架清空数据
}
