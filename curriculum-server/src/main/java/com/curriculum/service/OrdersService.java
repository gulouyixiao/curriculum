package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.dto.OrderParamsDTO;
import com.curriculum.model.po.OrderMain;
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
	 * 生成支付二维码
	 * @return
	 */
	QrcodeVO createCode(OrderParamsDTO orderParamsDTO);
}
