package com.curriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.context.AuthenticationContext;
import com.curriculum.enums.ProductTypeEnum;
import com.curriculum.mapper.ShoppingCartMapper;
import com.curriculum.model.po.Acgn;
import com.curriculum.model.po.ShoppingCart;
import com.curriculum.model.po.Surroundings;
import com.curriculum.model.vo.ShoppingCartVO;
import com.curriculum.service.AcgnService;

import com.curriculum.service.ShoppingCartService;
import com.curriculum.service.SurroundingsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    private AcgnService acgnService;

    @Autowired
    private SurroundingsService surroundingsService;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public void saveShoppingCart(ShoppingCart shoppingCart) {
//        ShoppingCart sp = null;
        Long shopID = shoppingCart.getShoppingId();
        Long userId = AuthenticationContext.getContext();

        //商品类型前端传入了
        if (Objects.equals(shoppingCart.getShoppingType(), "01")){
            Surroundings surroundings = surroundingsService.getById(shopID);
            shoppingCart.setName(surroundings.getTitle());
            shoppingCart.setPic(surroundings.getPic());
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setVipPrice(Double.valueOf(surroundings.getVipPrice()));
            shoppingCart.setPrice(Double.valueOf(surroundings.getPrice()));
            shoppingCartMapper.insert(shoppingCart);
        }else if (Objects.equals(shoppingCart.getShoppingType(), "02")){
            Acgn surroundings = acgnService.getById(shopID);
            shoppingCart.setName(surroundings.getTitle());
            shoppingCart.setPic(surroundings.getPic());
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setVipPrice(Double.valueOf(surroundings.getVipPrice()));
            shoppingCart.setPrice(Double.valueOf(surroundings.getPrice()));
            shoppingCartMapper.insert(shoppingCart);
        }

    }

    @Override
    public List<ShoppingCartVO> listShoppingCartByIds(List<Long> ids) {
        return shoppingCartMapper.listShoppingCartByIds(ids,
                ProductTypeEnum.EXPO_PERFORMANCE.getCode(),
                ProductTypeEnum.PERIPHERAL.getCode());
    }

}
