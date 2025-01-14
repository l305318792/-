<template>
    <div>
        <!-- 卡片 -->
        <el-card>
            <!-- 搜索栏及增加检查 -->
            <el-row type="flex">
                <el-col :span="6">
                    <el-input v-model="query" placeholder="请输入患者id查询">
                        <el-button
                            slot="append"
                            @click="requestBeds"
                            style="font-size: 18px"
                        ><i class="iconfont icon-r-find" style="font-size: 22px;"></i> 搜索</el-button>
                    </el-input>
                </el-col>
                <el-col :span="6"></el-col>
                <el-col :span="6">
                    <el-button
                        type="primary"
                        style="font-size: 18px"
                        @click="addFormVisible = true"
                    >
                    <i class="iconfont icon-r-add" style="font-size: 22px;"></i> 
                        增加床位</el-button
                    >
                </el-col>
            </el-row>
            <!-- 表格 -->
            <el-table :data="bedData" stripe style="width: 100%" border>
                <el-table-column label="床号" prop="bId"></el-table-column>
                <el-table-column label="患者id" prop="pId"></el-table-column>
                <el-table-column label="医生id" prop="dId"></el-table-column>
                <el-table-column
                    label="开始时间"
                    prop="bStart"
                ></el-table-column>
                <el-table-column
                    label="申请理由"
                    prop="bReason"
                ></el-table-column>
                <el-table-column label="状态" prop="bState">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.bState === 1" type="danger"
                            >已占用</el-tag
                        >
                        <el-tag v-if="scope.row.bState === 0" type="success"
                            >空</el-tag
                        >
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="360" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                            style="font-size: 18px"
                            type="primary"
                            @click="editDialog(scope.row)"
                        ><i class="iconfont icon-r-edit" style="font-size: 22px;"></i> 编辑</el-button>
                        <el-button
                            style="font-size: 18px"
                            type="success"
                            @click="emptyDialog(scope.row.bId)"
                        ><i class="iconfont icon-r-delete" style="font-size: 22px;"></i> 清空</el-button>
                        <el-button
                            style="font-size: 18px"
                            type="danger"
                            @click="deleteDialog(scope.row.bId)"
                        ><i class="iconfont icon-r-delete" style="font-size: 22px;"></i> 删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页 -->
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                background
                layout="total, sizes, prev, pager, next, jumper"
                :current-page="pageNumber"
                :page-size="size"
                :page-sizes="[1, 2, 4, 8, 16]"
                :total="total"
            >
            </el-pagination>
        </el-card>

        <!-- 增加床位对话框 -->
        <el-dialog title="增加床位" :visible.sync="addFormVisible">
            <el-form :model="addForm" :rules="rules" ref="ruleForm">
                <el-form-item label="床号" prop="bId" label-width="80px">
                    <el-input v-model.number="addForm.bId"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="addFormVisible = false" style="font-size: 18px;"><i class="iconfont icon-r-left" style="font-size: 20px;"></i> 取 消</el-button>
                <el-button type="primary" @click="addBed('ruleForm')"
                    style="font-size: 18px;"><i class="iconfont icon-r-yes" style="font-size: 20px;"></i> 确 定</el-button
                >
            </div>
        </el-dialog>

        <!-- 编辑床位对话框 -->
        <el-dialog title="编辑床位信息" :visible.sync="editFormVisible">
            <el-form :model="editForm" :rules="editRules" ref="editForm">
                <el-form-item label="床号" prop="bId" label-width="120px">
                    <el-input v-model="editForm.bId" disabled></el-input>
                </el-form-item>
                <el-form-item label="患者ID" prop="pId" label-width="120px">
                    <el-input v-model.number="editForm.pId"></el-input>
                </el-form-item>
                <el-form-item label="医生ID" prop="dId" label-width="120px">
                    <el-input v-model.number="editForm.dId"></el-input>
                </el-form-item>
                <el-form-item label="申请理由" prop="bReason" label-width="120px">
                    <el-input type="textarea" v-model="editForm.bReason"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editFormVisible = false" style="font-size: 18px;"><i class="iconfont icon-r-left" style="font-size: 20px;"></i> 取 消</el-button>
                <el-button type="primary" @click="updateBed('editForm')" style="font-size: 18px;"><i class="iconfont icon-r-yes" style="font-size: 20px;"></i> 确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
import request from "@/utils/request.js";
import { toLoad } from "@/utils/initialize.js";
export default {
    name: "CheckList",
    data() {
        return {
            pageNumber: 1,
            size: 8,
            query: "",
            bedData: [],
            total: 3,
            addFormVisible: false,
            editFormVisible: false,
            addForm: {},
            editForm: {},
            rules: {
                bId: [
                    { required: true, message: "请输入床号", trigger: "blur" },
                    {
                        type: "number",
                        message: "床号必须数字类型",
                        trigger: "blur",
                    },
                ],
            },
            editRules: {
                pId: [
                    { required: true, message: "请输入患者ID", trigger: "blur" },
                    {
                        type: "number",
                        message: "患者ID必须为数字",
                        trigger: "blur",
                    },
                ],
                dId: [
                    { required: true, message: "请输入医生ID", trigger: "blur" },
                    {
                        type: "number",
                        message: "医生ID必须为数字",
                        trigger: "blur",
                    },
                ],
                bReason: [
                    { required: true, message: "请输入申请理由", trigger: "blur" }
                ]
            }
        };
    },
    methods: {
        //清空床位操作
        emptyBed(id) {
            request
                .get("bed/emptyBed", {
                    params: {
                        bId: id,
                    },
                })
                .then((res) => {
                    this.requestBeds();
                    console.log(res);
                });
        },
        //清空对话框
        emptyDialog(id) {
            this.$confirm("此操作将清空该床位, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    this.emptyBed(id);
                    this.$message({
                        type: "success",
                        message: "清空成功!",
                    });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "已取消清空",
                    });
                });
        },

        //删除床位操作
        deleteBed(id) {
            request
                .get("bed/deleteBed", {
                    params: {
                        bId: id,
                    },
                })
                .then((res) => {
                    this.requestBeds();
                    console.log(res);
                });
        },
        //删除对话框
        deleteDialog(id) {
            this.$confirm("此操作将删除该床位, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    this.deleteBed(id);
                    this.$message({
                        type: "success",
                        message: "删除成功!",
                    });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "已取消删除",
                    });
                });
        },
        //点击增加确认按钮
        addBed(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("bed/addBed", {
                            params: {
                                bId: this.addForm.bId,
                                pId: -1,
                                dId: -1,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("床号或已被占用！");
                            this.addFormVisible = false;
                            this.requestBeds();
                            this.$message.success("增加床位成功！");
                            console.log(res);
                        });
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.requestBeds();
        },
        //   页码改变时触发
        handleCurrentChange(num) {
            console.log(num);
            this.pageNumber = num;
            this.requestBeds();
        },
        // 加载检查列表
        requestBeds() {
            request
                .get("bed/findAllBeds", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.bedData = res.data.data.beds;
                    this.total = res.data.data.total;
                    toLoad()
                });
        },
        //打开编辑对话框
        editDialog(row) {
            this.editForm = JSON.parse(JSON.stringify(row));
            this.editFormVisible = true;
        },

        //更新床位信息
        updateBed(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("bed/updateBed", {
                            params: {
                                bId: this.editForm.bId,
                                pId: this.editForm.pId,
                                dId: this.editForm.dId,
                                bReason: this.editForm.bReason
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("更新失败，该床位可能已被占用！");
                            this.editFormVisible = false;
                            this.requestBeds();
                            this.$message.success("更新床位成功！");
                        });
                } else {
                    return false;
                }
            });
        },
    },
    created() {
        this.requestBeds();
    },
};
</script>
<style scoped lang="scss">
.el-table {
    margin-top: 20px;
    margin-bottom: 20px;
}
.el-form {
    margin-top: 0;
}
</style>