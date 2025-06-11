package com.curriculum.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.constant.MessageConstant;
import com.curriculum.context.AuthenticationContext;
import com.curriculum.enums.OrderStatusEnum;
import com.curriculum.exception.CurriculumException;
import com.curriculum.mapper.OrderMainMapper;
import com.curriculum.mapper.OrdersDetailMapper;
import com.curriculum.model.dto.OrderDTO;
import com.curriculum.model.po.OrderMain;
import com.curriculum.model.po.OrdersDetail;
import com.curriculum.model.po.User;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.ShoppingCartVO;
import com.curriculum.service.OrderMainService;
import com.curriculum.service.OrdersDetailService;
import com.curriculum.service.OrdersService;
import com.curriculum.utils.SnowFlakeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
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

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private OrdersDetailMapper ordersDetailMapper;
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

    /**
     * 更新订单状态
     * @param orderMain
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatus(OrderMain orderMain) {
        // 更新主订单
        this.updateById(orderMain);

        // 跟新子订单
        LambdaUpdateWrapper<OrdersDetail> updateWrapper = new LambdaUpdateWrapper<OrdersDetail>()
                .eq(OrdersDetail::getMainId, orderMain.getId())
                .set(OrdersDetail::getStatus, orderMain.getStatus());

        ordersDetailService.update(updateWrapper);
    }

    //用于订单页面的查询
    @Override
    public PageResult<OrdersDetail> PageQuery(OrderDTO orderDTO) {
        // 参数校验
        if (orderDTO.getPage() == null || orderDTO.getPage() < 1) {
            orderDTO.setPage(1L); // 设置默认页码
        }
        if (orderDTO.getPageSize() == null || orderDTO.getPageSize() < 1) {
            orderDTO.setPageSize(20L); // 设置默认每页大小
        }

        // 构建查询条件
        LambdaQueryWrapper<OrderMain> queryWrapper = new LambdaQueryWrapper<>();

        // 根据状态筛选（如果状态不为空）
        if (orderDTO.getStatus() != null && !orderDTO.getStatus().isEmpty()) {
            queryWrapper.eq(OrderMain::getStatus, orderDTO.getStatus());
        }
        queryWrapper.eq(OrderMain::getUserId, AuthenticationContext.getContext());
//        queryWrapper.eq(OrderMain::getUserId,"1932385841532006401");

        // 打印用户ID用于调试
        System.out.println("当前用户ID: " + AuthenticationContext.getContext());

        // 查询符合条件的订单ID列表
        List<Long> mainIds = orderMainMapper.selectObjs(queryWrapper.select(OrderMain::getId))
                .stream()
                .map(id -> Long.parseLong(id.toString()))
                .collect(Collectors.toList());

        // 打印查询到的订单ID数量用于调试
        System.out.println("查询到的订单ID数量: " + mainIds.size());
        System.out.println("订单ID列表: " + mainIds);

        // 构建订单详情查询条件
        LambdaQueryWrapper<OrdersDetail> queryWrapper1 = new LambdaQueryWrapper<>();

        // 查询mainId在mainIds列表中的订单详情
        if (!mainIds.isEmpty()) {
            queryWrapper1.in(OrdersDetail::getMainId, mainIds);
            System.out.println("已设置mainId查询条件");
        } else {
            // 如果没有订单ID，返回空结果
            System.out.println("没有找到符合条件的订单ID，返回空结果");
            return new PageResult<>(
                    Collections.emptyList(), // 空数据列表
                    0L,                      // 总记录数为0
                    orderDTO.getPage(),      // 当前页码
                    orderDTO.getPageSize()   // 每页大小
            );
        }

        // 设置分页参数
        Page<OrdersDetail> page = new Page<>(orderDTO.getPage(), orderDTO.getPageSize());

        // 执行查询并打印SQL日志
        Page<OrdersDetail> orderPage = ordersDetailMapper.selectPage(page, queryWrapper1);

        // 打印查询结果数量
        System.out.println("查询到的订单详情数量: " + orderPage.getRecords().size());
        System.out.println("总记录数: " + orderPage.getTotal());

        // 构建分页结果
        return new PageResult<>(
                orderPage.getRecords(), // 数据列表
                orderPage.getTotal(),   // 总记录数
                orderDTO.getPage(),     // 当前页码
                orderDTO.getPageSize()  // 每页大小
        );
    }

}
