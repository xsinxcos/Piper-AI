package com.zhuo.piper.controller;

import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.scheduler.DagBrain;
import com.zhuo.piper.service.IDagService;
import com.zhuo.piper.utils.SnowflakeIdGenerator;
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
    DagBrain dagBrain;


    @GetMapping("/test")
    public String test() {
        SimpleTaskExecution execution = new SimpleTaskExecution();
        dagBrain.enroll("1" , SnowflakeIdGenerator.getInstance().nextIdStr());
//        return object.get("value").toString();
        return null;
    }
}
