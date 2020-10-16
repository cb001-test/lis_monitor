package com.maxwell.monitor.lisdata.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "log")
public class Log {
    /**
     * 主键ID
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取日志具体描述
     *
     * @return log_desc - 日志具体描述
     */
    public String getLogDesc() {
        return logDesc;
    }

    /**
     * 设置日志具体描述
     *
     * @param logDesc 日志具体描述
     */
    public void setLogDesc(String logDesc) {
        this.logDesc = logDesc == null ? null : logDesc.trim();
    }

    /**
     * 获取创建时间
     *
     * @return create_date - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}