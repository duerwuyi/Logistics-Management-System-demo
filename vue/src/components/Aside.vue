<template>
  <el-menu
      style="width: 200px; min-height: calc(100vh - 50px) "
      :default-active="path"
      router
      class="el-menu-vertical-demo">
    <el-menu-item index="/home">
        <el-icon><location /></el-icon>
        <span>（用户）表单管理</span>
    </el-menu-item>
    <el-menu-item index="">
      <el-icon><icon-menu /></el-icon>
      <span>应用</span>
    </el-menu-item>
    <el-menu-item index="">
      <el-icon><setting /></el-icon>
      <span>设置</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
import {
  Location,
  Document,
  Menu as IconMenu,
  Setting,
} from '@element-plus/icons'

export default ({
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
