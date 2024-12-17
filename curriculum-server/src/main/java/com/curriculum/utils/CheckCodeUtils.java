package com.curriculum.utils;

import com.curriculum.model.vo.CheckCodeResult;
import com.curriculum.service.impl.UserCheckCodeServiceImpl;

import java.util.Random;
import java.util.UUID;

public class CheckCodeUtils {

	public static String generateNumberLetter(int length) {
		String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random=new Random();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<length;i++){
			int number=random.nextInt(36);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 纯数字生成器
	 */
	public static String generateNumber(int length) {
		String str = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(10);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}
	/**
	 * @param code_length        验证码长度
	 * @param keyPrefix          key的前缀
	 * @description 生成验证公用方法
	 */
	public static CheckCodeResult generate(Integer code_length, String keyPrefix,Boolean type) {
		//生成验证码
		String code = null;
		if(type){
			code = generateNumberLetter(code_length);
		}else
			code = generateNumber(code_length);

		//生成一个key
		String uuid = UUID.randomUUID().toString();
		String key = keyPrefix + uuid.replaceAll("-", "");
		CheckCodeResult checkCodeResult = new CheckCodeResult();
		checkCodeResult.setKey(key);
		checkCodeResult.setCaptcha(code);
		return checkCodeResult;
	}
}
