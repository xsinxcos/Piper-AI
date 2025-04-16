package com.zhuo.piper.save;

import cn.zhuo.domain.common.util.SnowflakeIdGenerator;
import com.zhuo.piper.save.entity.DagEdge;
import com.zhuo.piper.save.entity.DagEntity;
import com.zhuo.piper.save.entity.DagNode;
import com.zhuo.piper.save.mapper.DagEdgeMapper;
import com.zhuo.piper.save.mapper.DagEntityMapper;
import com.zhuo.piper.save.mapper.DagNodeMapper;
import com.zhuo.piper.struct.DAG;
import com.zhuo.piper.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DagService{

    @Resource
    private DagNodeMapper nodeMapper;

    @Resource
    private DagEntityMapper dagEntityMapper;

    @Resource
    private DagEdgeMapper dagEdgeMapper;

    public void save(DAG dag){
        DagEntity dagEntity = DagEntity.builder()
                .id(SnowflakeIdGenerator.getInstance().nextIdStr())
                .version(0L)
                .build();
        dagEntityMapper.insert(dagEntity);

        List<DagNode> saveNodes = getDagNodes(dag, dagEntity);
        nodeMapper.insert(saveNodes);

        List<DagEdge> saveEdges = getDagEdges(dag);
        dagEdgeMapper.insert(saveEdges);

    }

    private static List<DagEdge> getDagEdges(DAG dag) {
        Map<String, List<String>> edges = dag.getEdges();
        List<DagEdge> saveEdges = new ArrayList<>();
        edges.forEach((k ,v) -> {
            v.forEach(toNodeId -> {
                DagEdge edge = DagEdge.builder()
                        .id(SnowflakeIdGenerator.getInstance().nextIdStr())
                        .fromNodeId(k)
                        .toNodeId(toNodeId)
                        .version(0L)
                        .build();
                saveEdges.add(edge);
            });
        });
        return saveEdges;
    }

    private List<DagNode> getDagNodes(DAG dag, DagEntity dagEntity) {
        Map<String, DAG.DagNode> nodes = dag.getNodes();
        List<DagNode> saveNodes = new ArrayList<>();
        nodes.forEach((k ,v) -> {
            String config = JsonUtils.toJson(v);
            DagNode dagNode = DagNode.builder()
                    .id(k)
                    .dagId(dagEntity.getId())
                    .type(v.getType())
                    .config(config)
                    .nodeClass(v.getClass().getName())
                    .version(0L)
                    .build();
            saveNodes.add(dagNode);
        });
        return saveNodes;
    }

    public  Optional<DAG>  load(String dagId){
        // 获取最新的DAG实体
        DagEntity dagEntity = dagEntityMapper.selectLatest(dagId);
        if (dagEntity == null) {
            return null;
        }
        return buildDAG(dagEntity.getId());
    }

    public Optional<DAG> loadSubDag(String subDagId){
        // 获取最新的DAG实体
        DagEntity dagEntity = dagEntityMapper.selectById(subDagId);
        if (dagEntity == null) {
            return null;
        }
        return buildDAG(dagEntity.getId());
    }

    private Optional<DAG>  buildDAG(String dagId){
        // 加载所有节点
        List<DagNode> dagNodes = nodeMapper.selectByDagId(dagId);
        // 加载所有边
        List<DagEdge> dagEdges = dagEdgeMapper.selectByDagId(dagId);

        // 创建新的DAG实例
        DAG dag = new DAG();

        // 重建节点
        for (DagNode dagNode : dagNodes) {
            // 从配置中重建节点
            DAG.DagNode node = new DAG.DagNode(dagNode.getId(), dagNode.getType(), dagNode.getConfig(), dagNode.getNodeClass());
            dag.addNode(dagNode.getId(), node);
        }

        // 重建边
        for (DagEdge dagEdge : dagEdges) {
            dag.addEdge(dagEdge.getFromNodeId(), dagEdge.getToNodeId());
        }
        return Optional.of(dag);
    }
}
