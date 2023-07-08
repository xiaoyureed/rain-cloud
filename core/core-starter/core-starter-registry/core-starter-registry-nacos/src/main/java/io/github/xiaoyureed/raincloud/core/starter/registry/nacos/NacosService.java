package io.github.xiaoyureed.raincloud.core.starter.registry.nacos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * xiaoyureed@gmail.com
 */
@Component
public class NacosService {
    @Autowired
    private NacosServiceManager nacosServiceManager;
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    public InstanceProperties getInstanceProperties(String instanceName) {
        // deprecated
//        NamingService namingService = nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
        NamingService namingService = nacosServiceManager.getNamingService();
//        NamingService namingService = null;
//        try {
//            namingService = NacosFactory.createNamingService(nacosDiscoveryProperties.getNacosProperties());
//        } catch (NacosException e) {
//            //todo
//            throw new RuntimeException(e);
//        }

        Instance instance = null;
        try {
            instance = namingService.selectOneHealthyInstance(instanceName, nacosDiscoveryProperties.getGroup());
        } catch (NacosException e) {
            //todo
            e.printStackTrace();
        }

        return new InstanceProperties().setPort(instance.getPort()).setIp(instance.getIp());
    }

    @Getter
    @Setter
    @Accessors
    public static class InstanceProperties {
        private String ip;
        private Integer port;
    }
}
