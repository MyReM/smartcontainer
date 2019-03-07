<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input clearable @keyup.enter.native="search" style="width: 200px;" size="small" class="filter-item" placeholder="名称" v-model="listQuery.userName"></el-input>
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
      <el-button class="filter-item" size="small" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-edit">添加</el-button>
      <!-- <el-button class="filter-item" type="primary" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">{{$t('table.export')}}</el-button> -->
      <!-- <el-checkbox class="filter-item" style='margin-left:15px;' @change='tableKey=tableKey+1' v-model="showReviewer">{{$t('table.reviewer')}}</el-checkbox> -->
    </div>

    <el-table :data="list" size="mini" v-loading.body="listLoading" element-loading-text="Loading" stripe border fit highlight-current-row>
      
      <el-table-column align="center" label='ID' prop="deptId" width="95"></el-table-column>

      <el-table-column label="公司简称" width="110" prop="simpleName" align="center"></el-table-column>

      <el-table-column label="公司全称" prop="fullName" align="center"></el-table-column>

      <!-- <el-table-column label="所属集团" prop="parent.simpleName" width="110" align="center"></el-table-column> -->

      <el-table-column class-name="status-col" label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{scope.row.status | statusFilter2}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="level" label="级别" width="200"></el-table-column>

      <el-table-column align="center" prop="comment" label="备注" width="200"></el-table-column>

      <el-table-column align="center" label="操作" width="120" class-name="small-padding fixed-width">
        <template slot-scope="scope">

          <el-tooltip class="item" effect="dark" content="编辑" placement="top">
            <a style="cursor: pointer;"><i class="el-icon-edit" @click="handleUpdate(scope.row)"></i></a>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除" placement="top">
            <a style="cursor: pointer;margin-left: 10px;"><i class="el-icon-delete" @click="handleDelete(scope.row)"></i></a>
          </el-tooltip>
          
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
			<el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="70px" style='width: 90%;margin: auto;'>

				<el-form-item label="公司简称" prop="simpleName">
					 <el-input v-model="temp.simpleName" placeholder="请输入公司简称"></el-input>
				</el-form-item>

				<el-form-item label="公司全称" prop="fullName">
					<el-input v-model="temp.fullName" placeholder="请输入公司全称"></el-input>
				</el-form-item>

        <!-- <el-form-item label="所属集团">
          <el-cascader :options="options" v-model="deptSelect" clearable :show-all-levels="false" @change="deptTreeChange" change-on-select style="width: 100%"></el-cascader>
        </el-form-item> -->

        <el-form-item label="状态">
          <el-radio-group v-model="temp.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
				</el-form-item>

				<el-form-item label="描述">
					<el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="公司描述" v-model="temp.comment"></el-input>
				</el-form-item>

			</el-form>

			<div slot="footer" class="dialog-footer" style='width: 90%;margin: auto;'>
				<el-button @click="dialog.visible = false">取消</el-button>
				<el-button v-if="dialog.status == 'create'" type="primary" @click="createData">确认</el-button>
				<el-button v-else type="primary" @click="updateData">确认</el-button>
			</div>
		</el-dialog>

  </div>

</template>

<script>

import { dept, findAllDept, insertDept, findDeptTree, updateDept, deleteDept } from '@/api/dept'

export default {
  data() {
    return {
      total: null,
      list: null,
      listLoading: true,
      dialog: {
        title: '',
        visible: false,
        status: ''
      },
      listQuery: {
        page: 1,
        size: 10,
        simpleName: null,
        status: null
      },
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        userName: [{ required: true, message: 'title is required', trigger: 'blur' }]
      },
      options: [],
      deptSelect: [],
      temp: dept
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        0: 'danger',
        1: 'success',
        2: 'gray'
      }
      return statusMap[status]
    },
    statusFilter2(status) {
      const statusMap = {
        0: '禁用',
        1: '启用',
        2: 'danger'
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
      findAllDept(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = parseFloat(response.data.totalElements)
      })
      findDeptTree().then((response) => {
        var options = this.findTree(response.data)
        this.options = options
      })
      this.listLoading = false
    },
    findTree(arry) {
      var options = []
      arry.forEach(element => {
        var value = element.deptId
        var label = element.simpleName
        var children = element.children
        var option = null
        if (children === undefined || children.length === 0) {
          option = { 'value': value, 'label': label }
        } else {
          option = { 'value': value, 'label': label, 'children': this.findTree(children) }
        }
        options.push(option)
      })
      return options
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
    deptTreeChange(val) {
      var length = val.length
      if (length === 0) {
        this.temp.parentId = null
      } else {
        this.temp.parentId = parseInt(val[length - 1])
      }
      console.log(this.temp)
    },
    handleCreate() {
      this.deptSelect = []
      this.dialog = {
        title: '添加部门',
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
          // this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          // this.temp.author = 'vue-element-admin'
          insertDept(this.temp).then(() => {
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
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.deptSelect = [row.parentId]
      this.dialog = {
        title: '编辑部门',
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
          updateDept(tempData).then(() => {
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
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDept(row.deptId).then((response) => {
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
    },
    handleSizeChange(val) {
      this.listQuery.size = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.fetchData()
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
