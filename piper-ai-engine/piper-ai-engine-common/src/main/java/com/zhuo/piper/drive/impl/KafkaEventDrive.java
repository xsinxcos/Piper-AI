package com.zhuo.piper.drive.impl;

import com.zhuo.piper.drive.EventDriveConfiguration;
import com.zhuo.piper.drive.TopicMessage;
import jakarta.annotation.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventDrive implements EventDriveConfiguration {
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
