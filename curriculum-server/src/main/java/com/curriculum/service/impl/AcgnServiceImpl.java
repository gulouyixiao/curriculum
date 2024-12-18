package com.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.mapper.AcgnMapper;
import com.curriculum.model.po.Acgn;
import com.curriculum.model.vo.PageResult;
import com.curriculum.service.AcgnService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        LocalDateTime dateTime = LocalDateTime.now();

        if("一周内".equals(startTime)){
            dateTime = dateTime.plusYears(7);
        }else if ("一个月内".equals(startTime)){
            dateTime = dateTime.plusYears(30);
        }

        LambdaQueryWrapper<Acgn> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.le(startTime != null,Acgn::getStartTime,dateTime)
                .ge(startTime != null,Acgn::getStartTime,LocalDateTime.now())
                .like(cityName != null,Acgn::getCityName,cityName);
        Page<Acgn> page1 = new Page<>(page,size);
        Page<Acgn> acgnPage = acgnMapper.selectPage(page1, queryWrapper);

        PageResult<Acgn> acgnPageResult = new PageResult<>(acgnPage.getRecords(), acgnPage.getTotal(), page, size);
        return acgnPageResult;
    }
}
