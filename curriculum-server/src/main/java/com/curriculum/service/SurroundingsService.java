package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.dto.SurroundingsDTO;
import com.curriculum.model.po.Surroundings;
import com.curriculum.model.vo.PageResult;

/**
 * <p>
 * 周边表 服务类
 * </p>
 *
 * @author gulouyixiao
 * @since 2024-12-14
 */
public interface SurroundingsService extends IService<Surroundings> {

    PageResult pageQuery(SurroundingsDTO videoPageParams);

    void insert(Surroundings surroundings);
}
