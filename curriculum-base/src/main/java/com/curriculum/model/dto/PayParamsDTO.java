package com.curriculum.model.dto;


import lombok.Data;

@Data
public class PayParamsDTO {
	private Long orderId;
	private String orderNo;
	private String payType; // 支付的类型   wx:微信支付类型    alipay:支付宝类型
}
