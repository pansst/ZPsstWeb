package com.psst.common.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.psst.common.service.task.TaskManager;

@Service // 注入spring容器
public class SpringManager implements ApplicationListener<ContextRefreshedEvent> {
    private static ApplicationContext applicationContext = null;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null){
            applicationContext = event.getApplicationContext();
            TaskManager.begin();
        }
    }
    
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}