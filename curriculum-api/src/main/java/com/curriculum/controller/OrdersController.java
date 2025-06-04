package com.curriculum.controller;

import com.alipay.api.domain.OrderDetail;
import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.OrderDTO;
import com.curriculum.model.dto.OrderParamsDTO;
import com.curriculum.model.po.OrderMain;
import com.curriculum.model.po.OrdersDetail;
import com.curriculum.model.po.PayRecord;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.OrderMainService;
import com.curriculum.service.OrdersDetailService;
import com.curriculum.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 订单表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/curriculum")
@Api(tags = "用户订单相关接口")
@Validated
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private OrderDetail orderDetail;
    @Autowired
    private OrdersDetailService ordersDetailService;

    @ApiOperation("接收支付结果通知")
    @PostMapping("/receivenotify/{payType}")
    @Anonymous
    public void receiveNotify(HttpServletRequest request, @PathVariable String payType, HttpServletResponse response){
        ordersService.receiveNotify(request, payType);
    }

    @ApiOperation("查询支付结果")
    @GetMapping("/payresult")
    @ResponseBody
    public RestResponse<PayRecord> payresult(String payNo){
        //查询支付结果
        PayRecord payRecord = ordersService.queryPayResult(payNo);

        return RestResponse.success(payRecord);
    }

    @PostMapping("/curriculum/order/submit")
    @ApiOperation(value = "提交订单")
    public RestResponse AlipayCode(@Valid @RequestBody OrderParamsDTO orderParamsDTO) throws Exception {
        log.info("提交订单：{}",orderParamsDTO);
        return RestResponse.success(ordersService.createCode(orderParamsDTO));

    }

    @ApiOperation("查看历史订单")
    @PostMapping("/listPage")
    public RestResponse listPage(@Valid @RequestBody OrderDTO orderDTO) throws Exception {

        PageResult result = orderMainService.PageQuery(orderDTO);
        return RestResponse.success(result);
    }

    @ApiOperation("查看详细订单")
    @GetMapping("/order/{id}")
    public RestResponse getOrderById(@PathVariable Long id) throws Exception {
        OrdersDetail orderDetail = ordersDetailService.getById(id);
        return RestResponse.success(orderDetail);
    }
}
