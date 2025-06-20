package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.OrderDTO;
import com.curriculum.model.dto.OrderParamsDTO;
import com.curriculum.model.dto.OrderSubmitDTO;
import com.curriculum.model.po.OrdersDetail;
import com.curriculum.model.po.PayRecord;
import com.curriculum.model.vo.OrderDetailVO;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.QrcodeVO;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.OrderMainService;
import com.curriculum.service.OrdersDetailService;
import com.curriculum.service.OrdersService;
import com.sun.xml.bind.v2.TODO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 订单表 前端控制器
 */
//@Anonymous
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

//    @Autowired
//    private OrderDetail orderDetail;
    @Autowired
    private OrdersDetailService ordersDetailService;

    @ApiOperation("接收支付结果通知")
    @PostMapping("/receivenotify/{payType}")
    @Anonymous
    public void receiveNotify(HttpServletRequest request, @PathVariable String payType){
        log.info("接收支付结果通知：{}");
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

    @PostMapping("/order/submit")
    @ApiOperation(value = "提交订单")
    public RestResponse<QrcodeVO> submitOrder(@Valid @RequestBody OrderSubmitDTO orderSubmitDTO) throws Exception {
        log.info("提交订单：{}", orderSubmitDTO);
        return RestResponse.success(ordersService.createCode(orderSubmitDTO));
    }

    @GetMapping("/generatepaycode")
    @ApiOperation(value = "生成支付宝支付二维码")
    public RestResponse<QrcodeVO> alipayCode(OrderParamsDTO orderParamsDTO) throws Exception {
        log.info("生成支付宝支付二维码：{}", orderParamsDTO);

        QrcodeVO qrcodeVO = ordersService.directCreateCode(orderParamsDTO);

        return RestResponse.success(qrcodeVO);

    }

//    @Anonymous
//    TODO 这个地方要加order
    @ApiOperation("查看历史订单")
    @PostMapping("order/listPage")
//    @Anonymous
    public RestResponse listPage(@Valid @RequestBody OrderDTO orderDTO) throws Exception {
        log.info("{}",orderDTO.getPage().getClass());
        PageResult result = orderMainService.PageQuery(orderDTO);
//        String result = "1";
        return RestResponse.success(result);
    }

    @ApiOperation("查看详细订单")
    @GetMapping("/order/{id}")
    public RestResponse getOrderById(@PathVariable Long id) throws Exception {
        OrderDetailVO orderDetail = ordersDetailService.getByIdWpic(id);
        return RestResponse.success(orderDetail);
    }
}
