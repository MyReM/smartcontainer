<template>
  <div>

    <group>
    <!-- <group-title>4 columns</group-title> -->
    <grid :show-lr-borders="false">
      <grid-item>
        <img slot="icon" src="">
        <div style="text-align: center">
          <span slot="label">下单数</span>
          <badge text="8"></badge>
        </div>
      </grid-item>

      <grid-item label="打印款数">
        <img slot="icon" src="">
      </grid-item>

    </grid>

    <grid :show-lr-borders="false">

      <grid-item label="打印总数">
        <img slot="icon" src="">
      </grid-item>

      <grid-item label="补打总数">
        <img slot="icon" src="../../assets/mobile/绑定平台.png">
      </grid-item>
    </grid>

    <divider>今日数据统计</divider>
    <v-chart ref="labelData" :data="labelData">
      <v-bar />
      <v-tooltip :show-item-marker="false" />
    </v-chart>
    
    </group>
  </div>
</template>

<script>
import { Grid, GridItem, Divider, Badge, Group, GroupTitle } from 'vux'
import { VChart, VLine, VArea, VTooltip, VLegend, VBar } from 'vux'

export default {
  name: 'GridTable',
  components: {
    Grid,
    GridItem,
    Divider,
    Badge,
    Group,
    GroupTitle,
    VChart,
    VLine,
    VArea,
    VTooltip,
    VLegend,
    VBar
  },
  data() {
    return {
      labelData: [
        { year: '下单数', sales: 38 },
        { year: '打印款数', sales: 52 },
        { year: '打印总数', sales: 61 },
        { year: '补打总数', sales: 145 }
      ]
    }
  },
  methods: {
    onItemClick() {
      console.log('on item click')
    }
  }
}
</script>

<style scoped>
.grid-center {
  display: block;
  text-align: center;
  color: #666;
}
.weui-grids {
  background-color: #fff;
}
</style>