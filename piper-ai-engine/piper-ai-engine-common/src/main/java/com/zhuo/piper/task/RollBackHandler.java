package com.zhuo.piper.task;

public interface RollBackHandler {
    /**
     * 执行回滚
     *
     * @param taskId 任务ID
     * @param params 其他参数
     * @return 回滚结果
     */
    String executeRollBack(String taskId, String params);

    /**
     * 获取回滚状态
     *
     * @param taskId 任务ID
     * @return 回滚状态
     */
    String getRollBackStatus(String taskId);
}
