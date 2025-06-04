package com.curriculum.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 陆向荣
 * @version 1.0
 * @description: 购物车商品
 * @date 2025/6/4 9:48
 */
@Data
public class ShoppingCartVO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 图片
     */
    private String pic;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long shoppingId;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 单价
     */
    private Double price;

    /**
     * 会员价
     */
    private Double vipPrice;

    /**
     * 商品类型：01：周边，02：漫展演出
     */
    private String shoppingType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 当前商品实际价格
     */
    private Double actualPrice;
}
