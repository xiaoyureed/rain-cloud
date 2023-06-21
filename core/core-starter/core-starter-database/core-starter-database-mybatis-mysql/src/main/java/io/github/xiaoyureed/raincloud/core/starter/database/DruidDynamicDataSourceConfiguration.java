//package io.github.xiaoyureed.raincloud.core.starter.database;
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
//import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
//
///**
// * xiaoyureed@gmail.com
// */
//@Configuration
//@EnableAutoConfiguration(exclude = {
//    DruidDataSourceAutoConfigure.class
//})
//public class DruidDynamicDataSourceConfiguration {
//
//    @Configuration
//    @Import({
//        DruidDataSourceAutoConfigure.class
//    })
//    @ConditionalOnMissingBean({
//        DynamicDataSourceAutoConfiguration.class
//    })
//    public static class DruidDataSourceConditionalConfig {
//    }
//}
