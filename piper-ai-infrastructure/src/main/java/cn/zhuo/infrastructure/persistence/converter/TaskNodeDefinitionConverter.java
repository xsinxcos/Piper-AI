package cn.zhuo.infrastructure.persistence.converter;

import cn.zhuo.domain.task.model.TaskNodeDefinition;
import cn.zhuo.infrastructure.persistence.po.TaskNodeDefinitionPO;

/**
 * 任务节点定义转换器
 */
public class TaskNodeDefinitionConverter {

    /**
     * 将持久化对象转换为领域对象
     */
    public static TaskNodeDefinition toDomain(TaskNodeDefinitionPO po) {
        if (po == null) {
            return null;
        }
        
        return TaskNodeDefinition.reconstitute(
            po.getId(),
            po.getName(),
            po.getDescription(),
            po.getNodeType(),
            po.getDslContent(),
            po.getEnabled(),
            po.getVersion(),
            po.getCreatedAt(),
            po.getCreatedBy(),
            po.getUpdatedAt(),
            po.getUpdatedBy()
        );
    }

    /**
     * 将领域对象转换为持久化对象
     */
    public static TaskNodeDefinitionPO toPO(TaskNodeDefinition domain) {
        if (domain == null) {
            return null;
        }
        
        TaskNodeDefinitionPO po = new TaskNodeDefinitionPO();
        po.setId(domain.getId());
        po.setName(domain.getName());
        po.setDescription(domain.getDescription());
        po.setNodeType(domain.getNodeType());
        po.setDslContent(domain.getDslContent());
        po.setEnabled(domain.getEnabled());
        po.setVersion(domain.getVersion());
        po.setCreatedAt(domain.getCreatedAt());
        po.setCreatedBy(domain.getCreatedBy());
        po.setUpdatedAt(domain.getUpdatedAt());
        po.setUpdatedBy(domain.getUpdatedBy());
        
        return po;
    }
} 