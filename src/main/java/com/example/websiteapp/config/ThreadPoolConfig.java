package com.example.websiteapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: sandy
 * \* Date: 2024/11/12
 * \* Time: 17:49
 * \* Description:
 * \
 */
@Configuration
public class ThreadPoolConfig {
    @Bean("spiderExecutor")
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("spider-thread-");
        executor.initialize();
        return executor;
    }
}