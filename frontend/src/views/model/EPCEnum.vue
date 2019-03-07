<template>
<div id="drinks">
    <div style="width:96%;margin:auto;padding:20px 0 0 0;">
      <el-input clearable="clearable" v-model="listQuery.typeName" size="small" placeholder="类型名称" class="filter-item" style="width: 200px;"></el-input>
      <el-button size="small" @click="searchAntiType()" type="primary" icon="el-icon-search">查询</el-button>
      <el-button size="small" @click="addAntiType()" type="success" icon="el-icon-search">添加</el-button>
	</div>
	<div style="width: 96%;margin: auto;padding:20px 0 0 0;">
		
		<el-table v-loading.body="listLoading" :data="list" size="mini" element-loading-text="Loading"  stripe border fit highlight-current-row>
	            
      <el-table-column label="类型名称" prop="typeName" align="center"></el-table-column>

      <el-table-column label="地址" prop="url" align="center"></el-table-column>

      <el-table-column label="备注" prop="remark" align="center"></el-table-column>

      <el-table-column align="center" label="操作" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="warning" round style="height:25px;padding:5px 10px;fond-size:12px;margin-top:3px;" @click="updateAntiType(scope.row)">修改信息</el-button>
          <el-button type="danger" round style="height:25px;padding:5px 10px;fond-size:12px;margin-top:3px;" @click="deleteAntiType(scope.row)">删除信息</el-button>
        </template>
      </el-table-column>
	  </el-table>	
    
    <div style="margin-top: 10px;">
        <el-pagination :small="true" @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :current-page="listQuery.page" :page-sizes="[10, 20, 30, 40]" :page-size="listQuery.size"
          layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
		</div>
	</div>

  <!-- 添加和修改酒类信息 -->
	<div id="au">
		<el-dialog :title="antiTypeDialog.title" :visible.sync="antiTypeDialog.visiable" @close="closeDialog">
		  <el-form :model="temp" :rules="rules" ref="ruleForm" :label-position="labelPosition" label-width="60px" v-bind:class="{ show: antiTypeDialog.status == 'check' }">
			  <el-form-item label="类型名称" prop="typeName">
			  	<el-input v-model="temp.typeName" :disabled="antiTypeDialog.status == 'check'" placeholder="类型名称"></el-input>
				</el-form-item>
        <el-form-item label="地址" prop="url">
			  	<el-input v-model="temp.url" :disabled="antiTypeDialog.status == 'check'" placeholder="地址"></el-input>
				</el-form-item>
        <el-form-item label="备注" prop="remark">
			  	<el-input v-model="temp.remark" :disabled="antiTypeDialog.status == 'check'" placeholder="备注"></el-input>
				</el-form-item>
			</el-form>
			
		  <div slot="footer" class="dialog-footer">
	      <el-button @click="antiTypeDialog.visiable=false">取消</el-button>
	    	<el-button v-if="antiTypeDialog.status == 'create'" @click="changeAntiType" type="primary">确认</el-button>
	    	<el-button v-if="antiTypeDialog.status == 'update'" type="primary" @click="changeAntiType">修改</el-button>
	  	</div>
		</el-dialog>
	</div>

</div>
</template>

<script>
import { antiType, getPages, addAntiType, updateAntiType, deleteAntiType } from '@/api/anitType'
export default {
  data() {
    return {
      antiType: null,
      list: null,
      total: null,
      labelPosition: 'right',
      listLoading: true,
      listQuery: {
        page: 1,
        size: 10,
        typeName: null
      },
      temp: antiType,
      antiTypeDialog: {
        title: '',
        visiable: false,
        status: ''
      },
      rules: {
        typeName: [{ required: true, message: '请输入类型名称', trigger: 'blur' }],
        url: [{ required: true, message: '请填写地址', trigger: 'blur' }]
      }
    }
  },
  // 一开始执行的方法
  created() {
    this.getAntiTypePage()
  },
  methods: {
    // 添加、修改的对话框关闭时执行的方法
    closeDialog() {
      this.temp = {}
      if (this.$refs['ruleForm'] !== undefined) {
        this.$refs['ruleForm'].resetFields()
      }
      if (this.$refs['rfidForm'] !== undefined) {
        this.$refs['rfidForm'].resetFields()
      }
    },
    // 表格页数请求
    handleSizeChange(val) {
      this.listQuery.size = val
      this.getAntiTypePage()
    },
    // 表格页数请求
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.getAntiTypePage()
    },
    search() {
      this.getAntiTypePage()
    },
    updateAntiType(row) {
      this.temp = Object.assign({}, row)
      this.antiTypeDialog.visiable = true
      this.antiTypeDialog.title = '修改信息'
      this.antiTypeDialog.status = 'update'
    },
    changeAntiType() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          if (this.antiTypeDialog.status === 'update') {
            updateAntiType(this.temp).then(res => {
              if (res.code === 200) {
                this.getAntiTypePage()
                this.antiTypeDialog.visiable = false
                this.$message({
                  type: 'success',
                  message: '修改成功!'
                })
              }
            })
          } else if (this.antiTypeDialog.status === 'create') {
            addAntiType(this.temp).then(res => {
              if (res.code === 200) {
                this.getAntiTypePage()
                this.antiTypeDialog.visiable = false
                this.$message({
                  type: 'success',
                  message: '添加成功!'
                })
              }
            })
          }
        }
      })
    },
    getAntiTypePage() {
      getPages(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = parseInt(response.data.totalElements)
        this.listLoading = false
      })
    },
    addAntiType() {
      this.antiType = {}
      this.antiTypeDialog.visiable = true
      this.antiTypeDialog.title = '添加信息'
      this.antiTypeDialog.status = 'create'
    },
    searchAntiType() {
      this.getAntiTypePage()
    },
    deleteAntiType(row) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAntiType(row.id).then(res => {
          if (res.code === 200) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getAntiTypePage()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败!'
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}
</script>

<style>
  #drinks .el-dialog__body {
    padding: 0 0 0 0;
    width: 100%;
    text-align: center;
  }
  #drinks .el-form-item {
    width: 40%;
    display: inline-block;
    margin: 0 0 15px 0;
  }
  #drinks .el-form-item__label {
    float: left;
    width: 29%;
  }
  #drinks .el-form-item__content{
    display: inline-block;
  }
  #drinks #pigarea {
    width: 100%;
  }
  #drinks #pigarea .el-form-item__label {
    display: block;
    width: 100%;
    text-align: center;
  }
  #drinks #pigarea .el-form-item__content {
    display: block;
    width: 50%;
    margin: 0 0 0 25%;
  }
  #drinks #pigarea .el-form-item__content textarea{
    text-align: center;
    height: 100px;
  }
  #drinks .el-table--mini td {
    padding: 0;
    height: 35px;
    font-size: 12px;
  }
  #drinks .el-table--mini th {
    padding: 0;
    height: 40px;
    font-size: 14px;
  }
	.el-dialog__title::after{
	    content: "";
	    display: block;
	    width: 100%;
	    margin-top: 10px;
	    border:.5px solid #cccccc;
  	}
  	.el-table tr{
  		height: 35px;
  	}
	.fenl{
		height: 25px;
		border-bottom: 2px solid #e6e6e6;
		margin-bottom: 20px;
	}
  	.el-dialog__footer{
    	clear: both;
  	}
  	.el-input--suffix .el-input__inner{
		padding-right:16px;
  	}
  	.el-input__inner{
		height: 35px;
		line-height: 35px;
	}
	.el-input--suffix .el-input__inner{
		padding-right:16px;
	}
	.el-date-editor.el-input, .el-date-editor.el-input__inner{
		width: 185px;
	}
	.el-form-item {
    	margin-bottom: 18px;
	}
	.el-textarea__inner{
		padding: 5px 13px;
	}
	
	#au .el-form-item{
	    width: 48%;
	    display: inline-block;
    }
    #au .el-form-item__label{
    	color: slategray;
      width: 90px !important;
  	}
    #au .el-form-item__content{
    	margin-left: 0 !important;
  	}
  	#au .el-form-item__error{
    	left: 0;
  	}
  	#au .er{
  		margin-left: 4px;
  		margin-bottom: -40px;
  	}
  	
  	#au .el-dialog{
  		width: 48%;
  	}
  	@media screen and (max-width:1300px){
 		#au .el-dialog{
   			width: 81%;
  		}
  	}
  	
  	.show .el-input__inner{
  		border: 0px solid #fff;
  		background-color: #FFFFFF !important;
  		text-align: center;
  		border-radius: 0;
  		border-bottom: 1px solid #dcdfe6;
  	}
  	.show .el-textarea__inner{
		border: 0px solid #fff;
  		border-radius: 0;
  		background-color: #FFFFFF !important;
  		border-bottom: 1px solid #dcdfe6;
	}
</style>