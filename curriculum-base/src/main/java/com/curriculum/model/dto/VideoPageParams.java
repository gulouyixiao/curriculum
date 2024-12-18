package com.curriculum.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@ApiModel(description = "视频分页参数")
public class VideoPageParams {
	//当前页码
	@ApiModelProperty("页码")
	private Long page = 1L;
	//每页显示记录数
	@ApiModelProperty("每页记录数")
	private Long pageSize = 15L;


	@ApiModelProperty("关键字")
	private String tags;

	@ApiModelProperty("类型，\"001003\" 番剧， \"001002\" 视频")
	private String videoType;
}
