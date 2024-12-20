package com.curriculum.model.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 生成订单二维码参数
 */
@Data
@ApiModel(description = "生成订单二维码参数")
public class OrderParamsDTO {
	@ApiModelProperty(value = "商品类型 acgn,surrounding")
	private String orderType;
	@ApiModelProperty(value = "商品id")
	private Long id;
	@ApiModelProperty(value = "数量")
	private Integer number;
	@ApiModelProperty(value = "价格")
	private Float price;
}
