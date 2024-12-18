package com.curriculum.service;


import com.curriculum.model.dto.CheckCodeParamsDto;
import com.curriculum.model.vo.CheckCodeResult;

/**
 * @description 验证码接口
 */
public interface UserCheckCodeService {
	/**
	 * 生成验证码
	 * @param checkCodeParamsDto 生成验证码参数
	 * @return
	 */
	CheckCodeResult generate(CheckCodeParamsDto checkCodeParamsDto);

	/**
	 * @description 校验验证码
	 * @param key
	 * @param code
	 * @return boolean
	 */
	void verify(String key, String code);

}
