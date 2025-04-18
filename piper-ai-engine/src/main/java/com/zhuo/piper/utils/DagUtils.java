package com.zhuo.piper.utils;

import com.zhuo.piper.model.aggregates.DAG;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DAG工具类
 */
@Slf4j
@Component
public class DagUtils {

    /**
     * 获取多个DAG的交集
     * 交集包含所有DAG中共同存在的节点和边
     *
     * @param dags 多个DAG实例
     * @return 交集DAG
     */
    public static DAG getIntersection(List<DAG> dags) {
        if (dags == null || dags.isEmpty()) {
            return new DAG();
        }

        // 获取所有DAG的节点集合
        List<Set<String>> allNodeSets = dags.stream()
                .map(dag -> new HashSet<>(dag.getNodes().keySet()))
                .collect(Collectors.toList());

        // 获取所有DAG的边集合
        List<Set<Edge>> allEdgeSets = dags.stream()
                .map(DagUtils::getEdgeSet)
                .collect(Collectors.toList());

        // 计算节点的交集
        Set<String> intersectionNodes = allNodeSets.get(0);
        for (int i = 1; i < allNodeSets.size(); i++) {
            intersectionNodes.retainAll(allNodeSets.get(i));
        }

        // 计算边的交集
        Set<Edge> intersectionEdges = allEdgeSets.get(0);
        for (int i = 1; i < allEdgeSets.size(); i++) {
            intersectionEdges.retainAll(allEdgeSets.get(i));
        }

        // 创建新的DAG并添加交集的节点和边
        DAG result = new DAG();

        // 添加节点
        for (String nodeId : intersectionNodes) {
            // 从第一个DAG中获取节点信息
            DAG.DagNode node = dags.get(0).getNodes().get(nodeId);
            result.addNode(nodeId, node);
        }

        // 添加边
        for (Edge edge : intersectionEdges) {
            result.addEdge(edge.from, edge.to);
        }

        return result;
    }

    /**
     * 将DAG的边转换为Edge集合
     */
    private static Set<Edge> getEdgeSet(DAG dag) {
        Set<Edge> edgeSet = new HashSet<>();
        Map<String, List<String>> edges = dag.getEdges();
        for (Map.Entry<String, List<String>> entry : edges.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                edgeSet.add(new Edge(from, to));
            }
        }
        return edgeSet;
    }

    /**
     * 检测DAG中是否有环
     *
     * @param dag DAG实例
     * @return 是否有环
     */
    public static boolean hasCycle(DAG dag) {
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
     * 获取入度为0且未锁定的节点
     *
     * @param dag DAG实例
     * @return 入度为0且未锁定的节点ID列表
     */
    public static List<String> getZeroInDegreeAndNoLockNodes(DAG dag) {
        List<String> zeroInInDegreeNodes = new ArrayList<>();
        dag.getInDegrees().forEach((k, v) -> {
            if (v == 0 && !dag.getNode(k).getIsLock()) {
                zeroInInDegreeNodes.add(k);
            }
        });
        return zeroInInDegreeNodes;
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
    public static DAG safeRemoveNode(DAG dag, String nodeId) {
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
     * @param dag          主DAG实例
     * @param targetNodeId 目标节点ID，新的DAG将被插入到该节点之后
     * @param otherDAG     要插入的DAG
     * @return 当前DAG实例
     * @throws IllegalArgumentException 如果目标节点不存在
     * @throws IllegalStateException    如果插入后形成环
     */
    public static DAG insertDAGAfterNode(DAG dag, String targetNodeId, DAG otherDAG) {
        // 检查目标节点是否存在
        if (!dag.getNodes().containsKey(targetNodeId)) {
            throw new IllegalArgumentException("目标节点不存在：" + targetNodeId);
        }

        // 保存目标节点原来的后续节点
        List<String> originalNextNodes = dag.getEdges().getOrDefault(targetNodeId, new ArrayList<>());

        // 获取要插入的DAG的所有节点和边
        Map<String, DAG.DagNode> otherNodes = otherDAG.getNodes();
        Map<String, List<String>> otherEdges = otherDAG.getEdges();

        // 添加所有新节点
        for (Map.Entry<String, DAG.DagNode> entry : otherNodes.entrySet()) {
            dag.addNode(entry.getKey(), entry.getValue());
        }

        // 添加所有新边
        for (Map.Entry<String, List<String>> entry : otherEdges.entrySet()) {
            String fromNode = entry.getKey();
            for (String toNode : entry.getValue()) {
                dag.addEdge(fromNode, toNode);
            }
        }

        // 获取要插入的DAG的入度为0的节点（起始节点）
        List<String> startNodes = getZeroInDegreeAndNoLockNodes(otherDAG);

        // 将目标节点连接到新DAG的所有起始节点
        for (String startNode : startNodes) {
            dag.addEdge(targetNodeId, startNode);
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
                dag.addEdge(endNode, originalNextNode);
            }
        }

        // 检查是否形成环
        if (hasCycle(dag)) {
            // 回退操作：移除所有新添加的边和节点
            for (String startNode : startNodes) {
                dag.getEdges().get(targetNodeId).remove(startNode);
                dag.getInDegrees().put(startNode, dag.getInDegrees().get(startNode) - 1);
            }

            for (String endNode : endNodes) {
                for (String originalNextNode : originalNextNodes) {
                    dag.getEdges().get(endNode).remove(originalNextNode);
                    dag.getInDegrees().put(originalNextNode, dag.getInDegrees().get(originalNextNode) - 1);
                }
            }

            for (Map.Entry<String, List<String>> entry : otherEdges.entrySet()) {
                String fromNode = entry.getKey();
                for (String toNode : entry.getValue()) {
                    dag.getEdges().get(fromNode).remove(toNode);
                    dag.getInDegrees().put(toNode, dag.getInDegrees().get(toNode) - 1);
                }
            }

            for (String nodeId : otherNodes.keySet()) {
                dag.getNodes().remove(nodeId);
                dag.getInDegrees().remove(nodeId);
                dag.getEdges().remove(nodeId);
            }

            throw new IllegalStateException("插入DAG后形成了环，操作已回退");
        }

        return dag;
    }

    /**
     * 边类，用于表示DAG中的边
     */
    private static class Edge {
        final String from;
        final String to;

        Edge(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }
} 