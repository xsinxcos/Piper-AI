package com.zhuo.piper.controller;

import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.scheduler.SchedulerCore;
import com.zhuo.piper.service.IDagService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class TestController {

    @Resource
    IDagService dagService;

    @Resource
    SchedulerCore schedulerCore;


    @GetMapping("/test")
    public String test() {
        SimpleTaskExecution execution = new SimpleTaskExecution();
        schedulerCore.run("1");
        return null;
    }
}
