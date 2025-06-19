package com.curriculum.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 陆向荣
 * @version 1.0
 * @description: TODO
 * @date 2025/6/11 15:02
 */
@Data
@ApiModel(description = "生成订单二维码参数")
public class OrderParamsDTO {
    @ApiModelProperty(value = "商品类型 01-acgn,02-surrounding")
    private String orderType;
    @ApiModelProperty(value = "商品id")
    private Long id;
    @ApiModelProperty(value = "数量")
    private Integer number;
    @ApiModelProperty(value = "价格")
    private Double price;
}