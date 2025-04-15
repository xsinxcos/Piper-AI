package com.zhuo.piper.utils;

import com.zhuo.piper.struct.DAG;
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
            // 确保边的两个节点都在交集中
            if (intersectionNodes.contains(edge.getFrom()) && intersectionNodes.contains(edge.getTo())) {
                result.addEdge(edge.getFrom(), edge.getTo());
            }
        }

        return result;
    }

    /**
     * 将DAG的边转换为Edge对象集合
     * 
     * @param dag DAG实例
     * @return Edge集合
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
     * 边对象，用于表示DAG中的边
     */
    private static class Edge {
        private final String from;
        private final String to;

        public Edge(String from, String to) {
            this.from = from;
            this.to = to;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
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