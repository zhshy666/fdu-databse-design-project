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
          v-model="recordStatus.patientID"
          auto-complete="off"                    
        ></el-input>
      </el-form-item>      

      <el-form-item prop="temperature" size="medium" label="Temperature">
          <el-input
          size="medium"            
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
          @click="submit()"         
        >Submit</el-button>
      </el-form-item>
    </el-form>
</div>
</template>

<script>
import logo from "./Logo"
import moment from "moment"
export default {
  name:"RecordStatus",
  components:{logo,moment},
  data(){
  return{
      loading:false,
      recordStatus:{
        patientID:"1",        
        temperature:"36.5",
        symptom:"ahd",
        level:"light",
        status:"treating",        
        date:"",        
      }
    }
  },
  methods:{    
    submit(){         
      console.log(this.recordStatus.date);
      console.log(moment(Date.parse(this.recordStatus.date)). utcOffset(480).format("yy-MM-DD hh:mm:ss"));
      this.$axios
      .post("/recordPatientStatus", {
        hospital_nurse_id: this.$store.state.user.id,
        id:this.recordStatus.patientID,        
        temperature:this.recordStatus.temperature,
        symptom:this.recordStatus.symptom,
        life_status:this.recordStatus.status,
        disease_level:this.recordStatus.level,
        date:moment(Date.parse(this.recordStatus.date)). utcOffset(480).format("yy-MM-DD hh:mm:ss"),                
      })
      .then(resp => {
        if (resp.status === 200) {
          this.$message.success("Submit successfully!");
        } else {
          this.$message.error("Something wrong!");      
        }
      })
      .catch(error => {
        this.$message.error("Something wrong!");      
        console.log(error);
      });
    }
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