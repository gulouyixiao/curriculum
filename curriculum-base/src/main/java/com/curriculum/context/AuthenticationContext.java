package com.curriculum.context;

import com.curriculum.constant.JwtClaimsConstant;
import com.curriculum.utils.Convert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class AuthenticationContext {

    public final static ThreadLocal<Long> contextHolder = new ThreadLocal<>();

    public static void setContext(Long id)
    {
        RequestContextHolder.currentRequestAttributes().setAttribute(JwtClaimsConstant.USER_ID, id,
                RequestAttributes.SCOPE_REQUEST);
    }
    public static Long getContext()
    {

        return Convert.toLong(RequestContextHolder.currentRequestAttributes().getAttribute(JwtClaimsConstant.USER_ID,
                RequestAttributes.SCOPE_REQUEST));
    }

}
