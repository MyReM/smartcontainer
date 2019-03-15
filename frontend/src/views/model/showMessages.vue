<template>
<div id="second-div">
  <!-- <div class="backTim" :style="bacol">{{backTim}}</div> -->
  <div v-if="allDataLength !== 4 && allDataLength !== 0 && allDataLength !== null" class="allH"> 
    <el-row :gutter="20" type="flex" justify="center" >
      <el-col class="allColStyle" :span="14" v-show="drinkShow">
        <component :is="showDrinks" :drink="drink"/>
      </el-col>
      <el-col class="allColStyle" :span="14" v-show="smokeShow">
        <component :is="showSmoke" :smoke="smoke"/>
      </el-col>
      <el-col class="allColStyle" :span="14" v-show="healthProductShow">
        <component :is="showHealthProducts" :healthProduct="healthProduct" />
      </el-col>
    
    </el-row>
  </div>
</div>
</template>

<style>
.imgOutDiv{
  /* border-radius: 50%; */
}
  #second-div .allH{
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
    height: 100vh;
    width: 100vw;
    padding: 2%;
    /* display: flex; */
    /* 必须开启 flex 但因为我在 el-row 已经启动了flex，所以不要再启动 */
    justify-content:center;
    align-items:center;
  }
  #second-div .allColStyle{
    border-radius: 20px;
    border: 1px solid black;
    background-color: #e5e9f2;
  }
  #second-div .leftStyle{
    width: 100%;
    height: 100%;
  }
 #second-div .rightStyle{
    margin-left: 20px;
    margin-top: 50px;
  }
  #second-div .shortStyle{
    line-height: 300%;
   
    /* white-space: nowrap;
		text-overflow: ellipsis;
		overflow: hidden; */
  }
  #second-div .lengthStyle{
    line-height: 200%;
    margin-left: 20px;
    margin-top: 50px;
    height: 40vh;
    overflow-x: hidden;
    overflow-y: scroll;
  }
  #second-div .lengthStyle::-webkit-scrollbar{
    display: none;
  }
  #second-div .tipStyle{
    font-size: 18px;
  }
  #second-div .infoStyle{
    font-size: 	14px;
  }
  #second-div .yella{
    background: yellow;
  }
  #second-div img{
    height: 100%;
    width: 100%;
    padding: 1%;
  }
  #second-div .amountStyle{
    position: relative;
    height: 50px;
    width: 50px;
    margin-left: 95%;
    margin-bottom: -8%;
    font-size: 30px;
    text-align: center;
    line-height: 50px;
    border-radius:50%;
    background: red;
    opacity:0.7;
    z-index: 999; 
  }
  #second-div .backTim {
    position:absolute;
    right:1%;
    top:1%;
    font-size:large;
    width:40px;
    height:40px;
    line-height: 40px;
    text-align: center;
    border-radius: 20px;
    color: white;
    z-index: 999;
  }
</style>

<script>
import showDrinks from '@/views/model/showDrinks.vue'
import showHealthProducts from '@/views/model/showHealthProducts.vue'
import showSmoke from '@/views/model/showSmoke.vue'
import { drink, healthProduct, smoke, getCommodity } from '@/api/commodity'
import { getNonIn } from '@/api/rfidoperate'
import { setInterval, clearInterval, setTimeout, clearTimeout } from 'timers'

export default{
  data() {
    return {
      showDrinks: showDrinks,
      showSmoke: showSmoke,
      showHealthProducts: showHealthProducts,
      healthProductShow: false,
      smokeShow: false,
      drinkShow: false,
      types: [],
      drink,
      healthProduct,
      smoke,
      allDataLength: null,
      backTim: 8,
      bacol: 'background-color:rgba(137, 72, 211, 1)',
      setim: null,
      sint: null,
      searchCount: 0,
      oldDrinkSum: 0,
      oldSmokeSum: 0,
      oldHealSum: 0
    }
  },
  components: {
    showDrinks,
    showHealthProducts,
    showSmoke
  },

  created() {
    this.showData()
    this.getNonIn()
  },
  mounted() {
    this.setim = setTimeout(() => {
      this.sint = setInterval(() => {
        this.getNonIn()
        if (this.searchCount === 0) {
          setTimeout(() => {
            if (this.searchCount === 0) {
              clearInterval(this.sint)
              // this.$router.push('/showMessage')
            }
          }, 100)
        }
      }, 1000)
    }, 500)
  },
  beforyDestroy() {
    clearTimeout(this.setim)
    clearInterval(this.sint)
  },
  // type 1 酒类 2 烟类 3 保健品
  methods: {

    getNonIn() {
      getNonIn().then(res => {
        this.searchCount = res.data[0].length
        res.data[0].forEach(val => {
          if (val === 1 && this.drinkShow === false) {
            this.drinkShow = true
          } else if (val === 2 && this.smokeShow === false) {
            this.smokeShow = true
          } else if (val === 3 && this.healthProductShow === false) {
            this.healthProductShow = true
          }
        })
        res.data[1].forEach((val, index) => {
          switch (index) {
            case 0 : {
              drink.sum = this.oldDrinkSum - val
              break
            }
            case 1 : {
              smoke.sum = this.oldSmokeSum - val
              break
            }
            case 2 : {
              healthProduct.sum = this.oldHealSum - val
              break
            }
          }
        })
      })
    },
    addType(value) {
      if (this.types.indexOf(value) === -1) {
        this.types.push(value)
      }
    },
    deleteType(value) {
      if (this.types.indexOf(value) !== -1) {
        this.types.pop(value)
      }
    },
    showData() {
      this.addType(1)
      this.addType(2)
      this.addType(3)
      // this.addType(2)
      // this.deleteType(2)
      // console.log(this.types)
      getCommodity(this.types).then(res => {
        // console.log(res.data)
        // this.allDataLength = 4
        this.allDataLength = res.data.length
        res.data.forEach((v, i) => {
          if (v.commoditiesType === 1) {
            drink.name = v.object.name
            drink.commoditiesType = v.object.commoditiesType
            drink.commoditiesTypeName = v.commoditiesTypeName
            drink.madeDate = v.object.madeDate.split(' ')[0]
            drink.degree = v.object.degree
            drink.price = v.object.price
            drink.sum = v.object.sum
            this.oldDrinkSum = v.object.sum
            drink.totalNumber = v.totalNumber
            drink.img = v.object.img
            drink.message = v.object.message
          }
          if (v.commoditiesType === 2) {
            smoke.name = v.object.name
            smoke.commoditiesType = v.object.commoditiesType
            smoke.commoditiesTypeName = v.commoditiesTypeName
            smoke.madeDate = v.object.madeDate.split(' ')[0]
            smoke.price = v.object.price
            smoke.sum = v.object.sum
            this.oldSmokeSum = v.object.sum
            smoke.totalNumber = v.totalNumber
            smoke.img = v.object.img
            smoke.message = v.object.message
          }
          if (v.commoditiesType === 3) {
            healthProduct.name = v.object.name
            healthProduct.commoditiesType = v.object.commoditiesType
            healthProduct.commoditiesTypeName = v.commoditiesTypeName
            healthProduct.madeDate = v.object.madeDate.split(' ')[0]
            healthProduct.overDate = v.object.overDate.split(' ')[0]
            healthProduct.price = v.object.price
            healthProduct.sum = v.object.sum
            this.oldHealSum = v.object.sum
            healthProduct.madeData = v.object.madeData
            healthProduct.effect = v.object.effect
            healthProduct.totalNumber = v.totalNumber
            healthProduct.img = v.object.img
            healthProduct.message = v.object.message
          }
        })
      })
    }
  }
}
</script>

