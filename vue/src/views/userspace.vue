<template>

  <div style="padding: 150px;">
    <el-form ref="userform" :model="form" label-width="120px" style="width: 700px" :rules="rules">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" style="width: 80%" ></el-input>
      </el-form-item>
      <el-form-item label="年龄" prop ="age">
        <el-input v-model.number="form.age" style="width: 80%"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio v-model="form.sex" label="男">男</el-radio>
        <el-radio v-model="form.sex" label="女">女</el-radio>
        <el-radio v-model="form.sex" label="未知">未知</el-radio>
      </el-form-item>
      <el-form-item label="联系方式" prop="phonenum">
        <el-input v-model.number="form.phonenum" style="width: 80%"></el-input>
      </el-form-item>
    </el-form>
      <span style="margin: 150px auto">
        <el-button type="primary" @click="save" style="margin: 50px 200px">Confirm</el-button>
        <p v-if="this.user.authority !== 'admin'">p.s修改密码请联系管理员，手机号XXX,邮箱XXX（为了保证安全性没有实现此功能，但是实际上已经集成到管理员的维护范围之内）</p>
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
    const validnum = (rule, value, callback) => {
      if(!Number.isInteger(value)){
        callback(new Error("要求是数字"))
      } else {
        callback()
      }
    }
    const checklen = (rule, value, callback) => {
      if(String(this.form.phonenum).length !== 11) {
        callback(new Error("手机号要求11位"))
      } else {
        callback()
      }
    }
    return {
      user:{},
      form:{
        id:null,
        username:null,
        age:null,
        sex:null,
      },
      rules:{
        age:[
          {
            validator: validnum,
            trigger: 'blur',
          }
        ],
        phonenum: [
          {
            validator: validnum,
            trigger: 'blur',
          },
          {
            validator: checklen,
            trigger: 'blur',
          },
        ]
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
          this.form.username =res.data.username
          this.form.age = Number(res.data.age)
          this.form.sex = res.data.sex
          this.form.phonenum = Number(res.data.phonenum)
        }
      })
    }else {
      this.user = JSON.parse(userinfo)
      this.form.username =this.user.username
      this.form.age = Number(this.user.age)
      this.form.sex = this.user.sex
      this.form.phonenum = Number(this.user.phonenum)
    }
  },
  mounted(){
    document.title='个人信息'
  },
  methods:{
    save(){
      this.$refs['userform'].validate((valid) =>{
        if(valid){
          this.form.id =this.user.id
          request.put("/api/user",this.form).then(res => {
            console.log(res)
            if(res.code == "0"){
              ElMessage({
                type: 'success',
                message: '修改成功,自动为您跳转登录',
              })
              sessionStorage.clear()
              this.$router.push('/login')
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