package com.curriculum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.context.AuthenticationContext;
import com.curriculum.enums.ProductTypeEnum;
import com.curriculum.mapper.ShoppingCartMapper;
import com.curriculum.model.dto.ShoppingCatChangeDTO;
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

//    @Override
//    public void saveShoppingCart(ShoppingCart shoppingCart) {
////        ShoppingCart sp = null;
//        Long shopID = shoppingCart.getShoppingId();
//        Long userId = AuthenticationContext.getContext();
//
//        //商品类型前端传入了
//        if (Objects.equals(shoppingCart.getShoppingType(), "01")){
//            Surroundings surroundings = surroundingsService.getById(shopID);
//            shoppingCart.setName(surroundings.getTitle());
//            shoppingCart.setPic(surroundings.getPic());
//            shoppingCart.setUserId(userId);
//            shoppingCart.setCreateTime(LocalDateTime.now());
//            shoppingCart.setVipPrice(Double.valueOf(surroundings.getVipPrice()));
//            shoppingCart.setPrice(Double.valueOf(surroundings.getPrice()));
//            shoppingCartMapper.insert(shoppingCart);
//        }else if (Objects.equals(shoppingCart.getShoppingType(), "02")){
//            Acgn surroundings = acgnService.getById(shopID);
//            shoppingCart.setName(surroundings.getTitle());
//            shoppingCart.setPic(surroundings.getPic());
//            shoppingCart.setUserId(userId);
//            shoppingCart.setCreateTime(LocalDateTime.now());
//            shoppingCart.setVipPrice(Double.valueOf(surroundings.getVipPrice()));
//            shoppingCart.setPrice(Double.valueOf(surroundings.getPrice()));
//            shoppingCartMapper.insert(shoppingCart);
//        }
//
//    }
    @Override
    public void saveShoppingCart(ShoppingCart shoppingCart) {
        // 获取关键参数
        Long shopID = shoppingCart.getShoppingId();
        Long userId = AuthenticationContext.getContext();
        String shoppingType = shoppingCart.getShoppingType();

        // 参数校验
        if (shopID == null || userId == null || shoppingType == null) {
            throw new IllegalArgumentException("购物车参数不完整");
        }

        // 构建查询条件：用户ID + 商品ID + 商品类型
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId)
                .eq(ShoppingCart::getShoppingId, shopID)
                .eq(ShoppingCart::getShoppingType, shoppingType);

        // 先查询是否已存在该商品
        ShoppingCart existingCart = shoppingCartMapper.selectOne(queryWrapper);

        if (existingCart != null) {
            // 已存在，数量加1
            existingCart.setNumber(existingCart.getNumber() + 1);
//            existingCart.setUpdateTime(LocalDateTime.now());
            shoppingCartMapper.updateById(existingCart);
        } else {
            // 不存在，执行原添加逻辑
            if ("01".equals(shoppingType)) {
                fillShoppingCart(shoppingCart, surroundingsService.getById(shopID), userId);
            } else if ("02".equals(shoppingType)) {
                fillShoppingCart(shoppingCart, acgnService.getById(shopID), userId);
            } else {
                throw new IllegalArgumentException("不支持的商品类型：" + shoppingType);
            }
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 填充购物车公共字段（抽取公共逻辑）
     */
    private void fillShoppingCart(ShoppingCart cart, Object product, Long userId) {
        if (product instanceof Surroundings) {
            Surroundings surroundings = (Surroundings) product;
            cart.setName(surroundings.getTitle());
            cart.setPic(surroundings.getPic());
            cart.setVipPrice(Double.valueOf(surroundings.getVipPrice()));
            cart.setPrice(Double.valueOf(surroundings.getPrice()));
        } else if (product instanceof Acgn) {
            Acgn acgn = (Acgn) product;
            cart.setName(acgn.getTitle());
            cart.setPic(acgn.getPic());
            cart.setVipPrice(Double.valueOf(acgn.getVipPrice()));
            cart.setPrice(Double.valueOf(acgn.getPrice()));
        } else {
            throw new IllegalArgumentException("不支持的商品类型对象");
        }
        cart.setUserId(userId);
        cart.setCreateTime(LocalDateTime.now());
        cart.setNumber(1);        // 新增时数量设为1
//        cart.setUpdateTime(LocalDateTime.now());
    }

    @Override
    public void changeNumber(ShoppingCatChangeDTO shoppingCatChangeDTO) {
//        if (Objects.equals(shoppingCatChangeDTO.getFlag(), "T")){
//            ShoppingCart shoppingCart = this.getById(shoppingCatChangeDTO.getId());
//            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
//            shoppingCartMapper.updateById(shoppingCart);
//        }else if (Objects.equals(shoppingCatChangeDTO.getFlag(), "F")){
//            ShoppingCart shoppingCart = this.getById(shoppingCatChangeDTO.getId());
//            shoppingCart.setNumber(shoppingCart.getNumber() - 1);
//            if (shoppingCart.getNumber() <= 0){
//                this.removeById(shoppingCatChangeDTO.getId());
//            }
//        }
        if (shoppingCatChangeDTO.getNumber() > 0){
            ShoppingCart shoppingCart = this.getById(shoppingCatChangeDTO.getId());
            shoppingCart.setNumber(shoppingCart.getNumber());
            shoppingCartMapper.updateById(shoppingCart);
        }else {
            this.removeById(shoppingCatChangeDTO.getId());
        }
    }

    @Override
    public List<ShoppingCartVO> listShoppingCartByIds(List<Long> ids) {
        return shoppingCartMapper.listShoppingCartByIds(ids,
                ProductTypeEnum.EXPO_PERFORMANCE.getCode(),
                ProductTypeEnum.PERIPHERAL.getCode());
    }

}
