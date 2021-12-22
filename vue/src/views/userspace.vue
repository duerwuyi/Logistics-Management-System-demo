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
  name: 'Home',
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
    let userStr = sessionStorage.getItem("user")
    console.log(userStr)
    if(userStr){
      this.user = JSON.parse(userStr)
      request.post("/api/user/who",this.user.id).then(res =>{
        console.log(res)
        this.form.username = res.data.username
        this.form.age = res.data.age
        this.form.sex = res.data.sex
      })
    }else{
      this.$router.push("/login")
      ElMessage({
        type: 'error',
        message: '你没有登录！',
      })
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