<template>
  <div style="padding: 150px;">
    <el-form :model="form" label-width="120px" style="width: 700px">
      <el-form-item label="用户名">
        <el-input v-model="form.username" style="width: 80%" ></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nickName" style="width: 80%"></el-input>
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
    <template>
      <span class="dialog-footer">
        <el-button @click="$router.push('/home')">Cancel</el-button>
        <el-button type="primary" @click="">Confirm</el-button>
      </span>
    </template>
  </div>

</template>

<script>
export default {
  name: "userspace",
  data() {
    return {
      form:{},
    }
  },
}
</script>

<style scoped>

</style>