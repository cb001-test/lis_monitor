package com.maxwell.monitor.lisdata.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "switch_control")
public class SwitchControl {
    /**
     * 开关表主键
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 开关类型
     */
    @Column(name = "switch_type")
    private String switchType;

    /**
     * 开关状态
     */
    @Column(name = "switch_state")
    private String switchState;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 修改时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 获取开关表主键
     *
     * @return id - 开关表主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置开关表主键
     *
     * @param id 开关表主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取开关类型
     *
     * @return switch_type - 开关类型
     */
    public String getSwitchType() {
        return switchType;
    }

    /**
     * 设置开关类型
     *
     * @param switchType 开关类型
     */
    public void setSwitchType(String switchType) {
        this.switchType = switchType == null ? null : switchType.trim();
    }

    /**
     * 获取开关状态
     *
     * @return switch_state - 开关状态
     */
    public String getSwitchState() {
        return switchState;
    }

    /**
     * 设置开关状态
     *
     * @param switchState 开关状态
     */
    public void setSwitchState(String switchState) {
        this.switchState = switchState == null ? null : switchState.trim();
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

    /**
     * 获取修改时间
     *
     * @return update_date - 修改时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置修改时间
     *
     * @param updateDate 修改时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}