package com.brave.erp.base.goods.service.constant;

/**
 * TODO
 *
 * @author <a href='1286998496@qq.com'>zhangyong</a>
 * @date 2022-03-12 23:47
 */
public class RedisLockConstants {

    private static final String SEPARATOR = ":";

    private static final String PREFIX = "erp-base-goods-service" + SEPARATOR;
    /**
     * 商品类目新增
     */
    public static final String GOODS_CATEGORY_ADD_OR_UPDATE = PREFIX + "goods.category.addOrUpdate" + SEPARATOR;


}
