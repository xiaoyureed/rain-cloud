package io.github.xiaoyureed.raincloud.core.starter.registry.nacos;

import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
@ComponentScan
//如果我们本身是在一个微服务中进行调用，本身已经在配置文件中配置了nacos地址，此时会直接获取到默认的这些配置， 就不需要再有nacos地址的配置了
//@EnableNacosDiscovery(globalProperties = @NacosProperties(serverAddr = "nacos的ip地址:nacos的端口"))
public class NacosStarterConfiguration {
    {
        log.debug("!!! nacos configuration ok.");
    }
}
