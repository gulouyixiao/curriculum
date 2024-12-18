package com.curriculum.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程基本信息
 * </p>
 *
 * @author gulouyixiao
 */
@Data
@TableName("video_base")
public class VideoBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 媒资文件id
     */
    private String mediaId;

    /**
     * 上传用户ID
     */
    private Long userId;

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

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;

    /**
     * 点赞数
     */
    private Long thumbupCount;

    /**
     * 评论数
     */
    private Long commentCount;

    /**
     * 播放量
     */
    private Long playbackVolume;

    /**
     * 收费规则，201000：免费，201001：会员免费
     */
    private String member;

    /**
     * 审核状态
     */
    private String auditStatus;

    /**
     * 视频发布状态
     */
    private String status;

    /**
     * 视频路径
     */
    private String url;

}
