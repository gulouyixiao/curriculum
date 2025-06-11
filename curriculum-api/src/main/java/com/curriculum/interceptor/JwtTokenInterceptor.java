package com.curriculum.interceptor;

import com.curriculum.annotation.Anonymous;
import com.curriculum.constant.JwtClaimsConstant;
import com.curriculum.constant.MessageConstant;
import com.curriculum.context.AuthenticationContext;
import com.curriculum.exception.CurriculumException;
import com.curriculum.properties.JwtProperties;
import com.curriculum.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * jwt令牌校验的拦截器
 */

@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {
	@Autowired
	private JwtProperties jwtProperties;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//检查是否是方法级处理器
		log.info("JwtTokenInterceptor 拦截请求: {}", request.getRequestURI());
		if(handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod) handler;

			//检查方法上是否有“匿名访问不鉴权注解”
			if(handlerMethod.hasMethodAnnotation(Anonymous.class)){
				return true; //放行
			}

			//检查类上是否有“匿名访问不鉴权注解”
			Class<?> beanType = handlerMethod.getBeanType();
			if(beanType.isAnnotationPresent(Anonymous.class)){
				return true; //放行
			}

			//1.从请求头中获取令牌
			String token = request.getHeader(jwtProperties.getUserTokenName());
			//2.效验令牌
			if(token != null && !token.isEmpty()){
				log.info("jwt效验：{}",token);
				try{
					Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
					Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
					log.info("当前用户的id：{}", userId);
					AuthenticationContext.setContext(userId);
					//放行
					return true;
				}catch (Exception ex){
					CurriculumException.cast(MessageConstant.USER_NOT_LOGIN);
				}
			}
			CurriculumException.cast(MessageConstant.USER_NOT_LOGIN);
			return false;
		}
		return true;
	}
}
