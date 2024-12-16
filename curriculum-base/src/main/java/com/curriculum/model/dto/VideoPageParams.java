package com.curriculum.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@ApiModel(description = "视频分页参数")
public class VideoPageParams {
	//当前页码
	@ApiModelProperty(value = "页码", example = "1")
	private Long page = 1L;
	//每页显示记录数
	@ApiModelProperty(value = "每页记录数", example = "15")
	private Long pageSize = 15L;

	@ApiModelProperty("关键字")

	private String tags;
}
