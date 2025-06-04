package com.curriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.enums.ProductTypeEnum;
import com.curriculum.mapper.ShoppingCartMapper;
import com.curriculum.model.po.ShoppingCart;
import com.curriculum.model.vo.ShoppingCartVO;
import com.curriculum.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    /**
     * 获取购物车商品数据
     * @param ids 购物车id
     * @return 结果
     */
    @Override
    public List<ShoppingCartVO> listShoppingCartByIds(List<Long> ids) {
        return shoppingCartMapper.listShoppingCartByIds(ids,
                ProductTypeEnum.EXPO_PERFORMANCE.getCode(),
                ProductTypeEnum.PERIPHERAL.getCode());
    }
}
