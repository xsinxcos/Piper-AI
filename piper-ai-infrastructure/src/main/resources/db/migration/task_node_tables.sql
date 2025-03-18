-- 任务节点定义表
CREATE TABLE task_node_definition (
    id VARCHAR(36) NOT NULL COMMENT '节点定义ID',
    name VARCHAR(100) NOT NULL COMMENT '节点名称',
    description VARCHAR(500) COMMENT '节点描述',
    node_type VARCHAR(50) NOT NULL COMMENT '节点类型',
    dsl_content TEXT NOT NULL COMMENT 'DSL内容',
    enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    created_by VARCHAR(50) NOT NULL COMMENT '创建人',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    updated_by VARCHAR(50) NOT NULL COMMENT '更新人',
    PRIMARY KEY (id),
    INDEX idx_node_type (node_type),
    INDEX idx_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务节点定义表';

-- 任务节点参数表
CREATE TABLE task_node_parameter (
    id VARCHAR(36) NOT NULL COMMENT '参数ID',
    node_id VARCHAR(36) NOT NULL COMMENT '节点定义ID',
    param_name VARCHAR(100) NOT NULL COMMENT '参数名称',
    param_key VARCHAR(100) NOT NULL COMMENT '参数键',
    param_type VARCHAR(50) NOT NULL COMMENT '参数类型',
    default_value VARCHAR(255) COMMENT '默认值',
    description VARCHAR(500) COMMENT '参数描述',
    required TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否必填',
    display_order INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    created_by VARCHAR(50) NOT NULL COMMENT '创建人',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    updated_by VARCHAR(50) NOT NULL COMMENT '更新人',
    PRIMARY KEY (id),
    INDEX idx_node_id (node_id),
    CONSTRAINT fk_parameter_node FOREIGN KEY (node_id) REFERENCES task_node_definition (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务节点参数表';

-- 任务节点连接表
CREATE TABLE task_node_connection (
    id VARCHAR(36) NOT NULL COMMENT '连接ID',
    source_node_id VARCHAR(36) NOT NULL COMMENT '源节点ID',
    target_node_id VARCHAR(36) NOT NULL COMMENT '目标节点ID',
    connection_name VARCHAR(100) COMMENT '连接名称',
    connection_type VARCHAR(50) NOT NULL COMMENT '连接类型',
    condition_expression TEXT COMMENT '条件表达式',
    priority INT NOT NULL DEFAULT 0 COMMENT '优先级',
    enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    created_by VARCHAR(50) NOT NULL COMMENT '创建人',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    updated_by VARCHAR(50) NOT NULL COMMENT '更新人',
    PRIMARY KEY (id),
    INDEX idx_source_node (source_node_id),
    INDEX idx_target_node (target_node_id),
    INDEX idx_enabled (enabled),
    CONSTRAINT fk_connection_source FOREIGN KEY (source_node_id) REFERENCES task_node_definition (id) ON DELETE CASCADE,
    CONSTRAINT fk_connection_target FOREIGN KEY (target_node_id) REFERENCES task_node_definition (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务节点连接表';

-- 任务节点工作流表
CREATE TABLE task_node_flow (
    id VARCHAR(36) NOT NULL COMMENT '工作流ID',
    flow_name VARCHAR(100) NOT NULL COMMENT '工作流名称',
    description VARCHAR(500) COMMENT '工作流描述',
    flow_type VARCHAR(50) NOT NULL COMMENT '工作流类型',
    enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    created_by VARCHAR(50) NOT NULL COMMENT '创建人',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    updated_by VARCHAR(50) NOT NULL COMMENT '更新人',
    PRIMARY KEY (id),
    INDEX idx_flow_type (flow_type),
    INDEX idx_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务节点工作流表';

-- 工作流与节点的关联表
CREATE TABLE task_flow_node (
    id VARCHAR(36) NOT NULL COMMENT '关联ID',
    flow_id VARCHAR(36) NOT NULL COMMENT '工作流ID',
    node_id VARCHAR(36) NOT NULL COMMENT '节点ID',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_flow_node (flow_id, node_id),
    CONSTRAINT fk_flow_node_flow FOREIGN KEY (flow_id) REFERENCES task_node_flow (id) ON DELETE CASCADE,
    CONSTRAINT fk_flow_node_node FOREIGN KEY (node_id) REFERENCES task_node_definition (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作流节点关联表'; 