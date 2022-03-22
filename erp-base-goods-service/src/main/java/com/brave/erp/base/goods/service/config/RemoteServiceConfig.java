package com.brave.erp.base.goods.service.config;

import com.brave.erp.system.merchant.api.service.ShopQueryService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author <a href='1286998496@qq.com'>zhangyong</a>
 * @date 2022-03-18 22:59
 */
@Configuration
public class RemoteServiceConfig {

    @DubboReference(version = "1.0.1", timeout = 1000)
    private ShopQueryService shopQueryService;

}
