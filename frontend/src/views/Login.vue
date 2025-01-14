<template>
    <div class="login-index" :style="backgroundDiv">
        <div class="mid-index">
            <i
                style="
                    top: 40px;
                    font-size: 28px;
                    left: 20px;
                    position: absolute;
                "
                class="iconfont icon-r-love"
            >
                登录医院管理系统
            </i>
            <el-form
                :model="loginForm"
                :rules="loginRules"
                ref="ruleForm"
                class="loginForm"
            >
                <el-form-item prop="id">
                    <!--必须绑定v-model输入框才能输入字符---->
                    <el-input v-model="loginForm.id">
                        <i
                            slot="prefix"
                            class="iconfont icon-r-user1"
                            style="font-size: 22px"
                        ></i>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" show-password>
                        <i
                            slot="prefix"
                            class="iconfont icon-r-lock"
                            style="font-size: 22px"
                        ></i>
                    </el-input>
                </el-form-item>

                <!-- 角色单选框 -->
                <el-form-item class="role">
                    <el-radio-group v-model="role" size="small">
                        <el-radio label="患者"></el-radio>
                        <el-radio label="医生"></el-radio>
                        <el-radio label="管理员"></el-radio>
                    </el-radio-group>
                </el-form-item>

                <el-form-item class="btns">
                    <el-button
                        type="primary"
                        style="font-size: 18px"
                        @click="submitLoginForm('ruleForm')"
                    >
                        <i
                            class="iconfont icon-r-yes"
                            style="font-size: 20px"
                        ></i>
                        登录</el-button
                    >
                    <el-button
                        type="info"
                        style="font-size: 18px"
                        @click="registerFormVisible = true"
                    >
                        <i
                            class="iconfont icon-r-add"
                            style="font-size: 20px"
                        ></i>
                        注册新账号</el-button
                    >
                </el-form-item>
            </el-form>
        </div>

        <!-- 注册对话框 -->
        <el-dialog title="用户注册" :visible.sync="registerFormVisible">
            <el-form
                class="findPassword"
                :model="registerForm"
                :rules="registerRules"
                ref="registerForm"
            >
                <el-form-item label="账号" label-width="80px" prop="pId">
                    <el-input v-model="registerForm.pId"></el-input>
                </el-form-item>
                <el-form-item label="性别" label-width="80px">
                    <el-radio v-model="registerForm.pGender" label="男"
                        >男</el-radio
                    >
                    <el-radio v-model="registerForm.pGender" label="女"
                        >女</el-radio
                    >
                </el-form-item>
                <el-form-item label="密码" label-width="80px" prop="pPassword">
                    <el-input v-model="registerForm.pPassword"></el-input>
                </el-form-item>
                <el-form-item label="姓名" label-width="80px" prop="pName">
                    <el-input v-model="registerForm.pName"></el-input>
                </el-form-item>
                <el-form-item
                    label="出生日期"
                    label-width="80px"
                    prop="pBirthday"
                >
                    <el-date-picker
                        v-model="registerForm.pBirthday"
                        type="date"
                        placeholder="选择日期"
                        value-format="yyyy-MM-dd"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="手机号" label-width="80px" prop="pPhone">
                    <el-input v-model="registerForm.pPhone"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" label-width="80px" prop="pEmail">
                    <el-input v-model="registerForm.pEmail"></el-input>
                </el-form-item>
                <el-form-item label="身份证号" label-width="80px" prop="pCard">
                    <el-input v-model="registerForm.pCard"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button
                    @click="registerFormVisible = false"
                    style="font-size: 18px"
                    ><i
                        class="iconfont icon-r-left"
                        style="font-size: 20px"
                    ></i>
                    取 消</el-button
                >
                <el-button
                    type="primary"
                    @click="registerClick()"
                    style="font-size: 18px"
                    ><i class="iconfont icon-r-yes" style="font-size: 20px"></i>
                    确 定</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>

<script>
import request from "@/utils/request.js";
import { setToken } from "@/utils/storage.js";
import { toLoad } from "@/utils/initialize.js";
import qs from 'qs';

export default {
    name: "Login",
    data() {
        // 账号验证
        var validateId = (rule, value, callback) => {
            if (!value) {
                callback(new Error("请输入账号"));
            } else {
                // 确保输入的是数字
                const reg = /^\d{3,50}$/;
                if (!reg.test(value)) {
                    callback(new Error("账号必须是3-50位的数字"));
                } else {
                    callback();
                }
            }
        };
        
        // 邮箱验证
        var validateEmail = (rule, value, callback) => {
            if (!value) {
                callback(new Error("请输入邮箱地址"));
            } else {
                const reg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                if (!reg.test(value)) {
                    callback(new Error("请输入正确的邮箱地址"));
                } else {
                    callback();
                }
            }
        };
        
        // 手机号验证
        var validateMobile = (rule, value, callback) => {
            if (value === undefined || value === '') {
                callback(new Error("请输入手机号"));
            } else {
                let reg = /^1[3-9]\d{9}$/;
                if (!reg.test(value)) {
                    callback(new Error("请输入正确的手机号"));
                }
                callback();
            }
        };
        
        // 身份证号验证
        var validateCard = (rule, value, callback) => {
            if (value === undefined || value === '') {
                callback(new Error("请输入身份证号"));
            } else {
                let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
                if (!reg.test(value)) {
                    callback(new Error("请输入正确的身份证号"));
                }
                callback();
            }
        };
        
        // 密码验证
        var validatePassword = (rule, value, callback) => {
            if (value === undefined || value === '') {
                callback(new Error("请输入密码"));
            } else {
                let reg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{6,20}$/;
                if (!reg.test(value)) {
                    callback(new Error("密码必须包含大小写字母和数字，长度6-20位"));
                }
                callback();
            }
        };
        
        // 姓名验证
        var validateName = (rule, value, callback) => {
            if (value === undefined || value === '') {
                callback(new Error("请输入姓名"));
            } else {
                let reg = /^[\u4e00-\u9fa5a-zA-Z]{2,20}$/;
                if (!reg.test(value)) {
                    callback(new Error("姓名只能包含中文、英文字母，长度2-20位"));
                }
                callback();
            }
        };

        // 日期验证
        var validateBirthday = (rule, value, callback) => {
            if (!value) {
                callback(new Error("请选择出生日期"));
            } else {
                const today = new Date();
                const birthDate = new Date(value);
                if (birthDate > today) {
                    callback(new Error("出生日期不能是未来日期"));
                } else {
                    callback();
                }
            }
        };

        return {
            //背景图片
            backgroundDiv: {
                backgroundImage: "url(" + require("../assets/doctor.jpeg") + ")",
                backgroundRepeat: "no-repeat",
                backgroundSize: "100% 100%",
            },
            // 登录表单数据
            loginForm: {
                id: "",
                password: "",
            },
            // 登录表单验证规则
            loginRules: {
                id: [
                    { required: true, message: "请输入账号编号", trigger: "blur" },
                    { min: 3, max: 50, message: "长度在 3到 50 个字符", trigger: "blur" }
                ],
                password: [
                    { required: true, message: "请输入密码", trigger: "blur" }
                ]
            },
            // 角色选择
            role: "患者",
            findRole: "患者",
            
            // 找回密码相关数据
            findFormVisible: false,
            findForm: {
                code: "",
                newPassword: "",
                checkPassword: "",
                pEmail: "",
            },
            
            // 注册相关数据
            registerFormVisible: false,
            registerForm: {
                pId: "",
                pName: "",
                pPassword: "",
                pGender: "男",
                pEmail: "",
                pPhone: "",
                pCard: "",
                pBirthday: "",
            },
            
            // 注册表单验证规则
            registerRules: {
                pId: [
                    { required: true, validator: validateId, trigger: "blur" }
                ],
                pPassword: [
                    { required: true, validator: validatePassword, trigger: "blur" }
                ],
                pName: [
                    { required: true, validator: validateName, trigger: "blur" }
                ],
                pPhone: [
                    { required: true, validator: validateMobile, trigger: "blur" }
                ],
                pEmail: [
                    { required: true, validator: validateEmail, trigger: "blur" }
                ],
                pCard: [
                    { required: true, validator: validateCard, trigger: "blur" }
                ],
                pBirthday: [
                    { required: true, validator: validateBirthday, trigger: "change" }
                ],
                pGender: [
                    { required: true, message: "请选择性别", trigger: "change" }
                ]
            },
            
            // 验证码相关
            totalTime: 60,
            content: "发送验证码",
            canClick: true
        };
    },
    mounted() {
        toLoad()
    },
    methods: {
        // 重置注册表单
        resetForm() {
            this.$refs.registerForm.resetFields();
        },
        
        // 点击注册确认按钮
        registerClick() {
            this.$refs.registerForm.validate((valid) => {
                if (valid) {
                    const formData = new FormData();
                    // 确保 pId 是数字类型
                    const numericPid = parseInt(this.registerForm.pId);
                    formData.append('pId', numericPid);
                    formData.append('pName', this.registerForm.pName);
                    formData.append('pPassword', this.registerForm.pPassword);
                    formData.append('pGender', this.registerForm.pGender);
                    formData.append('pEmail', this.registerForm.pEmail);
                    formData.append('pPhone', this.registerForm.pPhone);
                    formData.append('pCard', this.registerForm.pCard);
                    formData.append('pBirthday', this.registerForm.pBirthday);
                    // 确保 pState 是数字类型
                    formData.append('pState', 1);
                    // 确保 pAge 是数字类型
                    const age = this.calculateAge(this.registerForm.pBirthday);
                    formData.append('pAge', age);

                    console.log('发送注册请求,参数:', {
                        pId: numericPid,
                        pName: this.registerForm.pName,
                        pPassword: this.registerForm.pPassword,
                        pGender: this.registerForm.pGender,
                        pEmail: this.registerForm.pEmail,
                        pPhone: this.registerForm.pPhone,
                        pCard: this.registerForm.pCard,
                        pBirthday: this.registerForm.pBirthday,
                        pState: 1,
                        pAge: age
                    });

                    request.post("patient/addPatient", formData)
                        .then(res => {
                            console.log('注册响应:', res);
                            if (res.data && res.data.status === 200) {
                                this.$message.success("注册成功");
                                this.registerFormVisible = false;
                                this.resetForm();
                            } else {
                                this.$message.error(res.data?.msg || "注册失败");
                            }
                        })
                        .catch(err => {
                            console.error('注册请求失败:', err);
                            this.$message.error("注册失败：" + (err.response?.data?.msg || err.message || "未知错误"));
                        });
                } else {
                    console.log("表单验证失败");
                    return false;
                }
            });
        },
        
        // 计算年龄
        calculateAge(birthday) {
            if (!birthday) return 0;
            const today = new Date();
            const birthDate = new Date(birthday);
            let age = today.getFullYear() - birthDate.getFullYear();
            const m = today.getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }
            return age;
        },
        // 点击找回密码确认按钮
        findPassword(findForm) {
            this.$refs[findForm].validate((valid) => {
                if (valid) {
                    //如果是选中患者
                    if (this.findRole === "患者") {
                        request
                            .get("patient/findPassword", {
                                params: {
                                    pEmail: this.findForm.pEmail,
                                    pPassword: this.findForm.newPassword,
                                    code: this.findForm.code,
                                },
                            })
                            .then((res) => {
                                if (res.data.status !== 200)
                                    return this.$message.error(
                                        "验证码错误或者已过期！！！"
                                    );
                                this.$message.success("密码修改成功！！请登录");
                                this.findFormVisible = false;
                            });
                    }
                    //如果是选中管理员
                    if (this.findRole === "管理员") {
                        request
                            .get("admin/findPassword", {
                                params: {
                                    aEmail: this.findForm.pEmail,
                                    aPassword: this.findForm.newPassword,
                                    code: this.findForm.code,
                                },
                            })
                            .then((res) => {
                                if (res.data.status !== 200)
                                    return this.$message.error(
                                        "验证码错误或者已过期！！！"
                                    );
                                this.$message.success("密码修改成功！！请登录");
                                this.findFormVisible = false;
                            });
                    }
                    //如果是选中患者
                    if (this.findRole === "医生") {
                        request
                            .get("doctor/findPassword", {
                                params: {
                                    dEmail: this.findForm.pEmail,
                                    dPassword: this.findForm.newPassword,
                                    code: this.findForm.code,
                                },
                            })
                            .then((res) => {
                                if (res.data.status !== 200)
                                    return this.$message.error(
                                        "验证码错误或者已过期！！！"
                                    );
                                this.$message.success("密码修改成功！！请登录");
                                this.findFormVisible = false;
                            });
                    }
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
        //点击发送验证码按钮
        sendEmail() {
            //倒计时
            if (!this.canClick) return; //改动的是这两行代码
            this.canClick = false;
            this.content = this.totalTime + "s后重新发送";
            let clock = window.setInterval(() => {
                this.totalTime--;
                this.content = this.totalTime + "s后重新发送";
                if (this.totalTime < 0) {
                    window.clearInterval(clock);
                    this.content = "重新发送验证码";
                    this.totalTime = 10;
                    this.canClick = true; //这里重新开启
                }
            }, 1000);

            //如果是选中患者
            if (this.findRole === "患者") {
                request
                    .get("patient/sendEmail", {
                        params: {
                            pEmail: this.findForm.pEmail,
                        },
                    })
                    .then((res) => {
                        console.log(this.findForm.pEmail);
                        console.log(res);
                        if (res.data.status !== 200)
                            return this.$message.error(
                                "该邮箱暂未注册！请先注册！"
                            );
                        this.$message.success("验证码发送成功！");
                    });
            }
            //如果是选中管理员
            if (this.findRole === "管理员") {
                request
                    .get("admin/sendEmail", {
                        params: {
                            aEmail: this.findForm.pEmail,
                        },
                    })
                    .then((res) => {
                        console.log(this.findForm.pEmail);
                        console.log(res);
                        if (res.data.status !== 200)
                            return this.$message.error(
                                "该邮箱暂未注册！请先注册！"
                            );
                        this.$message.success("验证码发送成功！");
                    });
            }
            //如果是选中医生
            if (this.findRole === "医生") {
                request
                    .get("doctor/sendEmail", {
                        params: {
                            dEmail: this.findForm.pEmail,
                        },
                    })
                    .then((res) => {
                        console.log(this.findForm.pEmail);
                        console.log(res);
                        if (res.data.status !== 200)
                            return this.$message.error(
                                "该邮箱暂未注册！请先注册！"
                            );
                        this.$message.success("验证码发送成功！");
                    });
            }
        },
        //提交表单
        submitLoginForm(formName) {
            if (!/^\d+$/.test(this.loginForm.id)) {
                this.$message.error("用户名有误");
                return;
            }
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    if (this.role === "管理员") {
                        var params = new URLSearchParams();
                        params.append("aId", this.loginForm.id);
                        params.append("aPassword", this.loginForm.password);

                        request
                            .post("admin/login", params)
                            .then((res) => {
                                console.log(res);
                                if (res.data.status != 200)
                                    return this.$message.error(
                                        "用户名或密码错误"
                                    );
                                setToken(res.data.data.token);
                                this.$router.push("/adminLayout");
                            })
                            .catch((e) => {
                                console.log(e);
                                if (
                                    e.response == undefined ||
                                    e.response.data == undefined
                                ) {
                                    this.$message({
                                        showClose: true,
                                        message: e,
                                        type: "error",
                                        duration: 5000,
                                    });
                                } else {
                                    this.$message({
                                        showClose: true,
                                        message: e.response.data,
                                        type: "error",
                                        duration: 5000,
                                    });
                                }
                            });
                    }
                    if (this.role === "医生") {
                        var params1 = new URLSearchParams();
                        params1.append("dId", this.loginForm.id);
                        params1.append("dPassword", this.loginForm.password);

                        request
                            .post("doctor/login", params1)
                            .then((res) => {
                                console.log(res);
                                if (res.data.status != 200)
                                    return this.$message.error(
                                        "用户名或密码错误"
                                    );
                                setToken(res.data.data.token);
                                this.$router.push("/doctorLayout");
                            })
                            .catch((e) => {
                                console.log(e);
                                if (
                                    e.response == undefined ||
                                    e.response.data == undefined
                                ) {
                                    this.$message({
                                        showClose: true,
                                        message: e,
                                        type: "error",
                                        duration: 5000,
                                    });
                                } else {
                                    this.$message({
                                        showClose: true,
                                        message: e.response.data,
                                        type: "error",
                                        duration: 5000,
                                    });
                                }
                            });
                    }
                    if (this.role === "患者") {
                        var params2 = new URLSearchParams();
                        params2.append("pId", this.loginForm.id);
                        params2.append("pPassword", this.loginForm.password);

                        request
                            .post("patient/login", params2)
                            .then((res) => {
                                console.log('登录响应:', res);
                                if (res.data.status !== 200) {
                                    return this.$message.error("用户名或密码错误");
                                }
                                setToken(res.data.data.token);
                                this.$router.push("/patientLayout");
                            })
                            .catch((e) => {
                                console.error('登录失败:', e);
                                if (e.response?.data) {
                                    this.$message.error(e.response.data);
                                } else {
                                    this.$message.error("登录失败，请稍后重试");
                                }
                            });
                    }
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
    }
};
</script>

<style lang="scss">
.codeInput {
    width: 70%;
    margin-right: 10px;
}
.findPassword {
    margin-top: 0px;
}
.login-index {
    background: #2b4b6b;
    height: 100%;
    position: relative;
}
.mid-index {
    opacity: 0.9;
    width: 450px;
    height: 390px;
    background: white;
    //绝对定位，相对于最左上角来说
    position: absolute;
    left: 70%;
    top: 50%;
    transform: translate(-50%, -50%);
}
.logo-index {
    background: white;
    height: 130px;
    width: 130px;
    border-radius: 50%;
    padding: 10px;
    //子绝父相,使一个div悬挂在另一个div上中间
    position: absolute;
    left: 50%;
    top: 0;
    transform: translate(-50%, -50%);

    border: 1px solid #eee;
    box-shadow: 0px 0px 10px #ddd;

    img {
        height: 100%;
        width: 100%;
        border-radius: 50%;
        background: #eeeeee;
    }
}

.loginForm {
    margin-top: 120px;
}
.el-form-item {
    margin-left: 20px;
    margin-right: 20px;
}
//角色单选
.role {
    margin-left: 90px;
    margin-right: 90px;
}
//按钮
.btns {
    display: flex;
    justify-content: flex-end;
    height: 25px;
}
</style>
