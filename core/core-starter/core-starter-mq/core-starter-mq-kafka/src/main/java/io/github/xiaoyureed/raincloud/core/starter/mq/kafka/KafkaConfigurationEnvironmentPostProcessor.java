package io.github.xiaoyureed.raincloud.core.starter.mq.kafka;

import java.util.Map;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.ClassPathResource;

/**
 * xiaoyureed@gmail.com
 * https://blog.csdn.net/weixin_43476020/article/details/128015972
 * https://www.51c51.com/danpianji/xinxi/84/975181.html
 */
public class KafkaConfigurationEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();

//        propertySources.stream().filter(source -> source instanceof OriginTrackedMapPropertySource)
//            .map(raw -> (OriginTrackedMapPropertySource) raw);

        YamlMapFactoryBean yamlMapFactoryBean = new YamlMapFactoryBean();
        // 返回 properties 对象
        //YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
//        Properties properties = factoryBean.getObject();

        yamlMapFactoryBean.setResources(new ClassPathResource("kafka.yml"));
        Map<String, Object> properties = yamlMapFactoryBean.getObject();

        System.out.println("!!! properties: " + properties);

        //Config resource 'class path resource [application.yml]' via location 'optional:classpath:/'
        /*
         * 配置源名称
         *  configurationProperties
         *  servletConfigInitParams
         *  servletContextInitParams
         *  systemProperties
         *  systemEnvironment
         *  random
         *  applicationConfig: [classpath:/config/application-druid.yml]
         *  applicationConfig: [classpath:/config/application.yml]
        * */
        propertySources.addBefore("Config resource 'class path resource [application.yml]' via location 'optional:classpath:/'",
            new OriginTrackedMapPropertySource("spring", properties));
    }
}
