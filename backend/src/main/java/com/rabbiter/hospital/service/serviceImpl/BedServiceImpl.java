package com.rabbiter.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.hospital.mapper.BedMapper;
import com.rabbiter.hospital.mapper.PatientMapper;
import com.rabbiter.hospital.mapper.DoctorMapper;
import com.rabbiter.hospital.pojo.Bed;
import com.rabbiter.hospital.pojo.Patient;
import com.rabbiter.hospital.pojo.Doctor;
import com.rabbiter.hospital.service.BedService;
import com.rabbiter.hospital.utils.TodayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service("BedService")
public class BedServiceImpl implements BedService {

    @Resource
    private BedMapper bedMapper;

    @Resource
    private PatientMapper patientMapper;

    @Resource
    private DoctorMapper doctorMapper;

    /**
     * 查找所有空床位
     */
    @Override
    public List<Bed> findNullBed(){
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        wrapper.select("b_id").eq("b_state", 0);
        return this.bedMapper.selectList(wrapper);
    }

    /**
     * 增加床位信息
     */
    @Override
    /**
     * 更新床位信息
     */
    public Boolean updateBed(Bed bed){
        // 1. 检查床位是否存在
        Bed existingBed = this.bedMapper.selectById(bed.getBId());
        if (existingBed == null) {
            return false;
        }
        
        // 2. 检查床位是否已被占用
        if (existingBed.getBState() == 1) {
            return false;
        }

        // 3. 验证患者是否存在且状态正常
        Patient patient = this.patientMapper.selectById(bed.getPId());
        if (patient == null || patient.getPState() != 1) {
            return false;
        }

        // 4. 验证医生是否存在且在职
        Doctor doctor = this.doctorMapper.selectById(bed.getDId());
        if (doctor == null || doctor.getdState() != 1) {
            return false;
        }

        // 5. 设置床位信息
        bed.setBStart(TodayUtil.getTodayYmd());
        bed.setBState(1);
        bed.setVersion(existingBed.getVersion());

        // 6. 更新床位信息
        return this.bedMapper.updateById(bed) > 0;
    }
    /**
     * 根据pId查询挂号
     */
    public List<Bed> findBedByPid(int pId){
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id", pId);
        return this.bedMapper.selectList(wrapper);
    }
    /**
     * 分页模糊查询所有检查信息
     */
    @Override
    public HashMap<String, Object> findAllBeds(int pageNumber, int size, String query) {
        Page<Bed> page = new Page<>(pageNumber, size);
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        if (query != null && !query.trim().isEmpty()) {
            wrapper.like("p_id", query);
        }
        IPage<Bed> iPage = this.bedMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("size", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("beds", iPage.getRecords()); //查询到的记录
        return hashMap;
    }
    /**
     * 根据id查找检查
     */
    @Override
    public Bed findBed(int bId){
        return this.bedMapper.selectById(bId);
    }
    /**
     * 增加床位信息
     */
    @Override
    public Boolean addBed(Bed bed){
        //如果床位号已存在则返回false
        Bed existingBed = this.bedMapper.selectById(bed.getBId());
        if (existingBed != null) {
            return false;
        }
        bed.setBState(0);
        bed.setPId(null);
        bed.setDId(null);
        this.bedMapper.insert(bed);
        return true;
    }
    /**
     * 删除床位信息
     */
    @Override
    public Boolean deleteBed(int bId) {
        Bed bed = this.bedMapper.selectById(bId);
        if (bed != null && bed.getBState() == 0) {
            this.bedMapper.deleteById(bId);
            return true;
        }
        return false;
    }
    /**
     * 清空床位信息
     */
    public Boolean emptyBed(int bId){
        Bed bed = this.bedMapper.selectById(bId);
        if (bed == null) {
            return false;
        }
        bed.setPId(null);
        bed.setDId(null);
        bed.setBReason(null);
        bed.setBStart(null);
        bed.setBState(0);
        this.bedMapper.updateById(bed);
        return true;
    }
    /**
     * 统计今天挂号人数
     */
    @Override
    public int bedPeople(String bStart){
        return this.bedMapper.bedPeople(bStart);
    }

}
