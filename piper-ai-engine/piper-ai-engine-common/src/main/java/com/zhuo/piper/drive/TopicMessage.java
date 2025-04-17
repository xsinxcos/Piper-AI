package com.zhuo.piper.drive;

import com.zhuo.piper.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TopicMessage implements Serializable {
    private String topicName;

    private String trace;

    private String msg;

    public static <T> TopicMessage getInstance(Topic topic, String trace, T msg) {
        TopicMessage topicMessage = new TopicMessage();
        topicMessage.topicName = topic.getTopic();
        topicMessage.trace = trace;
        topicMessage.msg = JsonUtils.toJson(msg);
        return topicMessage;
    }

}
