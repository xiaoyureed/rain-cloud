package io.github.xiaoyureed.raincloud.core.starter.mysql.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * xiaoyureed@gmail.com
 */
@Configuration
@MapperScan(basePackages = {
    "io.github.xiaoyureed.raincloud.**.mapper"
})
public class MybatisPlusConfiguration {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        return mybatisPlusInterceptor;
    }

    /**
     *
     * customize primary key generator
     */
//    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new IdentifierGenerator() {
            @Override
            public Number nextId(Object entity) {
                return null;
            }
        };
    }
}
