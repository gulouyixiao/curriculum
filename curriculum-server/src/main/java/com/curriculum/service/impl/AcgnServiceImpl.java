package com.curriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.mapper.AcgnMapper;
import com.curriculum.model.po.Acgn;
import com.curriculum.model.vo.PageResult;
import com.curriculum.service.AcgnService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 漫展演出表 服务实现类
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@Service
public class AcgnServiceImpl extends ServiceImpl<AcgnMapper, Acgn> implements AcgnService {

    @Autowired
    private AcgnMapper acgnMapper;
    @Override
    public PageResult<Acgn> getAcgnPageByTimeAndCityName(int page, int size, String startTime, String cityName) {
        PageHelper.startPage(page, size);
        List<Acgn> acgnList = acgnMapper.getAcgnPageByTimeAndCityName(startTime, cityName);

        PageResult<Acgn> acgnPageResult = new PageResult<>(acgnList, (long) acgnList.size(), page, size);
        return acgnPageResult;
    }
}
