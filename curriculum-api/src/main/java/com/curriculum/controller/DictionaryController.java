package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.po.Dictionary;
import com.curriculum.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags = "数据字典")
public class DictionaryController  {

    @Autowired
    private DictionaryService  dictionaryService;

    @GetMapping("/dictionary/all")
    @ApiOperation("查询所有数据字典内容")
    @Anonymous
    public List<Dictionary> queryAll() {
        return dictionaryService.queryAll();
    }

    @GetMapping("/dictionary/code/{code}")
    @ApiOperation("根据数据字典代码查询数据字典")
    @Anonymous
    public Dictionary getByCode(@PathVariable String code) {
        return dictionaryService.getByCode(code);
    }
}