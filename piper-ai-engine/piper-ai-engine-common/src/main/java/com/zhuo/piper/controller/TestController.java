package com.zhuo.piper.controller;

import com.zhuo.piper.context.MapObject;
import com.zhuo.piper.save.DagService;
import com.zhuo.piper.struct.DAG;
import com.zhuo.piper.scheduler.SchedulerCenter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class TestController {
    @Resource
    SchedulerCenter schedulerCenter;

    @Resource
    DagService dagService;


    @GetMapping("/test")
    public String test(){
        DAG load = dagService.load("1");

        schedulerCenter.schedule(load , MapObject.getInstance());
        return "success";
    }
}
