package com.brave.erp.base.goods.service.mapper;

import com.brave.erp.base.goods.service.domain.BaseGoodsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TODO
 *
 * @author <a href='1286998496@qq.com'>zhangyong</a>
 * @date 2022-03-11 18:26
 */
@Mapper
public interface BaseGoodsCategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BaseGoodsCategory record);

    int insertSelective(BaseGoodsCategory record);

    BaseGoodsCategory selectByPrimaryKey(Long id);

    List<BaseGoodsCategory> selectByShopIdAndName(@Param("shopId") Long shopId, @Param("name") String name);

    int countByShopIdAndName(@Param("shopId") Long shopId, @Param("name") String name);

    int updateByPrimaryKeySelective(BaseGoodsCategory record);

    int updateByPrimaryKey(BaseGoodsCategory record);

    int batchInsert(@Param("list") List<BaseGoodsCategory> list);
}