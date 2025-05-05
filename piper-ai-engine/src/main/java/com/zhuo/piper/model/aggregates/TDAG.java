package com.zhuo.piper.model.aggregates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 有向无环图 (Directed Acyclic Graph)
 */
@Getter
@Slf4j
public class TDAG implements Serializable {
    @Setter
    private String id;
    // 所有节点Map，Key为节点ID，Value为节点处理器
    private final Map<String, DagNode> nodes = new ConcurrentHashMap<>();

    // 节点连接关系，Key为源节点ID，Value为目标节点ID列表
    private final Map<String, List<String>> edges = new ConcurrentHashMap<>();

    // 入度表，用于拓扑排序
    private final Map<String, Integer> inDegrees = new ConcurrentHashMap<>();


    public DagNode getNode(String nodeId) {
        return nodes.get(nodeId);
    }

    /**
     * 添加节点
     *
     * @param nodeId 节点ID
     * @return 当前DAG实例
     */
    public TDAG addNode(String nodeId, DagNode node) {
        nodes.put(nodeId, node);
        inDegrees.putIfAbsent(nodeId, 0);
        return this;
    }

    /**
     * 添加边 (连接两个节点)
     *
     * @param fromNodeId 源节点ID
     * @param toNodeId   目标节点ID
     * @return 当前DAG实例
     */
    public TDAG addEdge(String fromNodeId, String toNodeId) {
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
        if (hasCycle(this)) {
            // 回退操作
            edges.get(fromNodeId).remove(toNodeId);
            inDegrees.put(toNodeId, inDegrees.get(toNodeId) - 1);
            throw new IllegalStateException("添加边后形成了环，无法构成DAG");
        }

        return this;
    }

    /**
     * 检测DAG中是否有环
     *
     * @param dag DAG实例
     * @return 是否有环
     */
    public static boolean hasCycle(TDAG dag) {
        // 复制一份入度表进行拓扑排序
        Map<String, Integer> inDegreeCopy = new HashMap<>(dag.getInDegrees());

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
            List<String> neighbors = dag.getEdges().getOrDefault(node, Collections.emptyList());
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
        return visitedCount < dag.getNodes().size();
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
    public List<DagNode> getZeroInDegreeAndNoLockNodes() {
        return getZeroInDegreeAndNoLockNodes(this);
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
    public TDAG safeRemoveNode(String nodeId) {
        return safeRemoveNode(this, nodeId);
    }

    /**
     * 安全移除节点
     * 只有当节点的入度为0时才允许移除
     *
     * @param dag    DAG实例
     * @param nodeId 要移除的节点ID
     * @return 当前DAG实例
     * @throws IllegalStateException 如果节点的入度不为0
     */
    public static TDAG safeRemoveNode(TDAG dag, String nodeId) {
        // 检查节点是否存在
        if (!dag.getNodes().containsKey(nodeId)) {
            throw new IllegalArgumentException("节点不存在：" + nodeId);
        }

        // 检查入度
        int inDegree = dag.getInDegrees().getOrDefault(nodeId, 0);
        if (inDegree > 0) {
            throw new IllegalStateException("无法移除节点 " + nodeId + "，因为其入度为 " + inDegree);
        }

        // 移除节点
        dag.getNodes().remove(nodeId);
        dag.getInDegrees().remove(nodeId);

        // 移除所有以该节点为起点的边
        List<String> outgoingEdges = dag.getEdges().remove(nodeId);
        if (outgoingEdges != null) {
            // 减少所有目标节点的入度
            for (String targetNodeId : outgoingEdges) {
                dag.getInDegrees().put(targetNodeId, dag.getInDegrees().get(targetNodeId) - 1);
            }
        }

        // 移除所有以该节点为终点的边
        for (Map.Entry<String, List<String>> entry : dag.getEdges().entrySet()) {
            entry.getValue().remove(nodeId);
        }

        return dag;
    }

    /**
     * 在指定节点后插入另一个DAG，并将子DAG的结束节点连接到目标节点原来的后续节点
     *
     * @param targetNodeId 目标节点ID，新的DAG将被插入到该节点之后
     * @param otherDAG     要插入的DAG
     * @return 当前DAG实例
     * @throws IllegalArgumentException 如果目标节点不存在
     * @throws IllegalStateException    如果插入后形成环
     */
    public TDAG insertDAGAfterNode(String targetNodeId, DAG otherDAG) {
        return insertDAGAfterNode(targetNodeId, otherDAG);
    }

    public TDAG deepCopy() {
        // 通过序列化进行深拷贝
        try {
            return SerializationUtils.clone(this);
        } catch (Exception e) {
            log.error("深拷贝失败", e);
            throw new RuntimeException("深拷贝失败", e);
        }
    }

    /**
     * 获取入度为0且未锁定的节点
     *
     * @param dag DAG实例
     * @return 入度为0且未锁定的节点ID列表
     */
    public List<DagNode> getZeroInDegreeAndNoLockNodes(TDAG dag) {
        List<DagNode> zeroInInDegreeNodes = new ArrayList<>();
        dag.getInDegrees().forEach((k, v) -> {
            if (v == 0) {
                zeroInInDegreeNodes.add(nodes.get(k));
            }
        });
        return zeroInInDegreeNodes;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DagNode implements Serializable {
        private String id;
        private String fromId;
        private String toId;
        private Integer type;
        private String config;
        private String className;

        public DagNode(String id, Integer type, String config, String className) {
            this.id = id;
            this.type = type;
            this.config = config;
            this.className = className;
        }
    }
}
