package com.rabbiter.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.hospital.mapper.PatientMapper;
import com.rabbiter.hospital.pojo.Patient;
import com.rabbiter.hospital.service.PatientService;
import com.rabbiter.hospital.utils.Md5Util;
import com.rabbiter.hospital.utils.TodayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("PatientService")
public class PatientServiceImpl implements PatientService {
    protected static final Logger Log = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Resource
    private PatientMapper patientMapper;


    /**
     * 登录数据校验
     * */
    @Override
    public Patient login(int pId, String pPassword){
        Patient patient = this.patientMapper.selectById(pId);
        String password = Md5Util.getMD5(pPassword);
        if (patient == null) {
            return null;
        } else {
            if ((patient.getPPassword()).equals(password)) {
                return patient;
            }
        }
        return null;
    }
    /**
     * 分页模糊查询所有患者信息
     */
    @Override
    public HashMap<String, Object> findAllPatients(int pageNumber, int size, String query) {
        Page<Patient> page = new Page<>(pageNumber, size);
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.like("p_name", query).orderByDesc("p_state");
        IPage<Patient> iPage = this.patientMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("patients", iPage.getRecords()); //查询到的记录
        return hashMap;
    }

    /**
     * 删除患者信息
     */
    @Override
    public Boolean deletePatient(int pId) {
        Patient patient = new Patient();
        patient.setPId(pId);
        patient.setPState(0);
        this.patientMapper.updateById(patient);
        return true;
    }
    /**
     * 根据患者id查询患者信息
     */
    @Override
    public Patient findPatientById(int pId){
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id", pId);
        return this.patientMapper.selectOne(wrapper);
    }

    /**
     * 增加患者信息
     */
    @Override
    public Boolean addPatient(Patient patient) {
        Log.info("=================== 开始处理患者注册请求 ===================");
        Log.info("接收到的完整参数: {}", patient.toString());
        
        try {
            // 1. 测试数据库连接
            try {
                Patient testPatient = this.patientMapper.selectById(1);
                Log.info("数据库连接测试成功");
            } catch (Exception e) {
                Log.error("数据库连接测试失败: {}", e.getMessage(), e);
                return false;
            }
            
            // 2. 基本参数检查
            Integer pId = patient.getPId();
            if (pId == null || pId <= 0) {
                Log.error("无效的患者ID: {}", pId);
                return false;
            }
            
            // 3. 查询账号是否存在
            try {
                Patient existingPatient = this.patientMapper.selectById(pId);
                Log.info("账号查询结果: {}", existingPatient);
                
                if (existingPatient != null) {
                    Log.warn("账号已存在 - ID:{}, 状态:{}, 邮箱:{}", 
                        existingPatient.getPId(), 
                        existingPatient.getPState(),
                        existingPatient.getPEmail());
                    return false;
                }
                Log.info("账号检查通过，继续处理");
            } catch (Exception e) {
                Log.error("账号查询失败: {}", e.getMessage(), e);
                return false;
            }
            
            // 4. 检查邮箱
            try {
                LambdaQueryWrapper<Patient> emailWrapper = new LambdaQueryWrapper<>();
                emailWrapper.eq(Patient::getPEmail, patient.getPEmail());
                
                List<Patient> emailPatients = this.patientMapper.selectList(emailWrapper);
                Log.info("邮箱查询结果数量: {}", emailPatients.size());
                
                if (!emailPatients.isEmpty()) {
                    for (Patient p : emailPatients) {
                        Log.warn("发现邮箱记录 - ID:{}, 状态:{}, 邮箱:{}", 
                            p.getPId(), p.getPState(), p.getPEmail());
                    }
                    return false;
                }
                Log.info("邮箱检查通过，继续处理");
            } catch (Exception e) {
                Log.error("邮箱查询失败: {}", e.getMessage(), e);
                return false;
            }
            
            // 5. 处理密码
            String password = patient.getPPassword();
            if (password == null || password.trim().isEmpty()) {
                Log.error("密码不能为空");
                return false;
            }
            String hashedPassword = Md5Util.getMD5(password);
            patient.setPPassword(hashedPassword);
            Log.info("密码已加密处理");
            
            // 6. 处理生日和年龄
            String birthday = patient.getPBirthday();
            if (birthday == null || birthday.trim().isEmpty()) {
                Log.error("出生日期不能为空");
                return false;
            }
            
            int age = TodayUtil.getAgeByBirth(birthday);
            patient.setPAge(age);
            Log.info("年龄计算完成: {}", age);
            
            // 7. 设置状态为有效
            patient.setPState(1);
            
            // 8. 执行插入操作
            try {
                Log.info("准备执行插入操作，完整患者数据: {}", patient);
                int result = this.patientMapper.insert(patient);
                Log.info("插入操作完成，影响行数: {}", result);
                return result > 0;
            } catch (Exception e) {
                Log.error("插入操作失败: {}", e.getMessage(), e);
                return false;
            }
            
        } catch (Exception e) {
            Log.error("注册过程中发生未预期的错误: {}", e.getMessage(), e);
            return false;
        } finally {
            Log.info("=================== 患者注册处理结束 ===================");
        }
    }
    /**
     * 统计患者男女人数
     */
    public List<Integer> patientAge(){
        List<Integer> ageList = new ArrayList<>();
        Integer age1 = this.patientMapper.patientAge(0, 9);
        Integer age2 = this.patientMapper.patientAge(10, 19);
        Integer age3 = this.patientMapper.patientAge(20, 29);
        Integer age4 = this.patientMapper.patientAge(30, 39);
        Integer age5 = this.patientMapper.patientAge(40, 49);
        Integer age6 = this.patientMapper.patientAge(50, 59);
        Integer age7 = this.patientMapper.patientAge(60, 69);
        Integer age8 = this.patientMapper.patientAge(70, 79);
        Integer age9 = this.patientMapper.patientAge(80, 89);
        Integer age10 = this.patientMapper.patientAge(90, 99);
        ageList.add(age1);
        ageList.add(age2);
        ageList.add(age3);
        ageList.add(age4);
        ageList.add(age5);
        ageList.add(age6);
        ageList.add(age7);
        ageList.add(age8);
        ageList.add(age9);
        ageList.add(age10);
        return ageList;

    }


    }


