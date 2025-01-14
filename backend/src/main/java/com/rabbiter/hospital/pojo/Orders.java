package com.rabbiter.hospital.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 订单实体类
 */
@TableName("orders")
public class Orders {
    @TableId(value = "o_id")
    @JsonProperty("oId")
    private Integer oId;
    @JsonProperty("pId")
    private Integer pId;
    @JsonProperty("dId")
    private Integer dId;
    @JsonProperty("oRecord")
    private String oRecord;
    @JsonProperty("oStart")
    private String oStart;
    @JsonProperty("oEnd")
    private String oEnd;
    @JsonProperty("oState")
    private Integer oState;
    @JsonProperty("oDrug")
    private String oDrug;
    @JsonProperty("oCheck")
    private String oCheck;
    @JsonProperty("oTotalPrice")
    private Double oTotalPrice;
    @JsonProperty("oPriceState")
    private Integer oPriceState;
    @JsonProperty("oAdvice")
    private String oAdvice;
    @JsonProperty("oPrice")
    @TableField(exist = false)
    private Double oPrice;
    @JsonProperty("oType")
    @TableField(exist = false)
    private Integer oType;
    @JsonProperty("arId")
    @TableField(exist = false)
    private String arId;

    @TableField(exist = false)
    private String pName;
    @TableField(exist = false)
    private String dName;
    @TableField(exist = false)
    private String dSection;
    @TableField(exist = false)
    private Patient patient;
    @TableField(exist = false)
    private Doctor doctor;
    @TableField(exist = false)
    private Integer countGender;
    @TableField(exist = false)
    private Integer countSection;

    public Orders() {
    }

    public Orders(Integer oId, Integer pId, Integer dId, String oStart, String oEnd, Integer oState, Double oPrice, Integer oType, String arId) {
        this.oId = oId;
        this.pId = pId;
        this.dId = dId;
        this.oStart = oStart;
        this.oEnd = oEnd;
        this.oState = oState;
        this.oPrice = oPrice;
        this.oType = oType;
        this.arId = arId;
    }

    public Integer getOId() {
        return oId;
    }

    public void setOId(Integer oId) {
        this.oId = oId;
    }

    public Integer getPId() {
        return pId;
    }

    public void setPId(Integer pId) {
        this.pId = pId;
    }

    public Integer getdId() {
        return dId;
    }

    public void setdId(Integer dId) {
        this.dId = dId;
    }

    public String getOStart() {
        return oStart;
    }

    public void setOStart(String oStart) {
        this.oStart = oStart;
    }

    public String getOEnd() {
        return oEnd;
    }

    public void setOEnd(String oEnd) {
        this.oEnd = oEnd;
    }

    public Integer getOState() {
        return oState;
    }

    public void setOState(Integer oState) {
        this.oState = oState;
    }

    public Double getOPrice() {
        return oPrice;
    }

    public void setOPrice(Double oPrice) {
        this.oPrice = oPrice;
    }

    public Integer getOType() {
        return oType;
    }

    public void setOType(Integer oType) {
        this.oType = oType;
    }

    public String getArId() {
        return arId;
    }

    public void setArId(String arId) {
        this.arId = arId;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdSection() {
        return dSection;
    }

    public void setdSection(String dSection) {
        this.dSection = dSection;
    }

    public String getORecord() {
        return oRecord;
    }

    public void setORecord(String oRecord) {
        this.oRecord = oRecord;
    }

    public String getODrug() {
        return oDrug;
    }

    public void setODrug(String oDrug) {
        this.oDrug = oDrug;
    }

    public String getOCheck() {
        return oCheck;
    }

    public void setOCheck(String oCheck) {
        this.oCheck = oCheck;
    }

    public Double getOTotalPrice() {
        return oTotalPrice;
    }

    public void setOTotalPrice(Double oTotalPrice) {
        this.oTotalPrice = oTotalPrice;
    }

    public Integer getOPriceState() {
        return oPriceState;
    }

    public void setOPriceState(Integer oPriceState) {
        this.oPriceState = oPriceState;
    }

    public String getOAdvice() {
        return oAdvice;
    }

    public void setOAdvice(String oAdvice) {
        this.oAdvice = oAdvice;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Integer getCountGender() {
        return countGender;
    }

    public void setCountGender(Integer countGender) {
        this.countGender = countGender;
    }

    public Integer getCountSection() {
        return countSection;
    }

    public void setCountSection(Integer countSection) {
        this.countSection = countSection;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "oId=" + oId +
                ", pId=" + pId +
                ", dId=" + dId +
                ", oStart='" + oStart + '\'' +
                ", oEnd='" + oEnd + '\'' +
                ", oState=" + oState +
                ", oPrice=" + oPrice +
                ", oType=" + oType +
                ", arId='" + arId + '\'' +
                ", pName='" + pName + '\'' +
                ", dName='" + dName + '\'' +
                ", dSection='" + dSection + '\'' +
                '}';
    }
}
