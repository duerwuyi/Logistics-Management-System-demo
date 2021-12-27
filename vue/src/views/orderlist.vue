<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-popconfirm title="Are you sure to delete this?" @confirm="deleteAll">
        <template #reference>
          <el-button type="danger">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="Please input"  style="width: 20%" clearable />
      <el-button type="primary" style="margin-left: 7px" @click="load">查询</el-button>
      <el-tooltip :content="'查询需要绑定的订单（工作任务）'" placement="top">
        <el-switch
            v-model="Svalue"
            @click = handle
            active-color="#13ce66"
            style="margin-left: 30px"
        />
      </el-tooltip>
    </div>


    <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
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
      <el-table-column prop="sendadd" label="发件方地址" />
      <el-table-column prop="receadd" label="收件方地址" />
      <el-table-column prop="status" label="货物状态" />
      <el-table-column prop="checkfinish" label="是否收件" width="120px" sortable />

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
            <el-input v-model="form.senderid" style="width: 80%" placeholder="该项填后可不填以下两项"></el-input>
          </el-form-item>
          <el-form-item label="发件方姓名">
            <el-input v-model="form.sendername" style="width: 50%"></el-input>
            <el-button type="primary" style="margin-left: 7px" @click="findSenderId(form.sendername)"> 查找id </el-button>
          </el-form-item>
          <el-form-item label="发件方联系方式">
            <el-input v-model="form.senderphone" style="width: 80%" disabled></el-input>
          </el-form-item>
          <el-form-item type="textarea" label="发件方地址">
            <el-input v-model="form.sendadd" style="width: 80%"></el-input>
          </el-form-item>

          <el-form-item label="收件方ID">
            <el-input v-model="form.receiverid" style="width: 80%" placeholder="该项填后可不填以下两项"></el-input>
          </el-form-item>
          <el-form-item label="收件方姓名">
            <el-input v-model="form.receivername" style="width: 50%"></el-input>
            <el-button type="primary" style="margin-left: 7px" @click="findReceiverId(form.receivername)"> 查找id </el-button>
          </el-form-item>
          <el-form-item label="收件方联系方式">
            <el-input v-model="form.receiverphone" style="width: 80%" disabled></el-input>
          </el-form-item>
          <el-form-item type="textarea" label="收件方地址">
            <el-input v-model="form.receadd" style="width: 80%"></el-input>
          </el-form-item>

          <el-form-item label="派送员">
            <template #default="scope">
              <el-button @click="getFreeDriver(scope.row)" style="width: 50%;">选择派送员</el-button>
              <el-tag size="mini" type="success" v-if="this.form.employeeid != null && dialogForDriver === false" style="margin-left: 7px">ok</el-tag>
            </template>
          </el-form-item>

          <el-form-item label="车辆">
            <template #default="scope">
              <el-button @click="getFreeCar(scope.row)" style="width: 50%;">选择车辆</el-button>
              <el-tag size="mini" type="success" v-if="this.form.carid != null && dialogForCar === false" style="margin-left: 7px">ok</el-tag>
            </template>
          </el-form-item>

          <el-form-item label="货物重量">
            <el-input v-model="form.weight" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="物流费用">
            <el-input v-model="form.cost" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="货物状态" v-if="isEdit">
            <el-radio v-model="form.status" label="preparing">preparing</el-radio>
            <el-radio v-model="form.status" label="sending">sending</el-radio>
            <el-radio v-model="form.status" label="waiting">waiting</el-radio>
          </el-form-item>
          <el-form-item label="是否收件" v-if="isEdit">
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
        <el-table ref="carTable" :data="CarTableData" border stripe style="width: 100%">
          <el-table-column prop="id" label="车辆ID" sortable />
          <el-table-column prop="carname" label="车辆名字" />
          <el-table-column prop="maxweight" label="最大载重" />
          <el-table-column prop="status" label="车辆状态" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button size="mini" @click="selectCar(scope.row)" v-if="this.form.carid !== scope.row.id">选择</el-button>
              <el-tag size="mini" v-if="this.form.carid === scope.row.id">已被选择</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>



      <el-dialog v-model="dialogForDriver" title="Please Enter" width="30%">
        <el-table ref="driverTable" :data="DriverTableData" border stripe style="width: 100%">
          <el-table-column prop="driverid" label="司机ID" sortable />
          <el-table-column prop="status" label="司机状态" />
          <el-table-column prop="phonenum" label="联系方式" />
          <el-table-column label="Operations">
            <template #default="scope">
              <el-button size="mini" @click="selectDriver(scope.row)" v-if="this.form.employeeid !== scope.row.driverid">选择</el-button>
              <el-tag size="mini" v-if="this.form.employeeid === scope.row.driverid">已被选择</el-tag>
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
      Flag: 'admin',
      user:{},
      multipleSelection:[],
      isEdit:false,
      Svalue:false,
    }
  },
  created(){
    let userStr = sessionStorage.getItem("user")
    if(userStr){
      this.user = JSON.parse(userStr)
    }else{
      this.user = "无该用户"
    }
    this.load()
  },
  mounted(){
    document.title='订单管理'
  },
  methods :{
    add(){
      this.dialogVisible = true
      this.isEdit =false
      this.form={}
    },
    findSenderId( name ){
      request.post("/api/user/nametoid",name).then(res=>{
        if (res.code === "0") {
          this.form.senderid = res.data.id
          this.form.senderphone = res.data.phonenum
          ElMessage({
            type: 'success',
            message: '选择成功',
          })
        }else{
          ElMessage({
            type: 'error',
            message: res.msg,
          })
        }
      })
    },
    findReceiverId(name){
      request.post("/api/user/nametoid",name).then(res=>{
        if (res.code === "0") {
          this.form.receiverid = res.data.id
          this.form.receiverphone = res.data.phonenum
          ElMessage({
            type: 'success',
            message: '选择成功',
          })
        }else{
          ElMessage({
            type: 'error',
            message: res.msg,
          })
        }
      })
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
        this.Flag = "admin"
      })
    },
    handle(){
      if(!this.Svalue){
        this.load()
        return
      }
      this.Flag = "empty"
      this.load()
    },
    deleteAll(){
      if(!this.multipleSelection.length){
        ElMessage({
          type: 'error',
          message: '没有选中！',
        })
        return
      }
      request.post("/api/order/deleteAll", this.multipleSelection).then(res =>{
        if(res.code === '0'){
          ElMessage({
            type: 'success',
            message: '成功',
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
    handleSelectionChange(val){
      this.multipleSelection = val.map(v => v.id)
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
      this.isEdit =true;
    },
    getFreeCar(row) {
      request.get("/api/car",{
        params:{
          pageNum: 1,
          pageSize:10,
          search : "找车",
        },
      }).then(res => {
        this.CarTableData=res.data.records
        this.cntCar = res.data.total
        console.log(res)
      })
      this.dialogForCar = true
    },
    selectCar(row) {
      this.$refs.carTable.setCurrentRow(row)
      this.form.carid = JSON.parse(JSON.stringify(row.id))

    },
    getFreeDriver(row) {
      request.get("/api/driver",{
        params:{
          pageNum: 1,
          pageSize:10,
          search : "找司机",
        },
      }).then(res => {
        this.DriverTableData=res.data.records
        this.cntDriver = res.data.total
        console.log(res)
      })
      this.dialogForDriver = true
    },
    selectDriver(row) {
      this.$refs.driverTable.setCurrentRow(row)
      this.form.employeeid = JSON.parse(JSON.stringify(row.driverid))
    },
    HandleDelete(id) {
      console.log(id)
      request.delete("/api/order/" + id).then(res => {
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
