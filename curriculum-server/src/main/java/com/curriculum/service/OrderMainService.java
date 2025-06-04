package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.po.OrderMain;
import com.curriculum.model.po.ShoppingCart;
import com.curriculum.model.po.User;
import com.curriculum.model.vo.ShoppingCartVO;

import java.util.List;

/**
 * <p>
 * 订单主表 服务类
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
public interface OrderMainService extends IService<OrderMain> {

    /**
     * 提交订单
     * @param shoppingCartVOList 购物车商品
     * @param user
     * @return 生成订单
     */
    OrderMain submitOrder(List<ShoppingCartVO> shoppingCartVOList, User user);

    /**
     * 更新订单状态
     * @param orderMain
     */
    void updateOrderStatus(OrderMain orderMain);

}
