package com.curriculum.model.vo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@ApiModel(description = "视频基本信息")
public class VideoBaseVO {

	private Long id;

	/**
	 * 用户名称
	 */
	private String username;

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
	 * 视频等级，番剧级数
	 */
	private String grade;

	/**
	 * 视频介绍
	 */
	private String description;

	/**
	 * 封面（路径）
	 */
	private String cover;

	/**
	 * 时长，单位时:分:秒
	 */
	private String timelength;

	/**
	 * 开始播放时间
	 */
	private LocalDateTime startTime;

	/**
	 * 父级Id
	 */
	private Long parentid;

	/**
	 * 视频风格
	 */
	private String style;

}
