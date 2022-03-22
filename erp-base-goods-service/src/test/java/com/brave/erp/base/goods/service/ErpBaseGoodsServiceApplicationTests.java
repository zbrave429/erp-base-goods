package com.brave.erp.base.goods.service;

import com.brave.erp.base.goods.api.request.BaseGoodsCategoryAddRequest;
import com.brave.erp.base.goods.api.response.BaseResponse;
import com.brave.erp.base.goods.api.service.BaseGoodsCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class ErpBaseGoodsServiceApplicationTests {

    @Autowired
    private BaseGoodsCategoryService baseGoodsCategoryService;

    @Test
    void addBaseGoodsCategoryTest() {
        BaseGoodsCategoryAddRequest request = new BaseGoodsCategoryAddRequest();
//        request.setName("");
        request.setCode("101-01");
        request.setOperatorName("brave");
        request.setOperateTime(new Date());
        request.setShopId(1L);
        request.setParentId(14L);
        BaseResponse<Long> response = baseGoodsCategoryService.add(request);
        System.out.println(response);
    }

}
