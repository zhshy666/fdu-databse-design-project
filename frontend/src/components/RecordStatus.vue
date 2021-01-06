<template>
<div class="container">
    <logo></logo>
    <el-form
      @submit.native.prevent
      status-icon
      :model="recordStatus"      
      label-width="120px"
      label-position="left"
      v-loading="loading"
    >    
      <el-form-item prop="patientID" size="medium" label="Patient ID">
        <el-input
          size="medium"
          type="number"          
          v-model="recordStatus.patientID"
          auto-complete="off"                    
        ></el-input>
      </el-form-item>

      <el-form-item prop="patientName" size="medium" label="Patient Name">
        <el-input
          size="medium"
          type="text"          
          v-model="recordStatus.patientName"
          auto-complete="off"                    
        ></el-input>
      </el-form-item>

      <el-form-item prop="temperature" size="medium" label="Temperature">
          <el-input
          size="medium"
          type="number"          
          v-model="recordStatus.temperature"
          auto-complete="off"                    
        >
        <template slot="append">℃</template>
        </el-input>                
      </el-form-item>

      <el-form-item prop="symptom" size="medium" label="Symptom">
        <el-input
          size="medium"
          type="text"          
          v-model="recordStatus.symptom"
          auto-complete="off"                    
        ></el-input>
      </el-form-item>

      <el-form-item prop="level" size="medium" label="Disease Level">
        <el-radio-group v-model="recordStatus.level" size="medium">
            <el-radio border label="light"></el-radio>
            <el-radio border label="severe"></el-radio>
            <el-radio border label="critical"></el-radio>            
        </el-radio-group>
      </el-form-item>      

      <el-form-item prop="status" size="medium" label="Life Status">
        <el-radio-group v-model="recordStatus.status" size="medium">
            <el-radio border label="recoveried"></el-radio>
            <el-radio border label="treating"></el-radio>
            <el-radio border label="dead"></el-radio>
        </el-radio-group>
      </el-form-item> 

      <el-form-item prop="testResult" size="medium" label="Coronavirus test">
        <el-radio-group v-model="recordStatus.testResult" size="medium">
            <el-radio border label="positive"></el-radio>
            <el-radio border label="negative"></el-radio>            
        </el-radio-group>
      </el-form-item> 

      <el-form-item prop="date" size="medium" label="Date">
        <el-date-picker
            v-model="recordStatus.date"
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
        >Submit</el-button>
      </el-form-item>
    </el-form>
</div>
</template>

<script>
import logo from "./Logo"
export default {
  name:"RecordStatus",
  components:{logo},
  data(){
  return{
      loading:false,
      recordStatus:{
        patientID:123,
        patientName:"Tom",
        temperature:38,
        symptom:"Cough and fever",
        level:"light",
        status:"treating",        
        date:"",
        testResult:"positive",
      }
    }
  },
  methods:{
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.container{
    padding: 80px 200px 0 200px;
    text-align: left;
}
</style>