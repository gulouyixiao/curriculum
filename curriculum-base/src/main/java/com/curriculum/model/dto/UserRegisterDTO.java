package com.curriculum.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "注册时传递的数据模型")
public class UserRegisterDTO implements Serializable {

	@ApiModelProperty("账号")
	private String userName;

	@ApiModelProperty("密码")
	private String password;

	@ApiModelProperty("昵称")
	private String nickName;

	@ApiModelProperty("邮箱")
	private String email;

	@ApiModelProperty("手机号")
	private String cellphone;

	@ApiModelProperty("验证码")
	private String code;

	private String key;
}
