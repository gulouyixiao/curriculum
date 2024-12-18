package com.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.mapper.SurroundingsMapper;
import com.curriculum.model.dto.SurroundingsDTO;
import com.curriculum.model.po.Surroundings;
import com.curriculum.model.vo.PageResult;
import com.curriculum.service.SurroundingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 周边表 服务实现类
 */
@Slf4j
@Service
public class SurroundingsServiceImpl extends ServiceImpl<SurroundingsMapper, Surroundings> implements SurroundingsService {
    @Autowired
    private SurroundingsMapper baseMapper;

    /**
     * 周边分页查询
     * @param videoPageParams
     * @return
     */
    @Override
    public PageResult pageQuery(SurroundingsDTO videoPageParams) {
        LambdaQueryWrapper<Surroundings> queryWrapper = new LambdaQueryWrapper<>();
        String tags = videoPageParams.getTags();

        if(tags != null && !tags.isEmpty()){
            for (String s : tags.split(",")) {
                queryWrapper.like(Surroundings::getTags,s);
            }
        }

        Page<Surroundings> ipage = new Page<>(videoPageParams.getPage(),videoPageParams.getPageSize());
        Page<Surroundings> videoBasePage = baseMapper.selectPage(ipage, queryWrapper);
        return new PageResult(videoBasePage.getRecords(),videoBasePage.getTotal(), videoPageParams.getPage(), videoPageParams.getPageSize());
    }
}
