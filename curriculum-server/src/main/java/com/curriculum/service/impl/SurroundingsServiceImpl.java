package com.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.context.AuthenticationContext;
import com.curriculum.mapper.SurroundingsMapper;
import com.curriculum.model.dto.PageParams;
import com.curriculum.model.dto.SurroundingsDTO;
import com.curriculum.model.po.Surroundings;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.Products;
import com.curriculum.service.SurroundingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                if(s.equals("全部")){
                    queryWrapper.clear();
                    break;
                }
                queryWrapper.like(Surroundings::getTags,s);
            }
        }

        Page<Surroundings> ipage = new Page<>(videoPageParams.getPage(),videoPageParams.getPageSize());
        Page<Surroundings> videoBasePage = baseMapper.selectPage(ipage, queryWrapper);
        return new PageResult(videoBasePage.getRecords(),videoBasePage.getTotal(), videoPageParams.getPage(), videoPageParams.getPageSize());
    }

    @Override
    public void insert(Surroundings surroundings) {
        Long userId = AuthenticationContext.getContext();
        surroundings.setUserId(userId);
        baseMapper.insert(surroundings);
    }

    @Override
    public PageResult myProducts(PageParams pageParams) {
        Long userId = AuthenticationContext.getContext();
        LambdaQueryWrapper<Surroundings> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Surroundings::getUserId,userId);
        Page<Surroundings> ipage = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        Page<Surroundings> surroundingsPage = baseMapper.selectPage(ipage, queryWrapper);
        List<Surroundings> surroundingsList = surroundingsPage.getRecords();
        List<Products> collect = surroundingsList.stream().map(s -> {
            Products products = new Products();
            products.setName(s.getTitle());
            products.setPic(s.getPic());
            products.setAuditStatus("002001");
            products.setPrice(s.getPrice());
            return products;
        }).collect(Collectors.toList());
        return new PageResult(collect,surroundingsPage.getTotal(), pageParams.getPage(), pageParams.getPageSize());
    }
}
