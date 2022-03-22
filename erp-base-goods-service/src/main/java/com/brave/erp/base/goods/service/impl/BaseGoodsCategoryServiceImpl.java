package com.brave.erp.base.goods.service.impl;

import com.brave.erp.base.goods.api.enums.ErrCodeEnum;
import com.brave.erp.base.goods.api.enums.GoodsCategoryStatusCodeEnum;
import com.brave.erp.base.goods.api.request.BaseGoodsCategoryAddRequest;
import com.brave.erp.base.goods.api.response.BaseResponse;
import com.brave.erp.base.goods.api.service.BaseGoodsCategoryService;
import com.brave.erp.base.goods.service.annotation.WriteLog;
import com.brave.erp.base.goods.service.constant.RedisLockConstants;
import com.brave.erp.base.goods.service.domain.BaseGoodsCategory;
import com.brave.erp.base.goods.service.mapper.BaseGoodsCategoryMapper;
import com.brave.erp.system.merchant.api.dto.ShopModelDto;
import com.brave.erp.system.merchant.api.enums.ShopDataFieldEnum;
import com.brave.erp.system.merchant.api.request.ShopQueryRequest;
import com.brave.erp.system.merchant.api.service.ShopQueryService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 商品类目操作
 *
 * @author <a href='1286998496@qq.com'>zhangyong</a>
 * @date 2022-03-11 22:43
 */
@Slf4j
@DubboService(version = "1.0.1")
public class BaseGoodsCategoryServiceImpl implements BaseGoodsCategoryService {

    private final BaseGoodsCategoryMapper baseGoodsCategoryMapper;

    private final RedissonClient redissonClient;

    @Autowired
    private ShopQueryService shopQueryService;

    public BaseGoodsCategoryServiceImpl(BaseGoodsCategoryMapper baseGoodsCategoryMapper,
                                        RedissonClient redissonClient) {
        this.baseGoodsCategoryMapper = baseGoodsCategoryMapper;
        this.redissonClient = redissonClient;
    }

    /**
     * 商品类别新增
     *
     * > 参数校验（做成通用配置类型）
     * > 门店校验（校验门店是否存在）
     * > 父级类别ID校验（门店需和本次新增数据门店相同）
     *
     * { 分布式锁（门店级别），保证操作原子性
     *   > 门店下类别名称唯一性校验
     *   > 构造insert请求参数
     * }
     *
     * > 判断是否插入成功
     * > 构造响应参数
     *
     * @param request 入参
     * @return 返回
     */
    @Override
    @WriteLog("addBaseGoodsCategory")
    @Transactional
    public BaseResponse<Long> add(BaseGoodsCategoryAddRequest request) {

        // 参数校验（后续做成通用配置类型）
        if(checkParams(request)){
            return BaseResponse.buildError(ErrCodeEnum.PARAMS_MISS_ERROR);
        }

        ShopQueryRequest shopQueryRequest = new ShopQueryRequest();
        shopQueryRequest.setShopId(request.getShopId());
        shopQueryRequest.setShopDataFields(Lists.newArrayList(ShopDataFieldEnum.BASIC));
        Future<com.brave.erp.system.merchant.api.response.BaseResponse<ShopModelDto>> future = RpcContext.getServiceContext().asyncCall(()-> shopQueryService.queryById(shopQueryRequest));

        if(checkParentId(request)){
            return BaseResponse.buildError(ErrCodeEnum.PARAMS_PARENT_NO_EXIST);
        }

        BaseGoodsCategory baseGoodsCategory = buildAddGoodsCategoryReq(request);
        RLock lock = null;
        try{
            lock = redissonClient.getLock(RedisLockConstants.GOODS_CATEGORY_ADD_OR_UPDATE + request.getShopId());
            // 使用分布式锁，防止并发导致名称重复
            if(!lock.tryLock(1,  TimeUnit.SECONDS)){
                // 尝试获取锁失败
                return BaseResponse.buildError(ErrCodeEnum.SYSTEM_BUSY_ERROR);
            }

            // 门店下类别名称唯一性校验
            if(!checkNameIsRepeat(request)){
                return BaseResponse.buildError(ErrCodeEnum.PARAMS_NAME_REPEAT, request.getName());
            }

            com.brave.erp.system.merchant.api.response.BaseResponse<ShopModelDto> shopDtoBaseResponse = future.get();
            if (Objects.isNull(shopDtoBaseResponse) || !shopDtoBaseResponse.isSuccess()
                    || Objects.isNull(shopDtoBaseResponse.getResult())
                    || Objects.isNull(shopDtoBaseResponse.getResult().getShopDto())){
                return BaseResponse.buildError(ErrCodeEnum.PARAMS_SHOP_NO_EXIST);
            }

            int n = baseGoodsCategoryMapper.insert(baseGoodsCategory);
            if (n <= 0){
                return BaseResponse.buildError(ErrCodeEnum.SYSTEM_ERROR);
            }
        } catch (Exception e){
            log.error("baseGoodsCategoryMapper.insert error !", e);
            return BaseResponse.buildError(ErrCodeEnum.SYSTEM_ERROR);
        } finally {
            if(!Objects.isNull(lock) && lock.isLocked() ){
                lock.unlock();
            }
        }

        return BaseResponse.defaultBuildSuccess(baseGoodsCategory.getId());
    }

    @Override
    public BaseResponse<List<Long>> batchAdd(List<BaseGoodsCategoryAddRequest> request) {
        return null;
    }

    @Override
    @WriteLog("updateBaseGoodsCategory")
    public BaseResponse<Boolean> update(BaseGoodsCategoryAddRequest request) {
        // 参数校验（后续做成通用配置类型）
        if(checkParams(request)){
            return BaseResponse.buildError(ErrCodeEnum.PARAMS_MISS_ERROR);
        }

        // TODO 门店校验（校验门店是否存在）,暂未搭建门店微服务系统，后续增加

        if(checkParentId(request)){
            return BaseResponse.buildError(ErrCodeEnum.PARAMS_PARENT_NO_EXIST);
        }

        // 检查待更新的类目是否存在
        BaseGoodsCategory baseGoodsCategory = baseGoodsCategoryMapper.selectByPrimaryKey(request.getId());

        if(Objects.isNull(baseGoodsCategory)){
            return BaseResponse.buildError(ErrCodeEnum.PARAMS_DATA_NO_EXIST);
        }

        buildUpdateGoodsCategoryReq(baseGoodsCategory, request);
        RLock lock = null;
        try{
            lock = redissonClient.getLock(RedisLockConstants.GOODS_CATEGORY_ADD_OR_UPDATE + request.getShopId());
            // 使用分布式锁，防止并发导致名称重复
            if(!lock.tryLock(1,  TimeUnit.SECONDS)){
                // 尝试获取锁失败
                return BaseResponse.buildError(ErrCodeEnum.SYSTEM_BUSY_ERROR);
            }

            // 门店下类别名称唯一性校验
            if(!checkNameIsRepeat4Update(request)){
                return BaseResponse.buildError(ErrCodeEnum.PARAMS_NAME_REPEAT, request.getName());
            }

            int n = baseGoodsCategoryMapper.updateByPrimaryKeySelective(baseGoodsCategory);
            if (n <= 0){
                return BaseResponse.buildError(ErrCodeEnum.SYSTEM_ERROR);
            }
        } catch (Exception e){
            log.error("baseGoodsCategoryMapper.insert error !", e);
            return BaseResponse.buildError(ErrCodeEnum.SYSTEM_ERROR);
        } finally {
            if(!Objects.isNull(lock) && lock.isLocked() ){
                lock.unlock();
            }
        }

        return BaseResponse.defaultBuildSuccess(Boolean.TRUE);
    }

    @Override
    public BaseResponse<Boolean> batchDeleteByIds(List<Long> ids) {
        return null;
    }

    @Override
    public BaseResponse<Boolean> deleteById(Long id) {
        return null;
    }

    @Override
    public BaseResponse<Boolean> queryById(Long id) {
        return null;
    }

    @Override
    public BaseResponse<Boolean> queryListByIds(List<Long> ids) {
        return null;
    }

    @Override
    public BaseResponse<Boolean> queryListByShopId(Long shopId) {
        return null;
    }

    @Override
    public BaseResponse<Boolean> queryListByShopIds(List<Long> shopIds) {
        return null;
    }

    private BaseGoodsCategory buildAddGoodsCategoryReq(BaseGoodsCategoryAddRequest request){

        BaseGoodsCategory baseGoodsCategory = new BaseGoodsCategory();
        baseGoodsCategory.setName(request.getName());
        baseGoodsCategory.setCode(request.getCode());
        baseGoodsCategory.setAddBy(request.getOperatorName());
        baseGoodsCategory.setAddTime(request.getOperateTime());
        baseGoodsCategory.setShopId(request.getShopId());
        baseGoodsCategory.setUpdateBy(request.getOperatorName());
        baseGoodsCategory.setUpdateTime(request.getOperateTime());
        baseGoodsCategory.setParentId(request.getParentId());
        baseGoodsCategory.setDesc(request.getDesc());
        baseGoodsCategory.setIcon(request.getIcon());
        baseGoodsCategory.setStatusCode(GoodsCategoryStatusCodeEnum.NORMAL.getCode());
        baseGoodsCategory.setIsDelete(Boolean.FALSE);
        return baseGoodsCategory;
    }

    private void buildUpdateGoodsCategoryReq(BaseGoodsCategory baseGoodsCategory,
                                             BaseGoodsCategoryAddRequest request){
        baseGoodsCategory.setName(request.getName());
        baseGoodsCategory.setUpdateBy(request.getOperatorName());
        baseGoodsCategory.setUpdateTime(request.getOperateTime());
        baseGoodsCategory.setDesc(request.getDesc());
        baseGoodsCategory.setIcon(request.getIcon());
        baseGoodsCategory.setStatusCode(GoodsCategoryStatusCodeEnum.NORMAL.getCode());
    }

    private boolean checkParams(BaseGoodsCategoryAddRequest request){
        return StringUtils.isBlank(request.getName())
                || request.getShopId() == null || request.getShopId() <= 0;
    }

    private boolean checkNameIsRepeat(BaseGoodsCategoryAddRequest request){
        int n = baseGoodsCategoryMapper.countByShopIdAndName(request.getShopId(), request.getName());
        return n <= 0;
    }

    private boolean checkNameIsRepeat4Update(BaseGoodsCategoryAddRequest request){
        List<BaseGoodsCategory> baseGoodsCategories = baseGoodsCategoryMapper.selectByShopIdAndName(request.getShopId(), request.getName());

        if(CollectionUtils.isEmpty(baseGoodsCategories)){
            return true;
        }

        return baseGoodsCategories.size() == 1
                && Objects.equals(baseGoodsCategories.get(0).getId(), request.getId());
    }

    private boolean checkParentId(BaseGoodsCategoryAddRequest request){
        if(request.getParentId() == null || request.getParentId() <= 0){
            return false;
        }

        BaseGoodsCategory baseGoodsCategory = baseGoodsCategoryMapper.selectByPrimaryKey(request.getParentId());

        return Objects.isNull(baseGoodsCategory)
                || !Objects.equals(baseGoodsCategory.getShopId(), request.getShopId());
    }
}
