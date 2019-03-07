<template>
  <div id="first"> 
    <el-row style="width:100%;margin:5% 0 0 0;" align="center">
      <el-col :key="index" :span="6" v-for="(val,index) in message" :class="width">
        <transition  name="el-zoom-in-center">
        <div v-show="is">
          <img :src=message[index].img  class="imgStyle"/>
          <span>
            <label>商品名称：</label>
            <label>{{val.name}}</label>
          </span>
          <span>
            <label>商品数量：</label>
            <label>{{val.amount > 0 ? val.amount : '0'}}</label>
          </span>  
        </div> 
        </transition>
      </el-col>
    </el-row>
  </div>
</template>
<style>
  .oneGoodsWidth {
    width:36% !important;
    margin:0 0 0 34% !important;
  }
  .twoGoodsWidth {
   width:40% !important;
   margin: 0 0 0 110px !important;
  }
  .threeGoodsWidth {
    width:33.3% !important;
  }
  .fourGoodsWidth {
    width: 25% !important;
  }
  #first {
    width: 99%;
  }
  #first div {
    width: 100%;
    padding: 40px 5px 0 5px;
    text-align: center;
    float: left;
  }
  #first div span {
    font-family: Monospace;
    float: left;
    width: 100%;
    text-align: left;
    padding: 5% 0 0 15%;
    font-size: 2em;
    color: rgba(152, 21, 25, 1);
  }
  .imgStyle {
    width: 100%;
    height: 460px;
  }
</style>

<script>
import { getAllCommodity } from '@/api/commodity'
import { setInterval, clearInterval } from 'timers'
import { getNonIn, getRFID, openRFID } from '@/api/rfidoperate'
export default {
  data() {
    return {
      message: null,
      width: null,
      is: false,
      stim: null,
      sint: null
    }
  },
  created() {
    this.getAllCommodity()
    openRFID().then(res => {})
  },
  mounted() {
    this.is = !this.is
    this.sint = setInterval(() => {
      this.getNonIn()
    }, 800)
  },
  beforeDestroy() {
    clearInterval(this.sint)
  },
  methods: {
    getRF() {
      getRFID().then(res => {
        openRFID().then(res => {})
      })
    },
    getNonIn() {
      getNonIn().then(res => {
        if (res.data[0].length > 0) {
          clearInterval(this.sint)
          this.$router.push('/showMessages')
        }
      })
    },
    getAllCommodity() {
      const param = {
        isShow: 1
      }
      getAllCommodity(param).then(res => {
        if (res.code === 200) {
          this.message = res.data
          res.data.forEach((val, index) => {
            switch (index) {
              case 0 : {
                this.width = 'oneGoodsWidth'
                break
              }
              case 1 : {
                this.width = 'twoGoodsWidth'
                break
              }
              case 2 : {
                this.width = 'threeGoodsWidth'
                break
              }
              case 3 : {
                this.width = 'fourGoodsWidth'
                break
              }
            }
            this.message[index] = {
              name: val.commoditiesName,
              amount: val.sum,
              img: 'static/imgs/' + val.goodsImg.uploadFileName + val.goodsImg.sufName
            }
          })
        }
      })
    }
  }
}
</script>