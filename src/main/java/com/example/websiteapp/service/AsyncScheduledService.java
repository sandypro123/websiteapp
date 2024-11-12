package com.example.websiteapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: sandy
 * \* Date: 2024/11/12
 * \* Time: 17:49
 * \* Description:
 * \
 */
@Service
public class AsyncScheduledService {
    private final ThreadPoolTaskExecutor poolTaskExecutor;
    @Autowired
    public AsyncScheduledService(ThreadPoolTaskExecutor poolTaskExecutor){
        this.poolTaskExecutor=poolTaskExecutor;
    }
    public void executeTask(Runnable task){
        poolTaskExecutor.execute(task);
    }
}