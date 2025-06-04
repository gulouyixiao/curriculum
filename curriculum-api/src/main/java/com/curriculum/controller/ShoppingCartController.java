package com.curriculum.controller;


import com.curriculum.annotation.Anonymous;
import com.curriculum.model.po.ShoppingCart;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.AcgnService;
import com.curriculum.service.ShoppingCartService;
import com.curriculum.service.SurroundingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/curriculum/cart")
@Api(tags = "购物车表")
@Anonymous
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private SurroundingsService surroundingsService;

    @Autowired
    private AcgnService gnService;


    @ApiOperation("删除购物车信息")
    @GetMapping("remove/{id}")
    public RestResponse<String> remove(@PathVariable Integer id) {
        shoppingCartService.removeById(id);

        ShoppingCart shoppingCart = shoppingCartService.getById(id);
        log.info("删除的商品信息是：{}",shoppingCart);

        return RestResponse.success();
    }

    @ApiOperation("获取购物车信息")
    @GetMapping("list")
    public RestResponse<List<ShoppingCart>> list() {
        List<ShoppingCart> list = shoppingCartService.list();//应该是带ids的
        return RestResponse.success(list);
    }

    @ApiOperation("添加到购物车")
    @PostMapping("add")
    public RestResponse<String> add(@RequestBody ShoppingCart shoppingCart) {
        shoppingCartService.saveShoppingCart(shoppingCart);
        return RestResponse.success();
    }
}
