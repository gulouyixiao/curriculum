package com.curriculum.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "登录返回的数据格式")
public class UserLoginVO implements Serializable {

	@ApiModelProperty("主键值")
	private Long id;

	@ApiModelProperty("账号用户名")
	private String userName;

	@ApiModelProperty("昵称")
	private String nickName;

	@ApiModelProperty("jwt令牌")
	private String token;

}