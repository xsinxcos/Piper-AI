package com.zhuo.piper.process;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.save.DagService;
import com.zhuo.piper.scheduler.DAG;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class IfProcess implements Process{

    @Resource
    private DagService dagService;

    @Override
    public Object run(TaskExecution aTask, DAG dag) {
        String subDagId = aTask.get("sub_dag_id_" + dag);
        boolean condition = aTask.getBoolean("condition" + dag);
        String id = aTask.getId();
        // 逻辑判断是否满足条件
        // 子图展开
        if(condition){
            var subDag = dagService.loadSubDag(subDagId);
            dag.insertDAGAfterNode(id , subDag);
        }
        return null;
    }

    @Override
    public ProcessType type() {
        return null;
    }
}
