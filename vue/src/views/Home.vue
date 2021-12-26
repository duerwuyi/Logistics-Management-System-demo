<template>
  <div style="padding: 10px">
<!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>
<!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="Please input"  style="width: 20%" clearable />
      <el-button type="primary" style="margin-left: 7px" @click="load">查询</el-button>
    </div>


    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" sortable />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="age" label="年龄" />
      <el-table-column prop="sex" label="性别" />
      <el-table-column prop="phonenum" label="电话号码" />
      <el-table-column label="Operations">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">Edit</el-button>
          <el-popconfirm title="Are you sure to delete this?" @confirm="HandleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger">Delete</el-button>
            </template>
          </el-popconfirm>
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

      <el-dialog v-model="dialogVisible" title="Please Enter" width="30%">
        <el-form :model="form" label-width="120px">

          <el-form-item label="用户名">
            <el-input v-model="form.username" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="form.age" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="form.phonenum" style="width: 80%"></el-input>
          </el-form-item>

          <el-form-item label="性别">
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
            <el-radio v-model="form.sex" label="未知">未知</el-radio>
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
    return {
      form:{},
      dialogVisible:false,
      search: '',
      currentPage: 1 ,
      total: 10 ,
      tableData: []
    }
  },
  created(){
    this.load()
  },
  methods :{
    add(){
      this.dialogVisible = true
      this.form={}
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
        })
      }
      this.dialogVisible = false
      this.load()
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
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
      })
      this.load()
    },
    handleCurrentChange(){
      this.load()
    },
  },

}
</script>
