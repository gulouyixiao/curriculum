package com.curriculum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curriculum.model.po.Acgn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 漫展演出表 Mapper 接口
 * </p>
 *
 * @author gulouyixiao
 */
public interface AcgnMapper extends BaseMapper<Acgn> {

    List<Acgn> getAcgnPageByTimeAndCityName(@Param("startTime") String startTime,
                                            @Param("cityName") String cityName);
}
