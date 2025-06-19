package com.curriculum.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 陆向荣
 * @version 1.0
 * @descriptio 商品
 * @date 2025/6/18 9:29
 */
@Data
public class Products {
    /**
     * 商品名称
     */
    private String name;

    /**
     * 图片
     */
    private String pic;


    /**
     * 单价
     */
    private Double price;
    /**
     * 审核状态
     */
    private String auditStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;



}
