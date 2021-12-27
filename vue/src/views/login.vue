<template>
<div style="width: 100%;height: 100vh;background-color: lightskyblue; overflow: hidden"  @keyup.enter="login">
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
        <el-button @click="onShow" type="warning" style="width:400px;"> 开始验证 </el-button>
        <Vcode :show="isShow" @success="onSuccess" @close="onClose" />
      </el-form-item>
      <el-form-item>
        <el-button style="width: 100%;" type="primary" @click="login" :disabled="disabled">登录</el-button>
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
import { ref } from "vue";
import Vcode from "vue3-puzzle-vcode";
import {mapMutations} from "vuex";


export default {
  name: "login",
  data(){
    return{
      Vcode,
      form:{},
      userStr:"",
      userToken:'',
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
    Vcode,
  },
  created() {
    this.userStr = localStorage.getItem('Authorization')
    if(this.userStr){
      request.post("/api/user/relog", this.userStr).then(res =>{
        console.log(res)
        if(res.code ==='0'){
          this.form.username =res.data.username
          this.form.password =res.data.password
          this.disabled = false
          this.isPass = true
          clearTimeout(this.timer);  //清除延迟执行
          ElMessage({
            type: 'success',
            message: '10秒后自动登录',
          })
          this.timer = setInterval(()=>{   //设置延迟执行
            this.login()
          },10000);

        }
      })
    }
  },
  mounted() {
    document.title='物流速运'
  },
  methods:{
    ...mapMutations(['changeLogin']),
    login(){
      this.$refs['form'].validate((valid) => {
        if (valid && this.isPass) {
          request.post("/api/user/login",this.form).then(res => {
            console.log(res)
            if(res.code == "0"){
              this.userToken = res.data.token
              // 将用户token保存到vuex中
              this.changeLogin({ Authorization:this.userToken })
              ElMessage({
                type: 'success',
                message: '登录成功',
              })
              sessionStorage.setItem("user", JSON.stringify(res.data.user))
              this.$router.push("/")
            }else{
              ElMessage({
                type: 'error',
                message: res.msg,
              })
              this.isPass = false
              this.disabled = true
            }
          })
        }else{
          ElMessage({
            type: 'error',
            message: '请输入验证码！',
          })
        }
      })
    },
  },
  setup() {
    const isShow = ref(false);
    const disabled = ref(true);
    const isPass = ref(false);
    const onShow = () => {
      isShow.value = true;
    };

    const onClose = () => {
      isShow.value = false;
    };

    const onSuccess = () => {
      onClose(); // 验证成功，需要手动关闭模态框
      disabled.value = false;
      isPass.value = true;
    };

    return {
      isShow,
      disabled,
      isPass,
      onShow,
      onClose,
      onSuccess
    };
  }
}
</script>

<style scoped>

</style>