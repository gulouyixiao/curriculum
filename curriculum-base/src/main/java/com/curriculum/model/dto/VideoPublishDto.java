package com.curriculum.model.dto;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "视频番剧发布参数")
public class VideoPublishDto {

	/**
	 * 媒资文件id
	 */
	private String mediaId;


	/**
	 * 标题
	 */
	private String title;

	/**
	 * 标签，关键字
	 */
	private String tags;

	/**
	 * 视频类型（普通视频，番剧）
	 */
	private String videoType;

	/**
	 * 视频介绍
	 */
	private String description;

	/**
	 * 封面（路径）
	 */
	private String cover;

	/**
	 * 父级Id
	 */
	private Long parentid;

	/**
	 * 视频风格
	 */
	private String style;
}
