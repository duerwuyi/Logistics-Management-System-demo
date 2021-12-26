<template>
<div>
  <!--头部-->
  <Header/>
  <!--主体-->
  <div style="display: flex">
    <!--侧边栏-->
    <Aside/>
    <!--内容-->
    <router-view style="flex: 1"/>
  </div>
</div>
</template>

<script>
import Header from "@/components/Header";
import Aside from "@/components/AsideAdmin";

export default {
  name: "Layout",
  components:{
    Header,
    Aside,
  },
  data(){
    return{
      user:{}
    }
  }

}
</script>

<style scoped>

</style>