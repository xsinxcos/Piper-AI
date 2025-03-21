package com.zhuo.piper.scheduler;

import com.zhuo.piper.process.Process;
import com.zhuo.piper.process.ProcessType;
import com.zhuo.piper.task.Handler;

import java.util.*;

/**
 * 有向无环图 (Directed Acyclic Graph)
 */
public class DAG {
    // 所有节点Map，Key为节点ID，Value为节点处理器
    private final Map<String, Handler<?>> nodes = new HashMap<>();
    
    // 节点连接关系，Key为源节点ID，Value为目标节点ID列表
    private final Map<String, List<String>> edges = new HashMap<>();
    
    // 节点类型映射，Key为节点ID，Value为节点类型
    private final Map<String, ProcessType> nodeTypes = new HashMap<>();
    
    // 入度表，用于拓扑排序
    private final Map<String, Integer> inDegrees = new HashMap<>();
    
    /**
     * 添加节点
     * 
     * @param nodeId 节点ID
     * @param handler 节点处理器
     * @param type 节点类型
     * @return 当前DAG实例
     */
    public DAG addNode(String nodeId, Handler<?> handler, ProcessType type) {
        nodes.put(nodeId, handler);
        nodeTypes.put(nodeId, type);
        inDegrees.putIfAbsent(nodeId, 0);
        return this;
    }
    
    /**
     * 添加节点
     * 
     * @param nodeId 节点ID
     * @param process 流程处理器
     * @return 当前DAG实例
     */
    public DAG addNode(String nodeId, Process<?> process) {
        nodes.put(nodeId, task -> process.run(task));
        nodeTypes.put(nodeId, process.type());
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
        if (hasCycle()) {
            // 回退操作
            edges.get(fromNodeId).remove(toNodeId);
            inDegrees.put(toNodeId, inDegrees.get(toNodeId) - 1);
            throw new IllegalStateException("添加边后形成了环，无法构成DAG");
        }
        
        return this;
    }
    
    /**
     * 检测图中是否有环
     * 
     * @return 是否有环
     */
    public boolean hasCycle() {
        // 复制一份入度表进行拓扑排序
        Map<String, Integer> inDegreeCopy = new HashMap<>(inDegrees);
        
        // 用于拓扑排序的队列
        Queue<String> queue = new LinkedList<>();
        
        // 将入度为0的节点加入队列
        for (Map.Entry<String, Integer> entry : inDegreeCopy.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        int visitedCount = 0;
        
        while (!queue.isEmpty()) {
            String node = queue.poll();
            visitedCount++;
            
            // 减少相邻节点的入度
            List<String> neighbors = edges.getOrDefault(node, Collections.emptyList());
            for (String neighbor : neighbors) {
                int degree = inDegreeCopy.get(neighbor) - 1;
                inDegreeCopy.put(neighbor, degree);
                
                // 如果入度变为0，则加入队列
                if (degree == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        // 如果访问的节点数量小于总节点数，说明存在环
        return visitedCount < nodes.size();
    }
    
    /**
     * 获取拓扑排序的节点顺序
     * 
     * @return 拓扑排序的节点ID列表
     */
    public List<String> getTopologicalOrder() {
        List<String> result = new ArrayList<>();
        Map<String, Integer> inDegreeCopy = new HashMap<>(inDegrees);
        Queue<String> queue = new LinkedList<>();
        
        // 将入度为0的节点加入队列
        for (Map.Entry<String, Integer> entry : inDegreeCopy.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        while (!queue.isEmpty()) {
            String node = queue.poll();
            result.add(node);
            
            List<String> neighbors = edges.getOrDefault(node, Collections.emptyList());
            for (String neighbor : neighbors) {
                int degree = inDegreeCopy.get(neighbor) - 1;
                inDegreeCopy.put(neighbor, degree);
                
                if (degree == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        return result.size() == nodes.size() ? result : Collections.emptyList();
    }
    
    /**
     * 根据节点ID获取下一个Handler
     * 
     * @param nodeId 当前节点ID
     * @return 下一个Handler的Map（key为节点ID，value为Handler）
     */
    public Map<String, Handler<?>> getNextHandler(String nodeId) {
        Map<String, Handler<?>> nextHandlers = new HashMap<>();
        
        // 获取当前节点的所有后续节点
        List<String> nextNodeIds = edges.getOrDefault(nodeId, Collections.emptyList());
        for (String nextNodeId : nextNodeIds) {
            nextHandlers.put(nextNodeId, nodes.get(nextNodeId));
        }
        
        return nextHandlers;
    }
    
    /**
     * 获取所有节点
     * 
     * @return 节点Map
     */
    public Map<String, Handler<?>> getNodes() {
        return Collections.unmodifiableMap(nodes);
    }
    
    /**
     * 获取所有边
     * 
     * @return 边关系Map
     */
    public Map<String, List<String>> getEdges() {
        return Collections.unmodifiableMap(edges);
    }
    
    /**
     * 获取节点类型
     * 
     * @return 节点类型Map
     */
    public Map<String, ProcessType> getNodeTypes() {
        return Collections.unmodifiableMap(nodeTypes);
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
     * 清空图
     */
    public void clear() {
        nodes.clear();
        edges.clear();
        nodeTypes.clear();
        inDegrees.clear();
    }
}
