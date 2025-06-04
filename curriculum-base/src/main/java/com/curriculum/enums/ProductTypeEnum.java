package com.curriculum.enums;

import lombok.Getter;

/**
 * @author 陆向荣
 * @version 1.0
 * @description: 商品类型枚举
 * @date 2025/6/4 9:37
 */
@Getter
public enum ProductTypeEnum {
    PERIPHERAL("01", "周边"),
    EXPO_PERFORMANCE("02", "漫展演出");

    private final String code;
    private final String desc;

    ProductTypeEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public static ProductTypeEnum fromCode(String code) {
        for (ProductTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
