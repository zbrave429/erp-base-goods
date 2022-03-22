package com.brave.erp.base.goods.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class ErpBaseGoodsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErpBaseGoodsWebApplication.class, args);
    }

}
