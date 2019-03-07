<template>
<div id="showCommodity">
	<div style="width: 96%;margin: auto;">
		<div style="margin-top:20px;margin-bottom:10px;">
			<el-input v-model="listQuery.commoditiesName" size="small" placeholder="商品名称" class="filter-item" style="width: 200px;"></el-input>
      <el-button size="small" @click="search()" type="primary" icon="el-icon-search">查询</el-button>
      <!-- <el-button size="small" @click="addCommodity()" type="success" icon="el-icon-search">添加</el-button> -->
		</div>
		
		<el-table v-loading.body="listLoading" :data="list" size="mini" element-loading-text="Loading"  stripe border fit highlight-current-row>
	            
      <el-table-column label="是否展示" align="center">
        <template slot-scope="scope">
          {{scope.row.isShow === 1 ? '已展示' : '未展示'}}
        </template>
      </el-table-column>

      <el-table-column label="商品名称" prop="commoditiesName" align="center"></el-table-column>

      <el-table-column label="商品类型" prop="commoditiesTypeName" align="center"></el-table-column>

      <el-table-column label="总数量" align="center">
        <template slot-scope="scope">
          {{scope.row.totalNumber > 0 ? scope.row.totalNumber : '0'}}
        </template>
      </el-table-column>
      <el-table-column label="在库数量" align="center">
        <template slot-scope="scope">
          {{scope.row.sum > 0 ? scope.row.sum : '0'}}
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" class-name="small-padding fixed-width" width="260px">
        <template slot-scope="scope">
          <el-button :type="scope.row.isShow === 1 ? 'success' : 'primary'" round style="height:25px;padding:5px 10px;fond-size:12px;margin-top:3px;" @click="showOrHide(scope.row)">{{scope.row.isShow === 1 ? '已展示' : '已隐藏'}}</el-button>
          <!-- <el-tooltip class="item" effect="dark" content="展示/隐藏" placement="top">
            <a style="cursor: pointer;"><i class="el-icon-edit" @click="showOrHide(scope.row)"></i></a>
          </el-tooltip> -->
          <el-button type="warning" round style="height:25px;padding:5px 10px;fond-size:12px;margin-top:3px;" @click="imgController(scope.row)">图片管理</el-button>
          <el-dialog title="图片管理" :visible.sync="dialogFormVisible">
            <el-form>
              <el-upload
                class="avatar-uploader"
                ref="uploadImgs"
                :data="imgData"
                :action="doUpload"
                :headers="headers"
                :on-success="uploadSuccess" 
                :before-upload="beforeUpload"
                :multiple="true"
                :limit="4"
                :on-exceed="handleExceed"
                :auto-upload="false"
                name="files"
              >
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
                <div slot="tip" class="el-upload__tip"></div>
              </el-upload>
              <el-carousel :interval="3000" type="card" height="320px">
                <el-carousel-item v-for="(item, index) in imgsList" :key="index">
                <div class="delImg" @click="delImg(item)">X</div>
                  <img height=320px width=100% :id=item.id :src="'static/imgs/' + item.uploadFileName+item.sufName"/>
                </el-carousel-item>
              </el-carousel>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
            </div>
            </el-dialog>
          <el-button type="danger" round style="height:25px;padding:5px 10px;fond-size:12px;margin-top:3px;" @click="deleteCommodity(scope.row)">删除</el-button>
          <!-- <el-tooltip class="item" effect="dark" content="删除" placement="top">
            <a style="cursor: pointer;margin-left: 10px;"><i class="el-icon-delete" @click="deleteCommodity(scope.row)"></i></a>
          </el-tooltip> -->
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

  <!-- 添加和修改
	<div id="au">
		<el-dialog :title="dialog.title" :visible.sync="dialog.visiable" @close="closeDialog">
		  <el-form :model="temp" :rules="rules" ref="ruleForm" :label-position="labelPosition" label-width="120px" v-bind:class="{ show: dialog.status == 'check' }">
			  <el-form-item label="地址" style="width: 100%;" prop="position">
			  	<el-input v-model="temp.position" :disabled="dialog.status == 'check'" placeholder="地址"></el-input>
				</el-form-item>
	      <el-form-item label="备注" style="width: 100%;">
	        <el-input :disabled="dialog.status == 'check'" type="textarea" :autosize="{ minRows: 2}" placeholder="备注" v-model="temp.remarks"></el-input>
	      </el-form-item>
			</el-form>
			
		  <div slot="footer" class="dialog-footer">
	      <el-button @click="dialog.visiable=false">取消</el-button>
	    	<el-button v-if="dialog.status == 'create'" @click="addCommodity" type="primary">确认</el-button>
	    	<el-button v-if="dialog.status == 'update'" type="primary" @click="updateCommodity">确认</el-button>
	  	</div>
		</el-dialog>
	</div> -->
</div>
</template>

<script>
import { showCommodity, getCommodityPage, getAllCommodity, updateCommodity, deleteCommodity } from '@/api/showCommodity'
import { findAllImgByCommoditiesType, delImgBySrc } from '@/api/commoditiesCode'
import { getToken } from '@/utils/auth'
import { setTimeout } from 'timers'
export default {
  data() {
    return {
      list: null,
      total: null,
      imgData: {
        commoditiesType: null
      },
      labelPosition: 'right',
      listLoading: true,
      listQuery: {
        page: 1,
        size: 10,
        commoditiesName: null
      },
      temp: showCommodity,
      dialog: {
        title: '',
        visiable: false,
        status: ''
      },
      rules: {
        commoditiesName: [{ required: true, message: '请输入商品名', trigger: 'blur' }],
        commoditiesType: [{ required: true, message: '请选择商品类型', trigger: 'blur' }],
        password: [{ required: true, message: '请选择是否显示', trigger: 'blur' }]
      },
      allCommodity: null,
      imgsList: null,
      doUpload: process.env.BASE_API + '/saveImg',
      dialogFormVisible: false,
      formLabelWidth: '120px',
      headers: {
        Authorization: getToken()
      }
    }
  },
  // 一开始执行的方法
  created() {
    this.getCommodityPage()
    this.getAllCommodity()
  },
  methods: {
    delImg(item) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delImgBySrc(item.src, item.id).then(res => {
          if (res.data === 200) {
            setTimeout(
              findAllImgByCommoditiesType(this.imgData.commoditiesType).then(response => {
                this.imgsList = response.data.data
              }), 1)
            this.$message({
              type: 'success',
              message: '删除成功!'
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
    },
    submitUpload() {
      this.$refs.uploadImgs.submit()
    },
    uploadSuccess() {
      this.$refs.uploadImgs.clearFiles()
      setTimeout(
        findAllImgByCommoditiesType(this.imgData.commoditiesType).then(response => {
          this.imgsList = response.data.data
        }), 1)
    },
    handleRemove(file, imgsList) {
      console.log(file, imgsList)
    },
    handlePreview(file) {
      console.log(file)
    },
    handleExceed(files, imgsList) {
      this.$message.warning(`当前限制选择 4 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + imgsList.length} 个文件`)
    },
    beforeRemove(file, imgsList) {
      return this.$confirm(`确定移除 ${file.name}?`)
    },
    // 添加、修改的对话框关闭时执行的方法
    closeDialog() {
      this.temp = {}
      this.$refs['ruleForm'].resetFields()
    },
    // 表格页数请求
    handleSizeChange(val) {
      this.listQuery.size = val
      this.getCommodityPage()
    },
    // 表格页数请求
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.getCommodityPage()
    },
    search() {
      this.getCommodityPage()
    },
    getCommodityPage() {
      this.listLoading = true
      getCommodityPage(this.listQuery).then(response => {
        this.list = response.data.content
        // this.list.forEach(element => {
        //   if (element.isShow === 1) {
        //     element.isShow = '已展示'
        //   } else {
        //     element.isShow = '未展示'
        //   }
        // })
        this.total = parseInt(response.data.totalElements)
        this.listLoading = false
      })
    },
    getAllCommodity() {
      getAllCommodity().then(response => {
        this.allCommodity = response.data
      })
    },
    // addCommodity() {
    //   this.temp = {}
    //   this.dialog = {
    //     title: '添加批发商用户',
    //     visiable: true,
    //     status: 'create'
    //   }
    //   this.saleDisable = false
    //   this.trueORfalse = false
    // },
    // updateCommodity(row) {
    //   this.temp = Object.assign({}, row)
    //   console.log('sss:' + JSON.stringify(this.temp))
    //   this.dialog = {
    //     title: '修改批发商信息',
    //     visiable: true,
    //     status: 'update'
    //   }
    // },
    showOrHide(row) {
      let index = 0
      this.allCommodity.forEach(val => {
        if (val.isShow === 1) {
          ++index
        }
      })
      this.temp = Object.assign({}, row)
      const oshow = this.temp.isShow
      this.$confirm(this.temp.isShow === 0 ? '是否展示该商品' : '是否隐藏该商品', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.temp.isShow = this.temp.isShow === 1 ? 0 : 1
        if (index <= 3 || (oshow === 1 && this.temp.isShow === 0)) {
          updateCommodity(this.temp).then(res => {
            this.$message({
              type: 'success',
              message: this.temp.isShow === 1 ? '已展示' : '已隐藏'
            })
            this.getCommodityPage()
            this.getAllCommodity()
          })
        } else {
          this.$message({
            type: 'danger',
            message: '展示商品数量不能超过四个!'
          })
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消该操作'
        })
      })
    },
    // 图片管理
    imgController(row) {
      this.dialogFormVisible = true
      this.imgData.commoditiesType = row.commoditiesType
      findAllImgByCommoditiesType(row.commoditiesType).then(response => {
        this.imgsList = response.data.data
      })
    },
    deleteCommodity(row) {
      this.$confirm('此操作将永久删除该商品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCommodity(row.id).then(res => {
          if (res.code === 200) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getAllCommodity()
            this.getCommodityPage()
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
  #showCommodity .el-dialog__body {
    padding: 0 0 0 0;
    width: 100%;
    text-align: center;
  }
  #showCommodity .el-form-item {
    width: 40%;
    display: inline-block;
    margin: 0 0 15px 0;
  }
  #showCommodity .el-form-item__label {
    float: left;
    width: 29%;
  }
  #showCommodity .el-form-item__content{
    display: inline-block;
  }
  #showCommodity #pigarea {
    width: 100%;
  }
  #showCommodity #pigarea .el-form-item__label {
    display: block;
    width: 100%;
    text-align: center;
  }
  #showCommodity #pigarea .el-form-item__content {
    display: block;
    width: 50%;
    margin: 0 0 0 25%;
  }
  #showCommodity #pigarea .el-form-item__content textarea{
    text-align: center;
    height: 100px;
  }
  #showCommodity .el-table--mini td {
    padding: 0;
    height: 35px;
    font-size: 12px;
  }
  #showCommodity .el-table--mini th {
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
	    width: 33%;
	    display: inline-block;
	    margin-bottom: 18px;
    }
    #au .el-form-item__label{
    	color: slategray;
   	 	width: 40%;
  	}
  	#au .el-form-item__error{
    	left: 0;
  	}
  	#au .er{
  		margin-left: 4px;
  		margin-bottom: -40px;
  	}
  	
  	#au .el-dialog{
  		width: 65%;
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
    .el-carousel__item h3 {
      color: #475669;
      font-size: 14px;
      opacity: 0.75; 
      line-height: 200px;
      margin: 0;
    }
    
    .el-carousel__item:nth-child(2n) {
      background-color: #99a9bf;
    }
    
    .el-carousel__item:nth-child(2n+1) {
      background-color: #d3dce6;
    }
    .delImg {
      padding: 0;
      margin: 0;
      top: 5px;
      position:absolute;
      right:6px;
      z-index:999;
      font-size: x-large;
      color: rosybrown
    }
    .delImg:hover {
      font-size: xx-large;
      color:red;
    }
</style>