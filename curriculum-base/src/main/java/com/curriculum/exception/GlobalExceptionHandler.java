package com.curriculum.exception;

import com.curriculum.constant.MessageConstant;
import com.curriculum.model.vo.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

 //对项目的自定义异常类型进行处理
   @ResponseBody
   @ExceptionHandler(CurriculumException.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
 public RestResponse customException(CurriculumException e){
    //记录异常
    log.error("系统异常{}",e.getErrMessage(),e);
    //..
    //解析出异常信息
    String errMessage = e.getErrMessage();
    if(MessageConstant.PERMISSION_DENIED.equals(e.getMessage())){
           return RestResponse.validfail("您没有权限操作此功能");
    }
    return RestResponse.validfail(errMessage);
   }

   @ResponseBody
   @ExceptionHandler(Exception.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public RestResponse exception(Exception e){
    //记录异常
    log.error("系统异常{}",e.getMessage(),e);
    return RestResponse.validfail(MessageConstant.UNKOWN_ERROR);
   }

    @ResponseBody
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse SqlException(Exception e){
        //记录异常
        log.error("数据库操作异常{}",e.getMessage(),e);

        return RestResponse.validfail(MessageConstant.UNKOWN_ERROR);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public RestResponse handleBindException(BindException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        return RestResponse.validfail(message);
    }

    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse NullException(Exception e){
        //记录异常
        log.error("数据为空{}",e.getMessage(),e);

        return RestResponse.validfail(MessageConstant.REQUEST_NULL);
    }





   //MethodArgumentNotValidException
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse methodArgumentNotValidException(MethodArgumentNotValidException e){

        BindingResult bindingResult = e.getBindingResult();
        //存储错误信息
        List<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().stream().forEach(item->{
            errors.add(item.getDefaultMessage());
        });

        //将list中的错误信息拼接起来
        String errMessage = StringUtils.join(errors, ",");
        //记录异常
        log.error("系统异常{}",e.getMessage(),errMessage);

        return RestResponse.validfail(errMessage);
    }


}
