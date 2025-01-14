package com.rabbiter.hospital.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 病床实体类
 */
@TableName("bed")
public class Bed {
    @TableId(value = "b_id")
    @JsonProperty("bId")
    private Integer bId;
    @JsonProperty("pId")
    private Integer pId;
    @JsonProperty("dId")
    private Integer dId;
    @JsonProperty("bState")
    private Integer bState;
    @JsonProperty("bStart")
    private String bStart;
    @JsonProperty("bReason")
    private String bReason;
    @Version
    private Integer version;

    public Bed() {
    }

    public Bed(Integer bId, Integer pId, Integer dId, Integer bState, String bStart, String bReason, Integer version) {
        this.bId = bId;
        this.pId = pId;
        this.dId = dId;
        this.bState = bState;
        this.bStart = bStart;
        this.bReason = bReason;
        this.version = version;
    }

    public Integer getBId() {
        return bId;
    }

    public void setBId(Integer bId) {
        this.bId = bId;
    }

    public Integer getPId() {
        return pId;
    }

    public void setPId(Integer pId) {
        this.pId = pId;
    }

    public Integer getDId() {
        return dId;
    }

    public void setDId(Integer dId) {
        this.dId = dId;
    }

    public Integer getBState() {
        return bState;
    }

    public void setBState(Integer bState) {
        this.bState = bState;
    }

    public String getBStart() {
        return bStart;
    }

    public void setBStart(String bStart) {
        this.bStart = bStart;
    }

    public String getBReason() {
        return bReason;
    }

    public void setBReason(String bReason) {
        this.bReason = bReason;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Bed{" +
                "bId=" + bId +
                ", pId=" + pId +
                ", dId=" + dId +
                ", bState=" + bState +
                ", bStart='" + bStart + '\'' +
                ", bReason='" + bReason + '\'' +
                ", version=" + version +
                '}';
    }
}
