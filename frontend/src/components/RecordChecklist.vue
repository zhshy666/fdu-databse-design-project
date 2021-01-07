<template>
<div class="container">
    <logo></logo>
    <el-card v-if="noNeed" style="padding:5px 200px;"> No checklist need to fill in now !</el-card>
    <el-form
      @submit.native.prevent
      status-icon
      :model="checklist"      
      label-width="120px"
      label-position="left"      
      v-loading="loading"
      v-else
    > 
    <el-form-item prop="checklistID" size="medium" label="Checklist ID">
        <el-input
          size="medium"
          type="number"          
          v-model="checklist.checklistID"
          auto-complete="off"                    
        ></el-input>
      </el-form-item>   
      <el-form-item prop="patientID" size="medium" label="Patient ID">
        <el-input
          size="medium"
          type="number"          
          v-model="checklist.patientID"
          auto-complete="off"                    
        ></el-input>
      </el-form-item>              

      <el-form-item prop="level" size="medium" label="Disease Level">
        <el-radio-group v-model="checklist.level" size="medium">
            <el-radio border label="light"></el-radio>
            <el-radio border label="severe"></el-radio>
            <el-radio border label="critical"></el-radio>            
        </el-radio-group>
      </el-form-item>            

      <el-form-item prop="testResult" size="medium" label="Coronavirus test">
        <el-radio-group v-model="checklist.testResult" size="medium">
            <el-radio border label="positive"></el-radio>
            <el-radio border label="negative"></el-radio>            
        </el-radio-group>
      </el-form-item> 

      <el-form-item prop="date" size="medium" label="Date">
        <el-date-picker
            v-model="checklist.date"
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
export default {
  name:"RecordChecklist",
  components:{logo},
  data(){
    return{
        loading:false,
        noNeed:false,
        checklist:{
            checklistID:"",
            patientID:"",
            level:"",
            testResult:"",
            date:""
        },
        }
  },
  methods:{
      submit(){          
        this.$axios
        .post("/recordChecklist", {
            hospital_nurse_id: this.$store.state.user.id,
            checklist_id:this.cheklist.checklistID,
            patient_id:this.cheklist.patientID,                        
            disease_level:this.cheklist.level,
            test_result:this.checklist.testResult,
            date:this.cheklist.date,        
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
  },
  created(){
      this.$axios
      .post("/getChecklistToDo", {
        id: this.$store.state.user.id,        
      })
      .then(resp => {
        if (resp.status === 200) {            
            this.checklist.checklistID = resp.data.checklist_id;
            this.checklist.patientID = resp.data.patient_id;
            if(resp.data==""){
                this.noNeed = true;
            }
        } else {
            this.$message.error("Something error!");
        }
      })
      .catch(error => {
        this.$message.error("Something error!");
        console.log(error);
      });
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