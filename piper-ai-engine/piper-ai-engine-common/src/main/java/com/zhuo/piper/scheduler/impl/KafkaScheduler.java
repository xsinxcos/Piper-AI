package com.zhuo.piper.scheduler.impl;

import com.zhuo.piper.scheduler.SchedulerConfiguration;
import com.zhuo.piper.scheduler.TopicMessage;
import jakarta.annotation.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaScheduler implements SchedulerConfiguration {
    @Resource
    private KafkaTemplate<Object, Object> template;

    @Override
    public void schedule(TopicMessage topicMessage) {
        template.send(topicMessage.getTopicName(), topicMessage);
    }

    @KafkaListener(id = "piper-ai", topics = "Task")
    public void taskListen(String topicMsg) {
    }

}
