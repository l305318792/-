package com.rabbiter.hospital.controller;

import com.rabbiter.hospital.mapper.OrderMapper;
import com.rabbiter.hospital.pojo.Orders;
import com.rabbiter.hospital.pojo.Patient;
import com.rabbiter.hospital.service.DoctorService;
import com.rabbiter.hospital.service.OrderService;
import com.rabbiter.hospital.service.PatientService;
import com.rabbiter.hospital.utils.JwtUtil;
import com.rabbiter.hospital.utils.PdfUtil;
import com.rabbiter.hospital.utils.ResponseData;
import com.rabbiter.hospital.utils.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("patient")
public class PatientController {

    private static final Logger Log = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;
    @Resource
    private OrderMapper orderMapper;

    /**
     * 登录数据验证
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData login(@RequestParam(value = "pId") int pId, @RequestParam(value = "pPassword") String pPassword) {
        Patient patient = this.patientService.login(pId, pPassword);
        if (patient != null) {
            Map<String,String> map = new HashMap<>();
            map.put("pName", patient.getPName());
            map.put("pId", String.valueOf(patient.getPId()));
            map.put("pCard", patient.getPCard());
            String token = JwtUtil.getToken(map);
            map.put("token", token);
            //response.setHeader("token", token);
            return ResponseData.success("登录成功", map);
        } else {
            return ResponseData.fail("登录失败，密码或账号错误");
        }
    }
    /**
     * 根据科室查询所有医生信息
     */
    @RequestMapping("findDoctorBySection")
    public ResponseData findDoctorBySection(@RequestParam(value = "dSection") String dSection){
        return ResponseData.success("根据科室查询所有医生信息成功", this.doctorService.findDoctorBySection(dSection));
    }
    /**
     * 增加挂号信息
     */
    @RequestMapping("addOrder")
    @ResponseBody
    public ResponseData addOrder(Orders order, String arId){
        System.out.println(arId);
        if (this.orderService.addOrder(order, arId))
        return ResponseData.success("插入挂号信息成功");
        return ResponseData.fail("插入挂号信息失败");
    }
    /**
     * 根据pId查询挂号
     */
    @RequestMapping("findOrderByPid")
    public ResponseData findOrderByPid(@RequestParam(value = "pId") int pId){
        return ResponseData.success("返回挂号信息成功", this.orderService.findOrderByPid(pId)) ;
    }

    /**
     * 增加患者信息
     */
    @PostMapping("addPatient")
    @ResponseBody
    public ResponseData addPatient(
            @RequestParam("pId") Integer pId,
            @RequestParam("pName") String pName,
            @RequestParam("pPassword") String pPassword,
            @RequestParam("pGender") String pGender,
            @RequestParam("pEmail") String pEmail,
            @RequestParam("pPhone") String pPhone,
            @RequestParam("pCard") String pCard,
            @RequestParam("pBirthday") String pBirthday,
            @RequestParam("pState") Integer pState,
            @RequestParam("pAge") Integer pAge) {
        
        try {
            // 添加详细的请求参数日志
            Log.info("========== 开始处理注册请求 ==========");
            Log.info("收到的参数信息:");
            Log.info("pId = {}", pId);
            Log.info("pName = {}", pName);
            Log.info("pPassword = {}", "***");
            Log.info("pGender = {}", pGender);
            Log.info("pEmail = {}", pEmail);
            Log.info("pPhone = {}", pPhone);
            Log.info("pCard = {}", pCard);
            Log.info("pBirthday = {}", pBirthday);
            Log.info("pState = {}", pState);
            Log.info("pAge = {}", pAge);
            
            // 参数空值检查
            if (pId == null) {
                Log.error("pId 为 null");
                return ResponseData.fail("账号不能为空");
            }
            if (pName == null || pName.trim().isEmpty()) {
                Log.error("pName 为空");
                return ResponseData.fail("姓名不能为空");
            }
            if (pPassword == null || pPassword.trim().isEmpty()) {
                Log.error("pPassword 为空");
                return ResponseData.fail("密码不能为空");
            }
            
            // 输入验证
            if (pId <= 0) {
                Log.warn("账号必须大于0: {}", pId);
                return ResponseData.fail("账号必须大于0");
            }
            if (!ValidationUtil.isValidName(pName)) {
                Log.warn("姓名格式不正确: {}", pName);
                return ResponseData.fail("姓名格式不正确(2-20个字符,只能包含中文、英文字母)");
            }
            if (!ValidationUtil.isValidPassword(pPassword)) {
                Log.warn("密码格式不正确");
                return ResponseData.fail("密码格式不正确(6-20位,至少包含大小写字母和数字)");
            }
            if (!"男".equals(pGender) && !"女".equals(pGender)) {
                Log.warn("性别只能是'男'或'女': {}", pGender);
                return ResponseData.fail("性别只能是'男'或'女'");
            }
            if (!ValidationUtil.isValidEmail(pEmail)) {
                Log.warn("邮箱格式不正确: {}", pEmail);
                return ResponseData.fail("邮箱格式不正确");
            }
            if (!ValidationUtil.isValidPhone(pPhone)) {
                Log.warn("手机号格式不正确: {}", pPhone);
                return ResponseData.fail("手机号格式不正确");
            }
            if (!ValidationUtil.isValidIdCard(pCard)) {
                Log.warn("身份证号格式不正确: {}", pCard);
                return ResponseData.fail("身份证号格式不正确");
            }
            if (pBirthday == null || pBirthday.trim().isEmpty()) {
                Log.warn("出生日期不能为空");
                return ResponseData.fail("出生日期不能为空");
            }
            
            // 构造Patient对象
            Patient patient = new Patient();
            patient.setPId(pId);
            patient.setPName(pName);
            patient.setPPassword(pPassword);
            patient.setPGender(pGender);
            patient.setPEmail(pEmail);
            patient.setPPhone(pPhone);
            patient.setPCard(pCard);
            patient.setPBirthday(pBirthday);
            patient.setPState(pState);
            patient.setPAge(pAge);
            
            Log.info("所有参数验证通过，准备调用service");
            
            Boolean success = this.patientService.addPatient(patient);
            Log.info("service调用结果: {}", success);
            
            if (success) {
                Log.info("注册成功: {}", pId);
                return ResponseData.success("注册成功");
            } else {
                Log.warn("注册失败: {}", pId);
                return ResponseData.fail("注册失败!账号或邮箱已被占用");
            }
        } catch (Exception e) {
            Log.error("注册过程发生异常: ", e);
            return ResponseData.fail("注册失败：" + e.getMessage());
        } finally {
            Log.info("========== 注册请求处理结束 ==========");
        }
    }
    @GetMapping("/pdf")
    public void downloadPDF(HttpServletRequest request, HttpServletResponse response, int oId) throws Exception {
        Orders order = this.orderMapper.findOrderByOid(oId);
        PdfUtil.ExportPdf(request, response, order);
    }
    /**
     * 统计患者男女人数
     */
    @RequestMapping("patientAge")
    public ResponseData patientAge(){
        return  ResponseData.success("统计患者男女人数成功", this.patientService.patientAge());

    }
}
