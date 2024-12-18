package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.CheckCodeParamsDto;
import com.curriculum.model.vo.CheckCodeResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.CheckCodeService;
import com.curriculum.service.UserCheckCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description 验证码服务接口
 */
@Api(tags = "验证码服务接口")
@RestController
@RequestMapping("/api/curriculum")
public class CheckCodeController {

	@Autowired
	private UserCheckCodeService userCheckCodeService;
	/**
	 * 获取验证码
	 * @param
	 * @return 返回验证码
	 */
	@PostMapping("/captcha")
	@ApiOperation(value = "获取验证码")
	@Anonymous
	public RestResponse<CheckCodeResult> getCode(@RequestBody CheckCodeParamsDto checkCodeParamsDto) {
		CheckCodeResult codeResult = userCheckCodeService.generate(checkCodeParamsDto);
		return RestResponse.success(codeResult);
	}

}
