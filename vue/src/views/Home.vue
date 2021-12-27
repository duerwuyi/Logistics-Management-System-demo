<template>
  <div style="padding: 10px">
<!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
    </div>
<!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="Please input"  style="width: 20%" clearable />
      <el-button type="primary" style="margin-left: 7px" @click="load">查询</el-button>
    </div>


    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" sortable width="100"/>
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="age" label="年龄" />
      <el-table-column prop="sex" label="性别" />
      <el-table-column prop="phonenum" label="电话号码" />
      <el-table-column prop="authority" label="权限等级" width="200" />
      <el-table-column label="Operations">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">Edit</el-button>
          <el-popconfirm title="Are you sure to delete this?" @confirm="HandleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger">Delete</el-button>
            </template>
          </el-popconfirm>
          <el-button size="mini" @click="findHisOrder(scope.row.id)" v-if="scope.row.authority === 'user'" >查看关联订单</el-button>
          <el-button size="mini" @click="findEmployeeOrder(scope.row.id)" v-if="scope.row.authority === 'employee'" >查看派送订单</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div>
      <el-pagination
          v-model:currentPage="currentPage"
          :page-size="10"
          layout="prev, pager, next, jumper"
          :total="total"
          @current-change="handleCurrentChange"
          background >
      </el-pagination>

      <el-dialog v-model="dialogTableVisible" title="有关Ta的订单（最近10条）">
        <el-table :data="gridData">
          <el-table-column prop="id" label="ID" sortable />
          <el-table-column prop="ordername" label="货物名称" />
          <el-table-column prop="status" label="货物状态" />
          <el-table-column prop="checkfinish" label="是否收件" />
        </el-table>
      </el-dialog>

      <el-dialog v-model="dialogVisible" title="添加司机或者管理员的唯一途径" width="30%">
        <el-form :model="form" label-width="120px" :rules="rules" ref="ruleForm">

          <el-form-item prop="username" label="用户名">
            <el-input v-model="form.username" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="form.age" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item prop="phonenum" label="联系电话">
            <el-input v-model.number="form.phonenum" style="width: 80%"></el-input>
          </el-form-item>

          <el-form-item prop="sex" label="性别">
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
            <el-radio v-model="form.sex" label="未知">未知</el-radio>
          </el-form-item>
          <el-form-item prop="authority" label="权限">
            <el-radio v-model="form.authority" label="admin">管理员</el-radio>
            <el-radio v-model="form.authority" label="employee">司机</el-radio>
            <el-radio v-model="form.authority" label="user">用户</el-radio>
          </el-form-item>
          <el-form-item prop="password" label="密码" style="width: 80%" >
            <el-input v-model="form.password" placeholder="密码" show-password>
            </el-input>
          </el-form-item>
          <el-form-item prop="confirm" label="确认密码" style="width: 80%" >
            <el-input v-model="form.confirm" placeholder="再次输入密码" show-password>
            </el-input>
          </el-form-item>

        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="save">Confirm</el-button>
          </span>
        </template>
      </el-dialog>

    </div>
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
    const validconf = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error("密码不同！"))
      } else {
        callback()
      }
    }
    const validnum = (rule, value, callback) => {
      if(String(value).length !== 11) {
        callback(new Error("手机号要求11位"))
      } else {
        callback()
      }
    }
    return {
      form:{},
      dialogTableVisible:false,
      held : 0 ,
      dialogVisible:false,
      search: '',
      currentPage: 1 ,
      total: 10 ,
      tableData: [],
      gridData:[],
      rules: {
        username: [
          {
            required: true,
            message: '请输入用户名',
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
      }
    }
  },
  created(){
    this.load()
  },
  mounted() {
    document.title='物流管理中心'
  },
  methods :{
    add(){
      this.dialogVisible = true
      this.held = 1
      this.form={}
      this.$refs['ruleForm'].resetFields()
    },
    findHisOrder(id){
      request.get("/api/order",{
        params:{
          pageNum: 1,
          pageSize:10,
          search : "",
          Flag: 'userx',
          userID: id,
        },
      }).then(res=>{
        if(res.code === '0'){
          this.dialogTableVisible=true
          this.gridData = res.data.records
        }
      })
    },
    findEmployeeOrder(id){
      request.get("/api/order",{
        params:{
          pageNum: 1,
          pageSize:10,
          search : "",
          Flag: 'driver',
          userID: id,
        },
      }).then(res=>{
        if(res.code === '0'){
          this.dialogTableVisible=true
          this.gridData = res.data.records
        }
      })
    },
    load(){
      request.get("/api/user",{
        params:{
          pageNum: this.currentPage,
          pageSize:10,
          search : this.search,
        },
      }).then(res => {
        console.log(res)
        this.tableData=res.data.records
        this.total = res.data.total
      })
    },
    save(){
      this.$refs['ruleForm'].validate((valid) =>{
        if(valid){
          if(this.form.id){
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
              this.load()
            })
          }
          else{
            request.post("/api/user",this.form).then(res => {
              console.log(res)
              if(res.code == "0"){
                ElMessage({
                  type: 'success',
                  message: '添加成功',
                })
              }else{
                ElMessage({
                  type: 'error',
                  message: res.msg,
                })
              }
              this.load()
            })
          }
          this.dialogVisible = false
        }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.held = 2
      this.dialogVisible = true
      this.$refs['ruleForm'].resetFields()
    },
    HandleDelete(id) {
      console.log(id)
      request.delete("/api/user/" + id).then(res => {
        console.log(res)
        if(res.code == "0"){
          ElMessage({
            type: 'success',
            message: '删除成功',
          })
        }else{
          ElMessage({
            type: 'error',
            message: res.msg,
          })
        }
        this.load()
      })
    },
    handleCurrentChange(){
      this.load()
    },
  },

}
</script>
