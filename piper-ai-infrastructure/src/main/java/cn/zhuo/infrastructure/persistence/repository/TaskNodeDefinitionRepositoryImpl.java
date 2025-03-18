package cn.zhuo.infrastructure.persistence.repository;

import cn.zhuo.domain.task.model.TaskNodeDefinition;
import cn.zhuo.domain.task.repository.TaskNodeDefinitionRepository;
import cn.zhuo.infrastructure.persistence.converter.TaskNodeDefinitionConverter;
import cn.zhuo.infrastructure.persistence.dao.TaskNodeDefinitionDAO;
import cn.zhuo.infrastructure.persistence.po.TaskNodeDefinitionPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 任务节点定义仓储实现
 */
@Repository
public class TaskNodeDefinitionRepositoryImpl implements TaskNodeDefinitionRepository {

    @Autowired
    private TaskNodeDefinitionDAO taskNodeDefinitionDAO;

    @Override
    public void save(TaskNodeDefinition taskNodeDefinition) {
        TaskNodeDefinitionPO po = TaskNodeDefinitionConverter.toPO(taskNodeDefinition);
        
        // 判断是新增还是更新
        TaskNodeDefinitionPO existPO = taskNodeDefinitionDAO.selectById(po.getId());
        if (existPO == null) {
            taskNodeDefinitionDAO.insert(po);
        } else {
            taskNodeDefinitionDAO.update(po);
        }
    }

    @Override
    public Optional<TaskNodeDefinition> findById(String id) {
        TaskNodeDefinitionPO po = taskNodeDefinitionDAO.selectById(id);
        return Optional.ofNullable(TaskNodeDefinitionConverter.toDomain(po));
    }

    @Override
    public List<TaskNodeDefinition> findByNodeType(String nodeType) {
        List<TaskNodeDefinitionPO> poList = taskNodeDefinitionDAO.selectByNodeType(nodeType);
        return poList.stream()
                .map(TaskNodeDefinitionConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskNodeDefinition> findAllEnabled() {
        List<TaskNodeDefinitionPO> poList = taskNodeDefinitionDAO.selectAllEnabled();
        return poList.stream()
                .map(TaskNodeDefinitionConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        taskNodeDefinitionDAO.deleteById(id);
    }
} 