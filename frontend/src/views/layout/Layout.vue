<template>
  <div class="app-wrapper" :class="classObj">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside"></div>
    <sidebar class="sidebar-container"></sidebar>
    <div class="main-container">
      <navbar></navbar>
      <tags-view></tags-view>
      <app-main></app-main>
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain, TagsView } from './components'
import ResizeMixin from './mixin/ResizeHandler'

export default {
  name: 'layout',
  components: {
    Navbar,
    Sidebar,
    AppMain,
    TagsView
  },
  mixins: [ResizeMixin],
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    device() {
      return this.$store.state.app.device
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  // data() {
  //   return {
  //     websocket: null
  //   }
  // },
  // created() {
  //   if (this.websocket === null) {
  //     // ws地址
  //     const userCode = this.$store.getters.userCode
  //     const wsuri = process.env.WS_API + userCode
  //     this.websocket = new WebSocket(wsuri)
  //     this.websocket.onopen = () => {
  //       console.log('websocket success!')
  //     }
  //     this.websocket.onmessage = (res) => {
  //       this.$notify({
  //         title: '消息提醒',
  //         message: res.data,
  //         type: 'success',
  //         duration: 0
  //       })
  //     }
  //     this.websocket.onclose = (e) => {
  //       console.log('connection closed (' + e.code + ')')
  //     }
  //   }
  // },
  // destroyed() {
  //   // 页面销毁时关闭长连接
  //   if (this.websocket !== null) {
  //     this.websocket.close()
  //   }
  // },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('CloseSideBar', { withoutAnimation: false })
    },
    websocketsend(message) { // 数据发送
      this.websocket.send(message)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/mixin.scss";
  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;
  }
  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }
  .main-container {
    // min-width: 1024px;
  }
</style>
