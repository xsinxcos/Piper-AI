package com.zhuo.piper.struct;

import com.zhuo.piper.utils.DagUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 有向无环图 (Directed Acyclic Graph)
 */
@Slf4j
@Component
public class DAG implements Serializable {
    // 所有节点Map，Key为节点ID，Value为节点处理器
    @Getter
    private final Map<String, DagNode> nodes = new ConcurrentHashMap<>();
    
    // 节点连接关系，Key为源节点ID，Value为目标节点ID列表
    @Getter
    private final Map<String, List<String>> edges = new ConcurrentHashMap<>();
    
    // 入度表，用于拓扑排序
    @Getter
    private final Map<String, Integer> inDegrees = new ConcurrentHashMap<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DagNode implements Serializable{
        private String id;
        private Integer type;
        private String config;
        private String className;
        private Boolean isLock;

        public DagNode(String id, Integer type, String config, String className) {
            this.id = id;
            this.type = type;
            this.config = config;
            this.className = className;
            this.isLock = false;
        }

        public void lock(){
            this.isLock = true;
        }

        public void unLock() {
            this.isLock = false;
        }
    }

    public DagNode getNode(String nodeId) {
        return nodes.get(nodeId);
    }
    
    /**
     * 添加节点
     * 
     * @param nodeId 节点ID
     * @return 当前DAG实例
     */
    public DAG addNode(String nodeId, DagNode node) {
        nodes.put(nodeId, node);
        inDegrees.putIfAbsent(nodeId, 0);
        return this;
    }
    
    /**
     * 添加边 (连接两个节点)
     * 
     * @param fromNodeId 源节点ID
     * @param toNodeId 目标节点ID
     * @return 当前DAG实例
     */
    public DAG addEdge(String fromNodeId, String toNodeId) {
        // 确保节点存在
        if (!nodes.containsKey(fromNodeId) || !nodes.containsKey(toNodeId)) {
            throw new IllegalArgumentException("节点不存在：" + 
                    (!nodes.containsKey(fromNodeId) ? fromNodeId : toNodeId));
        }
        
        // 添加边
        edges.computeIfAbsent(fromNodeId, k -> new ArrayList<>()).add(toNodeId);
        
        // 增加目标节点的入度
        inDegrees.put(toNodeId, inDegrees.getOrDefault(toNodeId, 0) + 1);
        
        // 检测是否有环
        if (DagUtils.hasCycle(this)) {
            // 回退操作
            edges.get(fromNodeId).remove(toNodeId);
            inDegrees.put(toNodeId, inDegrees.get(toNodeId) - 1);
            throw new IllegalStateException("添加边后形成了环，无法构成DAG");
        }
        
        return this;
    }
    
    /**
     * 根据节点ID获取下一个Handler
     * 
     * @param nodeId 当前节点ID
     * @return 下一个Handler的Map（key为节点ID，value为Handler）
     */
    public Map<String, DagNode> getNextHandler(String nodeId) {
        Map<String, DagNode> nextHandlers = new HashMap<>();
        
        // 获取当前节点的所有后续节点
        List<String> nextNodeIds = edges.getOrDefault(nodeId, Collections.emptyList());
        for (String nextNodeId : nextNodeIds) {
            nextHandlers.put(nextNodeId, nodes.get(nextNodeId));
        }
        
        return nextHandlers;
    }

    /**
     * 获取图的节点数量
     * 
     * @return 节点数量
     */
    public int size() {
        return nodes.size();
    }

    /**
     * 获取 入度为 0 的节点
     */
    public List<String> getZeroInDegreeAndNoLockNodes(){
        return DagUtils.getZeroInDegreeAndNoLockNodes(this);
    }

    /**
     * 清空图
     */
    public void clear() {
        nodes.clear();
        edges.clear();
        inDegrees.clear();
    }

    /**
     * 安全移除节点
     * 只有当节点的入度为0时才允许移除
     * 
     * @param nodeId 要移除的节点ID
     * @return 当前DAG实例
     * @throws IllegalStateException 如果节点的入度不为0
     */
    public DAG safeRemoveNode(String nodeId) {
        return DagUtils.safeRemoveNode(this, nodeId);
    }

    /**
     * 在指定节点后插入另一个DAG，并将子DAG的结束节点连接到目标节点原来的后续节点
     * 
     * @param targetNodeId 目标节点ID，新的DAG将被插入到该节点之后
     * @param otherDAG 要插入的DAG
     * @return 当前DAG实例
     * @throws IllegalArgumentException 如果目标节点不存在
     * @throws IllegalStateException 如果插入后形成环
     */
    public DAG insertDAGAfterNode(String targetNodeId, DAG otherDAG) {
        return DagUtils.insertDAGAfterNode(this, targetNodeId, otherDAG);
    }

    public DAG deepCopy() {
        // 通过序列化进行深拷贝
        try {
            return SerializationUtils.clone(this);
        } catch (Exception e) {
            log.error("深拷贝失败", e);
            throw new RuntimeException("深拷贝失败", e);
        }
    }
}
