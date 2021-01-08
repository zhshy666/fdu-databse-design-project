<template v-if="refresh">
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
        <span class="title">Age</span> | <em>{{this.patient.age}}</em>
      </el-col>
    </el-row>
    <br>
    <br>
    <h3> Treatment Info </h3>
    <el-row>          
      <el-col :span="4" style="margin-top:5px">            
          <span>Nurse ID</span> | <em>{{this.patient.nurse_id}}</em>
      </el-col>
      
      <el-col :span="5">
          <span >Treatment Region</span> | <el-tag>{{this.patient.treatment_region_level}}</el-tag>          
      </el-col>   
              
      <el-col :span="7">
          <span>Life Status</span> | 
          <el-select size="small" v-model="status" style="width:120px">
            <el-option
              v-for="item in getStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <el-button size="mini" v-if="status != this.patient.life_status" @click="modifyLifeStatus()">confirm</el-button>
      </el-col>

      <el-col :span="7">
          <span>Disease Level</span> | 
          <el-select size="small" v-model="diseaseLevel" style="width:100px">
            <el-option
              v-for="item in diseaseLevelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <el-button size="mini" v-if="diseaseLevel != this.patient.disease_level" @click="modifyDiseaseLevel()">Confirm</el-button>
      </el-col>               
    </el-row>        
    <br>
    <br>
    <div class="message">
      <p v-if="this.patient.can_be_discharged == 1"><i class="el-icon-message"></i>  This patient can discharge now. &nbsp; <el-button type="primary"  size="small" @click="permitDischarge()">Permit Discharge</el-button></p>      
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
    <el-button @click="addNewChecklist">Add New Checklist</el-button>
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
  inject:["reload"],
  data(){
  return{
    refresh:true,
    patient:null,
    statusRecords:[],
    checklist:[],    
    status:null,   
    diseaseLevel:null,
    diseaseLevelOptions:[{
            value: 'light',
            label: 'light'
          },{
            value: 'severe',
            label: 'severe',          
          }, {
            value: 'critical',
            label: 'critical'
          }]
    }
  },
  methods:{
    getTime(time){      
      return time.substr(0,10)+" "+time.substr(11,8);
    },
    modifyLifeStatus(){      
      this.$axios
      .post("/modifyLifeStatus", {
        doctor_id:this.$store.state.user.id,
        patient_id: this.patient.patient_id,
        new_life_status:this.status        
      })
      .then(resp => {
        if (resp.status === 200) {
          console.log(resp.data);
          this.patient.life_status = this.status;
          this.$message.success("Modify successfully");
        } else {
          console.log(error);
        }
      })
      .catch(error => {
       console.log(error);
      });
    },
    
    modifyDiseaseLevel(){     
      console.log(this.patient.patient_id);
      this.$axios
      .post("/modifyDiseaseLevel", {
        doctor_id:this.$store.state.user.id,
        patient_id: this.patient.patient_id,
        new_disease_level:this.diseaseLevel
      })
      .then(resp => {
        if (resp.status === 200) {
          console.log(resp.data);
          this.patient.disease_level = this.diseaseLevel;
          this.$message.success("Modify successfully");
          this.reload();
        } else {
          console.log(error);
        }
      })
      .catch(error => {
       console.log(error);
      });
    },

    addNewChecklist(){         
      this.$axios
      .post("/newChecklist", {
        doctor_id: this.$store.state.user.id,
        patient_id:this.patient.patient_id,        
      })
      .then(resp => {
        if (resp.status === 200) {
          this.$message.success("Add successfully!");
        } else {
          this.$message.error("Something wrong!");
          console.log(error);
        }
      })
      .catch(error => {
        this.$message.error("Something wrong!");
        console.log(error);
      });
    },
    
    permitDischarge(){
      this.$axios
      .post("/permitDischarge", {
        doctor_id: this.$store.state.user.id,
        patient_id:this.patient.patient_id,   
      })
      .then(resp => {
        if (resp.status === 200) {
          this.$message.success("Opetate successfully");
          this.reload();
        } else {
          this.$message.error("Something wrong");      
        }
      })
      .catch(error => {
        this.$message.error("Something wrong");      
      });
    },
  },
  computed:{
    getStatusOptions(){
      let statusOptions = [];
      switch(this.patient.life_status){        
        case 'healthy':
          statusOptions = [
            {
            value: 'healthy',
            label: 'healthy'
          },{
            value: 'treating',
            label: 'treating',          
          }, {
            value: 'dead',
            label: 'dead'
          }];
          return statusOptions;

        case 'dead':      
          statusOptions = [{
            value: 'dead',
            label: 'dead'            
          }];
          return statusOptions;

        case 'treating':
          statusOptions = [{
            value: 'healthy',
            label: 'healthy',            
          },{
            value: 'treating',
            label: 'treating'
          },{
            value: 'dead',
            label: 'dead',            
          }];
          return statusOptions;          
      }      
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
            this.status = this.patient.life_status;                        
            this.diseaseLevel = this.patient.disease_level;

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