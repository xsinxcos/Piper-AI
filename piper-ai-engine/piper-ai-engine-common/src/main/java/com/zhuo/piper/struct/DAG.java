package com.zhuo.piper.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 有向无环图 (Directed Acyclic Graph)
 */
@Slf4j
@Component
public class DAG {
    // 所有节点Map，Key为节点ID，Value为节点处理器
    private final Map<String, DagNode> nodes = new ConcurrentHashMap<>();
    
    // 节点连接关系，Key为源节点ID，Value为目标节点ID列表
    private final Map<String, List<String>> edges = new ConcurrentHashMap<>();
    
    // 入度表，用于拓扑排序
    private final Map<String, Integer> inDegrees = new ConcurrentHashMap<>();

    @AllArgsConstructor
    @Getter
    public static class DagNode {
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
     * 获取所有节点
     * 
     * @return 节点Map
     */
    public Map<String, DagNode> getNodes() {
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
        List<String> zeroInInDegreeNodes = new ArrayList<>();
        inDegrees.forEach((k ,v) -> {
            if(v == 0 && !nodes.get(k).getIsLock()){
                zeroInInDegreeNodes.add(k);
            }
        });
        return zeroInInDegreeNodes;
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
        // 检查节点是否存在
        if (!nodes.containsKey(nodeId)) {
            throw new IllegalArgumentException("节点不存在：" + nodeId);
        }
        
        // 检查入度
        int inDegree = inDegrees.getOrDefault(nodeId, 0);
        if (inDegree > 0) {
            throw new IllegalStateException("无法移除节点 " + nodeId + "，因为其入度为 " + inDegree);
        }
        
        // 移除节点
        nodes.remove(nodeId);
        inDegrees.remove(nodeId);
        
        // 移除所有以该节点为起点的边
        List<String> outgoingEdges = edges.remove(nodeId);
        if (outgoingEdges != null) {
            // 减少所有目标节点的入度
            for (String targetNodeId : outgoingEdges) {
                inDegrees.put(targetNodeId, inDegrees.get(targetNodeId) - 1);
            }
        }
        
        // 移除所有以该节点为终点的边
        for (Map.Entry<String, List<String>> entry : edges.entrySet()) {
            entry.getValue().remove(nodeId);
        }
        
        return this;
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
        // 检查目标节点是否存在
        if (!nodes.containsKey(targetNodeId)) {
            throw new IllegalArgumentException("目标节点不存在：" + targetNodeId);
        }

        // 保存目标节点原来的后续节点
        List<String> originalNextNodes = edges.getOrDefault(targetNodeId, new ArrayList<>());
        
        // 获取要插入的DAG的所有节点和边
        Map<String, DagNode> otherNodes = otherDAG.getNodes();
        Map<String, List<String>> otherEdges = otherDAG.getEdges();

        // 添加所有新节点
        for (Map.Entry<String, DagNode> entry : otherNodes.entrySet()) {
            this.addNode(entry.getKey(), entry.getValue());
        }

        // 添加所有新边
        for (Map.Entry<String, List<String>> entry : otherEdges.entrySet()) {
            String fromNode = entry.getKey();
            for (String toNode : entry.getValue()) {
                this.addEdge(fromNode, toNode);
            }
        }

        // 获取要插入的DAG的入度为0的节点（起始节点）
        List<String> startNodes = otherDAG.getZeroInDegreeAndNoLockNodes();

        // 将目标节点连接到新DAG的所有起始节点
        for (String startNode : startNodes) {
            this.addEdge(targetNodeId, startNode);
        }

        // 找出子DAG的结束节点（出度为0的节点）
        List<String> endNodes = new ArrayList<>();
        for (String nodeId : otherNodes.keySet()) {
            if (!otherEdges.containsKey(nodeId) || otherEdges.get(nodeId).isEmpty()) {
                endNodes.add(nodeId);
            }
        }

        // 将子DAG的结束节点连接到目标节点原来的后续节点
        for (String endNode : endNodes) {
            for (String originalNextNode : originalNextNodes) {
                this.addEdge(endNode, originalNextNode);
            }
        }

        // 检查是否形成环
        if (this.hasCycle()) {
            // 回退操作：移除所有新添加的边和节点
            for (String startNode : startNodes) {
                edges.get(targetNodeId).remove(startNode);
                inDegrees.put(startNode, inDegrees.get(startNode) - 1);
            }
            
            for (String endNode : endNodes) {
                for (String originalNextNode : originalNextNodes) {
                    edges.get(endNode).remove(originalNextNode);
                    inDegrees.put(originalNextNode, inDegrees.get(originalNextNode) - 1);
                }
            }
            
            for (Map.Entry<String, List<String>> entry : otherEdges.entrySet()) {
                String fromNode = entry.getKey();
                for (String toNode : entry.getValue()) {
                    edges.get(fromNode).remove(toNode);
                    inDegrees.put(toNode, inDegrees.get(toNode) - 1);
                }
            }
            
            for (String nodeId : otherNodes.keySet()) {
                nodes.remove(nodeId);
                inDegrees.remove(nodeId);
                edges.remove(nodeId);
            }
            
            throw new IllegalStateException("插入DAG后形成了环，操作已回退");
        }

        return this;
    }

    public DAG deepCopy() {
        DAG copy = new DAG();
        for (Map.Entry<String, DagNode> entry : nodes.entrySet()) {
            copy.addNode(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, List<String>> entry : edges.entrySet()) {
            String fromNodeId = entry.getKey();
            for (String toNodeId : entry.getValue()) {
                copy.addEdge(fromNodeId, toNodeId);
            }
        }
        return copy;
    }

}
