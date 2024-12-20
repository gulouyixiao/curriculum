package com.curriculum.service.impl;

import com.curriculum.constant.MessageConstant;
import com.curriculum.exception.CurriculumException;
import com.curriculum.model.dto.CheckCodeParamsDto;
import com.curriculum.model.vo.CheckCodeResult;
import com.curriculum.service.CheckCodeService;
import com.curriculum.service.UserCheckCodeService;
import com.curriculum.utils.RedisCheckCodeStore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


/**
 * @description 用户验证码接口
 */
@Slf4j
@Service
public class UserCheckCodeServiceImpl implements UserCheckCodeService {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private RedisCheckCodeStore redisCheckCodeStore;


	public CheckCodeResult generate(CheckCodeParamsDto checkCodeParamsDto){
		//获取验证码类型
		String type = checkCodeParamsDto.getCheckCodeType();
		String beanName = type + "_CheckCodeService";
		//从spring容器取出指定类型的bean
		CheckCodeService checkCodeService = applicationContext.getBean(beanName, CheckCodeService.class);

		CheckCodeResult checkCodeResult = checkCodeService.execute(checkCodeParamsDto);

		return checkCodeResult;

	}

	/**
	 * 验证码效验
	 * @param key
	 * @param code
	 */
	public void verify(String key, String code) {
		if (StringUtils.isBlank(key) || StringUtils.isBlank(code)) {
			CurriculumException.cast(MessageConstant.CAPTCHA_NULL);
		}
		String code_l = redisCheckCodeStore.get(key);
		if (code_l == null) {
			CurriculumException.cast(MessageConstant.CAPTCHA_NULL);
		}
		// 验证用户提交的验证码与缓存中的验证码是否匹配（不区分大小写）
		boolean result = code_l.equalsIgnoreCase(code);
		if (!result){
			CurriculumException.cast(MessageConstant.CAPTCHA_ERROR);

		}
		//删除验证码
		redisCheckCodeStore.remove(key);
	}

	@Data
	protected class GenerateResult {
		String key;
		String code;
	}


}
