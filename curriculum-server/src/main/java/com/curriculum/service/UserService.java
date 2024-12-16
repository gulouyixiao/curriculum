package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.dto.UserLoginDTO;
import com.curriculum.model.po.User;
import com.curriculum.model.vo.UserLoginVO;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户表 服务类
 */
public interface UserService extends IService<User> {

	/**
	 * 登录
	 * @param userLoginDTO
	 * @return
	 */
	UserLoginVO login( UserLoginDTO userLoginDTO);
}
