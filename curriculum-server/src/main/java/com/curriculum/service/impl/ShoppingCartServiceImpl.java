package com.curriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.mapper.ShoppingCartMapper;
import com.curriculum.model.po.ShoppingCart;
import com.curriculum.service.IShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {

}
