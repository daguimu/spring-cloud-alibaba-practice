package com.alibaba.cloud.youxia.controller;

import com.alibaba.cloud.youxia.config.StarterConfig;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping(value = "/good")
@ConditionalOnProperty(name = DiscoveryConstant.SPRING_APPLICATION_NAME, havingValue = "gray-publish-good-server")
public class GoodController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;
    @Autowired
    private StarterConfig starterConfig;

    @GetMapping(value = "/deductingInventory")
    public String deductingInventory() throws UnknownHostException {
        System.out.println("开始扣减商品的库存，商品ID为:"+ RandomUtils.nextLong());
        System.out.println("创建物流订单为:"+ RandomUtils.nextLong());
        StringBuilder stringBuilder = new StringBuilder();
        InetAddress inetAddress = InetAddress.getLocalHost();
        String port = environment.getProperty("server.port");
        String type = environment.getProperty("spring.application.config.path");
        String applicationName=environment.getProperty("spring.application.name");
        if(environment.getProperty("spring.cloud.nacos.discovery.metadata.version").equals("1.0")){
            stringBuilder.append("交易服务路由类型：").append(applicationName).append(",").append("StableRoute").append(";");
        }else if(environment.getProperty("spring.cloud.nacos.discovery.metadata.version").equals("1.1")){
            stringBuilder.append("交易服务路由类型：").append(applicationName).append(",").append("GrayRoute").append(";");
        }
        stringBuilder.append(type).append(";");
        stringBuilder.append("商品服务提供方的IP地址为：" + inetAddress.getHostAddress() + ":" + port);
        String result=starterConfig.execute("http://gray-publish-logistics-server/logistics/createLogisticsOrder");
        stringBuilder.append(result);
        return stringBuilder.toString();
    }
}
