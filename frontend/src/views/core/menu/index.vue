<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input clearable @keyup.enter.native="search" style="width: 200px;" size="small" class="filter-item" placeholder="菜单名称" v-model="listQuery.menuName"></el-input>
      <!-- <el-select clearable style="width: 90px" class="filter-item" v-model="listQuery.importance" :placeholder="$t('table.importance')">
        <el-option v-for="item in importanceOptions" :key="item" :label="item" :value="item">
        </el-option>
      </el-select> -->
      <el-input clearable @keyup.enter.native="search" style="width: 200px;" size="small" class="filter-item" placeholder="菜单编码" v-model="listQuery.menuCode"></el-input>

      <el-input clearable @keyup.enter.native="search" style="width: 200px;" size="small" class="filter-item" placeholder="父级菜单" v-model="listQuery.parentName"></el-input>

      <el-button class="filter-item" size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>

      <div style="margin-top: 10px;">
        <el-button class="filter-item" size="small" @click="handleCreate" type="primary" icon="el-icon-edit">添加</el-button>
        <!-- <el-button class="filter-item" type="small" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleMenu">菜单分配</el-button> -->
      </div>
    </div>

    <el-table :data="list" size="mini" v-loading.body="listLoading" element-loading-text="Loading" stripe border fit highlight-current-row>
      
      <el-table-column type="index" width="55"></el-table-column>
      
      <!-- <el-table-column align="center" label='ID' prop="menuId" width="95"></el-table-column> -->

      <el-table-column label="菜单名称" prop="menuName" align="center"></el-table-column>

      <el-table-column label="菜单编码" prop="menuCode" align="center"></el-table-column>

      <el-table-column label="菜单指向" prop="menuValue" align="center"></el-table-column>

      <el-table-column label="父级菜单" prop="parent.menuName" width="110" align="center"></el-table-column>

      <el-table-column class-name="status-col" label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{scope.row.status | statusFilter2}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="排序号" prop="sortNum" width="110" align="center"></el-table-column>

      <el-table-column label="可见性" width="80" align="center">
        <template slot-scope="scope">
          <span>{{scope.row.hidden === true ? '隐藏' : '显示'}}</span>
        </template>
      </el-table-column>

      <el-table-column label="图标" width="80" align="center">
        <template slot-scope="scope">
          <span><svg-icon :icon-class="scope.row.icon === null ? '' : scope.row.icon"/></span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="level" label="级别" width="80"></el-table-column>

      <el-table-column align="center" prop="comment" label="备注"></el-table-column>

      <el-table-column align="center" label="操作" width="120" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!--<el-button type="primary" size="mini" @click="update(scope.row)">编辑</el-button>-->
          <!--<i class="el-icon-edit-outline"></i>-->
          <!--<el-button size="mini" type="success" @click="modifyStatus(scope.row,'published')">发布</el-button>-->
          <!--<el-button size="mini" type="danger" @click="deleteUser(scope.row)">删除</el-button>-->
          <el-tooltip class="item" effect="dark" content="编辑" placement="top">
            <a style="cursor: pointer;"><i class="el-icon-edit" @click="handleUpdate(scope.row)"></i></a>
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

				<el-form-item label="菜单名称" prop="menuName">
					 <el-input v-model="temp.menuName" placeholder="请输入菜单名称"></el-input>
				</el-form-item>

        <el-form-item label="菜单编码" prop="menuCode">
					 <el-input v-model="temp.menuCode" placeholder="请输入菜单编码"></el-input>
				</el-form-item>

        <el-form-item label="菜单指向" prop="menuValue">
					 <el-select v-model="temp.menuValue" placeholder="请选择菜单指向" style="width: 100%">
            <el-option v-for="item in viewMap" :key="item.value" :label="item.name" :value="item.value">
              <span style="float: left">{{ item.value }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.name }}</span>
            </el-option>
          </el-select>
				</el-form-item>

        <el-form-item label="菜单排序号" prop="sortNum">
          <el-input v-model="temp.sortNum" placeholder="请输入菜单排序号"></el-input>
        </el-form-item>

			  <el-form-item label="状态">
          <el-radio-group v-model="temp.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
				</el-form-item>

        <el-form-item label="可见性">
          <el-radio-group v-model="temp.hidden">
            <el-radio :label="false">显示</el-radio>
            <el-radio :label="true">隐藏</el-radio>
          </el-radio-group>
				</el-form-item>

        <el-form-item label="全屏">
          <el-radio-group v-model="temp.fullScreen">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="菜单图标" prop="icon">
					 <el-select v-model="temp.icon" placeholder="请选择菜单图标" style="width: 100%" clearable>
            <el-option v-for="icon in iconList" :key="icon.value" :label="icon.name" :value="icon.value">
              <span style="float: left">{{ icon.value }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px" class=""><svg-icon :icon-class="icon.name" /></span>
            </el-option>
          </el-select>
				</el-form-item>

        <el-form-item label="父级菜单">
          <el-cascader :options="options" v-model="menuSelect" clearable :show-all-levels="false" @change="menuTreeChange" change-on-select style="width: 100%"></el-cascader>
        </el-form-item>

				<el-form-item label="描述">
					<el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="菜单描述" v-model="temp.comment"></el-input>
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

import { menu, findAllMenu, insertMenu, findMenuTree, getParentId, updateMenu, deleteMenu } from '@/api/menu'
import { viewMap, iconList } from '@/api/system'

export default {
  data() {
    return {
      total: null,
      list: null,
      listLoading: true,
      viewMap: viewMap,
      iconList: iconList,
      dialog: {
        title: '',
        visible: false,
        status: ''
      },
      listQuery: {
        page: 1,
        size: 10,
        menuName: null,
        menuCode: null,
        parentName: null
      },
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        menuName: [{ required: true, message: '菜单名称不能为空', trigger: 'blur' }],
        menuCode: [{ required: true, message: '菜单编码不能为空', trigger: 'blur' }],
        menuValue: [{ required: true, message: '菜单指向不能为空', trigger: 'blur' }],
        sortNum: [{ required: true, message: '菜单排序号不能为空', trigger: 'blur' }]
      },
      options: [],
      menuSelect: [],
      temp: menu
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
      findAllMenu(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = parseFloat(response.data.totalElements)
      })
      findMenuTree().then((response) => {
        var options = this.findTree(response.data)
        this.options = options
      })
      this.listLoading = false
    },
    findTree(arry) {
      var options = []
      arry.forEach(element => {
        var value = element.menuId
        var label = element.menuName
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
      if (
        this.listQuery.menuName == null &&
        this.listQuery.menuCode == null &&
        this.listQuery.parentName == null
      ) {
        return
      }
      if (this.listQuery.menuName === '') {
        this.listQuery.menuName = null
      }
      this.fetchData()
    },
    menuTreeChange(val) {
      var length = val.length
      if (length === 0) {
        this.temp.parentId = null
      } else {
        this.temp.parentId = parseInt(val[length - 1])
      }
    },
    handleCreate() {
      this.dialog = {
        title: '添加菜单',
        visible: true,
        status: 'create'
      }
      this.temp = Object.assign({}, menu)
      this.menuSelect = []
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          insertMenu(this.temp).then(() => {
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
      getParentId(row.menuId).then((res) => {
        this.menuSelect = res.data
      })
      this.dialog = {
        title: '编辑菜单',
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
          console.log(tempData.parentId)
          updateMenu(tempData).then(() => {
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
        deleteMenu(row.menuId).then((response) => {
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
