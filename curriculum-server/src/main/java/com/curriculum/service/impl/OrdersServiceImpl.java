package com.curriculum.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.constant.MessageConstant;
import com.curriculum.constant.OrderConstant;
import com.curriculum.context.AuthenticationContext;
import com.curriculum.exception.CurriculumException;
import com.curriculum.mapper.OrderMainMapper;
import com.curriculum.mapper.OrdersMapper;
import com.curriculum.model.dto.OrderDTO;
import com.curriculum.model.dto.OrderParamsDTO;
import com.curriculum.model.dto.PayStatusDTO;
import com.curriculum.model.po.*;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.QrcodeVO;
import com.curriculum.model.vo.ShoppingCartVO;
import com.curriculum.properties.AlipayProperties;
import com.curriculum.service.*;
import com.curriculum.utils.EncryptUtil;
import com.curriculum.utils.QrCodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 订单表 服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, OrderMain> implements OrdersService {
	private final PayRecordService payRecordService;
	private final AlipayProperties alipayConfig;
	private final UserService userService;
	private final ShoppingCartService shoppingCartService;
	private final OrderMainService orderMainService;
	private final OrderMainMapper orderMainMapper;


	/**
	 * 查询支付结果
	 *
	 * @param payNo
	 * @return
	 */
	@Override
	public PayRecord queryPayResult(String payNo) {
		LambdaQueryWrapper<PayRecord> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(PayRecord::getPayNo, payNo);
		PayRecord payRecord = payRecordService.getOne(queryWrapper);
		if (payRecord == null) {
			CurriculumException.cast("请重新点击支付");
		}
		return queryPayResult(payRecord);
	}

	/**
	 * 生成支付二维码
	 *
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public QrcodeVO createCode(OrderParamsDTO orderParamsDTO) {
		// 1.获取用户信息
		Long userId = AuthenticationContext.getContext();
		User user = userService.getById(userId);

		// 2.获取当前选中购物车商品信息
		List<Long> cartIdList = orderParamsDTO.getCartIdList();
		List<ShoppingCartVO> shoppingCartVOList = shoppingCartService.listShoppingCartByIds(cartIdList);
		if(CollUtil.isEmpty(shoppingCartVOList)){
			CurriculumException.cast(MessageConstant.GOODS_NOT_FOUND);
		}
		// 3.提交订单
		OrderMain orderMain = orderMainService.submitOrder(shoppingCartVOList, user);
		// 4.生成支付记录
		PayRecord payRecord = payRecordService.createOrder(orderMain, OrderConstant.ALIPAY);
		// 5.请求第三方下单接口，获取下单链接
		String payUrl = requestPay(payRecord);
		// 6.生成支付二维码，封装结果返回
        return createPayQrcode(payUrl, payRecord);
	}

	/**
	 * 支付通知接口
	 *
	 * @param request
	 */
	@Override
	public void receiveNotify(HttpServletRequest request, String payType) {
		receiveNotify(request);
	}



	/**
	 * 请求支付宝查询支付结果
	 *
	 * @param payRecord
	 * @return 支付记录信息
	 */
	public PayRecord queryPayResult(PayRecord payRecord) {
		//支付状态
		String status = payRecord.getStatus();
		//如果支付成功直接返回
		if ("600002".equals(status)) {
			return payRecord;
		}
		//从支付宝查询支付结果
		PayStatusDTO payStatusDto = queryPayResultFromAlipay(String.valueOf(payRecord.getPayNo()));
		//保存支付结果
		payRecordService.savePayStatus(payStatusDto);
		//重新查询支付记录
		LambdaQueryWrapper<PayRecord> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(PayRecord::getPayNo, payRecord.getPayNo());
		payRecord = payRecordService.getOne(queryWrapper);
		return payRecord;
	}

	/**
	 * 请求支付宝查询支付结果
	 *
	 * @param payNo 支付交易号
	 * @return 支付结果
	 */
	public PayStatusDTO queryPayResultFromAlipay(String payNo) {
		//========请求支付宝查询支付结果=============
		AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.url, alipayConfig.appId, alipayConfig.appPrivateKey, "json", alipayConfig.charset, alipayConfig.alipayPublicKey, alipayConfig.signType); //获得初始化的AlipayClient
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no", payNo);
		request.setBizContent(bizContent.toString());
		AlipayTradeQueryResponse response = null;
		try {
			response = alipayClient.execute(request);
			if (!response.isSuccess()) {
				CurriculumException.cast("请求支付查询查询失败");
			}
		} catch (AlipayApiException e) {
			log.error("请求支付宝查询支付结果异常:{}", e.toString(), e);
			CurriculumException.cast("请求支付查询查询失败");
		}

		//获取支付结果
		String resultJson = response.getBody();

		//转map
		Map resultMap = JSON.parseObject(resultJson, Map.class);
		Map alipay_trade_query_response = (Map) resultMap.get("alipay_trade_query_response");


		//支付结果
		String trade_status = (String) alipay_trade_query_response.get("trade_status");
		String total_amount = (String) alipay_trade_query_response.get("total_amount");
		String trade_no = (String) alipay_trade_query_response.get("trade_no");
		//保存支付结果
		PayStatusDTO payStatusDto = new PayStatusDTO();
		payStatusDto.setOut_trade_no(payNo);
		payStatusDto.setTrade_status(trade_status);
		payStatusDto.setApp_id(alipayConfig.appId);
		payStatusDto.setTrade_no(trade_no);
		payStatusDto.setTotal_amount(total_amount);
		payStatusDto.setPay_method(2);
		return payStatusDto;
	}


	/**
	 * 支付结果通知统一方法
	 *
	 * @param request
	 */
	private Boolean receiveNotify(HttpServletRequest request) {
		Boolean result = false;

		Map<String, String> params = new HashMap<>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		//验签
		boolean verify_result = false;

		try {
			verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.alipayPublicKey, alipayConfig.charset, alipayConfig.signType);
		} catch (AlipayApiException e) {
			log.error("支付宝验签错误：{}", e.getMessage());
			return result;
		}

		if (verify_result) {
			try {
				//验证成功
				//商户订单号
				String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				//支付宝交易号
				String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
				//交易状态
				String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
				//appid
				String app_id = new String(request.getParameter("app_id").getBytes("ISO-8859-1"), "UTF-8");
				//total_amount
				String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

				PayStatusDTO payStatusDto = new PayStatusDTO();
				payStatusDto.setOut_trade_no(out_trade_no);
				payStatusDto.setApp_id(app_id);
				payStatusDto.setTrade_no(trade_no);
				payStatusDto.setTotal_amount(total_amount);
				payStatusDto.setPay_method(2);//1微信支付 2支付宝支付

				//交易成功处理
				if (trade_status.equals("TRADE_SUCCESS")) {
					payStatusDto.setTrade_status(trade_status);
					result = true;
				} else {
					payStatusDto.setTrade_status("TRADE_FALSE");
					result = false;
				}
				//修改订单支付记录表状态和订单支付状态
				payRecordService.savePayStatus(payStatusDto);
			} catch (UnsupportedEncodingException e) {
				log.error("获取支付宝数据错误：{}", e.getMessage());
			}
		} else {
			log.error("支付宝验签错误：verify_result: {}", verify_result);
		}
		return result;
	}

	/**
	 * 生成支付二维码
	 * @param payUrl 支付路径
	 * @param payRecord 支付记录
	 * @return 结果
	 */
	private QrcodeVO createPayQrcode(String payUrl, PayRecord payRecord) {
		String resultImage = "";
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			BufferedImage image = QrCodeUtils.createImage(payUrl, null, true);
			ImageIO.write(image,"png",os);
			resultImage = new String("data:image/png;base64," + EncryptUtil.encodeBase64(os.toByteArray()));
		} catch (Exception e) {
			CurriculumException.cast(MessageConstant.PAY_ORCODE_CREATE);
		}
		QrcodeVO qrcodeVO = new QrcodeVO();
		qrcodeVO.setPayNo(payRecord.getPayNo());
		qrcodeVO.setQrcodeUrl(resultImage);
		return qrcodeVO;
	}

	/**
	 * 请求第三方支付
	 * @param payRecord 支付
	 * @return 结果
	 */
	private String requestPay(PayRecord payRecord) {
		AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.url, alipayConfig.appId, alipayConfig.appPrivateKey, alipayConfig.format, alipayConfig.charset, alipayConfig.alipayPublicKey, alipayConfig.signType);
		// 创建扫码支付请求
		AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest(); // 创建API对应的request
		alipayRequest.setNotifyUrl(alipayConfig.notifyurl); // 设置回调通知地址

		JSONObject bizContent = new JSONObject();
		// 商户订单号，商家自定义，保持唯一性
		bizContent.put("out_trade_no", payRecord.getPayNo());
		// 支付金额
		bizContent.put("total_amount", payRecord.getTotalPrice());
		// 检查订单标题是否为空
		String orderName = payRecord.getOrderName();
		if(StringUtils.isEmpty(orderName)){
			orderName = OrderConstant.DEFAULT_ORDER_TITLE;
		}

		bizContent.put("subject", orderName); // 设置订单标题

		// 设置扫码支付的产品码
		bizContent.put("product_code", "FACE_TO_FACE_PAYMENT");
		alipayRequest.setBizContent(bizContent.toString()); // 填充业务参数
		try {
			// 请求支付宝下单接口，发起http请求
			AlipayTradePrecreateResponse response = alipayClient.execute(alipayRequest);
			if (response.isSuccess()) {
				// 获取二维码链接
				return response.getQrCode();
			}
			log.error("生成二维码失败：{}", response.getSubMsg());
		} catch (AlipayApiException e) {
			log.error("调用支付宝下单接口错误：{}", alipayRequest, e);
		}
		CurriculumException.cast(OrderConstant.REQUEST_PAY_ERROR);
		return null;
	}


}
