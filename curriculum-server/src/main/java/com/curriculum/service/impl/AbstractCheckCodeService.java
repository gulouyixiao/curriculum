package com.curriculum.service.impl;

import com.curriculum.constant.MessageConstant;
import com.curriculum.exception.CurriculumException;
import com.curriculum.model.dto.CheckCodeParamsDto;
import com.curriculum.model.vo.CheckCodeResult;
import com.curriculum.service.CheckCodeService;
import com.curriculum.utils.RedisCheckCodeStore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @description 验证码接口
 */
@Slf4j
public abstract class AbstractCheckCodeService implements CheckCodeService {
	protected CheckCodeGenerator checkCodeGenerator;

	@Autowired
	protected RedisCheckCodeStore redisCheckCodeStore;

	public abstract void init();


	/**
	 * @param checkCodeParamsDto 生成验证码参数
	 * @param code_length        验证码长度
	 * @param keyPrefix          key的前缀
	 * @param expire             过期时间
	 * @description 生成验证公用方法
	 */
	public GenerateResult generate(CheckCodeParamsDto checkCodeParamsDto, Integer code_length, String keyPrefix, Integer expire) {
		//生成验证码
		String code = checkCodeGenerator.generate(code_length);
		log.debug("生成验证码:{}", code);
		//生成一个key
		String uuid = UUID.randomUUID().toString();
		String key = keyPrefix + uuid.replaceAll("-", "");

		//存储验证码
		redisCheckCodeStore.set(key, code, expire);
		//返回验证码生成结果
		GenerateResult generateResult = new GenerateResult();
		generateResult.setKey(key);
		generateResult.setCode(code);
		return generateResult;
	}

	public abstract CheckCodeResult generate(CheckCodeParamsDto checkCodeParamsDto);

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
		if (result) {
			//删除验证码
			redisCheckCodeStore.remove(key);
		} else
			CurriculumException.cast(MessageConstant.CAPTCHA_ERROR);
	}

	@Data
	protected class GenerateResult {
		String key;
		String code;
	}

}
