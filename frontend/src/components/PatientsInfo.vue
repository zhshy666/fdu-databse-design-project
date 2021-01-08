
<template>
<div class = "container">
    <logo></logo>   
    <el-table
    v-if="this.specifiedPatientId==null"
    :data="patients"
    max-height="510"
    :style="getPaddingLeft()"
    stripe
    >

    <el-table-column
      sortable
      prop="patient_id"
      label="ID"
      width="89">
    </el-table-column>

    <el-table-column      
      sortable
      prop="name"
      label="Name"
      width="120"
      >      
      <template slot-scope="scope">        
        <p>{{scope.row.name}}</p>
      </template>
    </el-table-column>
    
    <el-table-column      
      prop="gender"
      label="Gender"
      width="110"
      :filters="[{ text: 'male', value: 'male' }, { text: 'female', value: 'female'}]"
      :filter-method="filterGender"
      filter-placement="bottom-end">      
    </el-table-column>

    <el-table-column
      prop="age"
      label="Age"
      sortable
      width="100">
    </el-table-column>    

    <el-table-column
      prop="disease_level"
      label="Disease Level"      
      width="130"
      :filters="[{ text: 'light', value: 'light' }, { text: 'severe', value: 'severe'},{text:'critical',value:'critical'}]"
      :filter-method="filterDiseaseLevel"
      filter-placement="bottom-end">
      <template slot-scope="scope">
        <el-tag
          effect="dark"
          :type="parseType(scope.row.disease_level)"
          disable-transitions>{{scope.row.disease_level}}</el-tag>
      </template>
    </el-table-column>    

    <el-table-column
      prop="status"
      label="Life Status"      
      width="120"
      :filters="[{ text: 'healthy', value: 'healthy' }, { text: 'treating', value: 'treating'},{text: 'dead', value: 'dead'}]"
      :filter-method="filterLifeStatus"
      filter-placement="bottom-end">
      <template slot-scope="scope">
        <el-tag           
          :type="parseStatus(scope.row.life_status)"
          disable-transitions>{{scope.row.life_status}}</el-tag>                          
      </template>
    </el-table-column>    

    <el-table-column  
      v-if="this.$store.state.user.type=='emergency_nurse'"  
      prop="treatment_region_level"
      label="Treat Region"      
      width="120"
      :filters="[{ text: 'light', value: 'light' }, { text: 'severe', value: 'severe'},{text: 'critical', value: 'critical'},{ text: 'quarantine', value: 'quarantine'}]"
      :filter-method="filterRegion"
      filter-placement="bottom-end">
    </el-table-column>

    <el-table-column  
      v-else
      prop="treatment_region_level"
      label="Treat Region"      
      width="120"
      >
    </el-table-column>

    <el-table-column
      v-if="this.$store.state.user.type=='doctor'|| this.$store.state.user.type=='chief_nurse' "
      prop="need_transfer"
      label="Need Trans"  
      :filters="[{ text: 'yes', value: '1' }, { text: 'no', value: '0'}]"
      :filter-method="filterTransfer"
      filter-placement="bottom-end"    
      width="120">
      <template slot-scope="scope">    
        <!-- 已经死亡 -->
        <div v-if="scope.row.life_status =='dead'">dead</div>
        <div v-else>
          <!-- 没有死亡 -->
          <!-- 已经出院 -->
          <div v-if="scope.row.can_be_discharged == 2">discharged</div>
          <!-- 没有出院 -->
          <div v-else-if="scope.row.need_transfer == 1">yes</div>
          <div v-else-if="scope.row.need_transfer == 0">no</div>
        </div>        
      </template>
    </el-table-column>

    <el-table-column
      v-if="this.$store.state.user.type!='emergency_nurse'"
      prop="discharge"
      label="Discharge?"      
      :filters="[{ text: 'yes', value: '1' }, { text: 'no', value: '0'}]"
      :filter-method="filterDischarge"
      filter-placement="bottom-end"  
      width="120">    
      <template slot-scope="scope">            
        <div v-if ="scope.row.can_be_discharged == 0">no</div>
        <div v-else-if ="scope.row.can_be_discharged == 1" >yes</div>        
        <div v-else-if="scope.row.can_be_discharged == 2">discharged</div>
        <div v-else>dead</div>
      </template>
    </el-table-column>

    <el-table-column      
      label=""      
      width="130" v-if="this.$store.state.user.type == 'doctor'">
      <template slot-scope="scope">            
        <el-button @click="lookUpPatient(scope.row.patient_id)">See more</el-button>
      </template>
    </el-table-column>

  </el-table>    
  
  <patient :patientId="this.specifiedPatientId" v-else></patient>  
</div>
</template>
<script>
import logo from "./Logo"
import Patient from './Patient.vue'
export default {
  name:"PatientsInfo",
  components:{logo,Patient},
  data(){
  return{    
    patients:[],    
    specifiedPatientId:null,
    }
  },
  methods:{
    parseType(level){
      switch(level){
        case 'light':
          return 'info';
        case 'severe':
          return 'warning';
        case 'critical':
          return 'danger';
      }      
    },
    parseStatus(status){
      switch(status){
        case 'healthy':
          return 'success';
        case 'treating':
          return 'warning';
        case 'dead':
          return 'info';
      }
    },
    filterGender(value,row){
      return row.gender === value;
    },
    filterLifeStatus(value, row) {      
      return row.life_status === value;
    },
    filterDiseaseLevel(value,row){
      return row.disease_level === value;
    },
    filterTransfer(value,row){
      return (row.need_transfer == value && row.life_status != 'dead' && row.can_be_discharged !=2);
    },
    filterDischarge(value,row){
      return row.can_be_discharged == value;
    },
    filterRegion(value,row){
      return row.treatment_region_level == value;
    },
    getPatientInfo(){            
      this.$axios
      .post("/getPatientsInfo", {
        id: this.$store.state.user.id,        
      })
      .then(resp => {
        if (resp.status === 200) {
          console.log(resp.data);
          this.patients = resp.data;                            
        } else {
          this.$message.error("Error occurs when geting patient info");
          console.log(error);
        }
      })
      .catch(error => {
        this.$message.error("Error occurs when geting patient info");
        console.log(error);
      });      
    },
    getAllPatientsInfo(){
      this.$axios
      .post("/getAllPatientsInfo", {
        id: this.$store.state.user.id,        
      })
      .then(resp => {
        if (resp.status === 200) {
          console.log(resp.data);
          this.patients = resp.data;                 
        } else {
          this.$message.error("Error occurs when geting patient info");
          console.log(error);
        }
      })
      .catch(error => {
        this.$message.error("Error occurs when geting patient info");
        console.log(error);
      });
    },
    getRelatedPatientsInfo(){
      this.$axios
      .post("/getRelatedPatientsInfo", {
        id: this.$store.state.user.id,        
      })
      .then(resp => {
        if (resp.status === 200) {
          console.log(resp.data);
          this.patients = resp.data;                   
        } else {
          this.$message.error("Error occurs when geting patient info");
          console.log(error);
        }
      })
      .catch(error => {
        this.$message.error("Error occurs when geting patient info");
        console.log(error);
      });
    },
    lookUpPatient(id){
      this.specifiedPatientId=id;
    },
    permitDischarge(id){
      this.$axios
      .post("/permitDischarged", {
        doctor_id: this.$store.state.user.id,
        patient_id:id,   
      })
      .then(resp => {
        if (resp.status === 200) {
          this.$message.success("Opetate successfully");
        } else {
          this.$message.error("Something wrong");      
        }
      })
      .catch(error => {
        this.$message.error("Something wrong");      
      });
    },
    getPaddingLeft(){
      switch(this.$store.state.user.type) {
      case 'chief_nurse':
        return "padding-left:60px";
      case 'doctor':
        return "";        
      case 'emergency_nurse':        
        return "padding-left:200px";
      case 'hospital_nurse':                
        return "padding-left:140px";
    } 
    }    
  },

  created(){  
    switch(this.$store.state.user.type) {
      case 'chief_nurse':
      case 'doctor':
        this.getPatientInfo();
        return;
      case 'emergency_nurse':
        this.getAllPatientsInfo();
        return;
      case 'hospital_nurse':        
        this.getRelatedPatientsInfo();
        return;
    }    
  }  
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.container{
  margin-left:250px;
  padding:30px 5px;
}
.input-new-tag {
  width: 103px;
  margin-right: 5px;
  vertical-align: bottom;
}
</style>