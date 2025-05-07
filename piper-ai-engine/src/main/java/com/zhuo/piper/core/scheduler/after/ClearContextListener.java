package com.zhuo.piper.core.scheduler.after;

import com.zhuo.piper.core.context.ContextStore;
import com.zhuo.piper.core.scheduler.DagBrain;
import com.zhuo.piper.core.scheduler.ListenTopic;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@Component
public class ClearContextListener implements PropertyChangeListener {
    @Resource
    private ContextStore contextStore;

    @Resource
    private DagBrain dagBrain;

    @PostConstruct
    void init() {
        dagBrain.loadTrigger(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ListenTopic.ALL_FINISH.equals(evt.getPropertyName())) {
            String jobId = (String) evt.getNewValue();
            contextStore.clear(jobId);
        }
    }
}
