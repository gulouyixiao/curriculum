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
import com.curriculum.mapper.*;
import com.curriculum.model.dto.OrderDTO;
import com.curriculum.model.po.*;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.ShoppingCartVO;
import com.curriculum.model.vo.orderMainVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private SurroundingsMapper surroundingsMapper;

    @Autowired
    private AcgnMapper acgnMapper;
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
    public PageResult<orderMainVO> PageQuery(OrderDTO orderDTO) {
        // 参数校验
        if (orderDTO.getPage() == null || orderDTO.getPage() < 1) {
            orderDTO.setPage(1L);
        }
        if (orderDTO.getPageSize() == null || orderDTO.getPageSize() < 1) {
            orderDTO.setPageSize(20L);
        }

        // 构建主订单查询条件
        LambdaQueryWrapper<OrderMain> queryWrapper = new LambdaQueryWrapper<>();

        // 根据状态筛选
        if (orderDTO.getStatus() != null && !orderDTO.getStatus().isEmpty()) {
            queryWrapper.eq(OrderMain::getStatus, orderDTO.getStatus());
        }
        queryWrapper.eq(OrderMain::getUserId, AuthenticationContext.getContext());

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

        // 构建订单详情查询条件（关联主订单ID）
        LambdaQueryWrapper<OrdersDetail> detailQuery = new LambdaQueryWrapper<>();
        if (!mainIds.isEmpty()) {
            detailQuery.in(OrdersDetail::getMainId, mainIds);
            System.out.println("已设置mainId查询条件");
        } else {
            System.out.println("没有找到符合条件的订单ID，返回空结果");
            return new PageResult<>(
                    Collections.emptyList(),
                    0L,
                    orderDTO.getPage(),
                    orderDTO.getPageSize()
            );
        }

        // 设置分页参数
        Page<OrdersDetail> page = new Page<>(orderDTO.getPage(), orderDTO.getPageSize());

        // 执行查询
        Page<OrdersDetail> detailPage = ordersDetailMapper.selectPage(page, detailQuery);
        System.out.println("查询到的订单详情数量: " + detailPage.getRecords().size());
        System.out.println("总记录数: " + detailPage.getTotal());

        // 转换为orderMainVO并获取图片路径
        List<orderMainVO> voList = convertToVOList(detailPage.getRecords());

        // 构建分页结果
        return new PageResult<>(
                voList,
                detailPage.getTotal(),
                orderDTO.getPage(),
                orderDTO.getPageSize()
        );
    }

    /**
     * 将订单详情转换为VO对象并获取图片路径
     */
    private List<orderMainVO> convertToVOList(List<OrdersDetail> details) {
        if (details == null || details.isEmpty()) {
            return Collections.emptyList();
        }

        // 按类型分组处理
        Map<String, List<OrdersDetail>> typeGroup = details.stream()
                .collect(Collectors.groupingBy(OrdersDetail::getShoppingType));

        // 处理周边商品（类型01）
        Map<Long, String> surroundingPicMap = new HashMap<>();
        if (typeGroup.containsKey("01")) {
            List<Long> ids = typeGroup.get("01").stream()
                    .map(OrdersDetail::getOutBusinessId)
                    .collect(Collectors.toList());
            if (!ids.isEmpty()) {
                // 确保从数据库获取的ID转换为Long类型
                List<Surroundings> surroundings = surroundingsMapper.selectList(
                        new LambdaQueryWrapper<Surroundings>()
                                .in(Surroundings::getId, ids)
                );

                for (Surroundings s : surroundings) {
                    try {
                        // 将String类型的ID转换为Long
                        Long id = Long.parseLong(s.getId());
                        surroundingPicMap.put(id, s.getPic());
                    } catch (NumberFormatException e) {
                        // 处理ID无法转换为Long的情况
                        log.error("无法将ID转换为Long: " + s.getId(), e);
                    }
                }
            }
        }

        // 处理漫展演出（类型02）
        Map<Long, String> acgnPicMap = new HashMap<>();
        if (typeGroup.containsKey("02")) {
            List<Long> ids = typeGroup.get("02").stream()
                    .map(OrdersDetail::getOutBusinessId)
                    .collect(Collectors.toList());
            if (!ids.isEmpty()) {
                // 确保从数据库获取的ID转换为Long类型
                List<Acgn> acgnList = acgnMapper.selectList(
                        new LambdaQueryWrapper<Acgn>()
                                .in(Acgn::getId, ids)
                );

                for (Acgn a : acgnList) {
                    try {
                        // 将String类型的ID转换为Long
                        Long id = Long.parseLong(a.getId());
                        acgnPicMap.put(id, a.getPic());
                    } catch (NumberFormatException e) {
                        // 处理ID无法转换为Long的情况
                        log.error("无法将ID转换为Long: " + a.getId(), e);
                    }
                }
            }
        }

        // 转换为VO列表
        return details.stream().map(detail -> {
            orderMainVO vo = new orderMainVO();
            vo.setId(detail.getId());
            vo.setCreateDate(detail.getCreateDate());
            vo.setStatus(detail.getStatus());
            vo.setName(detail.getName());
            vo.setUnitPrice(detail.getUnitPrice());
            vo.setOrderNumber(detail.getOrderNumber());

            // 设置图片路径
            if ("01".equals(detail.getShoppingType())) {
                vo.setPic(surroundingPicMap.getOrDefault(detail.getOutBusinessId(), ""));
            } else if ("02".equals(detail.getShoppingType())) {
                vo.setPic(acgnPicMap.getOrDefault(detail.getOutBusinessId(), ""));
            } else {
                vo.setPic("");
            }

            return vo;
        }).collect(Collectors.toList());
    }

}
