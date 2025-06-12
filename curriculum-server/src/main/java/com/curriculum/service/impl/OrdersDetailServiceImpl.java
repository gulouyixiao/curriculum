package com.curriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.mapper.*;
import com.curriculum.model.po.*;
import com.curriculum.model.vo.OrderDetailVO;
import com.curriculum.service.OrdersDetailService;
import com.curriculum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单子表 服务实现类
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
@Service
public class OrdersDetailServiceImpl extends ServiceImpl<OrdersDetailMapper, OrdersDetail> implements OrdersDetailService {
    @Autowired
    private OrdersDetailMapper ordersDetailMapper;

    @Autowired
    private SurroundingsMapper surroundingsMapper;

    @Autowired
    private AcgnMapper acgnMapper;

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public OrderDetailVO getByIdWpic(Long id) {
        // 1. 查询订单详情基本信息
        OrdersDetail detail = ordersDetailMapper.selectById(id);
        if (detail == null) {
            return null;
        }

        // 2. 创建VO对象并设置基本信息
        OrderDetailVO vo = new OrderDetailVO();
        vo.setId(detail.getId());
        vo.setShoppingType(detail.getShoppingType());
        vo.setName(detail.getName());
        vo.setUnitPrice(String.valueOf(detail.getUnitPrice()));
        vo.setOrderNumber(String.valueOf(detail.getOrderNumber()));
        vo.setCreateDate(detail.getCreateDate());
        vo.setStatus(detail.getStatus());
        vo.setTotalPrice(detail.getTotalPrice());

        // 3. 获取主订单信息（用于获取payNo）
        OrderMain orderMain = orderMainMapper.selectById(detail.getMainId());
        if (orderMain != null) {
            vo.setPayNo(orderMain.getOrderName()); // 假设orderName作为payNo

            // 4. 获取用户信息（用于获取cellphone和nickname）
            User user = userMapper.selectById(orderMain.getUserId());
            if (user != null) {
                vo.setCellphone(user.getCellphone());
                vo.setNickname(user.getNickname());
            }
        }

        // 5. 根据商品类型获取图片路径
        if ("01".equals(detail.getShoppingType())) {
            // 周边商品：查询surroundings表
            Surroundings surroundings = surroundingsMapper.selectById(String.valueOf(detail.getOutBusinessId()));
            if (surroundings != null) {
                vo.setPic(surroundings.getPic());
            }
        } else if ("02".equals(detail.getShoppingType())) {
            // 漫展演出：查询acgn表
            Acgn acgn = acgnMapper.selectById(String.valueOf(detail.getOutBusinessId()));
            if (acgn != null) {
                vo.setPic(acgn.getPic());
            }
        }

        return vo;
    }

//    @Override
//    public void saveShoppingCart(ShoppingCart shoppingCart) {
//
//    }
}
