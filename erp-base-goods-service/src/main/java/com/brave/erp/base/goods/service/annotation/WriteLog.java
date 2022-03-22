package com.brave.erp.base.goods.service.annotation;

import java.lang.annotation.*;

/**
 * 日志输出
 *
 * @author <a href='ygzhang@chinaums.com'>ygzhang</a>
 * @date 2020-05-22 22:11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WriteLog {

    /**
     * 日志索引名称，默认使用方法名
     * @return string
     */
    String value() default "";

    /**
     * 耗时
     * @return default true
     */
    boolean takeTime() default true;

}
