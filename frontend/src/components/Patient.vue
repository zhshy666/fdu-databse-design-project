<template>
<div >
  <el-card>            
      <h3>Basic Info</h3>    
    <el-row>
      <el-col :span="6">
        <span class="title">ID</span> | <em>{{this.patient.patient_id}}</em>
      </el-col>

      <el-col :span="6">
          <span class="title">Name</span> | <em>{{this.patient.name}}</em>        
      </el-col>

      <el-col :span="6">
        <span class="title">Gender</span>  | <em>{{this.patient.gender}}</em>
      </el-col>

      <el-col :span="6">
        <span class="title">Age</span> | <em>12</em>
      </el-col>
    </el-row>
    <br>
    <br>
    <h3> Treatment Info </h3>
    <el-row>          
      <el-col :span="6">            
          <span>Nurse ID</span> | <em>{{this.patient.nurse_id}}</em>
      </el-col>
              
      <el-col :span="6">
          <span>Life Status</span> | <el-tag>{{this.patient.life_status}}</el-tag>
      </el-col>

      <el-col :span="6">
          <span>Disease Level</span> | <el-tag>{{this.patient.disease_level}}</el-tag>
      </el-col>
      
      <el-col :span="6">
          <span >Treatment Region</span> | <el-tag>light</el-tag>
      </el-col>            
    </el-row>        
    <br>
    <br>
    <div class="message">
      <p v-if="this.patient.can_be_discharged == 1"><i class="el-icon-message"></i>  This patient can discharge now. &nbsp; <el-button type="primary"  size="small">Permit Discharge</el-button></p>      
      <p v-if="this.patient.need_transfer == 1"><i class="el-icon-message"></i>  This patient is waiting for being transfered to another region.</p>
    </div>

  </el-card>  
  <el-card>
    <h3>Status Records</h3>
    <el-table
    :data="statusRecords"    
    style="width: 100%"
    max-height="510"
    stripe
    >
    <el-table-column
      prop="date"
      label="Date"
      width="170">
    </el-table-column>
    <el-table-column
      prop="nurse_id"
      label="Responsible Nurse"
      width="170">
    </el-table-column>
    <el-table-column
      prop="life_status"
      label="Life Status"
      width="170">
    </el-table-column>
    <el-table-column
      prop="temperature"
      label="Tempetature"
      width="170">
    </el-table-column>
    <el-table-column
      prop="symptom"
      label="Symptom">
    </el-table-column>
  </el-table>
  </el-card>
  <el-card>
    <h3>Coronavirus Check Records</h3>
    <el-button>Add New Checklist</el-button>
    <el-table
    :data="checklist"    
    style="width: 100%"
    max-height="510"
    stripe>
    <el-table-column
      prop="date"
      label="Time"
      width="240">
      <template slot-scope="scope">
        {{getTime(scope.row.date)}}
      </template>      
    </el-table-column>    
    <el-table-column
      prop="test_result"
      label="Result"
      width="240">
    </el-table-column>
    <el-table-column
      prop="disease_level"
      label="Disease Level"
      width="240">
    </el-table-column>
    <el-table-column
      prop="doctor_id"
      label="Doctor"
      width="240">
    </el-table-column>
    
  </el-table>
  </el-card>
</div>
</template>
<script>

export default {
  name:"Patient",
  components:{},
  props:["patientId"],
  data(){
  return{
    patient:null,
    statusRecords:[],
    checklist:[],
    }
  },
  methods:{
    getTime(time){      
      return time.substr(0,10)+" "+time.substr(11,8);
    }
  },
  created(){
      this.$axios
      .post("/getPatientInfo", {
        id: this.$store.state.user.id,
        patient_id:this.patientId
      })
      .then(resp => {
        if (resp.status === 200) {
            console.log(resp.data);
            this.patient = resp.data[1];
            this.checklist = resp.data[2];            
            this.statusRecords = resp.data[3];            
        } else {      
          console.log(error);
        }
      })
      .catch(error => {
       console.log(error);
      });      
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.el-card{
  text-align:left;
  margin:10px 0;  
}
.infoitem {
  margin-top: 1em;
  margin-bottom: 1em;
}
em{
  font-size: 1em;
}
.message{
  font-size:1.1em;
}
</style>