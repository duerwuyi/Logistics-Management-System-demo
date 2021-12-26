<template>
  <el-menu
      style="width: 200px; min-height: calc(100vh - 50px) "
      :default-active="$route.path"
      router
      class="el-menu-vertical-demo"
  >
    <el-menu-item index="/orderforuser">
      <el-icon><icon-menu /></el-icon>
      <span>订单管理</span>
    </el-menu-item>
    <el-menu-item index="/userspace">
      <el-icon><setting /></el-icon>
      <template #title>账户设置</template>
    </el-menu-item>
  </el-menu>
</template>
<script scope>

import {
  Location,
  Document,
  Menu as IconMenu,
  Setting,
  Van,
} from '@element-plus/icons'

export default {
  name:"AsideUser",
  components: {
    Location,
    Document,
    Setting,
    IconMenu,
    Van,
  },

}
</script>