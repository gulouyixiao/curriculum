package com.curriculum.model.dto;

/**
 * 发布评论参数
 */

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "发布评论参数")
public class CommentsDTO {
	private String content;
	private Long videoId;
	private Long parentCommentId;
}
