package com.zhuo.piper.persistence.repository;

import com.zhuo.piper.persistence.dao.DagEdgeMapper;
import com.zhuo.piper.persistence.dao.DagEntityMapper;
import com.zhuo.piper.persistence.dao.DagNodeMapper;
import com.zhuo.piper.persistence.po.DagEdgePO;
import com.zhuo.piper.persistence.po.DagEntityPO;
import com.zhuo.piper.persistence.po.DagNodePO;
import com.zhuo.piper.model.aggregates.DAG;
import com.zhuo.piper.service.IDagService;
import com.zhuo.piper.utils.JsonUtils;
import com.zhuo.piper.utils.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DagService implements IDagService {

    private final DagNodeMapper nodeMapper;

    private final DagEntityMapper dagEntityMapper;

    private final DagEdgeMapper dagEdgeMapper;

    private static List<DagEdgePO> getDagEdges(DAG dag) {
        Map<String, List<String>> edges = dag.getEdges();
        List<DagEdgePO> saveEdges = new ArrayList<>();
        edges.forEach((k, v) -> {
            v.forEach(toNodeId -> {
                DagEdgePO edge = DagEdgePO.builder()
                        .id(SnowflakeIdGenerator.getInstance().nextIdStr())
                        .fromNodeId(k)
                        .toNodeId(toNodeId)
                        .build();
                saveEdges.add(edge);
            });
        });
        return saveEdges;
    }

    public void save(DAG dag) {
        DagEntityPO dagEntity = DagEntityPO.builder()
                .id(SnowflakeIdGenerator.getInstance().nextIdStr())
                .build();
        dagEntityMapper.insert(dagEntity);

        List<DagNodePO> saveNodes = getDagNodes(dag, dagEntity);
        nodeMapper.insert(saveNodes);

        List<DagEdgePO> saveEdges = getDagEdges(dag);
        dagEdgeMapper.insert(saveEdges);

    }

    private List<DagNodePO> getDagNodes(DAG dag, DagEntityPO dagEntity) {
        Map<String, DAG.DagNode> nodes = dag.getNodes();
        List<DagNodePO> saveNodes = new ArrayList<>();
        nodes.forEach((k, v) -> {
            String config = JsonUtils.toJson(v);
            DagNodePO dagNode = DagNodePO.builder()
                    .id(k)
                    .dagId(dagEntity.getId())
                    .type(v.getType())
                    .config(config)
                    .nodeClass(v.getClass().getName())
                    .build();
            saveNodes.add(dagNode);
        });
        return saveNodes;
    }

    public Optional<DAG> load(String dagId) {
        // 获取最新的DAG实体
        DagEntityPO dagEntity = dagEntityMapper.selectLatest(dagId);
        if (dagEntity == null) {
            return null;
        }
        return buildDAG(dagEntity.getId());
    }

    public Optional<DAG> loadSubDag(String subDagId) {
        // 获取最新的DAG实体
        DagEntityPO dagEntity = dagEntityMapper.selectById(subDagId);
        if (dagEntity == null) {
            return null;
        }
        return buildDAG(dagEntity.getId());
    }

    private Optional<DAG> buildDAG(String dagId) {
        // 加载所有节点
        List<DagNodePO> dagNodes = nodeMapper.selectByDagId(dagId);
        // 加载所有边
        List<DagEdgePO> dagEdges = dagEdgeMapper.selectByDagId(dagId);

        // 创建新的DAG实例
        DAG dag = new DAG();

        // 重建节点
        for (DagNodePO dagNode : dagNodes) {
            // 从配置中重建节点
            DAG.DagNode node = new DAG.DagNode(dagNode.getId(), dagNode.getType(), dagNode.getConfig(), dagNode.getNodeClass());
            dag.addNode(dagNode.getId(), node);
        }

        // 重建边
        for (DagEdgePO dagEdge : dagEdges) {
            dag.addEdge(dagEdge.getFromNodeId(), dagEdge.getToNodeId());
        }

        dag.setId(dagId);
        return Optional.of(dag);
    }
}
