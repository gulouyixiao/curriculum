package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.po.ShoppingCart;
import com.curriculum.model.vo.ShoppingCartVO;

import java.util.List;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 获取购物车商品数据
     * @param ids 购物车id
     * @return 结果
     */
    List<ShoppingCartVO> listShoppingCartByIds(List<Long> ids);

    void saveShoppingCart(ShoppingCart shoppingCart);

}
