package com.brave.erp.base.goods.api.request;

import lombok.Data;
import org.apache.dubbo.apidocs.annotations.RequestParam;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 新增商品类目请求
 *
 * @author <a href='1286998496@qq.com'>zhangyong</a>
 * @date 2022-03-11 22:35
 */
@Data
public class BaseGoodsCategoryAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8438681445680841149L;

    @RequestParam(value = "ID", required = false, description = "商品类目ID，update必传" , allowableValues = "id >0")
    private Long id;

    /**
     * 类目编码
     */
    @NotNull
    @Size(max = 64)
    private String code;

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 门店ID
     */
    @NotNull
    private Long shopId;

    /**
     * 状态码
     */
    private Integer statusCode;

    /**
     * 类目名称
     */
    @NotNull
    @Size(min = 1, max = 128)
    private String name;

    /**
     * 图片icon名称
     */
    private String icon;

    /**
     * 排序号
     */
    private Integer sortNum;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作员
     */
    private String operatorName;

    /**
     * 描述
     */
    private String desc;
}
