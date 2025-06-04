package com.curriculum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curriculum.model.po.ShoppingCart;
import com.curriculum.model.vo.ShoppingCartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author lxr
 * @since 2025-06-03 18:22:01
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    /**
     * * 获取购物车商品数据
     * @param ids 购物车id
     * @param acgnType 演出类型
     * @param surroundingType 周边类型
     * @return
     */
    List<ShoppingCartVO> listShoppingCartByIds(@Param("ids") List<Long> ids, @Param("acgnType") String acgnType, @Param("surroundingType") String surroundingType);
}

