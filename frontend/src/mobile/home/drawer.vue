<template>
  <div>

    <drawer width="200px;" show.sync="false">

      <div slot="drawer">
        <!-- 菜单内容 -->
      </div>

      <!-- rourer-view 作为默认插槽内容 -->
      <div>
        <router-view></router-view>
      </div>

    </drawer>
  </div>
</template>

<script>
import { Drawer } from 'vux'

export default {
  name: 'MobileDrawer',
  components: {
    Drawer
  }
}
</script>
