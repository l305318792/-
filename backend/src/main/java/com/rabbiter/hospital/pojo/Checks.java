package com.rabbiter.hospital.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 检查项目实体类
 */
@TableName(value = "checks")
public class Checks {
    @TableId(value = "ch_id")
    @JsonProperty("chId")
    private Integer chId;
    @JsonProperty("chName")
    private String chName;
    @JsonProperty("chPrice")
    private Double chPrice;

    public Checks() {
    }

    public Checks(Integer chId, String chName, Double chPrice) {
        this.chId = chId;
        this.chName = chName;
        this.chPrice = chPrice;
    }

    public Integer getChId() {
        return chId;
    }

    public void setChId(Integer chId) {
        this.chId = chId;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public Double getChPrice() {
        return chPrice;
    }

    public void setChPrice(Double chPrice) {
        this.chPrice = chPrice;
    }

    @Override
    public String toString() {
        return "Checks{" +
                "chId=" + chId +
                ", chName='" + chName + '\'' +
                ", chPrice=" + chPrice +
                '}';
    }
}
