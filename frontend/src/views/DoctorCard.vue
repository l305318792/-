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
                    <td style="">姓名：</td>
                    <td>
                        <el-input
                            :disabled="!isEditing"
                            v-model="editForm.dName"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>账号：</td>
                    <td>
                        <el-input disabled v-model="editForm.dId"></el-input>
                    </td>
                </tr>
                <tr>
                    <td>性别：</td>
                    <td>
                        <template v-if="isEditing">
                            <el-radio v-model="editForm.dGender" label="男">男</el-radio>
                            <el-radio v-model="editForm.dGender" label="女">女</el-radio>
                        </template>
                        <el-input v-else disabled v-model="editForm.dGender"></el-input>
                    </td>
                </tr>
                <tr>
                    <td>手机号：</td>
                    <td>
                        <el-input
                            :disabled="!isEditing"
                            v-model="editForm.dPhone"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>身份证号：</td>
                    <td>
                        <el-input
                            :disabled="!isEditing"
                            v-model="editForm.dCard"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>邮箱：</td>
                    <td>
                        <el-input
                            :disabled="!isEditing"
                            v-model="editForm.dEmail"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>职位：</td>
                    <td>
                        <template v-if="isEditing">
                            <el-select v-model="editForm.dPost" placeholder="请选择职称">
                                <el-option
                                    v-for="post in posts"
                                    :key="post"
                                    :label="post"
                                    :value="post"
                                >
                                </el-option>
                            </el-select>
                        </template>
                        <el-input v-else disabled v-model="editForm.dPost"></el-input>
                    </td>
                </tr>
                <tr>
                    <td>所属科室：</td>
                    <td>
                        <template v-if="isEditing">
                            <el-select v-model="editForm.dSection" filterable placeholder="请选择科室">
                                <el-option
                                    v-for="section in sections"
                                    :key="section"
                                    :label="section"
                                    :value="section"
                                >
                                </el-option>
                            </el-select>
                        </template>
                        <el-input v-else disabled v-model="editForm.dSection"></el-input>
                    </td>
                </tr>
                <tr>
                    <td>挂号价格：</td>
                    <td>
                        <el-input
                            :disabled="!isEditing"
                            v-model="editForm.dPrice"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>评分：</td>
                    <td>
                        <el-input disabled v-model="editForm.dAvgStar"></el-input>
                    </td>
                </tr>
                <tr>
                    <td>简介：</td>
                    <td>
                        <el-input
                            :disabled="!isEditing"
                            v-model="editForm.dIntroduction"
                            type="textarea"
                            :rows="4"
                        ></el-input>
                    </td>
                </tr>
            </table>
        </el-card>
    </div>
</template>
<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import request from "@/utils/request.js";
export default {
    name: "DoctorCard",
    data() {
        return {
            userId: "",
            doctorData: {},
            editForm: {},
            isEditing: false,
            posts: ["主任医师", "副主任医师", "主治医生"],
            sections: [
                "神经内科",
                "内分泌科",
                "呼吸与危重症医学科",
                "消化内科",
                "心血管内科",
                "发热门诊",
                "手足外科",
                "普通外科",
                "肛肠外科",
                "神经外科",
                "骨科",
                "烧伤整形外科",
                "妇科",
                "产科",
                "儿科",
                "耳鼻咽喉科",
                "眼科",
                "中医科",
                "急诊科",
                "皮肤病科",
                "口腔科",
            ],
        };
    },
    methods: {
        //请求医生信息
        requestDoctor() {
            request
                .get("admin/findDoctor", {
                    params: {
                        dId: this.userId,
                    },
                })
                .then((res) => {
                    if (res.data.status != 200)
                        return this.$message.error("获取数据失败");
                    this.doctorData = res.data.data;
                    this.editForm = JSON.parse(JSON.stringify(this.doctorData));
                });
        },
        //token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
        // 开始编辑
        startEdit() {
            this.isEditing = true;
            this.editForm = JSON.parse(JSON.stringify(this.doctorData));
        },
        // 保存编辑
        saveEdit() {
            request
                .get("admin/modifyDoctor", {
                    params: {
                        dId: this.editForm.dId,
                        dGender: this.editForm.dGender,
                        dName: this.editForm.dName,
                        dPost: this.editForm.dPost,
                        dSection: this.editForm.dSection,
                        dPhone: this.editForm.dPhone,
                        dEmail: this.editForm.dEmail,
                        dCard: this.editForm.dCard,
                        dPrice: this.editForm.dPrice,
                        dIntroduction: this.editForm.dIntroduction,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200) {
                        this.$message.error("修改信息失败！");
                        return;
                    }
                    this.isEditing = false;
                    this.doctorData = JSON.parse(JSON.stringify(this.editForm));
                    this.$message.success("修改信息成功！");
                });
        },
        // 取消编辑
        cancelEdit() {
            this.isEditing = false;
            this.editForm = JSON.parse(JSON.stringify(this.doctorData));
        },
    },
    created() {
        //解码token信息
        this.userId = this.tokenDecode(getToken()).dId;
        this.requestDoctor();
    },
};
</script>
<style lang="scss" scope>
td, th {
  white-space: nowrap;
  padding: 10px;
}
.el-select {
  width: 100%;
}
</style>