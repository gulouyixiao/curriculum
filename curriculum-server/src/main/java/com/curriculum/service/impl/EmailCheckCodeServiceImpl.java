package com.curriculum.service.impl;

import com.curriculum.model.dto.CheckCodeParamsDto;
import com.curriculum.model.vo.CheckCodeResult;
import com.curriculum.service.CheckCodeService;
import com.curriculum.utils.RedisCheckCodeStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 邮箱验证码
 */
@Service("email_CheckCodeService")
@Slf4j
public class EmailCheckCodeServiceImpl implements CheckCodeService {
	@Autowired
	private RedisCheckCodeStore redisCheckCodeStore;

	@Override
	public CheckCodeResult execute(CheckCodeParamsDto checkCodeParamsDto) {
		return null;
	}
}
