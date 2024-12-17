package com.curriculum.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "评论区分页参数")
public class CommentsPageParams {
	//当前页码
	@ApiModelProperty(value = "页码", example = "1")
	private Long page = 1L;
	//每页显示记录数
	@ApiModelProperty(value = "每页记录数", example = "30")
	private Long pageSize = 30L;

	@ApiModelProperty("视频id")
	private Long id;

	@ApiModelProperty("父评论id")
	private Long parentCommentId;
}
