<template>

  <div class="app-container">

    <el-row>
      <el-col :span="5">
        <div class="content tree">
          <tree :deptTreeData="deptTreeData" :roleTreeData="roleTreeData" @role-node-click="roleNodeClick" @dept-node-click="deptNodeClick"></tree>
        </div>
      </el-col>

      <el-col :span="19">
        <div class="content table">
          <div class="filter-container">
            <div style="margin-bottom: 10px">
              <el-input clearable @keyup.enter.native="search" style="width: 200px;" size="medium" class="filter-item" placeholder="用户名" v-model="listQuery.userName"></el-input>
              <!-- <el-select clearable style="width: 90px" class="filter-item" v-model="listQuery.importance" :placeholder="$t('table.importance')">
                <el-option v-for="item in importanceOptions" :key="item" :label="item" :value="item">
                </el-option>
              </el-select> -->
              <el-select clearable class="filter-item" size="medium" style="width: 130px" v-model="listQuery.status" placeholder="级别">
                <el-option :key="1" :label="1" :value="1"></el-option>
                <el-option :key="2" :label="2" :value="2"></el-option>
                <el-option :key="3" :label="3" :value="3"></el-option>
              </el-select>
              <el-date-picker v-model="listQuery.createTime" size="medium" type="datetimerange" :picker-options="pickerOptions" range-separator="至" start-placeholder="创建开始日期" end-placeholder="创建结束日期" align="center" style="width:350px;"></el-date-picker>
              <el-button class="filter-item" size="medium" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
            </div>

            <el-button class="filter-item" size="medium" v-permission="['insert']" style="margin-left: 10px;" @click="handleCreate" type="success" icon="el-icon-plus">添加</el-button>
            <!-- <el-button class="filter-item" type="small" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">{{$t('table.export')}}</el-button> -->
            <el-button class="filter-item" size="medium" v-permission="['update']" type="primary" icon="el-icon-edit-outline" @click="handleUpdate">编辑</el-button>
            <el-button class="filter-item" size="medium" v-permission="['delete']" type="danger" icon="el-icon-delete" @click="handleDelete">删除</el-button>
            <el-button type="warning" size="medium" icon="el-icon-refresh">重置密码</el-button>
            <el-button type="danger" size="medium" icon="el-icon-warning">冻结</el-button>
            <el-button type="primary" size="medium" plain @click="handleRole">角色分配</el-button>
            <el-button type="primary" size="medium" plain @click="handleDept">公司分配</el-button>
          </div>

          <el-table :data="list" size="medium" v-loading.body="listLoading" element-loading-text="Loading" @selection-change="selectionChange" stripe border fit highlight-current-row>
            
           <el-table-column type="selection" width="55"></el-table-column>
            
            <el-table-column align="center" label='ID' prop="userId" width="95"></el-table-column>

            <el-table-column label="用户名" width="110" prop="userName" align="center">
              <template slot-scope="scope">
                <span>{{scope.row.userName}}</span>
              </template>
            </el-table-column>

            <el-table-column class-name="status-col" label="性别" width="110" align="center">
              <template slot-scope="scope">
                <span>{{scope.row.sex == 1 ? "男" : "女"}}</span>
              </template>
            </el-table-column>

            <el-table-column class-name="status-col" label="级别" width="110" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status | statusFilter">{{scope.row.status}}</el-tag>
              </template>
            </el-table-column>

            <el-table-column label="描述" align="center">
              <template slot-scope="scope">
                {{scope.row.description}}
              </template>
            </el-table-column>

            <el-table-column label="图片" width="110" align="center">
              <template slot-scope="scope">
                <img v-if="scope.row.avator" :src="scope.row.avator" style="width:25px;height: 25px;">
              </template>
            </el-table-column>
            
            <el-table-column align="center" prop="createTime" label="创建时间" width="200">
              <template slot-scope="scope">
                <i class="el-icon-time"></i>
                <span>{{scope.row.createTime}}</span>
              </template>
            </el-table-column>

            <!-- <el-table-column align="center" label="操作" width="120" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-tooltip class="item" effect="dark" content="编辑" placement="top">
                  <a style="cursor: pointer;"><i class="el-icon-edit" @click="update(scope.row)"></i></a>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" content="删除" placement="top">
                  <a style="cursor: pointer;margin-left: 10px;"><i class="el-icon-delete" @click="deleteUser(scope.row)"></i></a>
                </el-tooltip>           
              </template>
            </el-table-column> -->

          </el-table>

          <div style="margin-top: 10px;">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="listQuery.page" :page-sizes="[10, 20, 30, 40]" :page-size="listQuery.size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
            </el-pagination>
          </div>

        </div>
        
      </el-col>
    </el-row>

    <el-dialog :title="dialog.title" :visible.sync="dialog.visiable" width="40%">

      <el-form :rules="rules" ref="dataForm" :model="temp" label-position="right" label-width="70px" style="width: 90%; margin: auto">

        <el-form-item label="头像" prop="avatar" style="width: 100%">
          <el-upload class="avatar-uploader" :action="fileUploadUrl" :show-file-list="false" :headers="headers" :on-success="uploadSuccess" :before-upload="beforeUpload">
              <img v-if="temp.avator" :src="temp.avator" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
        </el-form-item>

        <el-form-item label="账户" prop="userCode">
          <el-input v-model="temp.userCode"></el-input>
        </el-form-item>

        <el-form-item label="用户名" prop="userName">
          <el-input v-model="temp.userName"></el-input>
        </el-form-item>

        <el-form-item v-if="dialog.status === 'create'" label="密码" prop="password">
          <el-input v-model="temp.password"></el-input>
        </el-form-item>

        <el-form-item v-if="dialog.status === 'create'" label="确认密码" prop="confirmPassword">
          <el-input v-model="temp.confirmPassword"></el-input>
        </el-form-item>

        <el-form-item label="性别">
					<el-select v-model="temp.sex" placeholder="请选择性别" style="width: 100%">
						<el-option key="1" label="男" value="1"></el-option>
						<el-option key="2" label="女" value="2"></el-option>
					</el-select>
				</el-form-item>

        <el-form-item label="出生年月">
          <el-date-picker v-model="temp.birthday" type="datetime" placeholder="请选择" style="width: 100%"></el-date-picker>
        </el-form-item>

        <el-form-item label="备注">
          <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="用户描述" v-model="temp.description"></el-input>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer" style='width: 90%;margin: auto;'>
        <el-button @click="dialog.visiable=false">取消</el-button>
        <el-button v-if="dialog.status == 'create'" type="primary" @click="createData">确认</el-button>
        <el-button v-else type="primary" @click="updateData">确认</el-button>
      </div>

    </el-dialog>

    <el-dialog :title="treeDialog.title" :visible.sync="treeDialog.visiable" width="30%">
      <el-tree :data="treeData" node-key="value" default-expand-all show-checkbox check-strictly ref="tree"></el-tree>
      <span slot="footer" class="dialog-footer">
        <el-button @click="treeDialog.visiable = false">取 消</el-button>
        <el-button type="primary" @click="roleOrDeptAllot">确 定</el-button>
      </span>
    </el-dialog>

  </div>

</template>

<script>

import { user, deptTree, getUserList, findByDeptId, findByRoleId, findDeptByUserId, findRoleByUserId, createUser, updateUser, updateDept, updateRole, deleteUser } from '@/api/user'
import { findRoleTree } from '@/api/role'
import { getToken } from '@/utils/auth'

import tree from '@/components/tree/index'

const pickerOptions = {
  shortcuts: [{
    text: '最近一周',
    onClick(picker) {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      picker.$emit('pick', [start, end])
    }
  }, {
    text: '最近一个月',
    onClick(picker) {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      picker.$emit('pick', [start, end])
    }
  }, {
    text: '最近三个月',
    onClick(picker) {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      picker.$emit('pick', [start, end])
    }
  }]
}

export default {
  components: {
    tree
  },
  data() {
    return {
      total: null,
      list: null,
      listLoading: true,
      headers: {
        Authorization: getToken()
      },
      pickerOptions: pickerOptions,
      selectionList: [],
      treeData: [],
      deptTreeData: [],
      roleTreeData: [],
      dialog: {
        title: '',
        visiable: false,
        status: ''
      },
      treeDialog: {
        title: '',
        type: '',
        visiable: false
      },
      fileUploadUrl: process.env.BASE_API + '/file/upload',
      listQuery: {
        page: 1,
        size: 10,
        userName: null,
        status: null,
        createTime: null
      },
      rules: {
        sex: [{ required: true, message: '性别不能为空！', trigger: 'change' }],
        userName: [{ required: true, message: '用户名称不能为空！', trigger: 'blur' }],
        userCode: [{ required: true, message: '用户编码不能为空！', trigger: 'blur' }],
        password: [{ required: true, message: '密码不能为空！', trigger: 'blur' }],
        confirmPassword: [{ required: true, message: '请再次输入密码！', trigger: 'blur' }]
      },
      temp: user
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        2: 'gray',
        3: 'danger'
      }
      return statusMap[status]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    selectionChange(val) {
      this.selectionList = val
    },
    fetchData() {
      this.listLoading = true
      getUserList(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = parseFloat(response.data.totalElements)
        this.listLoading = false
      })
      deptTree().then((response) => {
        var tree = this.findDeptTree(response.data)
        this.deptTreeData = tree
      })
      findRoleTree().then((response) => {
        var tree = this.findRoleTree(response.data)
        this.roleTreeData = tree
      })
    },
    findDeptTree(arry) {
      var options = []
      arry.forEach(element => {
        var value = element.deptId
        var label = element.simpleName
        var children = element.children
        var option = null
        if (children === undefined || children.length === 0) {
          option = { 'value': value, 'label': label }
        } else {
          option = { 'value': value, 'label': label, 'children': this.findDeptTree(children) }
        }
        options.push(option)
      })
      return options
    },
    findRoleTree(arry) {
      var options = []
      arry.forEach(element => {
        var value = element.roleId
        var label = element.roleName
        var children = element.children
        var option = null
        if (children === undefined || children.length === 0) {
          option = { 'value': value, 'label': label }
        } else {
          option = { 'value': value, 'label': label, 'children': this.findRoleTree(children) }
        }
        options.push(option)
      })
      return options
    },
    roleNodeClick(tree) {
      findByRoleId(tree.ele.value, this.listQuery).then((res) => {
        this.list = res.data.content
        this.total = res.data.totalElements
      })
    },
    deptNodeClick(tree) {
      findByDeptId(tree.ele.value, this.listQuery).then((res) => {
        this.list = res.data.content
        this.total = res.data.totalElements
      })
    },
    search() {
      if (this.listQuery.userName == null && this.listQuery.status == null) {
        return
      }
      if (this.listQuery.userName === '') {
        this.listQuery.userName = null
      }
      this.fetchData()
    },
    handleCreate() {
      this.dialog = {
        title: '创建用户',
        visiable: true,
        status: 'create'
      }
      this.temp = {}
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.temp.confirmPassword !== this.temp.password) {
            this.$message.error('两次输入的密码不一致')
            return
          }
          createUser(this.temp).then(() => {
            this.dialog.visiable = false
            this.$notify({ title: '成功', message: '创建成功', type: 'success', duration: 2000 })
            this.fetchData()
          })
        }
      })
    },
    handleUpdate() {
      if (this.selectionList.length === 0) {
        this.$message.error('请选择一个用户')
      } else if (this.selectionList.length > 1) {
        this.$message.error('最多只能选择一个用户')
      } else {
        this.temp = Object.assign({}, this.selectionList[0]) // copy obj
        this.dialog = {
          title: '编辑用户',
          visiable: true,
          status: 'update'
        }
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      }
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateUser(tempData.userId, tempData).then(() => {
            this.dialog.visiable = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.fetchData()
          })
        }
      })
    },
    handleDelete() {
      if (this.selectionList.length === 0) {
        this.$message.error('请选择一个用户')
      } else if (this.selectionList.length > 1) {
        this.$message.error('最多只能选择一个用户')
      } else {
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteUser(this.selectionList[0].userId).then((response) => {
            if (response.message === 'success') {
              // const index = this.list.indexOf(row)
              // this.list.splice(index, 1)
              this.$message({
                type: 'success',
                message: '删除成功!'
              })
              this.fetchData()
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      }
    },
    handleRole() {
      if (this.selectionList.length === 0) {
        this.$message.error('请选择一个用户')
      } else if (this.selectionList.length > 1) {
        this.$message.error('最多只能选择一个用户')
      } else {
        this.treeDialog = {
          title: '角色分配',
          type: 'role',
          visiable: true
        }
        this.treeData = this.roleTreeData
        findRoleByUserId(this.selectionList[0].userId).then((res) => {
          if (res.data !== undefined || res.data !== null || res.data !== 'null') {
            this.$refs.tree.setCheckedKeys([res.data.roleId])
          }
        })
      }
    },
    handleDept() {
      if (this.selectionList.length === 0) {
        this.$message.error('请选择一个用户')
      } else if (this.selectionList.length > 1) {
        this.$message.error('最多只能选择一个用户')
      } else {
        this.treeDialog = {
          title: '部门分配',
          type: 'dept',
          visiable: true
        }
        this.treeData = this.deptTreeData
        findDeptByUserId(this.selectionList[0].userId).then((res) => {
          if (res.data !== undefined || res.data !== null || res.data !== 'null') {
            this.$refs.tree.setCheckedKeys([res.data.deptId])
          }
        })
      }
    },
    roleOrDeptAllot() {
      const nodeArry = this.$refs.tree.getCheckedNodes()
      if (nodeArry.length === 0) {
        this.$message.error('请选择一个')
      } else if (nodeArry.length > 1) {
        this.$message.error('最多只能选择一个')
      } else {
        if (this.treeDialog.type === 'dept') {
          updateDept(this.selectionList[0].userId, nodeArry[0].value).then((res) => {
            this.treeDialog.visiable = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        } else if (this.treeDialog.type === 'role') {
          updateRole(this.selectionList[0].userId, nodeArry[0].value).then((res) => {
            this.treeDialog.visiable = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      }
    },
    handleSizeChange(val) {
      this.listQuery.size = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.fetchData()
    },
    uploadSuccess(res, file, fileList) {
      // console.log(file)
      // console.log(fileList)
      this.temp.avator = res
    },
    beforeUpload(file) {
      const imageType = file.type === 'image/jpeg' || file.type === 'image/jpg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!imageType) {
        this.$message.error('上传头像的图片格式不支持!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return imageType && isLt2M
    }
  }
}
</script>

<style>

  .filter-container{
    padding-bottom: 10px;

  }

  .content{
    border-radius: 4px;
    min-height: 500px;
  }
  .table{
    padding-left: 15px;
  }

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    line-height: 100px;
    text-align: center;
  }
  .avatar {
    width: 100px;
    height: 100px;
    display: block;
  }
</style>
