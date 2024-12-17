package com.curriculum.service;
import com.curriculum.model.dto.CheckCodeParamsDto;
import com.curriculum.model.vo.CheckCodeResult;

/**
 * 验证码service
 */
public interface CheckCodeService {
	/**
	 * 验证码实现统一接口
	 * @param checkCodeParamsDto
	 */
	CheckCodeResult execute(CheckCodeParamsDto checkCodeParamsDto);

}
