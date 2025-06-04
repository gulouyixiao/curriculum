package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.po.ShoppingCart;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
public interface IShoppingCartService extends IService<ShoppingCart> {

    void saveShoppingCart(ShoppingCart shoppingCart);
}
