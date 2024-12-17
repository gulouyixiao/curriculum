package com.curriculum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import com.curriculum.exception.CurriculumException;
import com.curriculum.model.dto.CheckCodeParamsDto;
import com.curriculum.model.vo.CheckCodeResult;
import com.curriculum.service.CheckCodeService;
import com.curriculum.utils.CheckCodeUtils;
import com.curriculum.utils.RedisCheckCodeStore;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @description 短信验证码
 */
@Service("sms_CheckCodeService")
@Slf4j
public class SmsCheckCodeServiceImpl implements CheckCodeService {

	@Autowired
	private Client smsClient;

	@Value("${curriculum.aliyun.sms.signName}")
	private String signName;

	@Value("${curriculum.aliyun.sms.smsCodeTemplateCode}")
	private String smsCodeTemplateCode;

	@Autowired
	private RedisCheckCodeStore redisCheckCodeStore;
	@Override
	public CheckCodeResult execute(CheckCodeParamsDto checkCodeParamsDto) {
		CheckCodeResult generate = CheckCodeUtils.generate(4, "checkcode:",false);
		String key = generate.getKey();
		String code = generate.getCaptcha();
		//向目标手机号发送短信
		sendSmsCode(checkCodeParamsDto.getParam1(),code);

		CheckCodeResult checkCodeResult = new CheckCodeResult();
		checkCodeResult.setKey(key);
		checkCodeResult.setCaptcha(null);
		return checkCodeResult;
	}

	/**
	 * 发送短信验证码
	 * @param phoneNumber 手机号
	 */
	private void sendSmsCode(String phoneNumber,String checkCode){
		HashMap<Object, Object> templateParamMap = Maps.newHashMap();
		templateParamMap.put("code",checkCode);
		SendSmsRequest sendSmsRequest = new SendSmsRequest();
		sendSmsRequest.setPhoneNumbers(phoneNumber);
		sendSmsRequest.setSignName(signName);
		sendSmsRequest.setTemplateCode(smsCodeTemplateCode);
		sendSmsRequest.setTemplateParam(JSONObject.toJSONString(templateParamMap));
		try {
			SendSmsResponse sendSmsResponse = smsClient.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
			// 200为成功
			SendSmsResponseBody body = sendSmsResponse.getBody();
			System.out.println(body);
			// code和message为OK成功
			String code = body.getCode();
			String message = body.getMessage();
			if ("OK".equals(code) && "OK".equals(message)) {
				log.info("向 手机号：{} 用户发送短信验证码成功 code：{}！", phoneNumber,checkCode);
			} else {
				log.warn("向 手机号：{} 用户发送短信验证码失败！message: {}", phoneNumber,message);
				throw new RuntimeException();
			}
		} catch (Exception e) {
			e.printStackTrace();
			CurriculumException.cast("发送短信验证码失败");
		}

	}

}
