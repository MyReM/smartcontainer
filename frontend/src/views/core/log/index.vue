<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input clearable @keyup.enter.native="search" style="width: 200px;" size="small" class="filter-item" placeholder="日志信息" v-model="listQuery.message"></el-input>
      <!-- <el-select clearable style="width: 90px" class="filter-item" v-model="listQuery.importance" :placeholder="$t('table.importance')">
        <el-option v-for="item in importanceOptions" :key="item" :label="item" :value="item">
        </el-option>
      </el-select> -->
      <el-select clearable class="filter-item" size="small" style="width: 130px" v-model="listQuery.type" placeholder="类别">
        <el-option v-for="type in typeFilter" :key="type.value" :label="type.label" :value="type.value"></el-option>
      </el-select>

      <el-select clearable class="filter-item" size="small" style="width: 130px" v-model="listQuery.level" placeholder="级别">
        <el-option v-for="level in levelFilter" :key="level.value" :label="level.label" :value="level.value"></el-option>
      </el-select>
      <el-button class="filter-item" size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
      <el-button class="filter-item" size="small" style="margin-left: 10px;" @click="deleteAll" type="primary" icon="el-icon-edit">清空日志</el-button>
      <!-- <el-button class="filter-item" type="primary" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">{{$t('table.export')}}</el-button> -->
      <!-- <el-checkbox class="filter-item" style='margin-left:15px;' @change='tableKey=tableKey+1' v-model="showReviewer">{{$t('table.reviewer')}}</el-checkbox> -->
    </div>

    <el-table :data="list" size="mini" v-loading.body="listLoading" element-loading-text="Loading" stripe border fit highlight-current-row>
      
      <!-- <el-table-column type="section"></el-table-column> -->
      
      <el-table-column align="center" label='ID' prop="logId" width="95"></el-table-column>

      <el-table-column label="日志信息" prop="message" align="center"></el-table-column>

      <el-table-column label="类别" prop="type" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.type | typeFilter }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="level" label="级别" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.level | levelFilter }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="createTime" label="创建时间" width="200"></el-table-column>

      <el-table-column align="center" label="操作" width="120" class-name="small-padding fixed-width">
        <template slot-scope="scope">
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

  </div>

</template>

<script>

import { log, findAll, deleteLog, deleteAll } from '@/api/log'

export default {
  data() {
    return {
      total: null,
      list: null,
      listLoading: true,
      levelFilter: null,
      typeFilter: null,
      listQuery: {
        page: 1,
        size: 10,
        message: null,
        type: null,
        level: null
      },
      temp: log
    }
  },
  filters: {
    levelFilter(status) {
      const statusMap = {
        0: '成功',
        1: '调试',
        2: '警告',
        3: '错误'
      }
      return statusMap[status]
    },
    typeFilter(status) {
      const statusMap = {
        0: '登录',
        1: '业务',
        2: '扫描'
      }
      return statusMap[status]
    }
  },
  created() {
    this.fetchData()
    // getFilter().then((res) => {
    //   this.levelFilter = res.level
    //   this.typeFilter = res.type
    // })
    this.levelFilter = [
      { label: '成功', value: 0 },
      { label: '调试', value: 1 },
      { label: '成功', value: 2 },
      { label: '警告', value: 3 }
    ]
    this.typeFilter = [
      { label: '登录', value: 0 },
      { label: '业务', value: 1 },
      { label: '扫描', value: 2 }
    ]
  },
  methods: {
    fetchData() {
      this.listLoading = true
      findAll(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = parseFloat(response.data.totalElements)
      })
      // getFilter().then((res) => {
      //   this.levelFilter = res.data.level
      //   this.typeFilter = res.data.type
      // })
      this.listLoading = false
    },
    search() {
      if (
        this.listQuery.message === null &&
        this.listQuery.level === null &&
        this.listQuery.type === null
      ) {
        return
      }
      if (this.listQuery.message === '') {
        this.listQuery.message = null
      }
      this.fetchData()
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteLog(row.logId).then((response) => {
          this.$message.success('删除成功!')
          this.fetchData()
        })
      }).catch(() => {
        this.$message.info('已取消删除!')
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
    deleteAll() {
      this.$confirm('此操作将清空日志记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAll().then((response) => {
          this.$message.success('删除成功!')
          this.fetchData()
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    }
  }
}
</script>

<style>
  .filter-container{
    margin-top: 10px;
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
