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
              <el-dropdown-item @click="$router.push('/userspace')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="exit">退出系统</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
</template>

<script>
import { ArrowDown } from "@element-plus/icons";
import request from "../utils/request";

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
      userStr:"",
    }
  },
  created() {
    //这一段代码比较重要，是通过token去后端请求其user信息
    this.userStr = localStorage.getItem('Authorization')
    let userinfo = sessionStorage.getItem("user")
    if(!userinfo){
      //console.log(JSON.stringify(this.userStr))
      request.post("/api/user/self", this.userStr).then(res =>{
        if(res.code === '0'){
          this.user = res.data
          this.names = this.user.username
        }
      })
    }else{
      this.user = JSON.parse(userinfo)
      this.names = this.user.username
    }
  },
  methods:{
    exit(){
      localStorage.removeItem('Authorization')
      sessionStorage.clear()
      this.$router.push("/login")
    }
  }

}
</script>

<style scoped>

</style>