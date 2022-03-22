package com.brave.erp.base.goods.service.domain;

import lombok.Data;

import java.util.Date;

/**
 * 商品分类主表
 *
 * @author <a href='1286998496@qq.com'>zhangyong</a>
 * @date 2022-03-11 18:26
 */
@Data
public class BaseGoodsCategory {

    private Long id;

    /**
     * 类目编码
     */
    private String code;

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 门店ID
     */
    private Long shopId;

    /**
     * 状态码
     */
    private Integer statusCode;

    /**
     * 类目名称
     */
    private String name;

    /**
     * 图片icon名称
     */
    private String icon;

    /**
     * 排序号
     */
    private Integer sortNum;

    private Date addTime;

    private String addBy;

    private Date updateTime;

    private String updateBy;

    /**
     * 是否删除
     */
    private Boolean isDelete;

    /**
     * 描述
     */
    private String desc;
}