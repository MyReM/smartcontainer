<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input clearable @keyup.enter.native="search" style="width: 200px;" size="small" class="filter-item" placeholder="名称" v-model="listQuery.roleName"></el-input>
      <!-- <el-select clearable style="width: 90px" class="filter-item" v-model="listQuery.importance" :placeholder="$t('table.importance')">
        <el-option v-for="item in importanceOptions" :key="item" :label="item" :value="item">
        </el-option>
      </el-select> -->
      <el-select clearable class="filter-item" size="small" style="width: 130px" v-model="listQuery.status" placeholder="级别">
        <el-option :key="1" :label="1" :value="1"></el-option>
        <el-option :key="2" :label="2" :value="2"></el-option>
        <el-option :key="3" :label="3" :value="3"></el-option>
      </el-select>
      <el-button class="filter-item" size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
      <el-button class="filter-item" size="small" style="margin-left: 10px;" @click="handleCreate" type="success" icon="el-icon-plus">添加</el-button>
      <el-button class="filter-item" size="small" style="margin-left: 10px;" @click="handleUpdate" type="primary" icon="el-icon-edit">编辑</el-button>
      <el-button class="filter-item" size="small" style="margin-left: 10px;" @click="handleDelete" type="danger" icon="el-icon-edit">删除</el-button>
      <el-button class="filter-item" size="small" style="margin-left: 10px;" @click="handleMenu" type="primary" icon="el-icon-setting">菜单分配</el-button>
      <el-button class="filter-item" size="small" style="margin-left: 10px;" @click="handlePermission" type="primary" icon="el-icon-edit">权限分配</el-button>
    </div>

    <el-table :data="list" size="mini" v-loading.body="listLoading" @selection-change="handleSelectionChange" element-loading-text="Loading" stripe border fit highlight-current-row>

      <el-table-column type="selection" width="55" align="center"></el-table-column>
      
      <!-- <el-table-column align="center" label='ID' prop="roleId" width="95"></el-table-column> -->
      <el-table-column type="index" width="55" align="center"></el-table-column>

      <el-table-column label="角色名" width="110" prop="roleName" align="center"></el-table-column>

      <el-table-column label="父级" width="110" prop="parent.roleName" align="center"></el-table-column>

      <el-table-column label="级别" width="110" prop="level" align="center"></el-table-column>

      <el-table-column class-name="status-col" label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 0 ? 'danger' : 'success'">{{ scope.row.status === 0 ? '禁用' : '启用' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="备注" prop="comment" align="center"></el-table-column>

      <el-table-column align="center" prop="createTime" label="创建时间" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span>{{scope.row.createTime}}</span>
        </template>
      </el-table-column>

    </el-table>

    <div style="margin-top: 10px;">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="listQuery.page" :page-sizes="[10, 20, 30, 40]" :page-size="listQuery.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

		<el-dialog :title="dialog.title" :visible.sync="dialog.visible" style="width: 70%;margin: auto;">
			<el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="80px" style='width: 90%;margin: auto;'>
        
				<el-form-item label="角色名" prop="roleName">
					<el-input v-model="temp.roleName"></el-input>
				</el-form-item>

        <el-form-item label="父级" prop="parentId">
					<el-cascader :options="options" :value="parentRoleSelect" v-model="parentRoleSelect" clearable :show-all-levels="false" @change="roleTreeChange" change-on-select style="width: 100%"></el-cascader>
				</el-form-item>

				<el-form-item label="备注">
					<el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="用户描述" v-model="temp.comment"></el-input>
				</el-form-item>

			</el-form>

			<div slot="footer" class="dialog-footer" style='width: 90%;margin: auto;'>
				<el-button @click="dialog.visible = false">取消</el-button>
				<el-button v-if="dialog.status=='create'" type="primary" @click="createData">确认</el-button>
				<el-button v-else type="primary" @click="updateData">确认</el-button>
			</div>
		</el-dialog>

    <el-dialog title="菜单分配" :visible.sync="treeDialog.visiable" width="30%">
      <el-tree :data="treeData" node-key="value" default-expand-all show-checkbox check-strictly ref="tree"></el-tree>
      <span slot="footer" class="dialog-footer">
        <el-button @click="treeDialog.visiable = false">取 消</el-button>
        <el-button type="primary" @click="allotMenu">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="权限分配" :visible.sync="permissionDialog.visiable" width="40%">
      <!-- <el-select v-model="permissionList" multiple placeholder="请选择权限" style="width: 100%">
        <el-option v-for="item in permissions" :key="item.permissionId" :label="item.permissionName" :value="item.permissionId">
          <span style="float: left">{{ item.permissionCode }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{ item.permissionName }}</span>
        </el-option>
      </el-select> -->

      <el-transfer filterable filter-placeholder="请输入权限名称" :props="{ key: 'permissionId', label: 'permissionName' }" :titles="['可选列表', '已选列表']" v-model="permissionList" :data="permissions"></el-transfer>

      <span slot="footer" class="dialog-footer">
        <el-button @click="permissionDialog.visiable = false">取 消</el-button>
        <el-button type="primary" @click="allotPermission">确 定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>

import { role, findAllRolePage, findRoleTree, findMenuIdByRoleId, allotMenu, allotPermission, insertRole, updateRole, deleteRole } from '@/api/role'
import { findMenuTree } from '@/api/menu'
import { findAllList } from '@/api/permission'
import { createTree } from '@/utils/tree'

export default {
  data() {
    return {
      total: null,
      list: null,
      listLoading: true,
      options: [],
      dialog: {
        title: '',
        visible: false,
        status: ''
      },
      treeDialog: {
        visiable: false
      },
      treeData: [],
      permissionDialog: {
        visiable: false
      },
      permissions: [],
      permissionList: [],
      listQuery: {
        page: 1,
        size: 10,
        roleName: null,
        status: null
      },
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        roleName: [{ required: true, message: '角色名不能为空！', trigger: 'blur' }]
      },
      selectionList: [],
      parentRoleSelect: [],
      temp: role
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
    fetchData() {
      this.listLoading = true
      findAllRolePage(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = parseFloat(response.data.totalElements)
        this.listLoading = false
      })
      findRoleTree().then((res) => {
        this.options = createTree(res.data, 'roleName', 'roleId')
      })
      findMenuTree().then((res) => {
        this.treeData = createTree(res.data, 'menuName', 'menuId')
      })
      findAllList().then((res) => {
        this.permissions = res.data
      })
    },
    search() {
      if (this.listQuery.roleName == null && this.listQuery.status == null) {
        return
      }
      if (this.listQuery.roleName === '') {
        this.listQuery.roleName = null
      }
      this.fetchData()
    },
    handleSelectionChange(val) {
      this.selectionList = val
    },
    handleCreate() {
      this.dialog = {
        title: '添加角色',
        visible: true,
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
          insertRole(this.temp).then(() => {
            this.dialog.visible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.fetchData()
          })
        }
      })
    },
    roleTreeChange(val) {
      console.log(val)
      var length = val.length
      if (length === 0) {
        this.temp.parentId = null
      } else {
        this.temp.parentId = parseInt(val[length - 1])
      }
    },
    handleUpdate() {
      if (!this.judge()) {
        return
      }
      this.temp = Object.assign({}, this.selectionList[0])
      this.parentRoleSelect = [this.temp.parentId]
      this.dialog = {
        title: '编辑角色',
        visible: true,
        status: 'update'
      }
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateRole(tempData).then(() => {
            this.dialog.visible = false
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
    handleDelete(row) {
      if (!this.judge()) {
        return
      }
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRole(this.selectionList[0].roleId).then((response) => {
          this.$message.success('删除成功!')
          this.fetchData()
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    handleSizeChange(val) {
      this.listQuery.size = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.fetchData()
    },
    handleMenu() {
      if (!this.judge()) {
        return
      }
      this.treeDialog.visiable = true
      findMenuIdByRoleId(this.selectionList[0].roleId).then((res) => {
        this.$refs.tree.setCheckedKeys(res.data)
      })
    },
    judge() {
      if (this.selectionList.length === 0) {
        this.$message.error('请选择一个用户')
        return false
      } else if (this.selectionList.length > 1) {
        this.$message.error('最多只能选择一个用户')
        return false
      } else {
        return true
      }
    },
    allotMenu(row) {
      const nodeArry = this.$refs.tree.getCheckedNodes()
      const menuList = []
      nodeArry.forEach(ele => {
        menuList.push(ele.value)
      })
      allotMenu(this.selectionList[0].roleId, menuList).then((res) => {
        this.$message.success('菜单分配成功')
        this.treeDialog.visiable = false
        this.fetchData()
      })
    },
    handlePermission() {
      if (!this.judge()) {
        return
      }
      this.permissionDialog = {
        visiable: true
      }
      const permissions = this.selectionList[0].permissions.map((ele, index, arry) => ele.permissionId)
      this.permissionList = permissions
    },
    allotPermission() {
      allotPermission(this.selectionList[0].roleId, this.permissionList).then((res) => {
        this.$message.success('权限分配成功')
        this.permissionDialog.visiable = false
        this.fetchData()
      })
    }
  }
}
</script>

<style>
  .filter-container{
    padding-bottom: 10px;
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
