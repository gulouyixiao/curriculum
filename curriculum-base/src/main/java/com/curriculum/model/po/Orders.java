package com.curriculum.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author gulouyixiao
 */
@Data
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private Long id;

    /**
     * 总价
     */
    private Float totalPrice;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 交易状态
     */
    private String status;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单类型 数据字典中604
     */
    private String orderType;

    /**
     * 具体商品id
     */
    private Long outBusinessId;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 订单描述
     */
    private String orderDescrip;

    /**
     * 单价
     */
    private Float unitPrice;

    /**
     * 数量
     */
    private Integer orderNumber;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;


}
