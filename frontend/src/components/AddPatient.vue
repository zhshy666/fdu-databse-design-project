<template>
<div class="container">
    <logo></logo>
    <el-form
      @submit.native.prevent
      status-icon
      :model="newPatient"      
      label-width="120px"
      label-position="left"
      v-loading="loading"
    >    
      <el-form-item prop="name" size="medium" label="Name">          
        <el-input
          size="medium"
          type="text"          
          v-model="newPatient.name"
          auto-complete="off"                    
        ></el-input>
      </el-form-item>

      <el-form-item prop="gender" size="medium" label="Gender">
        <el-radio-group v-model="newPatient.gender" size="medium">
            <el-radio border label="male"></el-radio>
            <el-radio border label="female"></el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item prop="age" size="medium" label="Age">
        <el-input
          size="medium"
          type="number"          
          v-model="newPatient.age"
          auto-complete="off"                    
        ></el-input>
      </el-form-item>

      <el-form-item prop="level" size="medium" label="Disease Level">
        <el-radio-group v-model="newPatient.level" size="medium">
            <el-radio border label="light"></el-radio>
            <el-radio border label="severe"></el-radio>
            <el-radio border label="critical"></el-radio>            
        </el-radio-group>
      </el-form-item> 

      <el-form-item size="medium" label="Coronavirus test">
        <el-radio-group v-model="newPatient.testResult" size="medium">
            <el-radio border label="positive"></el-radio>
            <el-radio border label="negative"></el-radio>            
        </el-radio-group>
      </el-form-item> 

      <el-form-item prop="date" size="medium" label="Date">
        <el-date-picker
            v-model="newPatient.date"
            type="datetime"
            placeholder="select time"
            default-time="12:00:00">
        </el-date-picker>
      </el-form-item> 

      <el-form-item size="medium">
        <el-button
          native-type="submit"          
          size="medium"
          type="primary"
          style="width:100% " 
          @click="submit"         
        >Submit</el-button>
      </el-form-item>
    </el-form>
</div>
</template>
<script>
import logo from "./Logo"
export default {
  name:"AddPatient",
  components:{logo},
  data(){
    return{
        loading:false,
        newPatient:{
            name:"",
            gender:"",
            age:"",
            level:"",
            testResult:"",
            date:"",
        },        
    }
  },  
  methods:{
    submit(){
      this.loading = true;
      this.$axios
      .post("/registerPatientInfo", {
        emergency_nurse_id: this.$store.state.user.id,  
        name:this.newPatient.name,
        gender:this.newPatient.gender,
        age:this.newPatient.age,
        disease_level:this.newPatient.level,      
        test_result:this.newPatient.testResult,
        date:this.newPatient.date
      })
      .then(resp => {
        if (resp.status === 200) {
          this.$message.success("Submit successfully!");
          this.loading = false;
          this.newPatient = {
            name:"",
            gender:"",
            age:"",
            level:"",
            testResult:"",
            date:"",
          }
        } else {
          this.$message.error("Submit error!");    
          this.loading = false;
        }
      })
      .catch(error => {
        this.$message.error("Submit error!");    
        console.log(error);
        this.loading = false;
      });
    }
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