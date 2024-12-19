package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.UserLoginDTO;
import com.curriculum.model.dto.UserRegisterDTO;
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
 * 用户相关接口
 */
@Slf4j
@RestController
@RequestMapping("/api/curriculum")
@Api(tags = "用户")
public class UserController {

    @Autowired
    private UserService  userService;


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
    

    /**
     * 注册
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "注册")
    @Anonymous
    public RestResponse register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("注册：{}", userRegisterDTO);
        userService.register(userRegisterDTO);
        return RestResponse.success();
    }


}
