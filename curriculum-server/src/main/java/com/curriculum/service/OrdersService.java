package com.curriculum.service;

import com.curriculum.model.dto.OrderParamsDTO;
import com.curriculum.model.dto.OrderSubmitDTO;
import com.curriculum.model.po.PayRecord;
import com.curriculum.model.vo.QrcodeVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单表 服务类
 */
public interface OrdersService{

	/**
	 * 支付结果通知
	 * @param request
	 */
	void receiveNotify(HttpServletRequest request, String PayType);

	/**
	 * 查询支付结果
	 * @param payNo
	 * @return
	 */
	PayRecord queryPayResult(String payNo);

	/**
	 * 生成下单支付二维码
	 * @return
	 */
	QrcodeVO createCode(OrderSubmitDTO orderSubmitDTO);

	/**
	 * 直接支付生成的支付二维码
	 * @return 结果
	 */
	QrcodeVO directCreateCode(OrderParamsDTO orderParamsDTO);

}
