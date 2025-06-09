package com.curriculum.model.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @description 验证码生成参数类
 */
@Data
public class CheckCodeParamsDto {

    /**
     * 验证码类型:pic、sms、email等
     */
    @ApiModelProperty(value = "验证码类型:pic、sms、email等")
    private String checkCodeType;

    /**
     * 业务携带参数
     */
    private String param1;
    private String param2;
    private String param3;
}
