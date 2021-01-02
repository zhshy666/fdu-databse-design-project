<template>
<div>
  <el-row>  
  <el-col :span="12" :offset="6" >
    <div class="loginFormContainer">      
      <logo></logo>
      <h3>LOG IN</h3>
    <el-form
      @submit.native.prevent
      status-icon
      :model="loginForm"
      :rules="rules"
      label-position="left"
      v-loading="loading"
    >
      <el-form-item prop="user" size="medium" >
        <el-input
          size="medium"
          type="text"
          v-model="loginForm.user"
          auto-complete="off"
          placeholder="UserID"
        ></el-input>
      </el-form-item>

      <el-form-item prop="password" size="medium">
        <el-input
          size="medium"
          type="password"
          v-model="loginForm.password"
          auto-complete="off"
          placeholder="Password"
        ></el-input>
      </el-form-item>

      <el-form-item size="medium">
        <el-button
          native-type="submit"
          :disabled ="isDisabled"
          size="medium"
          type="primary"
          style="width:100% "
          v-on:click="login"
        >Log In</el-button>
      </el-form-item>
    </el-form>
  </div>
  </el-col>
</el-row>
</div>

</template>
<script>
import logo from '../components/Logo'
export default {
  name: 'Login',
  components:{logo},
  data () {
    return {      
      loginForm: {
          user: "",
          password: "",          
        },
        rules: {
          user: [
            {required:true,message:"UserID is required",blur:"change",trigger:"blur"}
          ],
          password: [
            {required:true, message:"Password is required", blur:"change",trigger:"blur"},           
          ]        
        },
        loading: false,
      }    
  },
  computed:{
    isDisabled(){
      return (this.loginForm.user == "") ||
             (this.loginForm.password =="");
    }
  },
  methods:{    
    login() {
      this.loading = true;      
      this.$axios
        .post("/login", {
          id: this.loginForm.user,
          password: this.loginForm.password,
        })
        .then(resp => {          
          if (resp.status === 200) {
            //Save token
             // this.$store.commit("login", resp.data);
             //this.notify('success','Welcome back!');
             this.$message({
              message: 'Login successfully',
              type: 'success'
              });
             //this.$router.go(-1);
            this.$router.push("/");
          } else {          
            //this.notify('error','Username/Email or password is wrong!');
            this.$message({
              message: 'Login error',
              type: 'error'
              });
            this.loading = false;
          }
        })
        .catch(error => {
          console.log(error);
          this.$message({
              message: 'Login error',
              type: 'error'
              });
          // this.notify('error','Username/Email or password is wrong!');
          this.loading = false;
        });
      }
    },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
img{  
  height:650px;
  margin-top:2px;
  margin-left:50px;
  opacity: 0.8;
}
.loginFormContainer{
  margin:80px 100px 0 100px;
  padding:50px 50px; 
  background-color: rgba(0, 0, 0, 0.05); 
}
h3{
  text-align: center;
}
</style>