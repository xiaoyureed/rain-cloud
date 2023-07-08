package io.github.xiaoyureed.raincloud.core.starter.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * xiaoyureed@gmail.com
 */
@Configuration
public class JacksonConfiguration {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String DATE_HOURS_PATTERN = "yyyy-MM-dd HH:mm";

    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.registerModule(javaTimeModule());

        //没有这个配置,如果一个类有 name、Name、NAME 三个属性,Jackson 默认只会处理 name,会忽略 Name 和 NAME。
        //加上这个配置后,Jackson 会处理 name、Name 和 NAME 三个属性,无论它们的大小写。
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

//        如果是作为api返回结果，不需要反序列化时：不设置activateDefaultTyping或者使用EXISTING_PROPERTY。
//        如果作为缓存等，需要反序列化时：一般使用 WRAPPER_ARRAY、WRAPPER_OBJECT、PROPERTY中的一种。如果不指定则默认使用的是WRAPPER_ARRAY。
//        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
//            ObjectMapper.DefaultTyping.NON_FINAL,
//            JsonTypeInfo.As.WRAPPER_ARRAY // 默认值, 可省略, 序列化后的 json 是一个数组, 两个元素, 第一个是全类名, 第二个是 json 对象
//        );


        return objectMapper;
    }

    @Bean
    public SimpleModule javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();

        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));

        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_PATTERN)));

        return module;
    }
}
