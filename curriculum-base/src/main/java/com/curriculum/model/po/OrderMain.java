package com.curriculum.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * <p>
 * 订单主表
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
@Getter
@Setter
@ToString
@TableName("order_main")
public class OrderMain implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主订单号
     */
    @TableId("id")
    private Long id;

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
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 订单名称
     */
    @TableField("order_name")
    private String orderName;

    /**
     * 订单描述
     */
    @TableField("order_descrip")
    private String orderDescrip;

    /**
     * 支付时间
     */
    @TableField("pay_time")
    private LocalDateTime payTime;
}
