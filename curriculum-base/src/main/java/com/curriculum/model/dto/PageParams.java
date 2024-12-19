package com.curriculum.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @description 分页查询分页参数
 */
@Data
@ToString
public class PageParams {

    //当前页码
    @ApiModelProperty("页码")
    private Long page;
    //每页显示记录数
    @ApiModelProperty("每页记录数")
    private Long pageSize;

    public PageParams() {
    }

    public PageParams(Long page, Long pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }
}
