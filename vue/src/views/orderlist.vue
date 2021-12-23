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
      <el-table-column prop="id" label="货物ID" sortable />
      <el-table-column prop="ordername" label="货物名称" />
      <el-table-column prop="senderid" label="发件方ID" />
      <el-table-column prop="sendadd" label="发件方地址" />
      <el-table-column prop="receiverid" label="收件方ID" />
      <el-table-column prop="receadd" label="收件方地址" />
      <el-table-column prop="employeeid" label="派送员ID" />
      <el-table-column prop="carid" label="车辆ID" />
      <el-table-column prop="weight" label="货物重量" />
      <el-table-column prop="cost" label="物流费用" />
      <el-table-column prop="status" label="货物状态" />

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
          <el-form-item label="货物名称">
            <el-input v-model="form.ordername" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="发件方ID">
            <el-input v-model="form.senderid" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item type="textarea" label="发件方地址">
            <el-input v-model="form.sendadd" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="收件方ID">
            <el-input v-model="form.receiverid" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item type="textarea" label="收件方地址">
            <el-input v-model="form.receadd" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="派送员ID">
            <el-input v-model="form.employeeid" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="车辆ID">
            <el-input v-model="form.carid" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="货物重量">
            <el-input v-model="form.weight" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="物流费用">
            <el-input v-model="form.cost" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="货物状态">
            <el-input v-model="form.status" style="width: 80%"></el-input>
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
  name: 'orderlist',
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
      request.get("/api/order",{
        params:{
          pageNum: this.currentPage,
          pageSize:10,
          search : this.search,
        },
      }).then(res => {
        this.tableData=res.data.records
        this.total = res.data.total
        console.log(res)
      })
    },
    save(){
      if(this.form.id){
        request.put("/api/order",this.form).then(res => {
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
        request.post("/api/order",this.form).then(res => {
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
      request.delete("/api/order/" + id).then(res => {
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
