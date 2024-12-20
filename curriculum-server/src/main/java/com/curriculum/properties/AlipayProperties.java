package com.curriculum.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description 支付宝配置参数
 */
@Component
@ConfigurationProperties(prefix = "curriculum.alipay.pay")
@Data
public class AlipayProperties {
	public String appId;
	public String appPrivateKey;
	public String alipayPublicKey;
	public String notifyurl;
	public String returnUrl;
	public String url;
	public String charset;
	public String format;
	public String logPath;
	public String signType;
}
