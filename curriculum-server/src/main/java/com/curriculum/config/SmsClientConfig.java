package com.curriculum.config;


import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 阿里云sms短信验证配置
 */
@Component
public class SmsClientConfig {

	//阿里云账号的accessKeyId
	@Value("${curriculum.aliyun.sms.accessKeyId}")
	private String accessKeyId;
	//阿里云账号的accessKeySecret
	@Value("${curriculum.aliyun.sms.accessKeySecret}")
	private String accessKeySecret;
	//短信服务访问的域名
	@Value("${curriculum.aliyun.sms.endpoint}")
	private String endpoint;

	@Bean("smsClient")
	public Client createClient() throws Exception {
		Config config = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
		config.endpoint = endpoint;
		return new Client(config);
	}
}
