<template>
  <div style="width: 100%;height: 100vh;background-color: lightskyblue; overflow: hidden" @keyup.enter="register">
    <div style="width: 400px;margin: 150px auto">
      <div style="font-size: 30px;text-align: center; padding: 30px 0">注册</div>
      <el-form ref="form" :model="form" size="normal" :rules="rules">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名">
            <template #prefix>
              <el-icon class="el-input__icon"><avatar /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="phonenum">
          <el-input v-model.number="form.phonenum" placeholder="手机号码">
            <template #prefix>
              <el-icon class="el-input__icon"><bell /></el-icon>
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
        <el-form-item prop="confirm">
          <el-input v-model="form.confirm" placeholder="再次输入密码" show-password>
            <template #prefix>
              <el-icon class="el-input__icon"><warning /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="onShow" type="warning" style="width:400px;"> 开始验证 </el-button>
          <Vcode :show="isShow" @success="onSuccess" @close="onClose" />
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%;" type="primary" @click="register" :disabled="disabled">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {
  Avatar,
  Lock,
  Warning,
  Bell,
} from '@element-plus/icons'
import request from "../utils/request";
import {ElMessage} from "element-plus";
import {ref} from "vue";
import Vcode from "vue3-puzzle-vcode";

export default {
  name: "Register",
  data(){
    const validconf = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error("密码不同！"))
      } else {
        callback()
      }
    }
    const validnum = (rule, value, callback) => {
      if(!Number.isInteger(value)){
        callback(new Error("手机号要求是数字"))
      } else if(String(value).length !== 11) {
        callback(new Error("手机号要求11位"))
      } else {
        callback()
      }
    }
    return{
      Vcode,
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
          {
            min:5,
            message: '密码不得少于5个字符',
            trigger: 'blur',
          },
        ],
        phonenum:[
          {
            required: true,
            message: '请输入手机号',
            trigger: 'blur',
          },
          {
            validator: validnum,
            trigger: 'blur',
          }
        ],
        confirm: [
          {
            required: true,
            message: '请确认密码',
            trigger: 'blur',
          },
          {
            validator: validconf,
            trigger: 'blur',
          },
        ],
      },
    }
  },
  components: {
    Avatar,
    Lock,
    Warning,
    Bell,
    Vcode,
  },
  mounted() {
    document.title="注册"
  },
  methods:{
    register(){
      this.$refs['form'].validate((valid) => {
        if (valid && this.isPass) {
          request.post("/api/user/register",this.form).then(res => {
            console.log(res)
            if(res.code == "0"){
              ElMessage({
                type: 'success',
                message: '注册成功，请登录',
              })
              this.$router.push("/login")
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