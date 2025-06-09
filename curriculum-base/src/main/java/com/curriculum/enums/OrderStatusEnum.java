package com.curriculum.enums;

import lombok.Getter;

/**
 * @author 陆向荣
 * @version 1.0
 * @description: 订单状态枚举类
 * @date 2025/6/4 9:32
 */
@Getter
public enum OrderStatusEnum {
    UNPAID("600001", "待支付"),
    PAID("600002", "已支付"),
    CLOSED("600003", "超时结束"),
    REFUNDED("600004", "已退款"),
    COMPLETED("600005", "待退款");

    private final String code;
    private final String desc;

    OrderStatusEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public static OrderStatusEnum fromCode(String code) {
        for (OrderStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
