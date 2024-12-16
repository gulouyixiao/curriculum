package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.UserLoginDTO;
import com.curriculum.model.vo.CheckCodeResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.model.vo.UserLoginVO;
import com.curriculum.service.CheckCodeService;
import com.curriculum.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/curricuclum")
@Api(tags = "用户")
public class UserController {

    @Autowired
    private UserService  userService;

    @Resource(name = "PicCheckCodeService")
    private CheckCodeService picCheckCodeService;
    /**
     * 获取验证码
     * @param
     * @return 返回验证码(重复接口)
     */
    @GetMapping("/pic")
    @ApiOperation(value = "获取验证码")
    @Anonymous
    public RestResponse<CheckCodeResult> getCode() {
        CheckCodeResult codeResult = picCheckCodeService.generate(null);
        return RestResponse.success(codeResult);
    }

    /**
     * 登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    @Anonymous
    public RestResponse<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("登录：{}", userLoginDTO);
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return RestResponse.success(userLoginVO);
    }


}
