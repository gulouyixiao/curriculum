package com.curriculum.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * <p>
 * 订单子表
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
@TableName("orders_detail")
@Data
public class OrdersDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @TableId("id")
    private Long id;

    /**
     * 主订单号
     */
    @TableField("main_id")
    private Long mainId;

    /**
     * 总价
     */
    @TableField("total_price")
    private Double totalPrice;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 交易状态
     */
    @TableField("status")
    private String status;

    /**
     * 商品类型：01：周边，02：漫展演出
     */
    @TableField("shopping_type")
    private String shoppingType;

    /**
     * 具体商品id
     */
    @TableField("out_business_id")
    private Long outBusinessId;

    /**
     * 商品名称
     */
    @TableField("name")
    private String name;

    /**
     * 商品描述
     */
    @TableField("descrip")
    private String descrip;

    /**
     * 单价
     */
    @TableField("unit_price")
    private Double unitPrice;

    /**
     * 数量
     */
    @TableField("order_number")
    private Integer orderNumber;

    /**
     * 支付时间
     */
    @TableField("pay_time")
    private LocalDateTime payTime;
}
