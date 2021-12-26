<template>

  <div style="padding: 150px;">
    <el-form :model="form" label-width="120px" style="width: 700px">
      <el-form-item label="用户名">
        <el-input v-model="form.username" style="width: 80%" ></el-input>
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model="form.age" style="width: 80%"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio v-model="form.sex" label="男">男</el-radio>
        <el-radio v-model="form.sex" label="女">女</el-radio>
        <el-radio v-model="form.sex" label="未知">未知</el-radio>
      </el-form-item>
    </el-form>
      <span style="margin: 150px auto">
        <el-button @click="$router.push('/home')" style="margin: 50px 200px">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </span>
  </div>
</template>
<script>

import request from "../utils/request";
import {ElMessage} from "element-plus";

export default {
  name: 'userspace',
  components: {
  },
  data() {
    return {
      user:{},
      form:{
        id:null,
        username:null,
        age:null,
        sex:null,
      },
    }
  },
  created() {
    //这一段代码比较重要，是通过token去后端请求其user信息
    this.userStr = localStorage.getItem('Authorization')
    let userinfo = sessionStorage.getItem("user")
    if(!userinfo){
      //console.log(JSON.stringify(this.userStr))
      request.post("/api/user/self", this.userStr).then(res =>{
        console.log(res)
        if(res.code === '0'){
          this.form.username = res.data.username
          this.form.age = res.data.age
          this.form.sex = res.data.sex
        }
      })
    }else {
      this.user = JSON.parse(userinfo)
      this.form.username = this.user.username
      this.form.age = this.user.age
      this.form.sex = this.user.sex
    }
  },
  methods:{
    save(){
      this.form.id =this.user.id
      request.put("/api/user",this.form).then(res => {
        console.log(res)
        if(res.code == "0"){
          ElMessage({
            type: 'success',
            message: '修改成功',
          })
        }else{
          ElMessage({
            type: 'error',
            message: res.msg,
          })
        }
      })
    },
  }
}
</script>
<style scoped>

</style>