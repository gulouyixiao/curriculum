package com.curriculum.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 购物车
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
@Getter
@Setter
@ToString
@TableName("shopping_cart")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    @TableField("name")
    private String name;

    /**
     * 图片
     */
    @TableField("pic")
    private String pic;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 商品id
     */
    @TableField("shopping_id")
    private Long shoppingId;

    /**
     * 数量
     */
    @TableField("number")
    private Integer number;

    /**
     * 单价
     */
    @TableField("price")
    private Double price;

    /**
     * 会员价
     */
    @TableField("vip_price")
    private Double vipPrice;

    /**
     * 商品类型：01：周边，02：漫展演出
     */
    @TableField("shopping_type")
    private String shoppingType;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    @TableField("Title")
    private String Title;
}
