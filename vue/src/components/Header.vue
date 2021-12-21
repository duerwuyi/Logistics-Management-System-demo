<template>
    <div style="height: 50px; line-height: 50px;border-bottom: 1px solid #ccc; display: flex">
      <div style ="width: 200px; padding-left: 30px;font-weight: bolder; color: dodgerblue">物流管理</div>
      <div style ="flex: 1"></div>
      <div style="width: 100px">
        <el-dropdown>
         <span class="el-dropdown-link">
         {{names}}
           <el-icon class="el-icon--right">
             <arrow-down />
           </el-icon>
         </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>个人信息</el-dropdown-item>
              <el-dropdown-item @click="$router.push('/login')">退出系统</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
</template>

<script>
import { ArrowDown } from "@element-plus/icons";

export default {
  name: "Header",
  props: ['user'],
  components: {
    ArrowDown ,
  },
  data(){
    return{
      user:{},
      names:"",
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user")
    if(userStr){
      this.user = JSON.parse(userStr)
      if(this.user.nickName != null){
        this.names = this.user.nickName
      }
      else{
        this.names = this.user.username
      }
    }else{
     this.user = "无该用户"
    }
  }
}
</script>

<style scoped>

</style>