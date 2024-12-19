package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.dto.OrderParamsDTO;
import com.curriculum.model.dto.PayParamsDTO;
import com.curriculum.model.po.Orders;
import com.curriculum.model.po.PayRecord;
import com.curriculum.model.vo.QrcodeVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 订单表 服务类
 */
public interface OrdersService extends IService<Orders> {

	/**
	 * 支付结果通知
	 * @param request
	 */
	Boolean receiveNotify(HttpServletRequest request, String PayType);

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
