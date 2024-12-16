package com.curriculum.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "登录时传递的数据模型")
public class UserLoginDTO implements Serializable {

	@ApiModelProperty("账号")
	private String username;

	@ApiModelProperty("密码")
	private String password;

	@ApiModelProperty("验证码key")
	private String key;

	@ApiModelProperty("验证码值")
	private String code;

}