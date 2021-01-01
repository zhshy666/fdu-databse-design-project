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
            <el-radio border label="Male"></el-radio>
            <el-radio border label="Female"></el-radio>
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

      <el-form-item prop="age" size="medium" label="Disease Level">
        <el-radio-group v-model="newPatient.level" size="medium">
            <el-radio border label="Severe"></el-radio>
            <el-radio border label="Midium"></el-radio>
            <el-radio border label="Light"></el-radio>
        </el-radio-group>
      </el-form-item>      

      <el-form-item size="medium">
        <el-button
          native-type="submit"          
          size="medium"
          type="primary"
          style="width:100% "          
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
            name:"Zhang San",
            gender:"Male",
            age:21,
            level:"Severe",

        },        
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