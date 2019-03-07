<template>
  <el-row :type="isflex">
    <el-col :span="isflex === 'flex' ? 16 : 24">
      <el-col :xs="24" :sm="24" :md="12" :lg="12"> 
        <div class="amountStyle">{{drink.sum}}</div>
        <el-carousel :interval="2000" arrow="always" class="imgOutDiv">
          <el-carousel-item v-for="(v,i) in drink.img" :key="i">
            <img :src="'static/imgs/' + v.uploadFileName + v.sufName"/>
          </el-carousel-item>
        </el-carousel>
      </el-col>
      <!-- <el-col  :xs="23" :sm="23" :md="11" :lg="12" class="rightStyle"> -->
      <el-col :xs="23" :sm="23" :md="10" :lg="10" class="rightStyle">
        <div class="shortStyle">
          <label class="tipStyle">名称:</label>
          <label class="infoStyle">{{drink.name}}</label>
        </div>
        <div  class="shortStyle">
          <label class="tipStyle">商品类型：</label>
          <label class="infoStyle">{{drink.commoditiesTypeName}}</label>
        </div>
        <div  class="shortStyle">
          <label class="tipStyle">生产日期：</label>
          <label class="infoStyle">{{drink.madeDate}}</label>
        </div>
        <div  class="shortStyle">
          <label class="tipStyle">度数：</label>
          <label class="infoStyle">{{drink.degree}}</label>
        </div>
        <div  class="shortStyle">
          <label class="tipStyle">价格：</label>
          <label class="infoStyle">{{drink.price}}</label>
        </div>
        <div  class="shortStyle">
          <label class="tipStyle">总数量：</label>
          <label class="infoStyle">{{drink.totalNumber}}</label>
        </div>
      </el-col>
    </el-col>
    <el-col :span="isflex === 'flex' ? 8 : 24">
    <!-- <el-col  :sm="23" :md="11" :lg="23" :span="23"> -->
      <el-col :span="23">
        <div class="lengthStyle">
          <label class="tipStyle">描述:</label><br/>
          <label class="infoStyle">&nbsp;&nbsp;{{drink.message}}</label>
        </div>
      </el-col>
    </el-col>
  </el-row>
</template>
<style>
</style>

<script>
export default {
  data() {
    return {
    }
  },
  props: ['isflex', 'drink'],
  create() {

  },
  method: {

  }
}
</script>

