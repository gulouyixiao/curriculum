package com.curriculum.model.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 提交订单
 */
@Data
@ApiModel(description = "提交订单")
public class OrderParamsDTO {
	@ApiModelProperty(value = "购物车商品id集合")
	private List<Integer> cartIdList;

	@ApiModelProperty(value = "总价（沉余参数）")
	private String totalPrice;
}
