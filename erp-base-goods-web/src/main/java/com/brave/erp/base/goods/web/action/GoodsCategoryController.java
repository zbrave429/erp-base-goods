package com.brave.erp.base.goods.web.action;

import com.brave.dubbo.trace.*;
import com.brave.erp.base.goods.api.request.BaseGoodsCategoryAddRequest;
import com.brave.erp.base.goods.api.response.BaseResponse;
import com.brave.erp.base.goods.api.service.BaseGoodsCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * TODO
 *
 * @author <a href='1286998496@qq.com'>zhangyong</a>
 * @date 2022-03-13 13:27
 */
@RestController
@RequestMapping("/goodsCategory")
@Slf4j
public class GoodsCategoryController {

    @DubboReference(version = "1.0.1", async = true)
    private BaseGoodsCategoryService baseGoodsCategoryService;

    @RequestMapping("/add")
    private Object add(@RequestParam("name") String name) throws ExecutionException, InterruptedException {

        Tracer tracer = new Tracer();
        tracer.setTraceId(TraceIdGenerator.getTraceId(IdGenEnum.CURRENT_TIME));
        tracer.setSpanId("0");
        TraceContext.set(tracer);
        MDC.put(TraceConstants.TRACE_ID, tracer.getTraceId());
        MDC.put(TraceConstants.SPAN_ID, tracer.getSpanId());

        BaseGoodsCategoryAddRequest request = new BaseGoodsCategoryAddRequest();
        request.setName(name);
        request.setCode("101-01");
        request.setOperatorName("brave");
        request.setOperateTime(new Date());
        request.setShopId(1L);
        request.setParentId(14L);

//        BaseResponse<Long> response = baseGoodsCategoryService.add(request);
        Future<BaseResponse<Long>> future = RpcContext.getServiceContext().asyncCall(()->baseGoodsCategoryService.add(request));
        BaseResponse<Long> response = future.get();
        log.info("resp:{}", response);

        TraceContext.remove();
        return response;
    }


}
