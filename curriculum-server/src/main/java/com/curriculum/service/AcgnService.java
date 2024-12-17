package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.po.Acgn;
import com.curriculum.model.vo.PageResult;

/**
 * <p>
 * 漫展演出表 服务类
 * </p>
 *
 * @author gulouyixiao
 * @since 2024-12-14
 */
public interface AcgnService extends IService<Acgn> {

    PageResult<Acgn> getAcgnPageByTimeAndCityName(int page, int size, String startTime, String cityName);
}
