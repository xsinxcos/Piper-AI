package com.zhuo.piper.controller;

import com.zhuo.piper.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.save.DagService;
import com.zhuo.piper.scheduler.Scheduler;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("hello")
public class TestController {
    @Resource
    Scheduler schedule;

    @Resource
    DagService dagService;


    @GetMapping("/test")
    public String test() {
        SimpleTaskExecution execution = new SimpleTaskExecution();
        dagService.load("1").ifPresent(item -> {
            schedule.run(item ,execution);
        });
        HashMap<String, Object> output = (HashMap<String, Object>) execution.getOutput();
        return output.get("1").toString();
    }
}
