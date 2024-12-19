package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.dto.PayStatusDTO;
import com.curriculum.model.po.Orders;
import com.curriculum.model.po.PayRecord;

/**
 * <p>
 * 支付记录表 服务类
 * </p>
 *
 * @author gulouyixiao
 * @since 2024-12-14
 */
public interface PayRecordService extends IService<PayRecord> {

	/**
	 * 生成支付记录
	 * @param orders
	 * @return
	 */
	PayRecord createOrder(Orders orders, String payType);

	/**
	 * @description 保存支付结果
	 * @param payStatusDto  支付结果信息
	 * @return void
	 */
	void savePayStatus(PayStatusDTO payStatusDto);

}
