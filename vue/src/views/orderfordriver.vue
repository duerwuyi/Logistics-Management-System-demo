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
      <el-table-column type="expand">
        <template #default="props">
          <el-card class="box-card" style="width:800px; justify-content: center; margin:0 200px">
            <el-row>
              <el-col :span="5">
                发件方ID: {{ props.row.senderid }}
              </el-col>
              <el-col :span="8">
                发件人用户名：{{ props.row.sendername }}
              </el-col>
              <el-col :span="11">
                发件人电话：{{props.row.senderphone}}
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="5">
                收件方ID: {{ props.row.receiverid }}
              </el-col>
              <el-col :span="8">
                收件人用户名：{{ props.row.receivername }}
              </el-col>
              <el-col :span="11">
                收件人人电话：{{props.row.receiverphone}}
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="5">
                派送员ID：{{ props.row.employeeid }}
              </el-col>
              <el-col :span="8">
                派送员用户名：{{ props.row.employeename }}
              </el-col>
              <el-col :span="11">
                派送员电话：{{props.row.employeephone}}
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="5">
                车辆ID：{{ props.row.carid }}
              </el-col>
              <el-col :span="8">
                货物重量：{{ props.row.weight }}
              </el-col>
              <el-col :span="11">
                物流费用：{{ props.row.cost }}
              </el-col>
            </el-row>
          </el-card>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="订单ID" sortable />
      <el-table-column prop="ordername" label="货物名称" />
      <!--      <el-table-column prop="senderid" label="发件方ID" />-->
      <el-table-column prop="sendadd" label="发件方地址" />
      <!--      <el-table-column prop="receiverid" label="收件方ID" />-->
      <el-table-column prop="receadd" label="收件方地址" />
      <!--      <el-table-column prop="employeeid" label="派送员ID" />-->
      <!--      <el-table-column prop="carid" label="车辆ID" />-->
      <!--      <el-table-column prop="weight" label="货物重量" />-->
      <!--      <el-table-column prop="cost" label="物流费用" />-->
      <el-table-column prop="status" label="货物状态" />
      <el-table-column prop="checkfinish" label="是否收件"  width="120px" sortable />

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
          <el-form-item label="货物状态">
            <el-radio v-model="form.status" label="preparing">preparing</el-radio>
            <el-radio v-model="form.status" label="sending">sending</el-radio>
            <el-radio v-model="form.status" label="waiting">waiting</el-radio>
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
import request from "@/utils/request";
import {ElMessage} from "element-plus";

export default {
  name: "orderfordriver",
  components: {

  },
  data() {
    return {
      form:{},
      dialogVisible:false,
      search: '',
      currentPage: 1 ,
      total: 10 ,
      tableData: [],
      Flag: 'driver',
      user:{},
    }
  },
  created(){
    let userStr = sessionStorage.getItem("user")
    if(userStr){
      this.user = JSON.parse(userStr)
    }
    else{
      this.user = "无该用户"
    }
    this.load()
  },
  mounted(){
    document.title="订单管理"
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
          search: this.search,
          Flag: this.Flag,
          userID: this.user.id,
        },
      }).then(res => {
        res.data.records.forEach(function (item) {
          request.post("/api/user/who", item.employeeid).then(res => {
            if (res.code === "0") {
              item.employeename = res.data.username
            }
          })
          request.post("/api/user/who", item.senderid).then(res => {
            if (res.code === "0") {
              item.sendername = res.data.username
            }
          })
          request.post("/api/user/who", item.receiverid).then(res => {
            if (res.code === "0") {
              item.receivername = res.data.username
            }
          })
          request.post("/api/user/who", item.employeeid).then(res => {
            if (res.code === "0") {
              item.employeephone = res.data.phonenum
            }
          })
          request.post("/api/user/who", item.senderid).then(res => {
            if (res.code === "0") {
              item.senderphone = res.data.phonenum
            }
          })
          request.post("/api/user/who", item.receiverid).then(res => {
            if (res.code === "0") {
              item.receiverphone = res.data.phonenum
            }
          })
        })
        this.tableData = res.data.records
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
          this.load()
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
          this.load()
        })
      }
      this.dialogVisible = false
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
        this.load()
      })
    },
    handleCurrentChange(){
      this.load()
    },
  },
}
</script>

<style scoped>

</style>