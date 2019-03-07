<template>
  <div style="height: 100%;">

    <view-box ref="viewBox">

      <x-header slot="header" style="width:100%;position:absolute;left:0;top:0;z-index:100;" title="主页" :right-options="{showMore: true}">
        <x-icon slot="overwrite-left" type="navicon" size="35" style="fill:#fff;position:relative;top:-8px;left:-3px;"></x-icon>
      </x-header>

      <div style="margin-top: 60px;">
        <MobileDrawer></MobileDrawer>
      </div>

      <div style="margin-bottom: 40px;">
        <GridTable></GridTable>
      </div>

      <tabbar slot="bottom">
        <tabbar-item>
            <img slot="icon" src="../../assets/mobile/货品列表.png">
            <!-- <x-icon slot="icon" type="speedometer" size="30"></x-icon> -->
            <span slot="label">Wechat</span>
        </tabbar-item>
        <tabbar-item show-dot>
            <img slot="icon" src="../../assets/mobile/库存统计.png">
            <span slot="label">Message</span>
        </tabbar-item>
        <tabbar-item selected link="/component/demo">
            <img slot="icon" src="../../assets/mobile/数据报表.png">
            <span slot="label">Explore</span>
        </tabbar-item>
        <tabbar-item badge="2">
            <img slot="icon" src="../../assets/mobile/门店设置.png">
            <span slot="label">News</span>
        </tabbar-item>
      </tabbar>
      
    </view-box>
  </div>
</template>

<script>
import { ViewBox, Tabbar, TabbarItem, XHeader, Group, Cell } from 'vux'
import MobileDrawer from './drawer'
import GridTable from './grid'

export default {
  components: {
    ViewBox,
    Tabbar,
    TabbarItem,
    XHeader,
    Group,
    Cell,
    MobileDrawer,
    GridTable
  }
}
</script>

<style>
html, body {
  height: 100%;
  width: 100%;
  overflow-x: hidden;
}
</style>
