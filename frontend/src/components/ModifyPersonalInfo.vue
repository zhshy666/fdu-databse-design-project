<template>
<div class="container">
    <logo></logo>    
    <el-form
      @submit.native.prevent
      status-icon
      :model="modifyForm"
      :rules="rules"
      label-width="120px"
      label-position="left"
      v-loading="loading"
    >    
      <el-form-item prop="userID" size="medium" label="Staff ID">          
        <el-input
          size="medium"
          type="text"          
          v-model="modifyForm.userID"
          auto-complete="off"          
          disabled
        ></el-input>
      </el-form-item>

      <el-form-item prop="username" size="medium" label="Name">
        <el-input
          size="medium"
          type="text"          
          v-model="modifyForm.username"
          auto-complete="off"          
          disabled
        ></el-input>
      </el-form-item>

      <el-form-item prop="title" size="medium" label="Title">
        <el-input
          size="medium"
          type="text"          
          v-model="modifyForm.title"
          auto-complete="off"          
          disabled
        ></el-input>
      </el-form-item>

      <el-form-item prop="password" size="medium" label="Password">
        <el-input
          size="medium"
          type="password"
          v-model="modifyForm.password"
          auto-complete="off"          
        ></el-input>
      </el-form-item>

      <el-form-item prop="gender" size="medium" label="Gender">
        <el-radio-group v-model="modifyForm.gender" size="medium">
            <el-radio border label="Male"></el-radio>
            <el-radio border label="Female"></el-radio>
        </el-radio-group>    
      </el-form-item>

      <el-form-item prop="age" size="medium" label="Age">
        <el-input
          size="medium"
          type="number"
          v-model="modifyForm.age"
          auto-complete="off"
          value = "test"
        ></el-input>
      </el-form-item>


      <el-form-item size="medium">
        <el-button
          native-type="submit"
          :disabled ="isDisabled"
          size="medium"
          type="primary"
          style="width:100% "          
        >Modify</el-button>
      </el-form-item>
    </el-form>
</div>
</template>
<script>
import logo from "./Logo"
export default {
  name:"ModifyPersonalInfo",
  components:{logo},
  data(){
  return{
      loading:false,
      modifyForm:{
          userID:"123",
          username:"Jaxon Liu",
          title:"doctor",
          password:"123456",
          age:"21",
          gender:"Male"
      },
      rules:{
          password: [
            {required:true,message:"Password is required",blur:"change",trigger:"blur"}
          ],
          age: [
            {required:true, message:"Age is required", blur:"change",trigger:"blur"},
          ],
          gender: [
            {required:true, message:"Gender is required", blur:"change",trigger:"blur"},
          ] 
      }
    }
  },
  computed:{
    isDisabled(){
      return (this.modifyForm.age == "") ||
             (this.modifyForm.password =="")||
             (this.modifyForm.gender == "");
    }
  },
  methods:{
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.container{
    padding: 100px 200px;
    text-align: left;
}
</style>