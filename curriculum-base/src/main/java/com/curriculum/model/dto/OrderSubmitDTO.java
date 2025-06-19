package com.curriculum.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 提交订单
 */
@Data
@ApiModel(description = "提交订单")
public class OrderSubmitDTO {
	@ApiModelProperty(value = "购物车商品id集合")
	@NotEmpty(message = "购物车商品id集合不能为空")
	private List<Long> cartIdList;

	@ApiModelProperty(value = "总价（沉余参数）")
	private String totalPrice;
}
