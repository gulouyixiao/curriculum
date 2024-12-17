package com.curriculum.controller;

import com.curriculum.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("orders")
public class OrdersController {

    @Autowired
    private OrdersService  ordersService;
}
