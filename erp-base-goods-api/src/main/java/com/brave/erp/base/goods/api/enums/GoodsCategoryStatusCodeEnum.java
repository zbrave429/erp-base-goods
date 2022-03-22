package com.brave.erp.base.goods.api.enums;

/**
 * 商品类目状态码
 *
 * @author <a href='1286998496@qq.com'>zhangyong</a>
 * @date 2022-03-12 22:30
 */
public enum GoodsCategoryStatusCodeEnum {
    INIT(0, "初始"),
    NORMAL(1, "初始"),
    DISABLE(9, "禁用"),
    ;

    private final int code;

    private final String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    GoodsCategoryStatusCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
