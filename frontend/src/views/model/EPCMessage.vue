<template>
<div id="drinks">
    <div style="width:96%;margin:auto;padding:20px 0 0 0;">
      <el-input clearable="clearable" v-model="listQuery.epc" size="small" placeholder="EPC" class="filter-item" style="width: 200px;"></el-input>
      <el-select v-model="listQuery.typeId" clearable placeholder="请选择产品类型" @change="getAntiFakePage()">
        <el-option
          v-for="item in searchTypes"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button size="small" @click="searchAntiFake()" type="primary" icon="el-icon-search">查询</el-button>
      <el-button size="small" @click="addAntiFake()" type="success" icon="el-icon-search">添加</el-button>
	</div>
	<div style="width: 96%;margin: auto;padding:20px 0 0 0;">
		
		<el-table v-loading.body="listLoading" :data="list" size="mini" element-loading-text="Loading"  stripe border fit highlight-current-row>
	            
      <el-table-column label="EPC" prop="epc" align="center"></el-table-column>

      <el-table-column label="二维码" prop="qrCode" align="center"></el-table-column>

      <el-table-column label="类型" prop="remark" align="center">
        <template slot-scope="scope">
          {{typeList[scope.row.typeId].typeName === undefined ? '' : typeList[scope.row.typeId].typeName}}
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="warning" round style="height:25px;padding:5px 10px;fond-size:12px;margin-top:3px;" @click="updateAntiFake(scope.row)">修改信息</el-button>
          <el-button type="danger" round style="height:25px;padding:5px 10px;fond-size:12px;margin-top:3px;" @click="deleteAntiFake(scope.row)">删除信息</el-button>
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
		<el-dialog :title="antiFakeDialog.title" :visible.sync="antiFakeDialog.visiable" @close="closeDialog">
		  <el-form :model="temp" :rules="rules" ref="ruleForm" :label-position="labelPosition" label-width="60px" v-bind:class="{ show: antiFakeDialog.status == 'check' }">
			  <el-form-item label="串口选择" prop="" id="serial">
			  	<el-select v-model="serial" clearable placeholder="请选择串口">
            <el-option
              v-for="item in serialList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
				</el-form-item>

        <el-form-item label="类型" prop="typeId" id="changeType">
			  	<el-select v-model="temp.typeId" clearable placeholder="请选择产品类型" @change="getAntiFakePage()">
            <el-option
              v-for="item in types"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
				</el-form-item>
        <!-- <el-form-item label="" prop="" id="openserial">
			  	<el-button type="success" @click="serialOperate">{{serialMessage}}</el-button>
				</el-form-item> -->
        <el-form-item label="EPC" prop="epc" id="epc">
			  	<el-input v-model="temp.epc" :disabled="true" placeholder="请获取NFC"></el-input>
				</el-form-item>

        <el-form-item label="" prop="" id="serepc">
			  	<el-button type="success" @click="getEPC" :disabled="getNFC">获取NFC</el-button>
				</el-form-item>
        
        <el-form-item label="二维码" prop="qrCode" id="qrCode">
			  	<el-input v-model="temp.qrCode" :disabled="antiFakeDialog.status == 'check'" placeholder="请扫描二维码"></el-input>
				</el-form-item>

        <el-form-item label="备注" prop="remark" id="remark">
			  	<el-input v-model="temp.remark" :disabled="antiFakeDialog.status == 'check'" placeholder="备注"></el-input>
				</el-form-item>
			</el-form>
			
		  <div slot="footer" class="dialog-footer">
	      <el-button @click="antiFakeDialog.visiable=false">取消</el-button>
	    	<el-button v-if="antiFakeDialog.status == 'create'" @click="changeAntiFake" type="primary">确认</el-button>
	    	<el-button v-if="antiFakeDialog.status == 'update'" type="primary" @click="changeAntiFake">修改</el-button>
	  	</div>
		</el-dialog>
	</div>

</div>
</template>

<script>
import { antiFake, getPages, addAntiFake, updateAntiFake, deleteAntiFake, getAllAntiType, getSerial, openNFC, searchNFC, closeNFC } from '@/api/antiFake'
import { setTimeout } from 'timers'
export default {
  data() {
    return {
      serialMessage: '开启串口',
      serialStatus: false,
      antiFake: null,
      list: null,
      total: null,
      labelPosition: 'right',
      listLoading: true,
      listQuery: {
        page: 1,
        size: 10,
        epc: null,
        typeId: null
      },
      searchTypes: [{
        value: null,
        label: '所有'
      }],
      types: [],
      temp: antiFake,
      antiFakeDialog: {
        title: '',
        visiable: false,
        status: ''
      },
      rules: {
        epc: [{ required: true, message: '请输入EPC', trigger: 'blur' }],
        qrCode: [{ required: true, message: '请扫描二维码', trigger: 'blur' }]
      },
      serialList: [],
      serial: null,
      typeList: [],
      getNFC: false
    }
  },
  // 一开始执行的方法
  created() {
    this.getAntiFakePage()
    this.getAllType()
    this.getSerial()
  },
  methods: {
    getAllType() {
      getAllAntiType().then(res => {
        this.typeList = res.data
        res.data.forEach((val, index) => {
          this.searchTypes.push({
            value: `${index}`,
            label: `${val.typeName}`
          })
          this.types.push({
            value: `${index}`,
            label: `${val.typeName}`
          })
        })
      })
    },
    // 添加、修改的对话框关闭时执行的方法
    closeDialog() {
      if (this.serialStatus) {
        this.serialOperate()
      }
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
      this.getAntiFakePage()
    },
    // 表格页数请求
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.getAntiFakePage()
    },
    search() {
      this.getAntiFakePage()
    },
    updateAntiFake(row) {
      this.temp = Object.assign({}, row)
      this.antiFakeDialog.visiable = true
      this.antiFakeDialog.title = '修改信息'
      this.antiFakeDialog.status = 'update'
    },
    changeAntiFake() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          if (this.serialStatus) {
            this.$message({
              type: 'error',
              message: '请关闭窜口!'
            })
          } else {
            if (this.antiFakeDialog.status === 'update') {
              updateAntiFake(this.temp).then(res => {
                if (res.code === 200) {
                  this.getAntiFakePage()
                  this.antiFakeDialog.visiable = false
                  this.$message({
                    type: 'success',
                    message: '修改成功!'
                  })
                }
              })
            } else if (this.antiFakeDialog.status === 'create') {
              addAntiFake(this.temp).then(res => {
                if (res.code === 200) {
                  this.getAntiFakePage()
                  this.antiFakeDialog.visiable = false
                  this.$message({
                    type: 'success',
                    message: '添加成功!'
                  })
                }
              })
            }
          }
        }
      })
    },
    getAntiFakePage() {
      getPages(this.listQuery).then(response => {
        this.list = response.data.content
        console.log(this.list)
        this.total = parseInt(response.data.totalElements)
        this.listLoading = false
      })
    },
    addAntiFake() {
      this.getNFC = false
      this.antiFake = {}
      this.temp.typeId = this.types[0].value
      this.temp.epc = null
      this.temp.qrCode = null
      this.temp.remark = null
      this.antiFakeDialog.visiable = true
      this.antiFakeDialog.title = '添加信息'
      this.antiFakeDialog.status = 'create'
    },
    searchAntiType() {
      this.getAntiFakePage()
    },
    deleteAntiFake(row) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAntiFake(row.id).then(res => {
          if (res.code === 200) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getAntiFakePage()
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
    },
    // 获取串口
    getSerial() {
      getSerial().then(res => {
        this.serial = res.data[0]
        res.data.forEach(val => {
          this.serialList.push({
            value: `${val}`,
            label: `${val}`
          })
        })
      })
    },
    // 开启关闭串口
    serialOperate() {
      let status
      if (!this.serialStatus) {
        openNFC('COM4').then(res => {
          status = res.data
        })
        this.serialMessage = '关闭串口'
      } else {
        closeNFC().then(res => {
          status = res.data
        })
        this.serialMessage = '开启串口'
      }
      if (status === 200) {
        this.serialStatus = !this.serialStatus
      }
    },
    // 获取epc
    getEPC() {
      this.getNFC = true
      openNFC(this.serial).then(res => {
        if (res.data === 200) {
          searchNFC().then(res => {
            if (res.data === '读取失败') {
              this.$message({
                type: 'error',
                message: res.data + ',请检查串口是否正确或NFC是否损坏'
              })
            } else {
              this.temp.epc = res.data
            }
            this.getNFC = false
          })
        } else {
          this.getNFC = false
          this.$message({
            type: 'error',
            message: '读取失败，请检查串口是否正确或NFC是否损坏！'
          })
        }
      })
      // if (this.serialStatus) {
      //   alert('获取epc')
      // } else {
      //   this.$message({
      //     type: 'error',
      //     message: '请开启串口'
      //   })
      // }
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
		height: 35px !important;
		line-height: 35px !important;
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
  #qrCode,#remark{
    width: 90% !important;
  }
  #qrCode .el-form-item__content,#remark .el-form-item__content {
    width: 82% !important;
  }
  #epc {
    width: 50% !important;
  }
  #epc .el-form-item__content, {
    width: 75% !important;
  }
  #changeType .el-select {
    width: 100% !important;
  }
  #openserial {
    width: 20% !important;
  }
</style>