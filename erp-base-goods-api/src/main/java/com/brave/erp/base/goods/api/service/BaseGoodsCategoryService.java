package com.brave.erp.base.goods.api.service;

import com.brave.erp.base.goods.api.request.BaseGoodsCategoryAddRequest;
import com.brave.erp.base.goods.api.response.BaseResponse;
import org.apache.dubbo.apidocs.annotations.ApiDoc;
import org.apache.dubbo.apidocs.annotations.ApiModule;

import java.util.List;

/**
 * 商品类目基础服务
 *
 * @author <a href='1286998496@qq.com'>zhangyong</a>
 * @date 2022-03-11 22:41
 */
@ApiModule(value = "商品类目基础服务",
        apiInterface = BaseGoodsCategoryService.class)
public interface BaseGoodsCategoryService {

    /**
     * 新增商品类目
     * @param request 请求
     * @return 响应
     */
    @ApiDoc(value = "新增商品类目", description = "用于新增商品类目")
    BaseResponse<Long> add(BaseGoodsCategoryAddRequest request);

    @ApiDoc(value = "批量新增商品类目", description = "用于批量新增商品类目")
    BaseResponse<List<Long>> batchAdd(List<BaseGoodsCategoryAddRequest> request);

    /**
     * 更新商品类目
     * @param request 请求
     * @return 响应
     */
    @ApiDoc(value = "更新商品类目", description = "用于更新商品类目")
    BaseResponse<Boolean> update(BaseGoodsCategoryAddRequest request);

    @ApiDoc(value = "批量删除商品类目", description = "用于批量删除商品类目")
    BaseResponse<Boolean> batchDeleteByIds(List<Long> ids);

    @ApiDoc(value = "删除商品类目", description = "用于删除商品类目")
    BaseResponse<Boolean> deleteById(Long id);

    @ApiDoc(value = "根据商品类目ID查询", description = "根据商品类目ID查询")
    BaseResponse<Boolean> queryById(Long id);

    @ApiDoc(value = "根据商品类目ID查询", description = "根据商品类目ID查询")
    BaseResponse<Boolean> queryListByIds(List<Long> ids);

    @ApiDoc(value = "根据商品类目ID查询", description = "根据商品类目ID查询")
    BaseResponse<Boolean> queryListByShopId(Long shopId);

    @ApiDoc(value = "根据商品类目ID查询", description = "根据商品类目ID查询")
    BaseResponse<Boolean> queryListByShopIds(List<Long> shopIds);

}
