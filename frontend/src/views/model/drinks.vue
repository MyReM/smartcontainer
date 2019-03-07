<template>
<div id="drinks">
	<div style="width: 96%;margin: auto;padding:20px 0 0 0;">
		
		<el-table v-loading.body="listLoading" :data="drinks" size="mini" element-loading-text="Loading"  stripe border fit highlight-current-row>
	            
      <el-table-column label="商品名称" prop="name" align="center"></el-table-column>

      <!-- <el-table-column label="商品类型" prop="commoditiesType" align="center"></el-table-column> -->

      <el-table-column label="生产日期" prop="madeDate" align="center"></el-table-column>

      <el-table-column label="度数" prop="degree" align="center"></el-table-column>

      <el-table-column label="价格" prop="price" align="center"></el-table-column>

      <el-table-column label="描述" prop="message" align="center"></el-table-column>

      <el-table-column label="总数量" prop="totalNumber" align="center"></el-table-column>

      <el-table-column label="在库数量" prop="sum" align="center"></el-table-column>

      <el-table-column align="center" label="操作" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="warning" round style="height:25px;padding:5px 10px;fond-size:12px;margin-top:3px;" @click="updateDrinks(scope.row)">修改信息</el-button>
        </template>
      </el-table-column>
	  </el-table>
    <!-- ----------------------RFID-------------------------- -->
    <div style="margin-top:20px;margin-bottom:10px;">
			<el-input v-model="listQuery.CommoditiesCode" size="small" placeholder="RFID" class="filter-item" style="width: 200px;"></el-input>
      <el-select v-model="listQuery.isIn" clearable placeholder="请选择是否在库" @change="searchCommoditiesCode()">
        <el-option
          v-for="item in isInList"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button size="small" @click="searchCommoditiesCode()" type="primary" icon="el-icon-search">查询</el-button>
      <el-button size="small" @click="addCommoditiesCode()" type="success" icon="el-icon-search">添加</el-button>
		</div>
		
    <el-table v-loading.body="listLoading1" :data="commoditiesCodes" size="mini" element-loading-text="Loading"  stripe border fit highlight-current-row>
	            
      <el-table-column label="RFID条码号" prop="commoditiesCode" align="center"></el-table-column>

      <el-table-column label="是否在库" align="center">
        <template slot="header">
          <span>是否在库</span>
        </template>
        <template slot-scope="scope">
          {{scope.row.isIn === 1 ? '在库' : '已拿出'}}
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="warning" round style="height:25px;padding:5px 10px;fond-size:12px;margin-top:3px;" @click="updateCommoditiesCode(scope.row)">修改条码</el-button>
          <el-button type="danger" round style="height:25px;padding:5px 10px;fond-size:12px;margin-top:3px;" @click="deleteCommoditiesCode(scope.row)">删除条码</el-button>
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
		<el-dialog :title="drinksDialog.title" :visible.sync="drinksDialog.visiable" @close="closeDialog">
		  <el-form :model="temp" :rules="rules" ref="ruleForm" :label-position="labelPosition" label-width="60px" v-bind:class="{ show: drinksDialog.status == 'check' }">
			  <el-form-item label="名称" prop="name">
			  	<el-input v-model="temp.name" :disabled="drinksDialog.status == 'check'" placeholder="名称"></el-input>
				</el-form-item>
        <el-form-item label="生产日期" prop="madeDate">
          <el-date-picker
            v-model="temp.madeDate"
            type="date"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
            placeholder="选择日期">
          </el-date-picker>
			  	<!-- <el-input v-model="temp.madeDate" :disabled="drinksDialog.status == 'check'" placeholder="生产日期"></el-input> -->
				</el-form-item>
        <el-form-item label="度数" prop="degree">
			  	<el-input v-model="temp.degree" :disabled="drinksDialog.status == 'check'" placeholder="度数"></el-input>
				</el-form-item>
        <el-form-item label="价格" prop="price">
			  	<el-input v-model="temp.price" type="number" :disabled="drinksDialog.status == 'check'" placeholder="价格"></el-input>
				</el-form-item>
        <el-form-item label="描述" prop="message">
			  	<el-input v-model="temp.message" :disabled="drinksDialog.status == 'check'" placeholder="描述"></el-input>
				</el-form-item>
        <el-form-item label="总数量" prop="sum">
			  	<el-input v-model="temp.totalNumber" disabled="disabled" placeholder="总数量"></el-input>
				</el-form-item>
        <el-form-item label="在库数量" prop="sum">
			  	<el-input v-model="temp.sum" disabled="disabled" placeholder="总数量"></el-input>
				</el-form-item>
			</el-form>
			
		  <div slot="footer" class="dialog-footer">
	      <el-button @click="drinksDialog.visiable=false">取消</el-button>
	    	<el-button v-if="drinksDialog.status == 'create'" @click="addDrinks" type="primary">确认</el-button>
	    	<el-button v-if="drinksDialog.status == 'update'" type="primary" @click="changeDrinks">确认</el-button>
	  	</div>
		</el-dialog>
	</div>

   <!-- 添加和修改酒RFID信息 -->
	<div id="au">
		<el-dialog :title="rfidDialog.title" :visible.sync="rfidDialog.visiable" @close="closeDialog">
		  <el-form :model="commoditiesCode" :rules="rfidRules" ref="rfidForm" :label-position="labelPosition" label-width="60px" v-bind:class="{ show: rfidDialog.status == 'check' }">
			  <el-form-item label="RFID" prop="commoditiesCode">
			  	<el-input v-model="commoditiesCode.commoditiesCode" :disabled="rfidDialog.status == 'check'" placeholder="RFID"></el-input>
				</el-form-item>
			</el-form>
			
		  <div slot="footer" class="dialog-footer">
	      <el-button @click="rfidDialog.visiable=false">取消</el-button>
	    	<el-button v-if="rfidDialog.status == 'create'" @click="changeCommoditiesCode()" type="primary">确认</el-button>
	    	<el-button v-if="rfidDialog.status == 'update'" type="primary" @click="changeCommoditiesCode()">确认</el-button>
	  	</div>
		</el-dialog>
	</div>
</div>
</template>

<script>
import { getAllDrinks, drinks, updateDrinks } from '@/api/drinks'
import { commoditiesCode, getCommoditiesCodePage, deleteCommoditiesCode, updateCommoditiesCode, addCommoditiesCode } from '@/api/commoditiesCode'
export default {
  data() {
    return {
      isInList: [{
        value: 1,
        label: '在库'
      }, {
        value: 0,
        label: '已拿出'
      }, {
        value: null,
        label: '所有'
      }],
      commoditiesCodes: null,
      commoditiesCode: commoditiesCode,
      drinks: null,
      list: null,
      total: null,
      labelPosition: 'right',
      listLoading: true,
      listLoading1: true,
      listQuery: {
        page: 1,
        size: 10,
        commoditiesType: 1,
        CommoditiesCode: null,
        isIn: null
      },
      temp: drinks,
      drinksDialog: {
        title: '',
        visiable: false,
        status: ''
      },
      rfidDialog: {
        title: '',
        visiable: false,
        status: ''
      },
      rules: {
        name: [{ required: true, message: '请输入商品名', trigger: 'blur' }],
        madeDate: [{ required: true, message: '请填写出产日期', trigger: 'blur' }],
        degree: [{ required: true, message: '请填写度数', trigger: 'blur' }],
        price: [{ required: true, message: '请填写价格', trigger: 'blur' }]
      },
      rfidRules: {
        commoditiesCode: [{ required: true, message: '请输入条码号', trigger: 'blur' }]
      }
    }
  },
  // 一开始执行的方法
  created() {
    this.getAllDrinks()
    this.getCommoditiesCodePage()
  },
  methods: {
    // 添加、修改的对话框关闭时执行的方法
    closeDialog() {
      this.temp = {}
      this.commoditiesCode = {}
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
      this.getCommoditiesCodePage()
    },
    // 表格页数请求
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.getCommoditiesCodePage()
    },
    search() {
      this.getCommodityPage()
    },
    getAllDrinks() {
      this.listLoading = true
      getAllDrinks().then(response => {
        this.drinks = response.data
        this.listLoading = false
      })
    },
    updateDrinks(row) {
      this.temp = Object.assign({}, row)
      this.drinksDialog.visiable = true
      this.drinksDialog.title = '修改酒类信息'
      this.drinksDialog.status = 'update'
    },
    changeDrinks() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          updateDrinks(this.temp).then(res => {
            if (res.code === 200) {
              this.getAllDrinks()
              this.drinksDialog.visiable = false
              this.$message({
                type: 'success',
                message: '修改成功!'
              })
            }
          })
        }
      })
    },
    getCommoditiesCodePage() {
      getCommoditiesCodePage(this.listQuery).then(response => {
        this.commoditiesCodes = response.data.content
        this.total = parseInt(response.data.totalElements)
        this.listLoading1 = false
      })
    },
    updateCommoditiesCode(row) {
      this.commoditiesCode = Object.assign({}, row)
      this.rfidDialog.visiable = true
      this.rfidDialog.title = '修改条码信息'
      this.rfidDialog.status = 'update'
    },
    addCommoditiesCode() {
      this.commoditiesCode = {}
      this.rfidDialog.visiable = true
      this.rfidDialog.title = '添加条码信息'
      this.rfidDialog.status = 'create'
    },
    searchCommoditiesCode() {
      this.getCommoditiesCodePage()
    },
    changeCommoditiesCode() {
      this.$refs['rfidForm'].validate((valid) => {
        if (valid) {
          if (this.rfidDialog.status === 'update') {
            updateCommoditiesCode(this.commoditiesCode).then(res => {
              if (res.code === 200) {
                if (res.data === '105') {
                  this.$message({
                    type: 'error',
                    message: '已存在该条码!'
                  })
                } else {
                  this.getCommoditiesCodePage()
                  this.rfidDialog.visiable = false
                  this.$message({
                    type: 'success',
                    message: '修改成功!'
                  })
                }
              }
            })
          } else if (this.rfidDialog.status === 'create') {
            this.commoditiesCode.commoditiesType = 1
            this.temp = this.drinks[0]
            addCommoditiesCode(this.commoditiesCode).then(res => {
              if (res.code === 200) {
                if (res.data === '105') {
                  this.$message({
                    type: 'error',
                    message: '已存在该条码!'
                  })
                } else {
                  updateDrinks(this.temp).then(res => {
                    if (res.code === 200) {
                      this.getAllDrinks()
                    }
                  })
                  this.getCommoditiesCodePage()
                  this.rfidDialog.visiable = false
                  this.$message({
                    type: 'success',
                    message: '添加成功!'
                  })
                }
              }
            })
          }
        }
      })
    },
    deleteCommoditiesCode(row) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCommoditiesCode(row.id).then(res => {
          if (res.code === 200) {
            this.temp = this.drinks[0]
            updateDrinks(this.temp).then(res => {
              if (res.code === 200) {
                this.getAllDrinks()
              }
            })
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getCommoditiesCodePage()
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