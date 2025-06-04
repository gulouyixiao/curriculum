package com.curriculum.constant;

/**
 * @author 陆向荣
 * @version 1.0
 * @description: 订单常量
 * @date 2025/6/4 13:42
 */
public interface OrderConstant {
    //支付
     String ALIPAY = "alipay";

     //默认订单标题
    String DEFAULT_ORDER_TITLE = "默认订单标题";

    String REQUEST_PAY_ERROR = "请求第三方支付错误";
    
    String TRADE_SUCCESS = "TRADE_SUCCESS";

    String TRADE_FALSE  = "TRADE_FALSE";

    //10 分钟
    String TIMEOUT_EXPRESS = "10m";

}
