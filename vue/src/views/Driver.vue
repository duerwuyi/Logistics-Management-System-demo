<template>
  <div style="padding: 10px">
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="Please input"  style="width: 20%" clearable />
      <el-button type="primary" style="margin-left: 7px" @click="load">查询</el-button>
    </div>

    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="drivername" label="司机名" />
      <el-table-column prop="phonenum" label="联系电话" />
      <el-table-column prop="status" label="状态" />
      <el-table-column label="Operations">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">Edit</el-button>
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

          <el-form-item label="司机状态：">
            <el-radio v-model="form.status" label="空闲">空闲</el-radio>
            <el-radio v-model="form.status" label="任务中">任务中</el-radio>
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
  name: 'Driver',
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
      keyNum:0,
    }
  },
  created(){
    this.load()
    // clearTimeout(this.timer);  //清除延迟执行
    //
    // this.timer = setInterval(()=>{   //设置延迟执行
    //   this.keyNum++;
    // },1000);
  },
  mounted() {
    document.title='司机管理'
  },
  methods :{
    load(){
      request.get("/api/driver",{
        params:{
          pageNum: this.currentPage,
          pageSize:10,
          search : this.search,
        },
      }).then(async res => {
        for (const item of res.data.records) {
          await request.post("/api/user/who", item.driverid).then(res => {
            if (res.code == "0") {
              item.drivername = res.data.username
            }
          })
        }
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save(){
        request.put("/api/driver",this.form).then(res => {
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
      this.dialogVisible = false
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    HandleDelete(id) {
      console.log(id)
      request.delete("/api/driver/" + id).then(res => {
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