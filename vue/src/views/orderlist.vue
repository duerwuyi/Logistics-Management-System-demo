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
      <el-table-column type="expand">
        <template #default="props">
          <el-card class="box-card" style="width:600px; justify-content: center">
            <p>发件方ID: {{ props.row.senderid }},发件人用户名：{{ props.row.sendername }},发件人电话：{{props.row.senderphone}}</p>
            <p>收件方ID: {{ props.row.receiverid }},收件人用户名：{{ props.row.receivername }},收件人人电话：{{props.row.receiverphone}}</p>
            <p>派送员ID: {{ props.row.employeeid }},派送员用户名：{{ props.row.employeename }},派送员电话：{{props.row.employeephone}}</p>
            <p>车辆ID：{{ props.row.carid }} </p>
            <p>货物重量：{{ props.row.weight }} </p>
            <p>物流费用：{{ props.row.cost }} </p>
          </el-card>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="订单ID" sortable />
      <el-table-column prop="ordername" label="货物名称" />
      <el-table-column prop="sendadd" label="发件方地址" />
      <el-table-column prop="receadd" label="收件方地址" />
      <el-table-column prop="status" label="货物状态" />
      <el-table-column prop="checkfinish" label="是否收件" />

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
          <el-form-item label="发件方姓名">
            <el-input v-model="form.sendername" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="发件方联系方式">
            <el-input v-model="form.senderphone" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item type="textarea" label="发件方地址">
            <el-input v-model="form.sendadd" style="width: 80%"></el-input>
          </el-form-item>

          <el-form-item label="收件方ID">
            <el-input v-model="form.receiverid" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="收件方姓名">
            <el-input v-model="form.receivername" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="收件方联系方式">
            <el-input v-model="form.receiverphone" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item type="textarea" label="收件方地址">
            <el-input v-model="form.receadd" style="width: 80%"></el-input>
          </el-form-item>

          <el-form-item label="派送员ID">
            <template #default="scope">
              <el-input v-model="form.employeeid" style="width: 80%"></el-input>
              <el-button size="mini" @click="getFreeDriver(scope.row)">选择派送员</el-button>
            </template>
          </el-form-item>
          <el-form-item label="派送员姓名">
            <el-input v-model="form.employeename" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="派送员联系方式">
            <el-input v-model="form.employeephone" style="width: 80%"></el-input>
          </el-form-item>

          <el-form-item label="车辆ID">
            <template #default="scope">
              <el-input v-model="form.carid" style="width: 80%"></el-input>
              <el-button size="mini" @click="getFreeCar(scope.row)">选择车辆</el-button>
            </template>
          </el-form-item>

          <el-form-item label="货物重量">
            <el-input v-model="form.weight" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="物流费用">
            <el-input v-model="form.cost" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="货物状态">
            <el-radio v-model="form.status" label="preparing">preparing</el-radio>
            <el-radio v-model="form.status" label="sending">sending</el-radio>
            <el-radio v-model="form.status" label="waiting">waiting</el-radio>
          </el-form-item>
          <el-form-item label="是否收件">
            <el-radio v-model="form.checkfinish" label="是">是</el-radio>
            <el-radio v-model="form.checkfinish" label="否">否</el-radio>
          </el-form-item>
        </el-form>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="save">Confirm</el-button>
          </span>
        </template>
      </el-dialog>



      <el-dialog v-model="dialogForCar" title="Please Enter" width="30%">
        <el-table :data="CarTableData" border stripe style="width: 100%">
          <el-table-column prop="id" label="车辆ID" sortable />
          <el-table-column prop="carname" label="车辆名字" />
          <el-table-column prop="maxweight" label="最大载重" />
          <el-table-column prop="status" label="车辆状态" />
          <el-table-column label="Operations">
            <template #default="scope">
              <el-button size="mini" @click="selectCar(scope.row)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>



      <el-dialog v-model="dialogForDriver" title="Please Enter" width="30%">
        <el-table :data="DriverTableData" border stripe style="width: 100%">
          <el-table-column prop="driverid" label="司机ID" sortable />
          <el-table-column prop="status" label="司机状态" />
          <el-table-column prop="phonenum" label="联系方式" />
          <el-table-column label="Operations">
            <template #default="scope">
              <el-button size="mini" @click="selectDriver(scope.row)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
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
      dialogForCar:false,
      dialogForDriver:false,
      search: '',
      currentPage: 1 ,
      total: 10 ,
      tableData: [],
      cntDriver: 10,
      DriverTableData:[],
      cntCar: 10,
      CarTableData:[],
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
        res.data.records.forEach(function (item){
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
        this.tableData=res.data.records
        this.total = res.data.total
        console.log(res)
      })
      //获取司机信息
      request.get("/api/driver",{
        params:{
          pageNum: this.currentPage,
          pageSize:10,
          search : this.search,
        },
      }).then(res => {
        this.DriverTableData=res.data.records
        this.cntDriver = res.data.total
        this.DriverTableData = this.DriverTableData.filter(item => {
          return item.status==="空闲"
        })
        console.log(res)
      })

      //获取车辆信息
      request.get("/api/car",{
        params:{
          pageNum: this.currentPage,
          pageSize:10,
          search : this.search,
        },
      }).then(res => {
        this.CarTableData=res.data.records
        this.cntCar = res.data.total
        this.CarTableData = this.CarTableData.filter(item => {
          return item.status==="空闲"
        })
        console.log(res)
      })
    },
    save(){
      console.log(this.form)
      if(this.form.id){
        request.put("/api/order",this.form).then(res => {
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
    getFreeCar(row) {
      this.dialogForCar = true
    },
    selectCar(row) {
      this.form.carid = JSON.parse(JSON.stringify(row.id))
      this.dialogForCar = false
    },

    getFreeDriver(row) {
      this.dialogForDriver = true
    },
    selectDriver(row) {
      this.form.employeeid = JSON.parse(JSON.stringify(row.driverid))
      this.dialogForDriver = false
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
