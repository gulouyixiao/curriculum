package com.curriculum.model.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
public class OrderDetailVO {

    private static final long serialVersionUID = 1L;

    private Long id; // 子订单id
    private String payNo; // 交易编号
    private String cellphone; // 手机号
    private String pic; // 图片
    private String status; // 交易状态
    private String nickname; // 昵称
    private String orderNumber; // 数量
    private String unitPrice; // 单价
    private String shoppingType; // 商品类型：01：周边，02：漫展演出
    private String name; // 名称
    private LocalDateTime createDate; // 创建时间
    private Double totalPrice; // 总计
}
