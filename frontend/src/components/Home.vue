<template>
<el-container>
    <el-aside>    
        <logo id="logo"></logo>    
        <h1>张三</h1>    
        <el-menu
            default-active="1"            
            @open="handleOpen"
            @close="handleClose"
        >    
        <el-menu-item index="1">
            <i class="el-icon-s-cooperation"></i>
            <span slot="title">修改个人资料</span>
        </el-menu-item>

        <el-menu-item index="2">
            <i class="el-icon-document-add"></i>
            <span slot="title">导入病人信息</span>
        </el-menu-item>

        <el-menu-item index="3">
            <i class="el-icon-s-claim"></i>
            <span slot="title">登记病人日常信息</span>
        </el-menu-item>

        <el-menu-item index = "4">
            <i class="el-icon-user"></i>
            <span slot="title">查看病人信息</span>
        </el-menu-item>

        <el-menu-item index = "5">
            <i class="el-icon-user-solid"></i>
            <span slot="title">查看区域护士信息</span>
        </el-menu-item>

        <el-menu-item index = "6">
            <i class="el-icon-message"></i>
            <span slot="title">站内信</span>
        </el-menu-item>
    </el-menu>
    </el-aside>
    <el-main>
        <!-- todo: 4个不同的功能区 -->
    </el-main>
</el-container>
</template>
<script>
import logo from '../components/Logo'
export default {
  name:"Home",
  components:{logo},
  data(){
      return{

      }
  },
  methods:{
       handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      }
    
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.el-aside{    
    padding:20px 0;
    height:100vh;    
    background-color:rgba(112, 152, 187, 0.1);
}
.el-menu{
    margin-top:50px;
    background-color:rgba(112, 152, 187, 0);
    text-align:left;
}
h1{
    font-size: 1.5rem;    
}

</style>