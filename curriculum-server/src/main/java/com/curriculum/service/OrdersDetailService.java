package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.po.OrdersDetail;
import com.curriculum.model.vo.OrderDetailVO;

/**
 * <p>
 * 订单子表 服务类
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
public interface OrdersDetailService extends IService<OrdersDetail> {
    OrderDetailVO getByIdWpic(Long id);

//    void saveShoppingCart(ShoppingCart shoppingCart);
}
