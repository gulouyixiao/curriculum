package com.curriculum.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 视频审核
 */
@Data
@ApiModel(description = "视频审核的数据格式")
public class VideoAuditVO implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 视频id
     */
    private Long videoId;

    /**
     * 审核意见
     */
    private String auditMind;

    /**
     * 审核状态
     */
    private String auditStatus;


    /**
     * 审核时间
     */
    private LocalDateTime auditDate;

    private String title;

}
