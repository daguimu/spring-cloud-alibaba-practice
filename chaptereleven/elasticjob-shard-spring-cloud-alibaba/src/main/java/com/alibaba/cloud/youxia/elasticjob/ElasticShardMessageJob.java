package com.alibaba.cloud.youxia.elasticjob;

import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ElasticShardMessageJob implements SimpleJob {
    @Autowired
    private Environment environment;

    Map<String,Long> executeTime=new ConcurrentHashMap<>();
    final static String KEY_TIME="execute_time";

    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("执行分布式Job的IP地址为："+environment.getProperty("server.address")+":"+
                environment.getProperty("server.port"));
        Long current_time=System.currentTimeMillis();
        System.out.println("当前Job执行的时间："+new Date().toString());
        Long last_time=0L;
        if(null==executeTime.get(KEY_TIME)){
            System.out.println("定时任务第一次执行，执行时间为："+new Date().toString());
        }else {
            last_time = executeTime.get(KEY_TIME);
            System.out.println("定时任务执行的间隔为："+(current_time-last_time)/1000+"秒");
        }
        executeTime.put(KEY_TIME,current_time);
        System.out.println("发送定时消息！");
    }
}
