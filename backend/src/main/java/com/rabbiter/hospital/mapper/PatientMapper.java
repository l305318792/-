package com.rabbiter.hospital.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.rabbiter.hospital.pojo.Patient;
import org.apache.ibatis.annotations.Param;

public interface PatientMapper extends BaseMapper<Patient> {
    /**
     * 统计患者男女人数
     */
    Integer patientAge(@Param("startAge") int startAge, @Param("endAge") int endAge);

    /**
     * 根据条件统计患者数量
     */
    Long selectCount(Patient patient);

    /**
     * 插入新患者
     */
    int insert(Patient patient);
}
