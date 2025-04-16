//package com.zhuo.piper.drive.zk;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.zhuo.piper.context.task.execution.TaskExecution;
//import com.zhuo.piper.context.MapContext;
//import com.zhuo.piper.drive.RpcServer;
//import com.zhuo.piper.drive.TopicMessage;
//import com.zhuo.piper.scheduler.SchedulerCenter;
//import com.zhuo.piper.task.impl.DefaultHandler;
//import com.zhuo.piper.worker.IWorker;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//public class ZkServer implements RpcServer {
//    @Resource
//    private ZkServiceFactory zkServiceFactory;
//
//    @Resource
//    private IWorker worker;
//
//    @Resource
//    ZkClient zkClient;
//
//
////    @Resource
////    SchedulerCenter schedulerCenter;
////
////    public ZkServer() {
////        this.context = MapContext.getInstance();
////    }
//
//
//    @PostMapping("/trigger")
//    public JsonNode trigger(@RequestBody TopicMessage tmsg) {
//        String msg = tmsg.getMsg();
//        //JsonNode jsonNode = JsonUtils.toJsonNode(msg);
//        //ITaskContext iTaskContext = JsonUtils.fromJsonNode(jsonNode, context.getClass());
//        TaskExecution context1 = MapContext.getInstance();
//        worker.run(context1, new DefaultHandler());
//        zkClient.fin();
//        return null;
//    }
//
//}
