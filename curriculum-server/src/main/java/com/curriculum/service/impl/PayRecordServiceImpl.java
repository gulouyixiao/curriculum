package com.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.constant.MessageConstant;
import com.curriculum.constant.OrderConstant;
import com.curriculum.enums.OrderStatusEnum;
import com.curriculum.exception.CurriculumException;
import com.curriculum.mapper.PayRecordMapper;
import com.curriculum.model.dto.PayStatusDTO;
import com.curriculum.model.po.OrderMain;
import com.curriculum.model.po.PayRecord;
import com.curriculum.properties.AlipayProperties;
import com.curriculum.service.OrderMainService;
import com.curriculum.service.PayRecordService;
import com.curriculum.utils.SnowFlakeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 支付记录表 服务实现类
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@Service
public class PayRecordServiceImpl extends ServiceImpl<PayRecordMapper, PayRecord> implements PayRecordService {

	@Autowired
	private AlipayProperties alipayConfig;

	@Autowired
	private PayRecordMapper payRecordMapper;

	@Autowired
	private OrderMainService orderMainService;

	@Override
	public PayRecord createOrder(OrderMain orderMain, String payType) {
		if(OrderStatusEnum.PAID.getCode().equals(orderMain.getStatus())){
			CurriculumException.cast(MessageConstant.ORDER_PAID);
		}

		if(!OrderStatusEnum.UNPAID.getCode().equals(orderMain.getStatus())){
			CurriculumException.cast(MessageConstant.ORDER_STATUS_ERROR);
		}
		PayRecord payRecord = new PayRecord();
		//生成支付交易
		String payNo = SnowFlakeUtils.getInstance().nextStringID(false);
		payRecord.setPayNo(payNo);
		payRecord.setOrderId(orderMain.getId());
		payRecord.setOrderName(orderMain.getOrderName());
		payRecord.setTotalPrice(orderMain.getTotalPrice());//实际金额
		payRecord.setCurrency("CNY");
		payRecord.setCreateDate(LocalDateTime.now());
		payRecord.setStatus(OrderStatusEnum.UNPAID.getCode());
		payRecord.setUserId(orderMain.getUserId());
		payRecord.setOutPayChannel(payType);
		this.save(payRecord);

		return payRecord;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void savePayStatus(PayStatusDTO payStatusDto) {
		//支付流水号
		String payNo = payStatusDto.getOut_trade_no();

		LambdaQueryWrapper<PayRecord> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(PayRecord::getPayNo,payNo);
		PayRecord payRecord = getOne(queryWrapper);
		if(payRecord == null){
			CurriculumException.cast(MessageConstant.PAY_RECORD_NOT_FOUND);
		}

		//支付结果
		String trade_status = payStatusDto.getTrade_status();
		log.debug("收到支付结果:{},支付记录:{}}", payStatusDto, payRecord);

		if(OrderConstant.TRADE_SUCCESS.equals(trade_status)){
			//支付金额变为分
			Double totalPrice = payRecord.getTotalPrice() * 100;
			Double total_amount = Double.valueOf(payStatusDto.getTotal_amount()) * 100;
			//校验是否一致
			if (!payStatusDto.getApp_id().equals(alipayConfig.appId) || totalPrice.intValue() != total_amount.intValue()) {
				//校验失败
				log.info("校验支付结果失败,支付记录:{},APP_ID:{},totalPrice:{}" ,payRecord.toString(),payStatusDto.getApp_id(),total_amount.intValue());
				CurriculumException.cast(MessageConstant.FAILED_VERIFY_PAY_RESULT);
			}
			log.debug("更新支付结果,支付交易流水号:{},支付结果:{}", payNo, trade_status);
			PayRecord payRecord_u = new PayRecord();
			payRecord_u.setStatus(OrderStatusEnum.PAID.getCode());//支付成功
			payRecord_u.setOutPayNo(payStatusDto.getTrade_no());//支付宝交易号
			payRecord_u.setPaySuccessTime(LocalDateTime.now());//通知时间
			payRecordMapper.update(payRecord_u,new LambdaUpdateWrapper<PayRecord>().eq(PayRecord::getPayNo,payNo));
			//关联的订单号
			Long orderId = payRecord.getOrderId();
			OrderMain orderMain = orderMainService.getById(orderId);
			if (orderMain == null) {
				log.info("根据支付记录[{}}]找不到订单", payRecord_u);
				CurriculumException.cast("根据支付记录找不到订单");
			}
			orderMain.setStatus(OrderStatusEnum.PAID.getCode());//支付成功
			orderMainService.updateOrderStatus(orderMain);
		}
	}
}
