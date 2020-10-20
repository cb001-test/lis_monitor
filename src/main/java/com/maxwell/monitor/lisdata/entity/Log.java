package com.maxwell.monitor.lisdata.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "log")
public class Log {
    /**
     * 主键ID
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 发生变化的表名
     */
    @Column(name = "table_name")
    private String tableName;

    /**
     * 发生变化的类型（增，删，改）
     */
    @Column(name = "op_type")
    private String opType;

    /**
     * 日志具体描述
     */
    @Column(name = "log_desc")
    private String logDesc;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;
}