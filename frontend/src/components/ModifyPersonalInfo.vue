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
          v-model="modifyForm.id"
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

      <el-form-item prop="gender" size="medium" label="Gender">
        <el-input
          size="medium"
          type="text"          
          v-model="modifyForm.gender"
          auto-complete="off"          
          disabled
        ></el-input>
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

      <el-form-item prop="password" size="medium" label="New Password">
        <el-input
          size="medium"
          type="password"
          v-model="modifyForm.password"
          auto-complete="off"          
        ></el-input>
      </el-form-item>      

      <el-form-item size="medium">
        <el-button
          native-type="submit"
          :disabled ="isDisabled"
          size="medium"
          type="primary"
          style="width:100% "   
          @click="modifyInfo"       
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
          id:this.$store.state.user.id,
          username:this.$store.state.user.name,
          title:this.$store.state.user.type,
          password:"",
          age:this.$store.state.user.age,
          gender:this.$store.state.user.gender
      },
      rules:{
          password: [
            {required:true,message:"Password is required",blur:"change",trigger:"blur"}
          ],
          age: [
            {required:true, message:"Age is required", blur:"change",trigger:"blur"},
          ],          
      }
    }
  },
  computed:{
    isDisabled(){
      return (this.modifyForm.age == "") ||
             (this.modifyForm.password =="");             
    }
  },
  methods:{
    modifyInfo(){
      this.loading = true;      
      this.$axios
        .post("/modifyPersonalInfo", {
          id: this.modifyForm.id,          
          password:this.modifyForm.password,
          age:this.modifyForm.age,
        })
        .then(resp => {          
          if (resp.status === 200) {      
            this.$store.state.user.age = this.modifyForm.age;
            this.$store.state.user.password = this.modifyForm.password;            
            this.$message({
             message: 'Modify successfully',
             type: 'success'
            });
            this.loading = false
          } else {                      
            this.$message({
              message: 'Modify error',
              type: 'error'
              });
            this.loading = false;
          }
        })
        .catch(error => {
          console.log(error);
          this.$message({
              message: 'Modify error',
              type: 'error'
              });          
          this.loading = false;
        });      
    }
  },  
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.container{
    padding: 100px 200px;
    text-align: left;
}
</style>