package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.OrderParamsDTO;
import com.curriculum.model.dto.PayParamsDTO;
import com.curriculum.model.po.PayRecord;
import com.curriculum.model.vo.QrcodeVO;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.OrdersService;
import com.curriculum.utils.EncryptUtil;
import com.curriculum.utils.QrCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 订单表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/curriculum")
@Api(tags = "用户订单相关接口")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;


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

    @GetMapping("/curriculum/order/submit")
    @ApiOperation(value = "提交订单")
    public RestResponse AlipayCode(OrderParamsDTO orderParamsDTO) throws Exception {
        log.info("提交订单：{}",orderParamsDTO);

        QrcodeVO qrcodeVO = ordersService.createCode(orderParamsDTO);

        return RestResponse.success(qrcodeVO);

    }
}
