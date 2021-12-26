<template>
<div>
  <!--头部-->
  <Header/>
  <!--主体-->
  <div style="display: flex">
    <!--侧边栏-->
    <AsideAdmin v-if="user.authority === 'admin'" />
    <AsideUser v-if="user.authority === 'user'" />
    <AsideDriver v-if="user.authority === 'employee'" />
    <!--内容-->
    <router-view style="flex: 1"/>
  </div>
</div>
</template>

<script>
import Header from "@/components/Header";
import AsideAdmin from "../components/AsideAdmin";
import AsideUser from "../components/AsideUser";
import AsideDriver from "../components/AsideDriver";

export default {
  name: "Layout",
  components:{
    Header,
    AsideAdmin,
    AsideUser,
    AsideDriver
  },
  data(){
    return{
      user:{}
    }
  },
  created() {
    let userinfo = sessionStorage.getItem("user")
    this.user = JSON.parse(userinfo)
  }

}
</script>

<style scoped>

</style>