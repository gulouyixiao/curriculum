package com.curriculum.controller;


import com.curriculum.model.dto.ShoppingCatChangeDTO;
import com.curriculum.model.po.Acgn;
import com.curriculum.model.po.ShoppingCart;
import com.curriculum.model.po.Surroundings;
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
@RequestMapping("/api/curriculum/cart/")
@Api(tags = "购物车表")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private SurroundingsService surroundingsService;

    @Autowired
    private AcgnService gnService;


    @GetMapping("remove/{id}")
    @ApiOperation("删除购物车信息")
    public RestResponse<String> remove(@PathVariable Integer id) {
        shoppingCartService.removeById(id);

        ShoppingCart shoppingCart = shoppingCartService.getById(id);
        log.info("删除的商品信息是：{}",shoppingCart);

        return RestResponse.success();
    }

    @ApiOperation("批量删除")
    @PostMapping("/deletelist")
    public RestResponse<String> deletelist(@RequestBody List<Integer> ids) {
//        for (Integer id : ids) {
//            shoppingCartService.removeById(id);
//        }
        shoppingCartService.removeByIds(ids);
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

    //查看购物车商品详情
    @ApiOperation("查看商品详细")
    @GetMapping("/{id}")
    public RestResponse<Object> getById(@PathVariable Integer id) {
        ShoppingCart shoppingCart = shoppingCartService.getById(id);
        Long shopId = shoppingCart.getShoppingId();
        if (Objects.equals(shoppingCart.getShoppingType(), "01")) {
            Surroundings surroundings = surroundingsService.getById(shopId);
            return RestResponse.success(surroundings);
        }else if (Objects.equals(shoppingCart.getShoppingType(), "02")) {
            Acgn gn = gnService.getById(shopId);
            return RestResponse.success(gn);
        }
        return RestResponse.validfail("不合法的类型");
//        return RestResponse.success(shoppingCart);
    }

    @ApiOperation("购物车增加/减少数量")
    @PostMapping("/changeNumber")
    public RestResponse<String> change(@RequestBody ShoppingCatChangeDTO shoppingCatChangeDTO) {
        shoppingCartService.changeNumber(shoppingCatChangeDTO);
        return RestResponse.success();
    }


}
