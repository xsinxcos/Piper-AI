package com.zhuo.piper.controller;

import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.scheduler.Scheduler;
import com.zhuo.piper.service.IDagService;
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
    IDagService dagService;

    public TestController(Scheduler schedule, IDagService dagService) {
        this.schedule = schedule;
        this.dagService = dagService;
    }


    @GetMapping("/test")
    public String test() {
        SimpleTaskExecution execution = new SimpleTaskExecution();
        dagService.load("1").ifPresent(item -> {
            try {
                schedule.run(item, execution);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        HashMap<String, Object> output = (HashMap<String, Object>) execution.getOutput();
        return output.get("1").toString();
    }
}
