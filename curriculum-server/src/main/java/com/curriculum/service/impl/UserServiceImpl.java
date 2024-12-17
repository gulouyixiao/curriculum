package com.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.constant.JwtClaimsConstant;
import com.curriculum.constant.MessageConstant;
import com.curriculum.constant.UserConstants;
import com.curriculum.exception.CurriculumException;
import com.curriculum.mapper.UserMapper;
import com.curriculum.model.dto.UserLoginDTO;
import com.curriculum.model.dto.UserRegisterDTO;
import com.curriculum.model.po.User;
import com.curriculum.model.vo.UserLoginVO;
import com.curriculum.properties.JwtProperties;
import com.curriculum.service.UserCheckCodeService;
import com.curriculum.service.UserService;
import com.curriculum.utils.CheckCodeUtils;
import com.curriculum.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Autowired
	private UserCheckCodeService userCheckCodeService;
	@Autowired
	JwtProperties jwtProperties;
	/**
	 * 登录
	 * @param userLoginDTO
	 * @return
	 */
	public UserLoginVO login(UserLoginDTO userLoginDTO){
		//验证码校验
		userCheckCodeService.verify(userLoginDTO.getKey(),userLoginDTO.getCode());

		String username = userLoginDTO.getUsername();
		String password = userLoginDTO.getPassword();

		// 登录前置校验
		loginPreCheck(username, password);

		//1.根据用户名查询数据库中的数据
		User user = getByUserName(username);

		//2、处理各种异常情况（用户名不存在、密码不对）
		if(user == null){
			//账号不存在
			log.info("登录用户：{},不存在",username);
			CurriculumException.cast(MessageConstant.ACCOUNT_NOT_FOUND);
		}
		//密码比对
		//对前端传过来的明文密码进行md5加密处理
		password = DigestUtils.md5DigestAsHex(password.getBytes());
		if (!password.equals(user.getPassword())) {
			//密码错误
			CurriculumException.cast(MessageConstant.PASSWORD_ERROR);
		}


		//登录成功后，生成jwt令牌
		Map<String, Object> claims = new HashMap<>();
		claims.put(JwtClaimsConstant.USER_ID, user.getId());
		String token = JwtUtil.createJWT(
				jwtProperties.getUserSecretKey(),
				jwtProperties.getUserTtl(),
				claims);
		UserLoginVO userLoginVO = UserLoginVO.builder()
				.id(user.getId())
				.username(user.getUsername())
				.nickname(user.getNickname())
				.token(token)
				.build();

		
		return userLoginVO;
	}

	/**
	 * 注册
	 * @param userRegisterDTO
	 */
	@Override
	public void register(UserRegisterDTO userRegisterDTO) {
		//前置校验
		loginPreCheck(userRegisterDTO.getUsername(),userRegisterDTO.getPassword());

		userCheckCodeService.verify(userRegisterDTO.getKey(),userRegisterDTO.getCode());

		User user = new User();
		BeanUtils.copyProperties(userRegisterDTO,user);
		user.setStatus("1");
		user.setCreateTime(LocalDateTime.now());
		user.setGrade("1");
		user.setUtype("205001");
		this.save(user);
	}

	private User getByUserName(String username) {
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getUsername, username);
		return getOne(queryWrapper);
	}


	/**
	 * 前置校验
	 * @param username 用户名
	 * @param password 用户密码
	 */
	public void loginPreCheck(String username, String password)
	{
		// 用户名或密码为空错误
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
		{
			// 抛出用户密码不存在异常
			CurriculumException.cast(MessageConstant.ACCOUNT_NULL);
		}
		// 密码如果不在指定范围内错误
		if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
				|| password.length() > UserConstants.PASSWORD_MAX_LENGTH)
		{
			// 抛出用户密码不正确或不符合规范异常
			CurriculumException.cast(MessageConstant.PASSWORD_ERROR);
		}
		// 用户名不在指定范围内错误
		if (username.length() < UserConstants.USERNAME_MIN_LENGTH
				|| username.length() > UserConstants.USERNAME_MAX_LENGTH)
		{
			// 抛出用户密码不正确或不符合规范异常
			CurriculumException.cast(MessageConstant.ACCOUNT_NOT_FOUND);
		}

	}
}
