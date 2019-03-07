<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input clearable @keyup.enter.native="search" style="width: 200px;" size="small" class="filter-item" placeholder="资源名称" v-model="listQuery.resourceName"></el-input>
      <el-input clearable @keyup.enter.native="search" style="width: 200px;" size="small" class="filter-item" placeholder="资源指向" v-model="listQuery.resourceValue"></el-input>
      <el-button class="filter-item" size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
      <el-button class="filter-item" size="small" v-permission="['add']" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-edit">添加</el-button>
      <!-- <el-button class="filter-item" type="primary" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">{{$t('table.export')}}</el-button> -->
      <!-- <el-checkbox class="filter-item" style='margin-left:15px;' @change='tableKey=tableKey+1' v-model="showReviewer">{{$t('table.reviewer')}}</el-checkbox> -->
    </div>

    <el-table :data="list" size="mini" v-loading.body="listLoading" element-loading-text="Loading" stripe border fit highlight-current-row>
      
      <el-table-column align="center" label='ID' prop="resourceId" width="95"></el-table-column>

      <el-table-column label="权限编码" width="110" prop="resourceName" align="center"></el-table-column>

      <el-table-column label="权限名称" width="110" prop="resourceValue" align="center"></el-table-column>

      <el-table-column label="描述" width="110" prop="comment" align="center"></el-table-column>

      <el-table-column label="备注" prop="comment" align="center"></el-table-column>


      <el-table-column align="center" label="操作" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="编辑" placement="top">
            <a style="cursor: pointer;margin-left: 10px;"><i class="el-icon-edit" @click="handleUpdate(scope.row)"></i></a>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除" placement="top">
            <a style="cursor: pointer;margin-left: 10px;"><i class="el-icon-delete" @click="handleDelete(scope.row)"></i></a>
          </el-tooltip>
          
          <!--<i class="el-icon-share"></i>-->
          
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
        
        <el-form-item label="资源名称" prop="resourceName">
					<el-input v-model="temp.resourceName"></el-input>
				</el-form-item>

				<el-form-item label="资源指向" prop="resourceValue">
					<el-input v-model="temp.resourceValue"></el-input>
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

  </div>

</template>

<script>

import { resource, findAll, insert, update, deleted } from '@/api/resource'

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
        permissionName: null,
        permissionCode: null
      },
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        resourceName: [{ required: true, message: 'timestamp is required', trigger: 'blur' }],
        resourceValue: [{ required: true, message: '角色名不能为空！', trigger: 'blur' }]
      },
      temp: resource
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
      findAll(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = parseFloat(response.data.totalElements)
        this.listLoading = false
      })
    },
    search() {
      if (this.listQuery.resourceName == null && this.listQuery.resourceValue == null) {
        return
      }
      if (this.listQuery.resourceName === '') {
        this.listQuery.resourceName = null
      }
      this.fetchData()
    },
    handleCreate() {
      this.dialog = {
        title: '添加资源',
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
          insert(this.temp).then(() => {
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
      this.dialog = {
        title: '编辑资源',
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
          update(tempData).then(() => {
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
        deleted(row.resourceId).then((response) => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.fetchData()
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
