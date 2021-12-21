<template>
<div style="width: 100%;height: 100vh;background-color: lightskyblue; overflow: hidden">
  <div style="width: 400px;margin: 150px auto">
    <div style="font-size: 30px;text-align: center; padding: 30px 0">登录</div>
    <el-form ref="form" :model="form" size="normal" :rules="rules">
      <el-form-item prop="username">
        <el-input v-model="form.username" placeholder="用户名">
          <template #prefix>
            <el-icon class="el-input__icon"><avatar /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" placeholder="密码" show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button style="width: 100%;" type="primary" @click="login">登录</el-button>
      </el-form-item>
      <el-link :underline="false" href="/register">没有账号？注册一个！</el-link>
    </el-form>
  </div>
</div>
</template>

<script>
import {
  Avatar,
  Lock,
} from '@element-plus/icons'
import request from "../utils/request";
import {ElMessage} from "element-plus";

export default {
  name: "login",
  data(){
    return{
      form:{},
      rules: {
        username: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
          },
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur',
          },
        ],
      },
    }
  },
  components: {
    Avatar,
    Lock,
  },
  methods:{
    login(){

      this.$refs['form'].validate((valid) => {
        if (valid) {
          request.post("/api/user/login",this.form).then(res => {
            console.log(res)
            if(res.code == "0"){
              ElMessage({
                type: 'success',
                message: '登录成功',
              })
              sessionStorage.setItem("user", JSON.stringify(res.data))
              this.$router.push("/")
            }else{
              ElMessage({
                type: 'error',
                message: res.msg,
              })
            }
          })
        }
      })
    },
  }
}
</script>

<style scoped>

</style>