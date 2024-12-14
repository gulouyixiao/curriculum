package com.curriculum.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 周边表
 * </p>
 *
 * @author gulouyixiao
 */
@Data
@TableName("surroundings")
public class Surroundings implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 价格
     */
    private Float price;

    /**
     * 品牌
     */
    private String brand;

    /**
     * ip
     */
    private String ip;

    /**
     * 会员价
     */
    private Float vipPrice;

    /**
     * 标签，关键字
     */
    private String tags;

    /**
     * 图片
     */
    private String pic;


}
