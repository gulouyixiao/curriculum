package com.curriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.enums.ProductTypeEnum;
import com.curriculum.mapper.ShoppingCartMapper;
import com.curriculum.model.po.ShoppingCart;
import com.curriculum.model.vo.ShoppingCartVO;
import com.curriculum.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import com.curriculum.model.po.Surroundings;
import com.curriculum.service.AcgnService;
import com.curriculum.service.SurroundingsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.time.LocalDateTime;
import java.util.Objects;

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
    @Autowired
    private AcgnService acgnService;
    @Autowired
    private SurroundingsService surroundingsService;

    /**
     * 获取购物车商品数据
     *
     * @param ids 购物车id
     * @return 结果
     */
    @Override
    public List<ShoppingCartVO> listShoppingCartByIds(List<Long> ids) {
        return shoppingCartMapper.listShoppingCartByIds(ids,
                ProductTypeEnum.EXPO_PERFORMANCE.getCode(),
                ProductTypeEnum.PERIPHERAL.getCode());
    }

    @Override
    public void saveShoppingCart(ShoppingCart shoppingCart) {
        ShoppingCart sp = null;
        Long shopID = shoppingCart.getShoppingId();
        if (Objects.equals(shoppingCart.getShoppingType(), "1")) {
            Surroundings surroundings = surroundingsService.getById(shopID);
            BeanUtils.copyProperties(surroundings, sp);
            sp.setCreateTime(LocalDateTime.now());
            sp.setNumber(shoppingCart.getNumber());
            sp.setShoppingType("1");
        } else if (Objects.equals(shoppingCart.getShoppingType(), "0")) {

        }

    }
}
