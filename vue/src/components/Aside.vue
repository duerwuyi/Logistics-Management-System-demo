<template>
  <el-menu
      :default-active="path"
      router
      class="el-menu-vertical-demo"
  >
    <el-menu-item index="/home">
      <el-icon><location /></el-icon>
      <span>（管理员）用户管理</span>
    </el-menu-item>
    <el-sub-menu>
      <template #title>
        <el-icon><icon-menu /></el-icon>
        <span>应用</span>
      </template>

      <el-menu-item index="/orderlist">
        <el-icon></el-icon>
        <span>物流管理</span>
      </el-menu-item>

      <el-menu-item index="/Car">
        <el-icon></el-icon>
        <span>车辆管理</span>
      </el-menu-item>

      <el-menu-item-group title="Group Two">
        <el-menu-item>item three</el-menu-item>
      </el-menu-item-group>
    </el-sub-menu>

    <el-menu-item>
      <el-icon><setting /></el-icon>
      <template #title>设置</template>
    </el-menu-item>
  </el-menu>
</template>
<script scope>
import { defineComponent, ref } from 'vue'
import {
  Location,
  Document,
  Menu as IconMenu,
  Setting,
} from '@element-plus/icons-vue'

export default defineComponent({
  name:"Aside",
  data(){
    return{
      path:this.$route.path
    }
  },
  components: {
    Location,
    Document,
    Setting,
    IconMenu,
  },
})
</script>