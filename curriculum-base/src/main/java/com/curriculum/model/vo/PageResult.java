package com.curriculum.model.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @description 分页查询结果模型类
 */
@Data
@ToString
public class PageResult<T> implements Serializable {

    // 数据列表
    private List<T> dataList;

    //总记录数
    private long total;

    //当前页码
    private long page;

    //每页记录数
    private long pageSize;

    public PageResult(List<T> dataList, long total, long page, long pageSize) {
        this.dataList = dataList;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }


}
