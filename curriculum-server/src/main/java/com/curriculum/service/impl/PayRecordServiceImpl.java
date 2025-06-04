package com.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.exception.CurriculumException;
import com.curriculum.mapper.OrderMainMapper;
import com.curriculum.mapper.OrdersMapper;
import com.curriculum.mapper.PayRecordMapper;
import com.curriculum.model.dto.PayStatusDTO;
import com.curriculum.model.po.OrderMain;
import com.curriculum.model.po.PayRecord;
import com.curriculum.properties.AlipayProperties;
import com.curriculum.service.PayRecordService;
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
	private OrderMainMapper orderMainMapper;

	@Override
	public PayRecord createOrder(OrderMain orderMain, String payType) {
		// todo 业务逻辑还没更改，只是防止报错
//		if(orders == null){
//			CurriculumException.cast(MessageConstant.ORDER_NOT_FOUND);
//		}
//
//		if("601002".equals(orders.getStatus())){
//			CurriculumException.cast(MessageConstant.ORDER_PAID);
//		}
//
//		PayRecord payRecord = new PayRecord();
//		//生成支付交易
//		String payNo = SnowFlakeUtils.getInstance().createStringID(false);
//		payRecord.setPayNo(payNo);
//		payRecord.setOrderId(orders.getId());
//		payRecord.setOrderName(orders.getOrderName());
//		payRecord.setTotalPrice(orders.getTotalPrice());//实际金额
//		payRecord.setCurrency("CNY");
//		payRecord.setCreateDate(LocalDateTime.now());
//		payRecord.setStatus("600001");
//		payRecord.setUserId(orders.getUserId()); //todo 待完善
//		payRecord.setOutPayChannel(payType);
//		this.save(payRecord);
//
//		return payRecord;
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void savePayStatus(PayStatusDTO payStatusDto) {
		// todo 业务逻辑还没更改，只是防止报错

		//支付流水号
		String payNo = payStatusDto.getOut_trade_no();

		LambdaQueryWrapper<PayRecord> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(PayRecord::getPayNo,payNo);
		PayRecord payRecord = getOne(queryWrapper);

		if(payRecord == null){
			CurriculumException.cast("支付记录不存在");
		}
		//支付结果
		String trade_status = payStatusDto.getTrade_status();
		log.debug("收到支付结果:{},支付记录:{}}", payStatusDto.toString(),payRecord.toString());

		if("TRADE_SUCCESS".equals(trade_status)){
			//支付金额变为分
			Float totalPrice = payRecord.getTotalPrice() * 100;
			Float total_amount = Float.valueOf(payStatusDto.getTotal_amount()) * 100;
			//校验是否一致
			if (!payStatusDto.getApp_id().equals(alipayConfig.appId) || totalPrice.intValue() != total_amount.intValue()) {
				//校验失败
				log.info("校验支付结果失败,支付记录:{},APP_ID:{},totalPrice:{}" ,payRecord.toString(),payStatusDto.getApp_id(),total_amount.intValue());
				CurriculumException.cast("校验支付结果失败");
			}
			log.debug("更新支付结果,支付交易流水号:{},支付结果:{}", payNo, trade_status);
			PayRecord payRecord_u = new PayRecord();
			payRecord_u.setStatus("2");//支付成功
			payRecord_u.setOutPayNo(payStatusDto.getTrade_no());//支付宝交易号
			payRecord_u.setPaySuccessTime(LocalDateTime.now());//通知时间
			int update = payRecordMapper.update(payRecord_u,new LambdaUpdateWrapper<PayRecord>().eq(PayRecord::getPayNo,payNo));
			if(update > 0){
				log.info("更新支付记录状态成功:{}", payRecord_u.toString());
			}else {
				log.info("更新支付记录状态失败:{}", payRecord_u.toString());
				CurriculumException.cast("更新支付记录状态失败");
			}
			//关联的订单号
			Long orderId = payRecord.getOrderId();
			OrderMain orders = orderMainMapper.selectById(orderId);
			if (orders == null) {
				log.info("根据支付记录[{}}]找不到订单", payRecord_u.toString());
				CurriculumException.cast("根据支付记录找不到订单");
			}
			orders.setStatus("600002");//支付成功
			int b = orderMainMapper.updateById(orders);
			if(b > 0){
				log.info("更新订单表状态成功,订单号:{}", orderId);
			}else {
				log.info("更新订单表状态失败,订单号:{}", orderId);
				CurriculumException.cast("更新订单表状态失败");
			}
		}
	}
}
