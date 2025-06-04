package com.curriculum.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.constant.MessageConstant;
import com.curriculum.enums.OrderStatusEnum;
import com.curriculum.exception.CurriculumException;
import com.curriculum.mapper.OrderMainMapper;
import com.curriculum.model.po.OrderMain;
import com.curriculum.model.po.OrdersDetail;
import com.curriculum.model.po.User;
import com.curriculum.model.vo.ShoppingCartVO;
import com.curriculum.service.OrderMainService;
import com.curriculum.service.OrdersDetailService;
import com.curriculum.utils.SnowFlakeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单主表 服务实现类
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
@Service
@Slf4j
public class OrderMainServiceImpl extends ServiceImpl<OrderMainMapper, OrderMain> implements OrderMainService {
    @Autowired
    private  OrdersDetailService ordersDetailService;
    /**
     * 提交订单
     * @param shoppingCartVOList 购物车商品
     * @param user
     * @return 生成订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderMain submitOrder(List<ShoppingCartVO> shoppingCartVOList, User user) {
        if(CollUtil.isEmpty(shoppingCartVOList)){
            CurriculumException.cast(MessageConstant.DATA_ERROR);
        }

        double totalPrice = shoppingCartVOList.stream().mapToDouble(s -> s.getNumber() * s.getActualPrice()).sum();

        // 3.主生成订单
        OrderMain orderMain = new OrderMain();
        orderMain.setCreateDate(LocalDateTime.now());
        orderMain.setStatus(OrderStatusEnum.UNPAID.getCode());
        orderMain.setUserId(user.getId());
        orderMain.setId(SnowFlakeUtils.getInstance().nextID());
        orderMain.setTotalPrice(totalPrice);
        orderMain.setOrderName(user.getNickname() + orderMain.getId());
        this.save(orderMain);

        // 4.保存子订单数据
        AtomicInteger count = new AtomicInteger(0);
        List<OrdersDetail> ordersDetailList = shoppingCartVOList.stream().map(s -> {
            OrdersDetail ordersDetail = new OrdersDetail();
            ordersDetail.setId((orderMain.getId() << 3) + count.getAndIncrement()); // 构造唯一子订单ID
            ordersDetail.setMainId(orderMain.getId());
            ordersDetail.setName(s.getName());
            ordersDetail.setDescrip(s.getRemark());
            ordersDetail.setOutBusinessId(s.getShoppingId());
            ordersDetail.setUnitPrice(s.getPrice());
            ordersDetail.setOrderNumber(s.getNumber());
            ordersDetail.setTotalPrice(s.getPrice() * s.getNumber());
            ordersDetail.setCreateDate(LocalDateTime.now());
            ordersDetail.setShoppingType(s.getShoppingType());
            return ordersDetail;
        }).collect(Collectors.toList());

        ordersDetailService.saveBatch(ordersDetailList);
        return  orderMain;
    }
}
