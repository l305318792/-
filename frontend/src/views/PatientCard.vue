<!--
 * @Description: 
 * @Author: Rabbiter
 * @Date: 2023-01-14 20:02:29
-->
<template>
<div style="width: 100%;margin-top: -10px">
    <el-card shadow="hover">
        <div style="text-align: right; margin-bottom: 20px;">
            <el-button type="primary" @click="startEdit" v-if="!isEditing">
                <i class="iconfont icon-r-edit"></i> 编辑
            </el-button>
            <template v-else>
                <el-button type="success" @click="saveEdit">
                    <i class="iconfont icon-r-yes"></i> 保存
                </el-button>
                <el-button @click="cancelEdit">
                    <i class="iconfont icon-r-left"></i> 取消
                </el-button>
            </template>
        </div>
        <table>
            <tr>
                <td>姓名：</td>
                <td>
                    <el-input 
                        :disabled="!isEditing" 
                        v-model="editForm.pName"
                    ></el-input>
                </td>
            </tr>
            <tr>
                <td>账号：</td>
                <td>
                    <el-input disabled v-model="editForm.pId"></el-input>
                </td>
            </tr>
            <tr>
                <td>性别：</td>
                <td>
                    <template v-if="isEditing">
                        <el-radio v-model="editForm.pGender" label="男">男</el-radio>
                        <el-radio v-model="editForm.pGender" label="女">女</el-radio>
                    </template>
                    <el-input v-else disabled v-model="editForm.pGender"></el-input>
                </td>
            </tr>
            <tr>
                <td>手机号：</td>
                <td>
                    <el-input 
                        :disabled="!isEditing" 
                        v-model="editForm.pPhone"
                    ></el-input>
                </td>
            </tr>
            <tr>
                <td>身份证号：</td>
                <td>
                    <el-input 
                        :disabled="!isEditing" 
                        v-model="editForm.pCard"
                    ></el-input>
                </td>
            </tr>
            <tr>
                <td>邮箱：</td>
                <td>
                    <el-input 
                        :disabled="!isEditing" 
                        v-model="editForm.pEmail"
                    ></el-input>
                </td>
            </tr>
            <tr>
                <td>出生日期：</td>
                <td>
                    <template v-if="isEditing">
                        <el-date-picker
                            v-model="editForm.pBirthday"
                            type="date"
                            placeholder="选择日期"
                            style="width: 100%;"
                            value-format="yyyy-MM-dd"
                        >
                        </el-date-picker>
                    </template>
                    <el-input v-else disabled v-model="editForm.pBirthday"></el-input>
                </td>
            </tr>
            <tr>
                <td>年龄：</td>
                <td>
                    <el-input disabled v-model="editForm.pAge"></el-input>
                </td>
            </tr>
        </table>
    </el-card>
</div>
</template>
<script>
import jwtDecode from "jwt-decode";
import { getToken} from "@/utils/storage.js";
import request from "@/utils/request.js";
export default {
    name: "PatientCard",
    data() {
        return {
            userId:"",
            patientData:{},
            editForm: {},
            isEditing: false
        }
    },
    methods: {
        //请求患者信息
        requestPatient(){
            request.get("doctor/findPatientById", {
                params: {
                    pId: this.userId
                }
            })
            .then(res => {
                if(res.data.status != 200)
                    return this.$message.error("获取数据失败");
                this.patientData = res.data.data;
                this.editForm = JSON.parse(JSON.stringify(this.patientData));
            })
        },
        //token解码
        tokenDecode(token){
            if (token !== null)
                return jwtDecode(token);
        },
        // 开始编辑
        startEdit() {
            this.isEditing = true;
            this.editForm = JSON.parse(JSON.stringify(this.patientData));
        },
        // 保存编辑
        saveEdit() {
            request.get("patient/modifyPatient", {
                params: {
                    pId: this.editForm.pId,
                    pName: this.editForm.pName,
                    pGender: this.editForm.pGender,
                    pCard: this.editForm.pCard,
                    pPhone: this.editForm.pPhone,
                    pEmail: this.editForm.pEmail,
                    pBirthday: this.editForm.pBirthday
                }
            })
            .then(res => {
                if(res.data.status !== 200) {
                    this.$message.error("修改信息失败！");
                    return;
                }
                this.isEditing = false;
                this.patientData = JSON.parse(JSON.stringify(this.editForm));
                this.$message.success("修改信息成功！");
            });
        },
        // 取消编辑
        cancelEdit() {
            this.isEditing = false;
            this.editForm = JSON.parse(JSON.stringify(this.patientData));
        }
    },
    created(){
        //解码token信息
        this.userId = this.tokenDecode(getToken()).pId;
        this.requestPatient();
    }
}
</script>
<style lang="scss" scope>
td, th {
  white-space: nowrap;
  padding: 10px;
}
</style>